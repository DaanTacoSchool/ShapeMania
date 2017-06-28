package trdaan.shapemania.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import trdaan.shapemania.entity_classes.Level;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class AsyncAddLevel extends AsyncTask<String, Void, Void> {
    private static final String DATABASE_TABLE_NAME3 = "Levels";
    private static final String DATABASE_TABLE_COLUMN_A3 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B3 = "LevelNumber";
    private static final String DATABASE_TABLE_COLUMN_C3 = "AmountOfTargets";
    private static final String DATABASE_TABLE_COLUMN_D3 = "AmountOfDuds";
    private static final String DATABASE_TABLE_COLUMN_E3 = "Time";
    private DatabaseHandler dbh;

    private String TAG = "ASyncAddLevel";
    private ArrayList<Level> arrLvl;

    public AsyncAddLevel(DatabaseHandler dbh, ArrayList<Level> arrLvl)
    {
        Log.i(TAG,"Constructor");
        this.dbh = dbh;
        this.arrLvl = arrLvl;
    }

    @Override
    public Void doInBackground(String... strings)
    {
        Log.i(TAG, "DoInBackground start");

        SQLiteDatabase db = dbh.getWritableDatabase();
        for(Level l:arrLvl) {//for each level in supplied array
            ContentValues values = new ContentValues();
            values.put(DATABASE_TABLE_COLUMN_B3, l.levelNum);
            values.put(DATABASE_TABLE_COLUMN_C3, l.amountOfTargets);
            values.put(DATABASE_TABLE_COLUMN_D3, l.amountOfDuds);
            values.put(DATABASE_TABLE_COLUMN_E3, l.time);
            db.insert(DATABASE_TABLE_NAME3, null, values);
        }

        Log.i(TAG, "DoInBackground addtodb");
        return null;
    }

    @Override
    public void onPostExecute(Void result)
    {
        //DO NOTHING:)
    }
}
