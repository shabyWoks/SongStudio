package com.shabywoks.songstudio.components;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shabywoks.songstudio.R;
import com.shabywoks.songstudio.core.LocalDB;
import com.shabywoks.songstudio.core.StudioManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyStudio extends Fragment {

    TextView    note;

    public MyStudio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_studio, container, false);
        note        = (TextView)v.findViewById(R.id.note);

        if(StudioManager.localDB.getBoolean(LocalDB.PREMIUM)) {
            note.setText("Yay, you are a premium member. You are enjoying unlimited download.");
        }

        return v;
    }

}
