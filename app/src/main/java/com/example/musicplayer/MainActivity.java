package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Widgets
    Button play,pause,forward,backward;
    TextView title,time_txt;
    SeekBar seekBar;

    //Media Player
    MediaPlayer mediaPlayer;
    double startTime=0;
    double finalTime=0;
    //Handler
    Handler handler=new Handler();
    int forwardTime=10000;
    int backwardTime=10000;
    boolean oneTimeOnly=true;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play=findViewById(R.id.play);
        pause=findViewById(R.id.pause);
        backward=findViewById(R.id.backward);
        forward=findViewById(R.id.forward);
        title=findViewById(R.id.title);
        time_txt=findViewById(R.id.time_txt);
        seekBar=findViewById(R.id.seekBar);
        mediaPlayer=MediaPlayer.create(this,R.raw.aashayein);
        seekBar.setClickable(false);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMusic();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int)startTime-backwardTime;
                if(temp>0){
                    startTime=temp;
                    mediaPlayer.seekTo((int) startTime);
                }
                else{
                    startTime=0;
                    mediaPlayer.seekTo((int)startTime);
                }
            }

        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int)startTime+forwardTime;
                if(temp<=finalTime){
                    startTime=temp;
                    mediaPlayer.seekTo((int)startTime);
                }
                else{
                    startTime=finalTime;
                    mediaPlayer.seekTo((int)startTime);
                }
            }
        });
    }

    private void playMusic() {
        mediaPlayer.start();
        startTime=mediaPlayer.getCurrentPosition();
        finalTime=mediaPlayer.getDuration();
        if(oneTimeOnly){
            seekBar.setMax((int)finalTime);
            oneTimeOnly=false;
        }
        seekBar.setProgress((int)startTime);
        handler.postDelayed(UpdateSongTime,100);
    }
    private  Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
            startTime=mediaPlayer.getCurrentPosition();
            time_txt.setText(String.format("%d min,%d sec", TimeUnit.MILLISECONDS.toMinutes((long)startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long)startTime) -
                    TimeUnit.MILLISECONDS.toSeconds((long)TimeUnit.MILLISECONDS.toMinutes((long)startTime))));
            seekBar.setProgress((int)startTime);
            handler.postDelayed(this,100);
        }
    };

}