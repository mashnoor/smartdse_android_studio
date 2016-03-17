package com.smartdse2.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by mashnoor on 3/17/16.
 */
public class BackupRestore {
    final String BACKUP_LINK = "http://104.131.22.246/dev/smartdsefiles/userbackup/";
    final String DEST_FILE = "/data/data/com.smartdse2.android/databases/dsedatabase";
    String userEmail;
    Activity activity;
    public BackupRestore(Activity activity)
    {
        this.activity = activity;
        userEmail = LoginHelper.getUserEmail(activity);
    }
    public void backup()
    {
        new backup_async().execute();
    }



    public  void restore()
    {


        final ProgressDialog progressDialog = ProgressDialog.show(activity, "",
                "Restoring your data from SmartDSE Server...", true);



        Uri downloadUri = Uri.parse(BACKUP_LINK +userEmail);
        Uri destinationUri = Uri.parse(DEST_FILE);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)

                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)

                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Log.d("----------------", "Complete");
                        if(progressDialog.isShowing() && progressDialog!=null)
                        {
                            progressDialog.dismiss();

                        }
                        Toast.makeText(activity, "Successfully resored data from Server", Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                        Log.d("----------------", "Failed");
                        if(progressDialog.isShowing() && progressDialog!=null)
                        {
                            progressDialog.dismiss();

                        }
                        Toast.makeText(activity, "Some Error Occured!", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {

                        Log.d("----------------", "Progress");
                    }
                });
        ThinDownloadManager downloadManager;


        downloadManager = new ThinDownloadManager();
        int downloadId = downloadManager.add(downloadRequest);
        Log.d("_______________", "" + downloadId);




    }
    public class restore_async extends AsyncTask<Void, Void, Void>
    {
        final ProgressDialog progressDialog = ProgressDialog.show(activity, "",
                "Restoring your data from SmartDSE Server...", true);

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }





    public class backup_async extends AsyncTask<Void, Void, Void>
    {
        final ProgressDialog progressDialog = ProgressDialog.show(activity, "",
                "Backing up your data in SmartDSE Server...", true);


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            String inFileName = activity.getDatabasePath("dsedatabase").getPath();
            try {
                // Set your file path here
                Log.i("fSnd", inFileName);
                FileInputStream fstrm = new FileInputStream(inFileName);

                String fileName = LoginHelper.getUserEmail(activity);
                // Set your server page url (and the file title/description)
                HttpFileUpload hfu = new HttpFileUpload("http://104.131.22.246/dev/smartdsefiles/upload.php",inFileName, fileName,"my file description");

                hfu.Send_Now(fstrm);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            return null;
        }
    }



}
