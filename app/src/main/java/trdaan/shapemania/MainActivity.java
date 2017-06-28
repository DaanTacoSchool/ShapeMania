package trdaan.shapemania;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import trdaan.shapemania.architecture_enforcement_classes.GameState;
import trdaan.shapemania.database.AsyncLoadPlayer;
import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.CurrentPlayerData;
import trdaan.shapemania.entity_classes.PlayerProfile;
import trdaan.shapemania.game_helper_classes.LevelManager;

public class MainActivity extends AppCompatActivity implements AsyncLoadPlayer.CallbackPlayers{

    private String TAG = "MainActivity";
    private ArrayList<PlayerProfile> app;
    private PlayerProfile activePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGoToPlayerProfileMain;
        btnGoToPlayerProfileMain = (Button) findViewById(R.id.btnGoToSettings);
        btnGoToPlayerProfileMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplication(),SettingsActivity.class);
                i.putExtra("PLAYER", CurrentPlayerData.getActivePlayer());
                startActivity(i);
                Log.i(TAG, "settings");

            }
        });

        Button btnGoToGameMenu;
        btnGoToGameMenu = (Button) findViewById(R.id.btnGoToGameMenu);
        btnGoToGameMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this is actually only used when the app is fired up for the first time or after a db upgrade.
                //the player is required to select a user first. this is a fix for bug 3 (FIXM3 in projecttracker)
                Log.i(TAG, "no player yet");
                Toast.makeText(getApplicationContext(), "Select a profile first.",
                        Toast.LENGTH_LONG).show();
            }
        });
        init();//call init then proceed because init fires an asynctask loadplayer

        Button btnHs = (Button) findViewById(R.id.btnHighscores);
        btnHs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplication(),HighscoreActivity.class);
                startActivity(i);
            }
        });
    }

    private void init(){

        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());//applicationcontext works fine here somehow
        try{
            Context c =
                    ((View)findViewById(android.R.id.content)).getContext();//get root from this activity then get context

            dbh.loadPlayer(c);//fires asynctask which has callback retrieveplayers which calls finishinit.
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "If this is your first time here, please set your name.",
                    Toast.LENGTH_LONG).show();
        }

        //while(C)
        //app = db.
        //CurrentPlayerData.setPlayers();


    }
    private void finishInit(){

        //ToDo#6 set textfield "welcome user" where user is username.

        //this is the same onclicklistener as in the oncreate method, except this one will override the previous.
        //if a player tries to press the button when there is no active user then the user will fire the other listener
        //because this one will only override the other when everything is ready to go.
        Button btnGoToGameMenu;
        btnGoToGameMenu = (Button) findViewById(R.id.btnGoToGameMenu);
        btnGoToGameMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Log.e(TAG,"playername active: "+CurrentPlayerData.getActivePlayer().name+", "+CurrentPlayerData.getActivePlayer().activePlayer);
                    Intent i = new Intent(getApplication(),GameMenuActivity.class);
                    i.putExtra("GAMESTATE", GameState.PAUSE);
                    i.putExtra("PLAYER",CurrentPlayerData.getActivePlayer());
                    startActivity(i);
                    Log.i(TAG, "init finished");
                }catch (Exception e){

                    Log.e(TAG,"error occurred, restarting main class ");
                    init();//if an error occurred because database wasnt done yet updating then refire init and try again.
                    //if this is done it is only done once on a cold startup after an update or upgrade,
                    //sometimes the main tries to get users before they are made and this prevents a crash.
                }

            }
        });

    }

    @Override
    public void retrievePlayers(ArrayList<PlayerProfile> clbArrayPlayers) {
    Log.i(TAG,"excuting callback");
        //below code is actually unnecessary because it tries to load players for like 10 times when actually the players are supplied as param

//        if(CurrentPlayerData.arePlayersLoaded()){
//            CurrentPlayerData.loadActivePlayer();
//        }else{
//            for(int i=0;i<10;i++){
//                if(CurrentPlayerData.arePlayersLoaded()) {
//                    CurrentPlayerData.loadActivePlayer();
//                }
//            }//endfor
//        }//endelse

            this.app=clbArrayPlayers;
            for(PlayerProfile px:app){
                if(px.activePlayer==1){
                    this.activePlayer=px;
                    CurrentPlayerData.setActivePlayer(px);
                    //do not break loop, in case older profile hasnt had its tag updated, this way the last added (active)user will be used
                }
            }
        finishInit();

    }


}
