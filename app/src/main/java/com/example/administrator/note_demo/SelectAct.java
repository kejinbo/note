package com.example.administrator.note_demo;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by Administrator on 2017/5/12.
 */

public class SelectAct extends Activity implements View.OnClickListener {
    private Button s_delete, s_back;
    private ImageView s_img;
    private VideoView s_video;
    private TextView s_tv;
    private NotesDB mNotesDB;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        s_back = (Button) findViewById(R.id.s_back);
        s_delete = (Button) findViewById(R.id.s_delete);
        s_video = (VideoView) findViewById(R.id.s_video);
        s_img = (ImageView) findViewById(R.id.s_img);
        s_tv = (TextView) findViewById(R.id.s_tv);
        mNotesDB = new NotesDB(this);
        dbWriter = mNotesDB.getWritableDatabase();
        s_delete.setOnClickListener(this);
        s_back.setOnClickListener(this);
        initView();
    }

    private void initView() {
        if (getIntent().getStringExtra(NotesDB.PATH).equals("null")) {
            s_img.setVisibility(View.GONE);
        } else {
            s_img.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra(NotesDB.PATH));
            s_img.setImageBitmap(bitmap);
        }
        if (getIntent().getStringExtra(NotesDB.VIDEO).equals("null")) {
            s_video.setVisibility(View.GONE);
        } else {
            s_video.setVisibility(View.VISIBLE);
            s_video.setVideoURI(Uri.parse(getIntent().getStringExtra(NotesDB.VIDEO)));
            s_video.start();
        }
        s_tv.setText(getIntent().getStringExtra(NotesDB.CONTENT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.s_delete:
                deletaDate();
                finish();
                break;

            case R.id.s_back:
                finish();
                break;
        }
    }

    public void deletaDate() {
        dbWriter.delete(NotesDB.TABLE_NAME,
                "_id=" + getIntent().getIntExtra(NotesDB.ID, 0), null);
    }
}
