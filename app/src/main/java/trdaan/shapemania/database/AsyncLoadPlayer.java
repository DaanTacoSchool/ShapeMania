package trdaan.shapemania.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import trdaan.shapemania.entity_classes.CurrentPlayerData;
import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class AsyncLoadPlayer  extends AsyncTask<Void, Void, Cursor> {
    private static final String DATABASE_TABLE_NAME1 = "PlayerNames";
    private static final String DATABASE_TABLE_COLUMN_A1 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B1 = "Name";
    private static final String DATABASE_TABLE_COLUMN_C1 = "TotalScore";
    private static final String DATABASE_TABLE_COLUMN_D1 = "CurrentLevel";
    private static final String DATABASE_TABLE_COLUMN_E1 = "activePlayer";
    private DatabaseHandler dbh;

    private String TAG = "ASyncLoadPlayer";
    private PlayerProfile p;//marked
    private Context c;

    public AsyncLoadPlayer(DatabaseHandler dbh,Context c){
    this.dbh = dbh;
        this.c=c;
        Log.i(TAG,"constructor");
    }
    @Override
    protected Cursor doInBackground(Void... c)
    {
        Log.i(TAG, "DoInBackground");
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_NAME1;
        SQLiteDatabase db = dbh.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        Log.i(TAG, "OnPostExecute");
        CallbackPlayers cbi;
        ArrayList<PlayerProfile> app = new ArrayList<>();
        if (cursor != null)
        {
            //for each item in cursor, make playerprofile object and add it to array
            while(cursor.moveToNext()){
                PlayerProfile p = new PlayerProfile();
                p.uid= Integer.parseInt(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_A1)));
                p.name = cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_B1));
                p.totalScore = Long.parseLong(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_C1)));
                p.currentLevel = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_D1)));
                p.activePlayer = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_E1)));

                // Log.i("LOAD","rowid:"+ta.uid);//flooded log
                app.add(p);
               // Log.i(TAG,"player active, name"+p.activePlayer+","+p.name);//flooded log

            }

            Log.i(TAG,"Data loaded in array, sending it to static class");
            CurrentPlayerData.setPlayers(app);

            //doing the callback stuff
            if (this.c instanceof CallbackPlayers){
                cbi = (CallbackPlayers) c;
                if(cbi!=null){
                    //Log.i(TAG,"calling retrieve arr");
                    Log.i(TAG,"executing callback");
                    cbi.retrievePlayers(app);
                }
            }
            else
            {
                Log.e(TAG,"wrong context?");

            }

        }
    }

    //callback interface
    public interface CallbackPlayers {

        void retrievePlayers(ArrayList<PlayerProfile> clbArrayPlayers);
    }

}