package com.shabywoks.songstudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.shabywoks.songstudio.components.MyStudio;
import com.shabywoks.songstudio.components.Payments;
import com.shabywoks.songstudio.components.Play;
import com.shabywoks.songstudio.core.LocalDB;
import com.shabywoks.songstudio.core.PlayListItem;
import com.shabywoks.songstudio.core.ShortMediaPlayerControl;
import com.shabywoks.songstudio.core.StudioManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar                 toolbarTop;
    BottomNavigationView    bottomNavigationView;

    ArrayList<String>       tabList                     = new ArrayList<>();
    ArrayList<Fragment>     tabFragments                = new ArrayList<>();

    ShortMediaPlayerControl shortMediaPlayerControl;

    Play                    play;

    Menu                    menu;
    FrameLayout             frameLayout;

    RelativeLayout          layoutBottomSheet,
                            scrim;

    LinearLayout            shortMPCont;
    BottomSheetBehavior     sheetBehavior;

    boolean                 smpOpen                     = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeGlobals();
        initializeComponents();

        setSupportActionBar(toolbarTop);
        configureToolbar();

        shortMediaPlayerControl.findRecent();

        // setting click listener on get premium button
        ((Button)layoutBottomSheet.getChildAt(0)).setOnClickListener(getPremiumClickListener);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        prepareTabFragments();
        setDefaultBottomMenu();

        if ((StudioManager.localDB.getRecent()).size() != 0) {
            // if recent songs is there open media player
            animateSMP();
        }

    }

    private void initializeGlobals() {
        StudioManager.mainActivity = this;

        LocalDB localDB         = new LocalDB(this);
        StudioManager.localDB   = localDB;
    }


    private void initializeComponents() {
        frameLayout             = (FrameLayout)findViewById(R.id.bottom_nav_frag_holder);
        scrim                   = (RelativeLayout)findViewById(R.id.scrim);
        toolbarTop              = (Toolbar) findViewById(R.id.toolbar_top);
        shortMediaPlayerControl = (ShortMediaPlayerControl)findViewById(R.id.smp_control);
        bottomNavigationView    = findViewById(R.id.bottom_navigation);
        shortMPCont             = (LinearLayout)findViewById(R.id.short_mp);
        layoutBottomSheet       = (RelativeLayout) findViewById(R.id.bottom_sheet);

        scrim.setOnClickListener(scrimClickListener);
    }

    private void configureToolbar() {
        getSupportActionBar().setTitle("Song Studio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void prepareTabFragments() {

        menu = bottomNavigationView.getMenu();

        play = new Play();
        tabFragments.add(play);
        tabList.add("Player");

        tabFragments.add(new MyStudio());
        tabList.add("My Studio");

        int[] arr = new int[] { R.drawable.mp, R.drawable.cd };

        for(int i=0; i<tabList.size(); i++) {
            menu.add(Menu.NONE, i, Menu.NONE, tabList.get(i).toString()).setIcon(arr[i]);
        }

    }

    // load play menu when this activity loads
    private void setDefaultBottomMenu() {
        Fragment settingFragment    = tabFragments.get(0);
        FragmentTransaction ft      = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.bottom_nav_frag_holder, settingFragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            int id = menuItem.getItemId();

            Fragment settingFragment    = tabFragments.get(id);
            FragmentTransaction ft      = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.bottom_nav_frag_holder, settingFragment).commit();

            return true;
        }
    };

    // for delegating the media player with song info
    public void setMediaTrack(PlayListItem playListItem) {

        animateSMP();

        shortMediaPlayerControl.playItem(playListItem);

        // update localdb recent song
        StudioManager.localDB.playedItem(playListItem);

        // update recent song listview
        play.setRecentlyPlayedAdapter();
    }

    public void openSubscriptionDialog() {

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            scrim.setVisibility(View.VISIBLE);
            scrim.setBackgroundColor(getResources().getColor(R.color.scrim));
        }

    }

    View.OnClickListener scrimClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            scrim.setBackgroundColor(getResources().getColor(R.color.scrimOff));
            scrim.setVisibility(View.GONE);

        }
    };

    View.OnClickListener getPremiumClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getApplicationContext(), Payments.class);
            startActivity(intent);

            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            scrim.setBackgroundColor(getResources().getColor(R.color.scrimOff));
            scrim.setVisibility(View.GONE);
        }
    };

    private void animateSMP() {

        if (smpOpen) return;

        smpOpen             = true;
        ValueAnimator anim  = ValueAnimator.ofInt(shortMPCont.getMeasuredHeight(), getPixels(50));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val                             = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = shortMPCont.getLayoutParams();
                layoutParams.height                 = val;

                shortMPCont.setLayoutParams(layoutParams);
            }
        });

        anim.setDuration(600);
        anim.start();

    }

    private int getPixels(int dps) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();

        FragmentTransaction ft      = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.bottom_nav_frag_holder, play).commit();

        configureToolbar();
        return true;
    }
}




