package com.example.aplikacja3;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;



public class DownloadService extends IntentService {
    private static final String ACTION_DOWNLOAD = "com.example.aplikacja3.action.DOWNLOAD";
    public static final String EXTRA_URL = "com.example.aplikacja3.extra.URL";

    private LocalBroadcastManager broadcastManager;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    public static void startDownload(Context context, String url) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_URL, url);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                String url = intent.getStringExtra(EXTRA_URL);
                downloadFile(url);
            }

        }
    }

    private void downloadFile(String url) {
        // ...
        // implement the file downloading logic here
        // ...

        int progressInfo = 50; // set the progress information
        // send a broadcast with the result of downloading the file
        Intent intent = new Intent("com.example.aplikacja3.PROGRESS_UPDATE");
        intent.putExtra("progress", progressInfo);
        broadcastManager.sendBroadcast(intent);
    }


}
