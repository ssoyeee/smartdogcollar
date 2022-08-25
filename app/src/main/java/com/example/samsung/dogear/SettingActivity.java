package com.example.samsung.dogear;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "Setting";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    SharedPreferences sf;
    String sfName = "file";
    String dog_name;
  //  static final int GET_TARGET = 3;

    Button icon1, icon2, icon3;
    Button edit_bnt, edit_target_btn, select_btn, device_control_btn;
    Button module1, module2, module3, module4, ble_search;
    TextView walk_value, ble_dev,total_ft;
    Button back_btn, ble_btn;
    SharedPreferences.Editor editor;


    private DBPhoto dbPhoto;
    SQLiteDatabase db, db1;
    //   static final int GET_DOG = 1;



    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_setting);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_setting);

        TextView btn_text = (TextView) findViewById(R.id.btn_text);
        btn_text.setText("Setting");

        ImageView img_view = (ImageView) findViewById(R.id.img_view);
        icon1 = (Button) findViewById(R.id.icon1);
        icon2 = (Button) findViewById(R.id.icon2);
        icon3 = (Button) findViewById(R.id.icon3);
        edit_bnt = (Button) findViewById(R.id.edit_bnt);
        edit_target_btn = (Button) findViewById(R.id.edit_target_bnt);
        walk_value = (TextView) findViewById(R.id.walk_value);
        back_btn = (Button) findViewById(R.id.back_btn);
        ble_btn = (Button) findViewById(R.id.ble_btn);
        select_btn = (Button) findViewById(R.id.select_btn);
        device_control_btn = (Button) findViewById(R.id.device_control_btn);
        ble_dev = (TextView) findViewById(R.id.ble_dev);
        module1 = (Button) findViewById(R.id.module1);
        module2 = (Button) findViewById(R.id.module2);
        module3 = (Button) findViewById(R.id.module3);
        module4 = (Button) findViewById(R.id.module4);
        total_ft=(TextView) findViewById(R.id.total_ft);


        dbPhoto = new DBPhoto(this);
        try {
            db1 = dbPhoto.getWritableDatabase();
        } catch (SQLiteException ex) {
            db1 = dbPhoto.getReadableDatabase();
        }

        SharedPreferences sp = getSharedPreferences("target_step", 0);
        SharedPreferences sp2 = getSharedPreferences("select_dog", 0);

        walk_value.setText(sp.getString("target_walk", "10000"));
        editor = sp.edit();

        dog_name = sp2.getString("select_dog_name","");

        if(!dog_name.equals("")) {
            Cursor result = db1.rawQuery("select img from photo where name='"+dog_name +"'", null);
            result.moveToFirst();
            int count = result.getCount();

            Log.i("result", String.valueOf(count));

            if(count > 0) {
                Bitmap bit = getPhoto(result.getBlob(0));
                img_view.setImageBitmap(bit);

                Log.i("blob", String.valueOf(result.getBlob(0)));

            }
        }

        icon1.setOnClickListener(listner);
        icon2.setOnClickListener(listner);
        icon3.setOnClickListener(listner);
        edit_bnt.setOnClickListener(listner);
        edit_target_btn.setOnClickListener(listner);
        back_btn.setOnClickListener(listner);
        select_btn.setOnClickListener(listner);
        device_control_btn.setOnClickListener(listner);
        module1.setOnClickListener(listner);
        module2.setOnClickListener(listner);
        module3.setOnClickListener(listner);
        module4.setOnClickListener(listner);
        ble_btn.setOnClickListener(listner);
        ble_dev.setOnClickListener(listner);
    }


    public Bitmap getPhoto(byte[] b) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

    View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.icon1:
                    Intent intent1 = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.icon2:
                    Intent intent = new Intent(SettingActivity.this, StateActivity.class);
                    startActivity(intent);
                    break;
                case R.id.icon3:
                    break;
                case R.id.edit_bnt:
                    Intent intent2 = new Intent(SettingActivity.this, EditProfileActivityActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.edit_target_bnt:
                    final Dialog target_dialog = new Dialog(SettingActivity.this);
                    target_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    target_dialog.setContentView(R.layout.activity_dialog_target);

                    Button target_save = (Button) target_dialog.findViewById(R.id.target_save);
                    final NumberPicker target_picker = (NumberPicker) target_dialog.findViewById(R.id.target_picker);
                    target_picker.setMinValue(0);
                    target_picker.setMaxValue(20);
                    target_picker.setWrapSelectorWheel(true);
                    target_picker.setValue(Integer.parseInt(walk_value.getText().toString())/1000);
                    List<String> displayedValues = new ArrayList<>();
                    for (int i = 0; i <= 20000; i += 1000)
                    {
                        displayedValues.add(String.format("%01d", i));
                    }
                    target_picker.setDisplayedValues(displayedValues.toArray(new String[0]));

                    target_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            walk_value.setText(String.valueOf(0+(target_picker.getValue()*1000)));
                            editor.putString("target_walk", walk_value.getText().toString());
                            editor.commit();
                            target_dialog.dismiss();
                        }
                    });
                    target_dialog.show();
                    break;

                case R.id.target_save:
                    Intent save = new Intent();
                    save.putExtra("INPUT_TARGET", total_ft.getText().toString());
                    setResult(RESULT_OK, save);
                    finish();
                    break;
                case R.id.back_btn:
                    Intent back = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(back);
                    break;
                case R.id.select_btn:
                    Intent select = new Intent(SettingActivity.this, SelectDogActivity.class);
                    startActivity(select);
                    break;
                case R.id.device_control_btn:
                    Intent control = new Intent(SettingActivity.this, StrengthActivity.class);
                    startActivity(control);
                    break;
                case R.id.module1:
                    Intent module1 = new Intent(SettingActivity.this, ModuleActivity.class);
                    startActivity(module1);
                    break;
                case R.id.module2:
                    Intent module2 = new Intent(SettingActivity.this, ModuleActivity.class);
                    startActivity(module2);
                    break;
                case R.id.module3:
                    Intent module3 = new Intent(SettingActivity.this, ModuleActivity.class);
                    startActivity(module3);
                    break;
                case R.id.module4:
                    Intent module4 = new Intent(SettingActivity.this, ModuleActivity.class);
                    startActivity(module4);
                    break;
                case R.id.ble_btn:
                    Intent i = new Intent(SettingActivity.this, DeviceScanActivity.class);
                    startActivity(i);
                    break;

                }
        }
    };
}
