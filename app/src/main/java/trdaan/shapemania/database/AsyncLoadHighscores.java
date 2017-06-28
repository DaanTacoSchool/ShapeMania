package trdaan.shapemania.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import trdaan.shapemania.entity_classes.HighScore;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;
import trdaan.shapemania.game_helper_classes.LevelManager;

/**
 * Created by D2110175 on 16-1-2017.
 */

public class AsyncLoadHighscores   extends AsyncTask<Void, Void, Cursor> {
    private static final String DATABASE_TABLE_NAME2 = "Highscores";
    private static final String DATABASE_TABLE_COLUMN_A2 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B2 = "Name";
    private static final String DATABASE_TABLE_COLUMN_C2 = "levelHighscore";
    private static final String DATABASE_TABLE_COLUMN_D2 = "AchievedOnLevel";
    private DatabaseHandler dbh;

    private String TAG = "ASyncLoadHs";

    private Context con;


    public AsyncLoadHighscores(DatabaseHandler dbh,Context con) {
        this.dbh = dbh;
        this.con = con;

    }

    @Override
    protected Cursor doInBackground(Void... c) {
        Log.i(TAG, "DoInBackground");
        //get results in descending order (highest score first)
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_NAME2 + " ORDER BY CAST("+DATABASE_TABLE_COLUMN_C2+" as INTEGER) DESC";
        SQLiteDatabase db = dbh.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        Log.i(TAG, "OnPostExecute");


        AsyncLoadHighscores.CallbackHighscore cbh;
        ArrayList<HighScore> hsl = new ArrayList<>();
        if (cursor != null) {

            //TODO : remaining entries should be removed, make remove class?
            int tx =1;
            while(cursor.moveToNext()) {


                if(tx==11){//if this is the 11th time to loop through here, break loop. we only want 10 items.(starts at 1 so item one will be tx=2
                    break;
                }
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

            Log.i(TAG, "Data loaded in array,");

                //callback
                if (this.con instanceof AsyncLoadHighscores.CallbackHighscore) {
                    cbh = (AsyncLoadHighscores.CallbackHighscore) con;

                    if (cbh != null) {
                        Log.i(TAG, "executing callback");
                        cbh.retrieveHS(hsl);
                    }
                } else {
                    Log.e(TAG, "wrong context?");

                }

            }

        }


    //callback interface
    public interface CallbackHighscore {

        void retrieveHS(ArrayList<HighScore> clbArrHS);

    }
}

