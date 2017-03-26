package com.apps.geo.notes;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.apps.geo.notes.db.PointInfoDBManager;
import com.apps.geo.notes.pojo.PointInfo;

/**
 * Created by 1038844 on 16.02.2017.
 */

public class AlarmActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private PointInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);
        Button shutUpButton = (Button) findViewById(R.id.shutUpButton);
        shutUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setActive(false);
                new PointInfoDBManager(AlarmActivity.this).updatePointById(info);
                AlarmActivity.this.finish();
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer = MediaPlayer.create(this, notification);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        if (getIntent() != null && getIntent().getExtras() != null) {
            Object obj = getIntent().getExtras().get("point");
            if (obj != null) {
                info = (PointInfo) obj;
                TextView textView = (TextView)findViewById(R.id.pointText);
                textView.setText(info.getName()+" ("+info.getDescription()+")");
            }
        }
    }

    @Override
    public void onDestroy(){
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }
}
