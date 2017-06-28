package trdaan.shapemania;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import trdaan.shapemania.architecture_enforcement_classes.GameState;
import trdaan.shapemania.entity_classes.PlayerProfile;
import trdaan.shapemania.game_helper_classes.LevelManager;

/**
 * Created by D2110175 on 15-1-2017.
 */

public class GameMenuActivity extends Activity {
    private String TAG = "GameMenu";
    private PlayerProfile pp;
    private FrameLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        init();

    }

    private void init(){
        this.main=(FrameLayout)findViewById(R.id.masterLayout);
        Bundle extras = getIntent().getExtras();
        try {
            this.pp = (PlayerProfile)extras.get("PLAYER");
            Log.i(TAG,"intent recieved: "+pp.activePlayer);

        }catch (Exception e){}//empty

        LevelManager.initDBHandler(main.getContext());//static method to instantiate dbhandler in levelmanagerclass
        Log.i(TAG,"intent received: "+pp.activePlayer);

        Button btnContinue;
        btnContinue = (Button) findViewById(R.id.btnContinue);
        final LevelManager lman = new LevelManager(main.getContext(),pp);//new levelmanager instance
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pp!=null){

                   lman.startCurrentLevel(pp.currentLevel);//starts level supplied, levelmanager class creates an intent to gameactivity
                    Log.i(TAG, "Game started");
                }else{
                    Toast.makeText(main.getContext(), "Please select a profile first.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        Button btnSelectLevel;
        btnSelectLevel = (Button) findViewById(R.id.btnSelectLevel);
        btnSelectLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pp!=null){
                    LevelManager lman = new LevelManager(main.getContext(),pp);//starts a new instance of levelmanager
                    lman.getLevelsFromDBAndStartSelectLevel();//retrieves the levels from the db nad then sends them to selectlevelactivity
                    //using an intent
                    Log.i(TAG,"firing selectlevel");
                }else{
                    Toast.makeText(main.getContext(), "Please select a profile first.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
