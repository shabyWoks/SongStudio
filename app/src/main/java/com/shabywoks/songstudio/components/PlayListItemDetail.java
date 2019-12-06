package com.shabywoks.songstudio.components;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shabywoks.songstudio.R;
import com.shabywoks.songstudio.core.LocalDB;
import com.shabywoks.songstudio.core.PlayListItem;
import com.shabywoks.songstudio.core.StudioManager;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListItemDetail extends Fragment {

    RelativeLayout  playItemParent,
                    playItemParent2;

    ImageButton     download;
    ImageView       pliImage,
                    pliImageBig;

    TextView        title, artists;

    PlayListItem    playListItem;

    public PlayListItemDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_play_list_item_detail, container, false);

        // fetching the passed song data
        Bundle b        = getArguments();
        String data     = b.getString("playListItem");

        playListItem    = (new Gson()).fromJson(data, PlayListItem.class);

        initializeComponents(v);
        configureToolbar();
        initializeSongInfoData(playListItem);

        return v;
    }

    private void initializeComponents(View v) {
        playItemParent      = (RelativeLayout)v.findViewById(R.id.play_item_parent);
        playItemParent2     = (RelativeLayout)v.findViewById(R.id.play_item_parent2);
        pliImage            = (ImageView)v.findViewById(R.id.pli_image);
        pliImageBig         = (ImageView)v.findViewById(R.id.pli_image_big);
        title               = (TextView)v.findViewById(R.id.pli_title);
        artists             = (TextView)v.findViewById(R.id.pli_artists);
        download            = (ImageButton)v.findViewById(R.id.pli_download);

        download.setOnClickListener(downloadClickListener);
        playItemParent.setOnClickListener(parentClickListener);
        playItemParent2.setOnClickListener(parentClickListener);
    }

    private void configureToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(playListItem.getSong());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeSongInfoData(PlayListItem playListItem) {
        title.setText(playListItem.getSong());
        artists.setText(playListItem.getArtists());
        Glide.with(getContext()).load(playListItem.getCover_image()).into(pliImage);
        Glide.with(getContext()).load(playListItem.getCover_image()).into(pliImageBig);
    }

    View.OnClickListener parentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // set media player with song when image or song name is clicked
            StudioManager.mainActivity.setMediaTrack(playListItem);
        }
    };

    View.OnClickListener downloadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // open bottomsheet for getting premium if not
            if (!StudioManager.localDB.getBoolean(LocalDB.PREMIUM)) {
                StudioManager.mainActivity.openSubscriptionDialog();
            } else {
                Toast.makeText(getContext(), "You are a premium member. You can download it.", Toast.LENGTH_SHORT).show();
            }

        }
    };

}
