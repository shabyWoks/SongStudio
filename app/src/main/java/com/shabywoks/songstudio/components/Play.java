package com.shabywoks.songstudio.components;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.shabywoks.songstudio.R;
import com.shabywoks.songstudio.core.DesignedForYouRVAdapter;
import com.shabywoks.songstudio.core.PlayListItem;
import com.shabywoks.songstudio.core.RecentlyPlayedRVAdapter;
import com.shabywoks.songstudio.core.StudioManager;
import com.shabywoks.songstudio.services.Services;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Play extends Fragment {

    ArrayList<PlayListItem>     playList;

    LinearLayout                recentlyPlayedLL,
                                designedForYouLL;

    RecyclerView                recentlyPlayedRV,
                                designedForYouRV;

    RecyclerView.LayoutManager  recentlyPlayedLM,
                                designedForYouLM;

    RecentlyPlayedRVAdapter     recentlyPlayedRVAdapter;
    DesignedForYouRVAdapter     designedForYouRVAdapter;

    public Play() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_play, container, false);

        initilializeComponent(v);
        fetchSongData();

        setRecentlyPlayedAdapter();

        // hide recently played song if no recent song available otherwise showit.
        if ((StudioManager.localDB.getRecent()).size() != 0) {

            recentlyPlayedLL.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lp    = (LinearLayout.LayoutParams) designedForYouLL.getLayoutParams();
            lp.weight                       = 15;
            designedForYouLL.setLayoutParams(lp);

        } else {

            recentlyPlayedLL.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp    = (LinearLayout.LayoutParams) designedForYouLL.getLayoutParams();
            lp.weight                       = 20;
            designedForYouLL.setLayoutParams(lp);

        }

        return v;
    }

    private void initilializeComponent(View v) {
        recentlyPlayedLL = (LinearLayout)v.findViewById(R.id.recently_played_ll);
        designedForYouLL = (LinearLayout)v.findViewById(R.id.designed_for_you_ll);

        recentlyPlayedRV = (RecyclerView) v.findViewById(R.id.recently_played_rv);
        designedForYouRV = (RecyclerView) v.findViewById(R.id.designed_for_you_rv);

        recentlyPlayedLM    = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        designedForYouLM    = new GridLayoutManager(getContext(), 2);

        recentlyPlayedRV.setLayoutManager(recentlyPlayedLM);
        designedForYouRV.setLayoutManager(designedForYouLM);
    }

    public void setRecentlyPlayedAdapter() {
        recentlyPlayedRVAdapter = new RecentlyPlayedRVAdapter(  getActivity(),
                                                                StudioManager.localDB.getRecent(),
                                                                R.layout.playlist_image_list_item);

        recentlyPlayedRV.setAdapter(recentlyPlayedRVAdapter);
        recentlyPlayedLL.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) designedForYouLL.getLayoutParams();
        lp.weight = 15;
        designedForYouLL.setLayoutParams(lp);
    }

    private void setDesignedForYouAdapter(ArrayList<PlayListItem> listItems) {
        designedForYouRVAdapter = new DesignedForYouRVAdapter(  getActivity(),
                                                                listItems,
                                                                R.layout.playlist_image_list_item);
        designedForYouRV.setAdapter(designedForYouRVAdapter);
    }

    private void fetchSongData() {
        Call<JsonArray> cb = Services.studioAPI.getSongList();

        cb.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.body() != null) {

                    Type collectionType = new TypeToken<ArrayList<PlayListItem>>(){}.getType();
                    playList            = (new Gson()).fromJson(response.body().getAsJsonArray().toString(), collectionType);

                    setDesignedForYouAdapter(playList);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getContext(), "Some problem occured while fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.playList = null;
    }

}
