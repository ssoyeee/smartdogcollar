package com.example.samsung.dogear;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    Button icon1, icon2, icon3, play_btn, rest_btn, active_btn,target_ok;
    public ImageView iv = null;
    TextView total_ft, ing_ft;
    //   static final int GET_TARGET = 3;

    Bitmap bitmap;

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("DOGEAR")
                    .setMessage("종료하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icon1 = (Button) findViewById(R.id.icon1);
        icon2 = (Button) findViewById(R.id.icon2);
        icon3 = (Button) findViewById(R.id.icon3);
        iv = (ImageView) findViewById(R.id.main_img);
        active_btn = (Button) findViewById(R.id.distance_btn);
        rest_btn = (Button) findViewById(R.id.temp_btn);
        total_ft = (TextView) findViewById(R.id.total_ft);
        ing_ft = (TextView) findViewById(R.id.ing_ft);
        TextView bark_text = (TextView) findViewById(R.id.bark);
        TextView distance = (TextView) findViewById(R.id.distance);
        TextView temperate = (TextView) findViewById(R.id.temperate);
        ImageView main_img = (ImageView) findViewById(R.id.main_img);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_main);

        Drawable alpha = ((ImageView) findViewById(R.id.main_img)).getBackground();
        alpha.setAlpha(70);


        SharedPreferences sp = getSharedPreferences("target_step", 0);
        SharedPreferences sp2 = getSharedPreferences("current_step", 0);
        SharedPreferences sp3 = getSharedPreferences("dog_name", 0);
        SharedPreferences sp4 = getSharedPreferences("dog_age", 0);

        //bitmap =

        int walk_count = sp2.getInt("current_walk", 0);

        total_ft.setText(sp.getString("target_walk", "10000"));
        Log.i("current",Integer.toString(sp2.getInt("current_walk", 0)));
        ing_ft.setText(Integer.toString(walk_count));
        bark_text.setText(Integer.toString(sp2.getInt("current_bark", 0)));
        distance.setText(Integer.toString(walk_count*12)+"cm");
        temperate.setText(Integer.toString(sp2.getInt("current_temp", 0))+"℃");


        icon1.setOnClickListener(listner);
        icon2.setOnClickListener(listner);
        icon3.setOnClickListener(listner);

    }

    View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.icon1:
                    break;
                case R.id.icon2:
                    Intent intent1 = new Intent(MainActivity.this, StateActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.icon3:
                    Intent intent2 = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent2);
                    break;

            }
        }
    };

}