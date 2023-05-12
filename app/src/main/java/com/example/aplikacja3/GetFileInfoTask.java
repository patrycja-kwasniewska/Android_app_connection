//package com.example.aplikacja3;
//
//import android.os.AsyncTask;
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class GetFileInfoTask extends AsyncTask<String, Void, String[]> {
//    protected String[] doInBackground(String... urls) {
//        String url = urls[0];
//        String[] fileInfo = new String[2];
//
//        try {
//            URL obj = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
//            conn.setRequestMethod("HEAD");
//
//            fileInfo[0] = conn.getHeaderField("Content-Length");
//            fileInfo[1] = conn.getHeaderField("Content-Type");
//
//            conn.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return fileInfo;
//    }
//
//    protected void onPostExecute(String[] fileInfo) {
//        // handle results of the task
//    }
//}
