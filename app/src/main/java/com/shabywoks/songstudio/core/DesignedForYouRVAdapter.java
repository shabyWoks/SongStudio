package com.shabywoks.songstudio.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shabywoks.songstudio.R;
import com.shabywoks.songstudio.components.PlayListItemDetail;

import java.util.ArrayList;

/**
 * Created by Shubham Bhiwaniwala on 2019-11-12.
 */
public class DesignedForYouRVAdapter extends RecyclerView.Adapter<PlayListItemViewHolder> {
    Activity                activity;
    ArrayList<PlayListItem> titleDescItemArrayList;
    int                     layoutId;

    public DesignedForYouRVAdapter(Activity activity, ArrayList<PlayListItem> titleDescItemArrayList, int layoutId) {
        this.activity               = activity;
        this.titleDescItemArrayList = titleDescItemArrayList;
        this.layoutId               = layoutId;
    }

    @NonNull
    @Override
    public PlayListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context         = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v                  = inflater.inflate(this.layoutId, viewGroup, false);

        return new PlayListItemViewHolder(v, i,titleDescItemClickListener);
    }

    PlayListItemClickListener titleDescItemClickListener = new PlayListItemClickListener() {
        @Override
        public void onItemClicked(int pos) {
            PlayListItem item       = titleDescItemArrayList.get(pos);

            Bundle  b               = new Bundle();
                    b.putString("playListItem", (new Gson()).toJson(item));

            PlayListItemDetail pd   = new PlayListItemDetail();
            pd.setArguments(b);

            // moving to playlist item detail view page
            FragmentTransaction ft  = ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.bottom_nav_frag_holder, pd).commit();

        }
    };

    @Override
    public void onBindViewHolder(@NonNull PlayListItemViewHolder titleDescViewHolder, int i) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(   ViewGroup.LayoutParams.MATCH_PARENT,
                                                                            ViewGroup.LayoutParams.MATCH_PARENT);
        // fetching device screen dimensions
        Dimension d     = getDeviceScreenDimension(activity);

        params.width    = (d.getWidth() - getPixels(24)) / 2;
        params.height   = d.getHeight() / 5;

        params.topMargin = getPixels(6);

        titleDescViewHolder.parent.setLayoutParams(params);
        titleDescViewHolder.title.setText(titleDescItemArrayList.get(i).song);
        titleDescViewHolder.position = i;

        // fetching image using image url
        Glide.with(activity).load(titleDescItemArrayList.get(i).cover_image).into(titleDescViewHolder.image);

    }

    // converting dp values to pixel values
    private int getPixels(int dps) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    // getting screen dimesions
    public static Dimension getDeviceScreenDimension(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new Dimension(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    @Override
    public int getItemCount() {
        return titleDescItemArrayList.size();
    }
}
