package com.moko.bxp.sethala.clients;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ResetMqttClient extends AsyncTask<String, Void, Boolean> {
    private Context context;
    private JsonObject body;
    private String macAddress;
    public ResetMqttClient(Context context, JsonObject body, String macAddress) {
        this.context = context;
        this.body = body;
        this.macAddress = macAddress.toUpperCase().replaceAll(":", "");
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            MqttClient client = new MqttClient(Configuration.MqttHost,macAddress ,new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(Configuration.MqttUsername);
            options.setPassword(Configuration.MqttPassword.toCharArray());
//            options.setConnectionTimeout(10);
//            options.setKeepAliveInterval(30);
            options.setCleanSession(true);
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            client.connect(options);
            if(client.isConnected()){
                try {
                    //client.setBufferOpts(getDisconnectedBufferOptions());
                    String msg = body.toString();
                    byte[] encodedPayload = new byte[0];
                    encodedPayload = msg.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(Configuration.MqttTopic+ "reset/"+ macAddress , message);
                    client.disconnect();
                } catch (Exception e) {
                    Log.e("publishDfuResult", e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("Sethala MQTT", e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }
}
