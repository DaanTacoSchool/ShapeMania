package trdaan.shapemania;

import android.app.Activity;
import android.app.ActivityOptions;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import trdaan.shapemania.architecture_enforcement_classes.GameState;
import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.CurrentPlayerData;
import trdaan.shapemania.entity_classes.PlayerProfile;

import trdaan.shapemania.game_helper_classes.LevelManager;

/**
 * Created by D2110175 on 12-1-2017.
 */

public class LevelFinishedActivity extends Activity {
    private FrameLayout main;
    private View v;
    private GameState gs;
    private PlayerProfile pp;
    private long score;
    private String TAG = "LvlFinishAct";

    public LevelFinishedActivity() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_finished);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }

    }

    private void init(){

        this.main= (FrameLayout) findViewById(R.id.masterLayout);//getlayout

        this.v = (View)this.findViewById(android.R.id.content);//getroot
        Bundle extras = getIntent().getExtras();
        try {
            gs = (GameState) extras.get("GAMESTATE");
            score = (long)extras.get("SCORE");
            this.pp=(PlayerProfile)extras.get("PLAYER");
            LevelManager.incrementLevel(pp);//acces static method to update player in db that level is finished
            //FIXME#4 there might be a bug where if a user plays multiple levels sequentially and then procedes to cancel out of gameactivity
            //that all made progress is lost, possible fix: move this to the end of gameactivity or maybe update static currentuserdata class
            //and retrieve profile from there instead
            this.pp.currentLevel = pp.currentLevel+1;
            Log.i(TAG, "lvl updated: next:"+pp.currentLevel);

            CurrentPlayerData.setActivePlayer(pp);//update static currentplayerdata class

        }catch (Exception e){}//empty
        final MediaPlayer finishedSound = MediaPlayer.create(this, R.raw.save);
        finishedSound.start();//happysound
        TextView txtScore = (TextView)main.findViewById(R.id.txtFadeInScore);
        txtScore.setText(score+"");

        Button btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pp!=null) {
                    LevelManager lman = new LevelManager(main.getContext(), pp);//new levelmanager instance
                    lman.startCurrentLevel(pp.currentLevel);//supply level to play, manager deals with the rest
                    finish();//closes this activity
                }else{
                    Toast.makeText(main.getContext(), "An unknown error occurred, please return to main menu.",
                            Toast.LENGTH_SHORT).show();//cause errors can always happen
                    Log.e(TAG,"warning, player is null progress might be unsaved!");//should never happen though
                }
            }
        });
        Button btnFinish = (Button)findViewById(R.id.btnFinished);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LevelFinishedActivity.super.finish();//go back to main actually just closes this activity, gameactivity is closed already
                //that means it will enter gamemenu, which s fine because a player will likely want to only cancel out next level.
            }
        });
    }
}
