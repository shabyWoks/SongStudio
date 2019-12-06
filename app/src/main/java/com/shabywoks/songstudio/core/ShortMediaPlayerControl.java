package com.shabywoks.songstudio.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shabywoks.songstudio.R;

import java.util.ArrayList;

enum PlayerState {
    NONE, PAUSED, PLAYING, STOPPED
}

public class ShortMediaPlayerControl extends RelativeLayout {

    ImageButton     playPauseButton;
    PlayerState     state               = PlayerState.STOPPED;
    MediaPlayer     mediaPlayer;
    boolean         initialStage        = true;

    PlayListItem    playListItem;
    TextView        title;
    ImageView       image;

    public ShortMediaPlayerControl(Context context) {
        super(context, null);
    }

    public ShortMediaPlayerControl(Context context, AttributeSet attrs) {
        super(context, attrs);

        // for attaching an xml with this class
        inflate(context);

        // followed convention to get the property value and then clearing it
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShortMediaPlayerControl, 0, 0);
        ta.recycle();
    }

    private void inflate(Context context) {

        inflate(context, R.layout.short_media_player, this);
        initializeComponents();
        initializeMediaPlayer();

    }

    private void initializeComponents() {

        playPauseButton = findViewById(R.id.playPause);
        title           = findViewById(R.id.smp_title);
        image           = findViewById(R.id.smp_image);

        playPauseButton.setOnClickListener(playPauseListener);
    }

    private void initializeMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    View.OnClickListener playPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // changing state of media player and associated display

            if(state == PlayerState.PAUSED) {

                state = PlayerState.PLAYING;
                ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.pause));
                mediaPlayer.start();

            } else if(state == PlayerState.PLAYING) {

                state = PlayerState.PAUSED;
                ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.play));
                mediaPlayer.pause();

            } else if(state == PlayerState.STOPPED) {

                state = PlayerState.PLAYING;
                ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.pause));
                playItem(playListItem);

            }

        }
    };

    // set the last track played when app starts
    public void findRecent() {
        ArrayList<PlayListItem> items = StudioManager.localDB.getRecent();
        if (items.size() == 0) return;

        this.playListItem = items.get(0);
        title.setText(this.playListItem.song);
        Glide.with(getContext()).load(this.playListItem.cover_image).into(image);
    }

    // initialize the media player with new song
    public void playItem(PlayListItem item) {
        this.playListItem = item;

        title.setText(item.song);
        Glide.with(getContext()).load(item.cover_image).into(image);

        mediaPlayer.stop();
        mediaPlayer.reset();

        initialStage = true;

        state = PlayerState.PLAYING;
        playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        new Player().execute(item.url);

    }

    private class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            // fetching the song data from server
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                    initialStage = true;

                    state = PlayerState.STOPPED;
                    playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.play));

                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {

                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}



