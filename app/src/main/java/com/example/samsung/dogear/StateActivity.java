package com.example.samsung.dogear;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.*;

public class StateActivity extends ActionBarActivity {
    Button icon1, icon2, icon3;
    Button back_btn, alarmBtn, sch;
    TextView state, temp, act, btn_text;
    int displayShowTitle;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
    private Date currentDate = new Date();
    private Date dateTimeMonth;
    private ViewPager pager;
    private int numberOfTabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_btn);

        icon1 = (Button) findViewById(R.id.icon1);
        icon2 = (Button) findViewById(R.id.icon2);
        icon3 = (Button) findViewById(R.id.icon3);
        back_btn = (Button) findViewById(R.id.back_btn);
        alarmBtn = (Button) findViewById(R.id.alarm);
        state = (TextView) findViewById(R.id.state);
        temp = (TextView) findViewById(R.id.temp);
        act= (TextView)findViewById(R.id.act);
        btn_text =(TextView)findViewById(R.id.btn_text);

        btn_text.setText("State");
        SharedPreferences sp = getSharedPreferences("current_step", 0);
        temp.setText(Integer.toString(sp.getInt("current_temp", 0))+"도");

        icon1.setOnClickListener(listner);
        icon2.setOnClickListener(listner);
        icon3.setOnClickListener(listner);
        back_btn.setOnClickListener(listner);
        alarmBtn.setOnClickListener(listner);
    }

    View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.icon1:
                    Intent intent1 = new Intent(StateActivity.this, MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.icon2:
                    break;
                case R.id.icon3:
                    Intent intent = new Intent(StateActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.back_btn:
                    Intent back = new Intent(StateActivity.this, MainActivity.class);
                    startActivity(back);
                    break;
                case R.id.alarm:  //push 알람부분
                    NotificationManager notificationManager = (NotificationManager) StateActivity.this.getSystemService(StateActivity.this.NOTIFICATION_SERVICE);
                    Intent intent0 = new Intent(StateActivity.this.getApplicationContext(), StateActivity.class); //인텐트 생성.


                    Notification.Builder builder = new Notification.Builder(getApplicationContext());
                    intent0.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    PendingIntent pendingNotificationIntent = PendingIntent.getActivity(StateActivity.this, 0, intent0, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setSmallIcon(R.drawable.doogear2).setTicker("DOGEAR").setWhen(System.currentTimeMillis())
                            .setNumber(1).setContentTitle("병원가는 날").setContentText("오늘은 정기검진이 있는 날입니다")
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingNotificationIntent).setAutoCancel(true).setOngoing(true);

                    notificationManager.notify(1, builder.build()); // Notification send
                    break;
            }
        }
    };
}