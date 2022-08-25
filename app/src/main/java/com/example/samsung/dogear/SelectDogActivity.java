package com.example.samsung.dogear;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SelectDogActivity extends AppCompatActivity {

    ListView mListView;
    private DBHelper2 helper2;
    private DBPhoto dbPhoto;
    SQLiteDatabase db, db1;
    CustomList mAdapter;
    Button add_btn, back_btn;
    String name, birthday, day;
    SharedPreferences.Editor editor;
    int year, month, date;
    int tar_id;
    int i=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dog);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_btn);

        TextView btn_text = (TextView) findViewById(R.id.btn_text);
        btn_text.setText("Select Dog");

        back_btn = (Button) findViewById(R.id.back_btn);
        mListView = (ListView) findViewById(R.id.dog_list);
        add_btn = (Button) findViewById(R.id.add_btn);

        helper2 = new DBHelper2(this);
        dbPhoto = new DBPhoto(this);
        try {
            db = helper2.getWritableDatabase();
            db1 = dbPhoto.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = helper2.getReadableDatabase();
            db1 = dbPhoto.getReadableDatabase();
        }

        // ------------------------------------------------------------------------------------------------------------------------------
        SharedPreferences sp = getSharedPreferences("select_dog", 0);
        editor = sp.edit();


        ArrayList<DBHelper2.doginfo> arrayList = helper2.getAlldata();
        mAdapter = new CustomList(this, arrayList);
        mListView.setAdapter(mAdapter);

        if(helper2.getAlldata().isEmpty()){
            //dataInsert();
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // init();
               DBHelper2.doginfo list= (DBHelper2.doginfo)mListView.getItemAtPosition(i);
               name = list.dname;
               day = list.dbrith;

                callDialog(name, 3);


            }
        });

        add_btn.setOnClickListener(listener);
        back_btn.setOnClickListener(listener);
    }


    void callDialog(String Name, int Age) {
        final String na = Name;
        final int ag = Age;
        final Dialog dialog = new Dialog(SelectDogActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_dialog);

        Button connect_btn = (Button) dialog.findViewById(R.id.connect_btn);
        Button delete_btn = (Button) dialog.findViewById(R.id.delete_btn);

        dialog.show();
        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("select_dog_name", na);
                editor.putInt("select_dog_age", ag);
                editor.commit();
                dialog.dismiss();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_btn:
                    Intent in = new Intent(SelectDogActivity.this, AddActivity.class);
                    startActivity(in);
                    break;
                case R.id.back_btn:
                    Intent back = new Intent(SelectDogActivity.this, SettingActivity.class);
                    startActivity(back);
                    break;
            }

        }
    };



    void init(){
        name = null;
        day = null;
        mAdapter.clear();
        mAdapter.addAll(helper2.getAlldata());
        mAdapter.notifyDataSetChanged();
        mListView.invalidate();
    }

    public void delete(){
        db.delete("doginfo", "_id = ?", new String[] { Integer.toString(tar_id) });
        init();
    }

    public class CustomList extends ArrayAdapter<DBHelper2.doginfo>
    {
        private LayoutInflater mInflater;

        public CustomList(Context context, ArrayList<DBHelper2.doginfo> dogLists)
        {
            super(context,0,dogLists);
            mInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        public View getView(int position, View v, ViewGroup parent) {
            View view = null;

            if (v == null) {
                view = mInflater.inflate(R.layout.list_select_dog, null);

            } else {
                view = v;
            }
            final DBHelper2.doginfo data = this.getItem(position);

            if (data != null) {
                TextView name = (TextView) view.findViewById(R.id.name_dog);
                TextView age = (TextView) view.findViewById(R.id.age_dog);
                ImageView imageView = (ImageView) view.findViewById(R.id.image);

                long now = System.currentTimeMillis();
                Date hdate = new Date(now);
                SimpleDateFormat format = new SimpleDateFormat("yyyy");
                int hyear = Integer.parseInt(format.format(hdate));
                name.setText(data.dname);
                birthday = data.dbrith;

                Log.i("birthday", birthday);

                if(!birthday.equals("")) {
                    String[] str = birthday.split("\\.");
                    year = Integer.parseInt(str[0]);

                    Cursor result = db1.rawQuery("select img from photo where name='"+data.dname +"'", null);
                    result.moveToFirst();
                    int count = result.getCount();

                    Log.i("result", String.valueOf(count));

                    if(count > 0) {
                        Bitmap bit = getPhoto(result.getBlob(0));
                        imageView.setImageBitmap(bit);

                        Log.i("blob", String.valueOf(result.getBlob(0)));

                    }
                    int age2 = hyear - year + 1;
                    age.setText(Integer.toString(age2) + "세");
                }
            }
            return view;
        }
    }

    public Bitmap getPhoto(byte[] b) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }



}

