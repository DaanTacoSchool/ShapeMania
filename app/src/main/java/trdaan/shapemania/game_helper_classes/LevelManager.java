package trdaan.shapemania.game_helper_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import trdaan.shapemania.GameActivity;
import trdaan.shapemania.LevelFinishedActivity;
import trdaan.shapemania.SelectLevelActivity;
import trdaan.shapemania.architecture_enforcement_classes.GameState;
import trdaan.shapemania.database.AsyncLoadLevel;
import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 15-1-2017
 */

public class LevelManager implements AsyncLoadLevel.CallbackLevels{

    private String TAG = "LevelManager";
    private PlayerProfile pp;
    private Level l;
    private Context c;
    private ArrayList<Level> lvlList=new ArrayList<>();
    private static DatabaseHandler dbh;
    private static boolean runOnce =false;//for static instantiation of dbhandler. to prevent a lot of duplicates

    public LevelManager(Context c,PlayerProfile pp){
        this.pp=pp;
        this.c=c;
    }


    public void startCurrentLevel(int currLvl){
        Log.i(TAG,"startcurrentlevel");
        int currentLvl = currLvl;
        DatabaseHandler dbh = new DatabaseHandler(c);
        dbh.loadLevel(this,currentLvl);
    }
    public void getLevelsFromDBAndStartSelectLevel(){
        Log.i(TAG, "getlevelsfromdbandstartselectlevel");
        DatabaseHandler dbh = new DatabaseHandler(c);
        dbh.loadLevel(this,0);
    }
    private void setLevel(Level l){
        Log.i(TAG,"setlecel");
        this.l = l;
    }
    private void setLevelList(ArrayList<Level> arl){
        Log.i(TAG,"setlevellist");
        this.lvlList = arl;
    }

    @Override
    public void retrieveLevels(ArrayList<Level> clbArrayLevels) {//callback
        setLevelList(clbArrayLevels);
        //set levels to be used later
        Log.i(TAG,"starting selectlevelactivity");
        Intent i = new Intent(c,SelectLevelActivity.class);
        i.putExtra("GAMESTATE", GameState.PAUSE);
        i.putExtra("PLAYER",pp);
        i.putExtra("LEVELLIST",clbArrayLevels);
        c.startActivity(i);
        //fires selectlevel activity

    }

    @Override
    public void retrieveCurrentLevel(Level l) {//calback
    setLevel(l);//set level
        Log.i(TAG,"level targ,duds,num: "+l.amountOfTargets+","+l.amountOfDuds+","+l.levelNum);
        Intent i = new Intent(c,GameActivity.class);
        i.putExtra("GAMESTATE", GameState.START);
        i.putExtra("PLAYER",pp);
        i.putExtra("LEVEL",l);
        c.startActivity(i);
        //this method is also called from select level to start the gameactivity

    }
    public static void initDBHandler(Context cx){
        //if not yet run then do so and set dbhandler
        Log.i("LevelManager","initdbh");
        if(!runOnce){
            dbh = new DatabaseHandler(cx);
            runOnce=true;//prevent from running again
        }

    }
    public static DatabaseHandler getDbh(){
        Log.i("LevelManager","getDbh");
        return dbh;//yknow, for when it is needed :p
    }

    public static void incrementLevel(PlayerProfile pp){

        String sql = "UPDATE PlayerNames SET CurrentLevel = "+((pp.currentLevel)+1)+" WHERE Name = '"+pp.name+"'";
        Log.i("LevelManager","incrementlevel");
        SQLiteDatabase db = dbh.getWritableDatabase();
        db.execSQL(sql);//update level

    }
}
