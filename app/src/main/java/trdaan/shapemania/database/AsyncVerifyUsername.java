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
 * Created by D2110175 on 15-1-2017.
 */

public class AsyncVerifyUsername  extends AsyncTask<Void, Void, Cursor> {
    private static final String DATABASE_TABLE_NAME1 = "PlayerNames";
    private static final String DATABASE_TABLE_COLUMN_A1 = "UID";
    private static final String DATABASE_TABLE_COLUMN_B1 = "Name";
    private static final String DATABASE_TABLE_COLUMN_C1 = "TotalScore";
    private static final String DATABASE_TABLE_COLUMN_D1 = "CurrentLevel";
    private static final String DATABASE_TABLE_COLUMN_E1 = "activePlayer";
    private DatabaseHandler dbh;

    private String TAG = "ASyncCheckname";
    private PlayerProfile p;//marked
    private Context c;
    private String strName;

    public AsyncVerifyUsername(DatabaseHandler dbh, Context c, String strName){
        this.dbh = dbh;
        this.c=c;
        this.strName = strName;//name used for comparison
    }
    @Override
    protected Cursor doInBackground(Void... c)
    {
        Log.i(TAG, "DoInBackground");
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_NAME1 + " WHERE "+DATABASE_TABLE_COLUMN_B1+" =  \""+strName+"\"";
        SQLiteDatabase db = dbh.getReadableDatabase();
        Log.i(TAG,"Executing raw query");
        return db.rawQuery(selectQuery, null);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        Log.i(TAG, "OnPostExecute");
        AsyncVerifyUsername.CallbackCheckName cbi;
        //initcallback
        String playername="";
        if (cursor != null)
        {
            //get the name
            while(cursor.moveToNext()){
                playername= cursor.getString(cursor.getColumnIndex(DATABASE_TABLE_COLUMN_B1));
            }

        }
        if (this.c instanceof AsyncVerifyUsername.CallbackCheckName){
            cbi = (AsyncVerifyUsername.CallbackCheckName) c;
            if(cbi!=null){
                //use callback to tell calling class wether the name is taken (query has results), or not (no results)
                Log.i(TAG,"executing callback");
                if(playername==""){
                    cbi.nameIsTaken(false);
                }else{
                    cbi.nameIsTaken(true);
                }

            }
        }
        else
        {
            Log.e(TAG,"wrong context?");
            // throw new RuntimeException(c.toString() + " must implement callback");//we dont want crahses now do we?
        }
    }
    //callback interface
    public interface CallbackCheckName {

        boolean nameIsTaken (boolean isTaken);
    }

}