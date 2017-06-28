package trdaan.shapemania.entity_classes;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class CurrentPlayerData {
    private static Level l;
    private static PlayerProfile pp;
    private static ArrayList<PlayerProfile> app;
    private static String TAG ="currentplayerdata";




    public static void setActivePlayer(PlayerProfile ap){
        pp = ap;
        Log.i(TAG,"setactiveplayer");
    }


    public static void setPlayers(ArrayList<PlayerProfile> arpp){
        app=arpp;
        Log.i(TAG,"setplayers");
    }
    public static void loadActivePlayer(){
        Log.i(TAG,"loadActiveplayers");
        if(app!=null){
            for(PlayerProfile px:app){
                if(px.activePlayer==1){
                    pp=px;
                    //do not break loop, in case older profile hasnt had its tag updated
                }
            }
        }
    }
    public static PlayerProfile getActivePlayer(){
        Log.i(TAG,"getActiveplayer");
        return pp;
    }

    public static Boolean arePlayersLoaded(){
        Log.i(TAG,"arePlayersLoaded");
        boolean returnVal =false;
        if(app!=null){
            returnVal=true;
        }
        return returnVal;
    }

}
