package com.shabywoks.songstudio.core;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shabywoks.songstudio.R;

/**
 * Created by Shubham Bhiwaniwala on 2019-11-12.
 */
public class PlayListItemViewHolder extends RecyclerView.ViewHolder {

    PlayListItemClickListener   itemClickListener;
    int                         position;

    LinearLayout        parent;
    ImageView           image;
    TextView            title;
    RelativeLayout      playableScrim;

    public PlayListItemViewHolder(@NonNull View itemView, int position, PlayListItemClickListener itemClickListener) {
        super(itemView);

        parent          = itemView.findViewById(R.id.parent);
        image           = itemView.findViewById(R.id.playable_track_img);
        title           = itemView.findViewById(R.id.playable_track_title);
        playableScrim   = itemView.findViewById(R.id.playable);

        this.itemClickListener  = itemClickListener;
        this.position           = position;

        parent.setOnClickListener(viewClickListener);

    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(itemClickListener != null) {
                itemClickListener.onItemClicked(position);
            }
        }
    };
}
