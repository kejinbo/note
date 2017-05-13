package com.example.administrator.note_demo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/11.
 */

public class AddContent extends Activity implements View.OnClickListener {
    private String val;
    private Button savebtn, deletebtn;
    private EditText ettext;
    private ImageView c_img;
    private VideoView c_video;
    private NotesDB mNotesDB;
    private SQLiteDatabase dbWriter;
    private File phone,video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);
        val = getIntent().getStringExtra("flag");
        savebtn = (Button) findViewById(R.id.save);
        deletebtn = (Button) findViewById(R.id.delete);
        ettext = (EditText) findViewById(R.id.ettext);
        c_img = (ImageView) findViewById(R.id.c_img);
        c_video = (VideoView) findViewById(R.id.c_video);
        savebtn.setOnClickListener(this);
        deletebtn.setOnClickListener(this);
        mNotesDB = new NotesDB(this);
        dbWriter = mNotesDB.getWritableDatabase();
        initView();
    }
    public void initView(){
        if(val.equals("1")){
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.GONE);
        }
        if(val.equals("2")){
            c_img.setVisibility(View.VISIBLE);
            c_video.setVisibility(View.GONE);
            Intent iimg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            phone = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+getTime()+".jpg");
            iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phone));
            startActivityForResult(iimg,1);
        }
        if(val.equals("3")){
            c_img.setVisibility(View.GONE);
            c_video.setVisibility(View.VISIBLE);
            Intent myvideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            video = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+getTime()+".mp4");
            myvideo.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(video));
            myvideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//Todo jiashangqude
            startActivityForResult(myvideo,2);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                addDB();
                finish();
                break;

            case R.id.delete:
                finish();
                break;
        }
    }

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, ettext.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        cv.put(NotesDB.PATH,phone+"");
        cv.put(NotesDB.VIDEO,video+"");
        dbWriter.insert(NotesDB.TABLE_NAME , null , cv);
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Bitmap bitmap = BitmapFactory.decodeFile(phone.getAbsolutePath());
            c_img.setImageBitmap(bitmap);
        }
        if(requestCode == 2){
            c_video.setVideoURI(Uri.fromFile(video));
            c_video.start();
        }
    }
}
