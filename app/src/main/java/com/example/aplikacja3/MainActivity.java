package com.example.aplikacja3;

import android.Manifest;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 1;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private EditText urlEditText;
    private Button downloadButton;
    private Button fetchButton;
    private static TextView sizeTextView;
    private static TextView typeTextView;
    private static TextView bytesOutput;
    private ProgressDialog progressDialog;
    private int downloadProgress = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading file...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        //////


        urlEditText = findViewById(R.id.url_input);
        fetchButton = findViewById(R.id.fetch_button);
        downloadButton = findViewById(R.id.download_button);
        sizeTextView = findViewById(R.id.size_output);
        typeTextView = findViewById(R.id.type_output);
        bytesOutput = findViewById(R.id.bytes_output);

        //notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        urlEditText.setText("https://example.com");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading file...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlEditText.getText().toString();

                if (checkPermissions()) {
                    new DownloadService.DownloadTask().execute(url);
                } else {
                    requestPermissions();
                }
            }
        });


        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = urlEditText.getText().toString();

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlString)); //allow types of network to download files
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);

                request.setTitle("Download"); //set title in download notification
                request.setDescription("Downloading file...."); //set description in download notification
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + System.currentTimeMillis());

                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);


                //                if (checkPermissions()) {
//                    // ZMIANA
//                    //new DownloadAndSaveTask().execute(urlString);
//                    Intent downloadIntent = new Intent(MainActivity.this, DownloadService.class);
//                    downloadIntent.putExtra("url", urlString);
//                    startService(downloadIntent);
//
//                } else {
//                    requestPermissions();
//                }
            }
        });


    }

    private boolean checkPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkPermissions()) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class DownloadService extends IntentService {

        public DownloadService() {
            super("DownloadService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String urlString = intent.getStringExtra("url");
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int totalBytes = connection.getContentLength();
                InputStream inputStream = connection.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory(), "downloaded_file");
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                int downloadedBytes = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    downloadedBytes += bytesRead;
                }
                outputStream.close();
                inputStream.close();
                connection.disconnect();
                Log.d("Download", "File downloaded and saved successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static class DownloadTask extends AsyncTask<String, Void, FileData> {

            @Override
            protected FileData doInBackground(String... urls) {
                String urlString = urls[0];
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream inputStream = connection.getInputStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    byte[] fileBytes = outputStream.toByteArray();
                    int fileSize = fileBytes.length;
                    String fileType = connection.getContentType();

                    return new FileData(fileSize, fileType, fileBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(FileData fileData) {
                if (fileData != null) {
                    sizeTextView.setText("Size: " + fileData.getSize());
                    typeTextView.setText("Type: " + fileData.getType());


                } else {
                    sizeTextView.setText("Download failed");
                    typeTextView.setText("");
                }


            }

            private byte[] downloadFile(String urlString) throws IOException {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }


                return outputStream.toByteArray();
            }

            private void saveFile(byte[] fileBytes, String fileName) {
                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    fos.write(fileBytes);
                    System.out.printf("File %s saved successfully.%n", fileName);
                    bytesOutput.setText("Downloaded: " + fileBytes + " bytes");
                } catch (IOException ex) {
                    System.out.printf("An error occurred while saving file %s.%n", fileName);
                    ex.printStackTrace();
                }
            }
        }
    }
}




