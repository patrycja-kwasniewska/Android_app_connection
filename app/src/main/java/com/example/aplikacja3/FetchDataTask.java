//package com.example.aplikacja3;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.provider.ContactsContract;
//
//import java.io.BufferedInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class FetchDataTask extends AsyncTask<String, Integer, Boolean> {
//
//    private Context mContext;
//    private ProgressDialog mProgressDialog;
//    private String mFilePath;
//
//    public FetchDataTask(Context context) {
//        mContext = context;
//    }
//
//    public String getFilePath() {
//        return mFilePath;
//    }
//
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setMessage("Fetching data...");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
//    }
//
//    @Override
//    protected Boolean doInBackground(String... params) {
//        String url = params[0];
//        mFilePath = params[1];
//
//        try {
//            URL downloadUrl = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//
//            int fileLength = connection.getContentLength();
//
//            InputStream input = new BufferedInputStream(downloadUrl.openStream());
//            OutputStream output = new FileOutputStream(mFilePath);
//
//            byte[] data = new byte[1024];
//            long total = 0;
//            int count;
//            while ((count = input.read(data)) != -1) {
//                total += count;
//                publishProgress((int) (total * 100 / fileLength));
//                output.write(data, 0, count);
//            }
//
//            output.flush();
//            output.close();
//            input.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    protected void onProgressUpdate(Integer... progress) {
//        super.onProgressUpdate(progress);
//        mProgressDialog.setProgress(progress[0]);
//    }
//
//    protected void onPostExecute(ContactsContract.Contacts.Data result) {
//        super.onPostExecute(result);
//        mProgressDialog.dismiss();
//        ((MainActivity) mContext).updateUI(mFilePath, result);
//    }
//}
