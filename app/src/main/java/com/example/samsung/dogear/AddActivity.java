package com.example.samsung.dogear;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AddActivity extends AppCompatActivity {

    Button back_btn,submit_add;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private DBHelper2 helper2;
    private DBPhoto dbPhoto;
    SQLiteDatabase db, db1;
    Cursor cursor;
    String str1, str2, str3, str4, str5, str6;
    RadioGroup rg;
    RadioButton rd;
    EditText dog_name, dog_birth, dog_weight, dog_height, dog_length;
    ImageButton imgbtn;
    private Uri mImageCaptureUri;

    Bitmap pho, pho2;
    byte[] img;
    // private ImageView mPhotoImageView;

    public Bitmap CropCircle(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 84;
        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_btn);

        TextView btn_text = (TextView) findViewById(R.id.btn_text);
        btn_text.setText("Add Profile");

        back_btn = (Button) findViewById(R.id.back_btn);
        imgbtn = (ImageButton) findViewById(R.id.photo);
        submit_add=(Button)findViewById(R.id.submit_add);
        //   mPhotoImageView = (ImageView) findViewById(R.id.image);

        rg = (RadioGroup)findViewById(R.id.rg);
        dog_name = (EditText)findViewById(R.id.dog_name);
        dog_birth = (EditText)findViewById(R.id.dog_birth);
        dog_weight = (EditText)findViewById(R.id.dog_weight);
        dog_height = (EditText)findViewById(R.id.dog_height);
        dog_length = (EditText)findViewById(R.id.dog_length);

        back_btn.setOnClickListener(listner);
        imgbtn.setOnClickListener(listner);
        submit_add.setOnClickListener(listner);

        helper2 = new DBHelper2(this);
        dbPhoto = new DBPhoto(this);
        try {
            db = helper2.getWritableDatabase();
            db1 = dbPhoto.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = helper2.getReadableDatabase();
            db1 = dbPhoto.getReadableDatabase();
        }
    }

    View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.back_btn:
                    Intent back = new Intent(AddActivity.this, SelectDogActivity.class);
                    startActivity(back);
                    break;
                case R.id.submit_add:
                    str1 = dog_name.getText().toString();
                    str2  = dog_birth.getText().toString();
                    str3 = dog_weight.getText().toString();
                    str4 = dog_height.getText().toString();
                    str5 = dog_length.getText().toString();
                    rd = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                    str6 = rd.getText().toString();

                    if(!str2.contains(".")) {
                        Toast.makeText(AddActivity.this, "BIRTH DAY 형식이 잘못되었습니다.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent add = new Intent(AddActivity.this, SelectDogActivity.class);
                        startActivity(add);

                        db.execSQL("INSERT INTO doginfo VALUES (null, '" + str1 + "','" + str2 +
                                "','" + str3 + "','" + str4 + "','" + str5 + "','" + str6 + "');");

                        SQLiteStatement p = db1.compileStatement("insert into photo(img, name) values(?, ?);");
                        p.bindBlob(1, img);
                        p.bindString(2, str1);
                        p.execute();

                        Log.i("data1", "OK");
                    }
                    break;
                case R.id.photo:
                    DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

                            intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);

                            try {
                                intent.putExtra("data", true);
                                startActivityForResult(intent, PICK_FROM_CAMERA);
                            } catch (ActivityNotFoundException e) {
                                // Do nothing for now
                            }
                        }
                    };
                    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            // Gallery 호출
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            // 잘라내기 셋팅
                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 0);
                            intent.putExtra("aspectY", 0);
                            intent.putExtra("outputX", 200);
                            intent.putExtra("outputY", 150);
                            try {
                                intent.putExtra("data", true);
                                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
                            } catch (ActivityNotFoundException e) {
                                // Do nothing for now
                            }
                        }
                    };

                    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    };

                    new AlertDialog.Builder(AddActivity.this)
                            .setTitle("프로필 선택")
                            .setPositiveButton("카메라", cameraListener)
                            .setNeutralButton("갤러리", albumListener)
                            .setNegativeButton("취소", cancelListener)
                            .show();
                    break;
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CROP_FROM_CAMERA) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                pho = CropCircle(photo);
                imgbtn.setImageBitmap(pho);

                img = getByteArrayFromBitmap(pho);

            }
            // 임시 파일 삭제
            File f = new File(mImageCaptureUri.getPath());
            if (f.exists()) {
                f.delete();
            }
        }

        if (requestCode == PICK_FROM_GALLERY) {
            Bundle extras2 = data.getExtras();
            if (extras2 != null) {
                Bitmap output = extras2.getParcelable("data");
                pho2 = CropCircle(output);
                imgbtn.setImageBitmap(pho2);

                img = getByteArrayFromBitmap(pho2);
            }
        }

        if(requestCode == PICK_FROM_CAMERA){
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(mImageCaptureUri, "image/*");

            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 0);
            intent.putExtra("aspectY", 0);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 150);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, CROP_FROM_CAMERA);
        }
    }

    public byte[] getByteArrayFromBitmap(Bitmap b) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();


        return data;
    }
}


