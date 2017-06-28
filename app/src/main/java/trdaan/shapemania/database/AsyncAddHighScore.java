package trdaan.shapemania.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import trdaan.shapemania.entity_classes.HighScore;
import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class AsyncAddHighScore  extends AsyncTask<String, Void, Void> {
    private static final String DATABASE_TABLE_NAME2 = "Highscores";
    private static final String DATABASE_TABLE_COLUMN_A2 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B2 = "Name";
    private static final String DATABASE_TABLE_COLUMN_C2 = "levelHighscore";
    private static final String DATABASE_TABLE_COLUMN_D2 = "AchievedOnLevel";
    private DatabaseHandler dbh;

    private String TAG = "ASyncAddHighScore";
    private HighScore hs;

    public AsyncAddHighScore(DatabaseHandler dbh, HighScore hs)
    {
        Log.i(TAG,"Constructor");
        this.dbh = dbh;
        this.hs=hs;
    }

    @Override
    public Void doInBackground(String... strings)
    {
        Log.i(TAG, "DoInBackground start");

        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATABASE_TABLE_COLUMN_B2, hs.playerName);
        values.put(DATABASE_TABLE_COLUMN_C2, hs.score);
        values.put(DATABASE_TABLE_COLUMN_D2, hs.level);
        db.insert(DATABASE_TABLE_NAME2, null, values);//addtodb


        Log.i(TAG, "DoInBackground addtodb");


        return null;
    }

    @Override
    public void onPostExecute(Void result)
    {
        //DO NOTHING:)
        //why is below code here? it makes no sense.. did i forget something ?
       /* Log.i(TAG,"posted "+hs.score);
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_NAME2 + " ORDER BY \""+DATABASE_TABLE_COLUMN_C2+"\" DESC";
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor= db.rawQuery(selectQuery, null);
        ArrayList<HighScore> hsl = new ArrayList<>();
        if (cursor != null) {

            //TODO : remaining entries should be removed, make remove class?
            //for(int i=1;i<11;i++) {

            int tx =1;
            while(cursor.moveToNext()) {



                //   cursor.moveToNext();
                HighScore hs = new HighScore();
                hs.playerName = cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_B2));
                hs.score = Long.parseLong(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_C2)));
                hs.level = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_D2)));
                hsl.add(hs);
                Log.i(TAG, "hs loaded " + hs.playerName + "," + hs.score);
                //    }
                tx++;

            }



        }*/

    }
}
