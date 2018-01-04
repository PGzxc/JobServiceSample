package com.example.jobservicesample;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 2018/1/4.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {
    private final static String TAG=MyJobService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        if(isNetworkConnected()){
            new WebDownLoadTask().execute(params);
            return true;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");
        return false;
    }
    private boolean isNetworkConnected(){
        ConnectivityManager connManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    private class WebDownLoadTask extends AsyncTask<JobParameters,Void,String> {
        protected  JobParameters mJobParam;
        @Override
        protected String doInBackground(JobParameters... params) {
            Log.d(TAG, "doInBackground: ");
            mJobParam=params[0];
            try{
                InputStream is=null;
                int len=50;
                URL url=new URL("https://www.baidu.com");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                int response=conn.getResponseCode();
                is=conn.getInputStream();
                Reader reader=new InputStreamReader(is,"UTF-8");
                char[] buffer=new char[len];
                reader.read(buffer);
                return new String(buffer);
            }catch (IOException e){
            e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jobFinished(mJobParam,false);
            Log.d(TAG, "onPostExecute: ");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
