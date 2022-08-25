package com.example.samsung.dogear;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

public class StrengthActivity extends AppCompatActivity {
    private DBHelper4 helper;
    SQLiteDatabase db;
    Cursor cursor;
    Button back_btn, save_set, help;
    ListView mListView;

    private FrameLayout mainLayout;
    private PieChart mChart;
    private final static int LEGEND = 1;
    private float[] yData = {5, 10, 15, 30, 40};
    private String[] xData = new String[]{"Sony", "Huawei", "LG", "Apple", "Samsung"};
    private String[] kind0 = {"1단계", "2단계", "3단계", "4단계", "5단계", "6단계", "7단계", "8단계", "9단계", "10단계"};
    private String[] kind1 = {"음성녹음/재생", "진동자극", "전기자극"};
    private String[] kind2 = {"1", "2", "3"};
    AlertDialog.Builder alt;
    final static String TABLE_NAME="strength";
    private String str1,str2,str3;

    private int index1, index2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_btn);
        TextView btn_text = (TextView) findViewById(R.id.btn_text);
        btn_text.setText("Device");

        back_btn = (Button) findViewById(R.id.back_btn);
        help = (Button) findViewById(R.id.help);
        save_set = (Button) findViewById(R.id.save_set);
        back_btn.setOnClickListener(listener);
        help.setOnClickListener(listener);
        save_set.setOnClickListener(listener);
        mListView = (ListView)findViewById(R.id.strength_list);
        CustomList mAdapter = new CustomList(StrengthActivity.this);
        mListView.setAdapter(mAdapter);

        helper = new DBHelper4(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = helper.getReadableDatabase();
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_btn:
                    Intent back = new Intent(StrengthActivity.this, SettingActivity.class);
                    startActivity(back);
                    break;
                case R.id.help:
                    Intent help = new Intent(StrengthActivity.this, helpActivity.class);
                    startActivity(help);
                    break;
                case R.id.save_set: //저장된 설정을 가져와야 돼 ㅁㅇㅁ
                   /* db.execSQL("INSERT INTO strength VALUES (null, '" + str_email + "','" + str_password +
                            "','" + str_phone + "');");*/
                    Intent save = new Intent(StrengthActivity.this, SettingActivity.class);
                    startActivity(save);
                    break;
            }
        }
    };


    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;

        public CustomList(Activity context) {
            super(context, R.layout.strength_item, kind0);
            this.context = context;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.strength_item, null);
            }
            final Button button = (Button)v.findViewById(R.id.step);
            final TextView tv1 = (TextView)v.findViewById(R.id.stepkind);
            final TextView tv2 =(TextView)v.findViewById(R.id.stepstr);
            button.setText(kind0[position]);

            cursor= db.rawQuery("SELECT kind, level FROM strength WHERE step ='" + button.getText().toString() + "';", null);

            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                tv1.setText(cursor.getString(0));
                tv2.setText(cursor.getString(1));
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str1 = button.getText().toString();
                    alt = new AlertDialog.Builder(StrengthActivity.this);
                    alt.setTitle(kind0[position]+" - "+"자극 종류");
                    alt.setSingleChoiceItems(kind1, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            index1 = i;
                            str2 = kind1[i];
                        }
                    });
                    alt.setPositiveButton("계속", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder alt2 = new AlertDialog.Builder(StrengthActivity.this);
                            alt2.setTitle(kind0[position]+" - "+"자극 강도");
                            alt2.setSingleChoiceItems(kind2, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    index2 = i;
                                    str3 = kind2[i];
                                }
                            });
                            alt2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(tv1.getText().toString().equals("")) {
                                        db.execSQL("INSERT INTO strength VALUES (null, '" + str1 + "','" + str2 +
                                                "','" + str3 + "');");
                                    } else {
                                        db.execSQL("UPDATE strength SET kind='" + str2
                                                + "', level='"+ str3 + "' WHERE step='"+ str1 + "';");
                                        Log.i("data1", "OK");
                                    }
                                    tv1.setText(kind1[index1]);
                                    tv2.setText(kind2[index2]);

                                }
                            });
                            alt2.setNegativeButton("닫기", null);
                            alt2.show();
                        }
                    });
                    alt.setNegativeButton("닫기", null);
                    alt.show();
                }
            });
            return v;
        }
    }

}




