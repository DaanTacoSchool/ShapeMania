package trdaan.shapemania.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;
import trdaan.shapemania.game_helper_classes.LevelManager;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private String TAG = "DatabaseHandler";
    private static final String DATABASE_NAME = "ShapeManiaDB";
    private static final int DATABASE_VERSION = 9;

    private static final String DATABASE_TABLE_NAME1 = "PlayerNames";
    private static final String DATABASE_TABLE_COLUMN_A1 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B1 = "Name";
    private static final String DATABASE_TABLE_COLUMN_C1 = "TotalScore";
    private static final String DATABASE_TABLE_COLUMN_D1 = "CurrentLevel";
    private static final String DATABASE_TABLE_COLUMN_E1 = "activePlayer";
    private static final String DATABASE_CREATE_TABLE1 =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_NAME1 + " (" +
                    DATABASE_TABLE_COLUMN_A1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DATABASE_TABLE_COLUMN_B1 + " TEXT, " +
                    DATABASE_TABLE_COLUMN_C1 + " TEXT, " +
                    DATABASE_TABLE_COLUMN_D1 + " TEXT, " +
                    DATABASE_TABLE_COLUMN_E1 + " INTEGER);";

    private static final String DATABASE_TABLE_NAME2 = "Highscores";
    private static final String DATABASE_TABLE_COLUMN_A2 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B2 = "Name";
    private static final String DATABASE_TABLE_COLUMN_C2 = "levelHighscore";
    private static final String DATABASE_TABLE_COLUMN_D2 = "AchievedOnLevel";
    private static final String DATABASE_CREATE_TABLE2 =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_NAME2 + " (" +
                    DATABASE_TABLE_COLUMN_A2 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DATABASE_TABLE_COLUMN_B2 + " TEXT, " +
                    DATABASE_TABLE_COLUMN_C2 + " TEXT, " +
                    DATABASE_TABLE_COLUMN_D2 + " TEXT);";

    private static final String DATABASE_TABLE_NAME3 = "Levels";
    private static final String DATABASE_TABLE_COLUMN_A3 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B3 = "LevelNumber";
    private static final String DATABASE_TABLE_COLUMN_C3 = "AmountOfTargets";
    private static final String DATABASE_TABLE_COLUMN_D3 = "AmountOfDuds";
    private static final String DATABASE_TABLE_COLUMN_E3 = "Time";
    private static final String DATABASE_CREATE_TABLE3 =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_NAME3 + " (" +
                    DATABASE_TABLE_COLUMN_A3 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DATABASE_TABLE_COLUMN_B3 + " TEXT, " +
                    DATABASE_TABLE_COLUMN_C3 + " TEXT, " +
                    DATABASE_TABLE_COLUMN_D3 + " TEXT, " +
                    DATABASE_TABLE_COLUMN_E3 + " TEXT);";

    private Context c;
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG,"Constructor");
        this.c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "OnCreate, create table");
        db.execSQL(DATABASE_CREATE_TABLE1);//names
        db.execSQL(DATABASE_CREATE_TABLE2);//highscores
        db.execSQL(DATABASE_CREATE_TABLE3);//levels
        //created tables

        //the basic levels will be generated (there are 100)
        int targ =1;
        int dud=5;
        int lvl=1;
        long time=7000;
        ArrayList<Level> arrLVL = new ArrayList<>();

        for(int i=1; i<101;i++){


            time = time+500;
            if(i%2 == 0){
                targ++;
            }
            dud++;
            dud++;
            /*if(i %50 ==0){
                targ=targ-35;
                dud=dud-50;
                time= time-15000;
                //adding scorebump to increase difficulty while not filling up screen entirely with shapes
                //commented out because it was ignored and somehow resulted in negative time for a some levels
            }*/
            //make level object
            Level lvlObj = new Level();
            lvlObj.levelNum=lvl;
            lvlObj.amountOfDuds=dud;
            lvlObj.amountOfTargets=targ;
            lvlObj.time=time;
            arrLVL.add(lvlObj);
            lvl++;
        }//endfor
        addLevel(arrLVL);//addtodb
        //make default player
        PlayerProfile pp = new PlayerProfile();
        pp.activePlayer=1;
        pp.name="Default";
        pp.currentLevel=1;
        pp.totalScore=0;
        addPlayer(pp);
        //make player (for testing performance on higher levels)
        PlayerProfile test2 = new PlayerProfile();
        test2.activePlayer=0;
        test2.name="test";
        test2.currentLevel=99;
        test2.totalScore=0;
        addPlayer(test2);


        Log.e(TAG,"GOOD TO GO!");//if this appears then app is ready to play games, before this que, no gameactivity should be launched

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG, "OnUpgrade, Drop table then run onCreate function");
        try {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME1);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME2);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME3);
            onCreate(db);
        }catch (SQLiteException sqle){
            Log.i( TAG,"_EXCEPTION_:"+sqle.getMessage());
        }
    }


    public void addLevel(ArrayList<Level> arrLvl)
    {
        AsyncAddLevel addLvl = new AsyncAddLevel(this,arrLvl);
        addLvl.execute();
        Log.i(TAG,"AddLevel");
    }
    public void loadLevel(LevelManager lman,int lvlNum){
        AsyncLoadLevel all = new AsyncLoadLevel(this,lman,lvlNum);
        all.execute();
        Log.i(TAG,"loadLevel");

    }
    public void loadPlayer(Context con)
    {
        AsyncLoadPlayer alp = new AsyncLoadPlayer(this,con);
        alp.execute();
        Log.i(TAG,"loadPlayer");

    }
    public void addPlayer(PlayerProfile pp){
        AsyncAddName addP = new AsyncAddName(this,pp);
        addP.execute();
        Log.i(TAG,"addPlayer");
    }
}