package trdaan.shapemania.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class AsyncAddName  extends AsyncTask<String, Void, Void> {
    private static final String DATABASE_TABLE_NAME1 = "PlayerNames";
    private static final String DATABASE_TABLE_COLUMN_A1 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B1 = "Name";
    private static final String DATABASE_TABLE_COLUMN_C1 = "TotalScore";
    private static final String DATABASE_TABLE_COLUMN_D1 = "CurrentLevel";
    private static final String DATABASE_TABLE_COLUMN_E1 = "activePlayer";
    private DatabaseHandler dbh;

    private String TAG = "ASyncAddPlayer";
    private PlayerProfile p;

    public AsyncAddName(DatabaseHandler dbh, PlayerProfile pp)
    {
        Log.i(TAG,"Constructor");
        this.dbh = dbh;
        this.p=pp;
    }

    @Override
    public Void doInBackground(String... strings)
    {
        Log.i(TAG, "DoInBackground start");

        SQLiteDatabase db = dbh.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DATABASE_TABLE_COLUMN_B1, p.name);
            values.put(DATABASE_TABLE_COLUMN_C1, p.totalScore);
            values.put(DATABASE_TABLE_COLUMN_D1, p.currentLevel);
            values.put(DATABASE_TABLE_COLUMN_E1, p.activePlayer);
            db.insert(DATABASE_TABLE_NAME1, null, values);
        Log.i(TAG, "DoInBackground addtodb");

        return null;
    }

    @Override
    public void onPostExecute(Void result)
    {
        //DO NOTHING:)
    }
}
