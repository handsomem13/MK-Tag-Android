package com.moko.bxp.sethala.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonObject {
    private static GsonBuilder gson_builder = new GsonBuilder();

    public static JsonObject convert(String jsonString) {
        Gson gson = gson_builder.create();
        JsonElement element = gson.fromJson(jsonString, JsonElement.class );
        return element.getAsJsonObject();
    }
}
