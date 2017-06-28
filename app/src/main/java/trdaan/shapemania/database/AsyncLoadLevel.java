package trdaan.shapemania.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import trdaan.shapemania.entity_classes.CurrentPlayerData;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;
import trdaan.shapemania.game_helper_classes.LevelManager;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class AsyncLoadLevel  extends AsyncTask<Void, Void, Cursor> {
    private static final String DATABASE_TABLE_NAME3 = "Levels";
    private static final String DATABASE_TABLE_COLUMN_A3 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B3 = "LevelNumber";
    private static final String DATABASE_TABLE_COLUMN_C3 = "AmountOfTargets";
    private static final String DATABASE_TABLE_COLUMN_D3 = "AmountOfDuds";
    private static final String DATABASE_TABLE_COLUMN_E3 = "Time";
    private DatabaseHandler dbh;

    private String TAG = "ASyncLoadPlayer";
    private PlayerProfile p;//marked
    private Level l;//marked
    private Context c;//marked
    private int lvlNum;
    private LevelManager lman;

    public AsyncLoadLevel(DatabaseHandler dbh, LevelManager lman,int lvlNum){
        this.dbh = dbh;
        this.lman=lman;
        this.lvlNum=lvlNum;//if set load that level, if 0 load all levels
    }
    @Override
    protected Cursor doInBackground(Void... c)
    {
        Log.i(TAG, "DoInBackground");
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_NAME3;
        SQLiteDatabase db = dbh.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        Log.i(TAG, "OnPostExecute");

        AsyncLoadLevel.CallbackLevels cbl;
        ArrayList<Level> arl = new ArrayList<>();
        if (cursor != null)
        {
            //for each item in cursor, make level object and add to array
            while(cursor.moveToNext()){
                Level lvl = new Level();
                lvl.levelNum = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_B3)));
                lvl.amountOfTargets = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_C3)));
                lvl.amountOfDuds = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_D3)));
                lvl.time = Long.parseLong(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_E3)));


                arl.add(lvl);
             //   Log.i(TAG,"level loaded"+lvl.levelNum);//flooded log

            }
            Log.i(TAG,"Data loaded in array,");
            if(lvlNum==0) {//if level not set

                if (this.lman instanceof AsyncLoadLevel.CallbackLevels) {
                    cbl = (AsyncLoadLevel.CallbackLevels) lman;

                    if(cbl!=null){
                        //Log.i(TAG,"calling retrieve arr");
                        Log.i(TAG,"executing callback");
                        cbl.retrieveLevels(arl);//sent all elvels
                    }
                }
                else
                {
                    Log.e(TAG,"wrong context?");
                    // throw new RuntimeException(c.toString() + " must implement OnFragmentInteractionListener");
                }
            }else {//if lvl set

                if (this.lman instanceof AsyncLoadLevel.CallbackLevels) {
                    cbl = (AsyncLoadLevel.CallbackLevels) lman;
                    if(cbl!=null){
                        //Log.i(TAG,"calling retrieve arr");
                        Log.i(TAG,"executing callback");
                        Level level=new Level();
                        for(Level xl:arl){
                            if(xl.levelNum==lvlNum){//get the required level
                                level.levelNum=xl.levelNum;
                                level.amountOfDuds=xl.amountOfDuds;
                                level.time=xl.time;
                                level.amountOfTargets=xl.amountOfTargets;
                            }
                        }
                        Log.e(TAG,"level x loaded in retrieve curr lvl"+level.levelNum);
                        cbl.retrieveCurrentLevel(level);//callback
                    }
                }
                else
                {
                    Log.e(TAG,"wrong context?");

                }
            }

        }
    }
    public interface CallbackLevels {

        void retrieveLevels(ArrayList<Level> clbArrayLevels);
        void retrieveCurrentLevel(Level l);
    }

}