package com.example.jobservicesample;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    private static final String TAG=MainActivity.class.getSimpleName();

    TextView mWakeLockMsg;
    Button mButton;
    ComponentName mServieComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWakeLockMsg=findViewById(R.id.text);
        mServieComponent=new ComponentName(this,MyJobService.class);
        Intent startServiceIntent=new Intent(this,MyJobService.class);
        startService(startServiceIntent);

        mButton=findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollServer();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void pollServer(){
        JobScheduler scheduler=(JobScheduler)this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        for(int i=0;i<10;i++){
            JobInfo jobInfo=new JobInfo.Builder(i,mServieComponent)
                    /*
                    * 这会使你的工作不启动直到规定的毫秒数已经过去了
                    * */
                    .setMinimumLatency(5000)
                    /*
                    * 这将设置你的工作期限。即使是无法满足其他要求，你的任务将约在规定的时间已经过去时开始执行
                    * */
                    .setOverrideDeadline(60000)
                    /*
                    * 只有在设备处于一种特定的网络中时，它才启动
                    * 默认值是JobInfo.NETWORK_TYPE_NONE :无论是否有网络连接，该任务均可以运行
                    * JobInfo.NETWORK_TYPE_ANY，这需要某种类型的网络连接可用，工作才可以运行
                    * JobInfo.NETWORK_TYPE_UNMETERED，这就要求设备在非蜂窝网络中
                    * */
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build();
            mWakeLockMsg.append("schedule job "+i+" !\n");
            scheduler.schedule(jobInfo);
        }
    }
}
