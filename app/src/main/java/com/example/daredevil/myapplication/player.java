package com.example.daredevil.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.nio.channels.FileLockInterruptionException;
import java.util.ArrayList;

public class player extends AppCompatActivity implements View.OnClickListener {
     static MediaPlayer mp;
    ArrayList<File> mysongs;
    int posi;
    Uri u;
    Button play,ff,fb,next,prev;
    SeekBar sb;
    Thread th;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        play =(Button) findViewById(R.id.play);
        ff =(Button) findViewById(R.id.ff);
        fb =(Button) findViewById(R.id.backward);
        next =(Button) findViewById(R.id.next);
        prev =(Button) findViewById(R.id.prev);
        sb =(SeekBar) findViewById(R.id.seekBar);

        play.setOnClickListener(this);
        ff.setOnClickListener(this);
        fb.setOnClickListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);

        th = new Thread(){
            @Override
            public void run(){
                int td =mp.getDuration();
                int cp =0;
                while (cp<td){
                    try {
                        sleep(500);
                        cp =mp.getCurrentPosition();
                        sb.setProgress(cp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        if(mp!=null){
            mp.stop();
            mp.release();
        }
        Intent i =getIntent();
        Bundle b =i.getExtras();
        mysongs = (ArrayList) b.getParcelableArrayList("songlist");
        posi =b.getInt("pos",0);

        u=Uri.parse(mysongs.get(posi).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setMax(mp.getDuration());
        th.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.play:
                if(mp.isPlaying())
                {
                    play.setText("play");
                    mp.pause();
                }
                else {
                    mp.start();
                    play.setText("pause");
                }
                break;
            case R.id.next:
                mp.stop();
                mp.release();
                posi=(posi+1)%mysongs.size();
                u=Uri.parse(mysongs.get(posi).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.prev:
                mp.stop();
                mp.release();
                posi=(posi-1<0)? mysongs.size()-1: posi-1;
                u=Uri.parse(mysongs.get(posi).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.ff:
                mp.seekTo(mp.getCurrentPosition()+10000);
                break;
            case R.id.backward:
                mp.seekTo(mp.getCurrentPosition()-10000);
                break;
        }
    }
}