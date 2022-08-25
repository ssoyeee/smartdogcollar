package com.example.samsung.dogear;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

//import android.graphics.Bitmap;

public class EditProfileActivityActivity extends ActionBarActivity {

    Button back_btn;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int CROP_FROM_CAMERA = 3;

    ImageButton imgbtn;
    private Uri mImageCaptureUri;
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
        setContentView(R.layout.edit_profile_activity);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_btn);

        TextView btn_text = (TextView) findViewById(R.id.btn_text);
        btn_text.setText("Edit Profile");

        back_btn = (Button) findViewById(R.id.back_btn);
        imgbtn = (ImageButton) findViewById(R.id.photo);
     //   mPhotoImageView = (ImageView) findViewById(R.id.image);

        back_btn.setOnClickListener(listner);
        imgbtn.setOnClickListener(listner);
    }

    View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.back_btn:
                    Intent back = new Intent(EditProfileActivityActivity.this, SettingActivity.class);
                    startActivity(back);
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

                    new AlertDialog.Builder(EditProfileActivityActivity.this)
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
                    imgbtn.setImageBitmap(CropCircle(photo));
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
                imgbtn.setImageBitmap(CropCircle(output));
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
}
