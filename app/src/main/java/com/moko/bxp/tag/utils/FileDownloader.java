package com.moko.bxp.tag.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader extends AsyncTask<String, Void, byte[]> {
    private Context context;

    public FileDownloader(Context context) {
        this.context = context;
    }

    @Override
    protected byte[] doInBackground(String... urls) {
        String fileUrl = urls[0];
        byte[] fileBytes = null;

        try {
            // Download the file
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            String filename = fileUrl.split("/")[fileUrl.split("/").length-1 ];
            // Save the file locally
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close streams
            inputStream.close();
            outputStream.close();

            // Read the file to bytes
            fileBytes = FileUtils.readFileToBytes(context, filename);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileBytes;
    }
}
