package trdaan.shapemania.game_helper_classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.util.ArrayList;
import trdaan.shapemania.R;

import trdaan.shapemania.SelectLevelActivity;
import trdaan.shapemania.SelectPlayerActivity;
import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.CurrentPlayerData;
import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 15-1-2017.
 */

public class SelectPlayerAdapter extends ArrayAdapter<PlayerProfile>  {
    private String TAG="playerAdap";
    public ArrayList<PlayerProfile> arl = new ArrayList<>();
    private FrameLayout main;
    private SelectPlayerActivity parentActivity;

    public SelectPlayerAdapter(Context context, ArrayList<PlayerProfile> arl, SelectPlayerActivity parentActivity) {
        super(context,0, arl);
        this.parentActivity = parentActivity;
    }
    @Override
    public int getCount() {
        Log.i(TAG,"getcount");
        return arl.size();
    }

    @Override
    public PlayerProfile getItem(int pos) {
        Log.i(TAG,"getitem");
        return arl.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        Log.i(TAG,"getid");
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        try {
            Log.i(TAG,"getView");
            final PlayerProfile pp = this.arl.get(pos);

          convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row_select_player, parent, false);
            TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
            TextView txtLevel = (TextView) convertView.findViewById(R.id.txtLVL);

            Button btnPick = (Button)convertView.findViewById(R.id.btnPick);
            txtName.setText(pp.name);
            txtLevel.setText("Level "+pp.currentLevel);//set current level
            final View tempView = convertView;
            btnPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   PlayerProfile oldP;
                    if(CurrentPlayerData.arePlayersLoaded()){
                        CurrentPlayerData.loadActivePlayer();
                        oldP= CurrentPlayerData.getActivePlayer();
                        //set old active player to inactive and newly selelcted to active
                        String sql = "UPDATE PlayerNames SET activePlayer = 0 WHERE Name = '"+oldP.name+"'";
                        String sql2 = "UPDATE PlayerNames SET activePlayer = 1 WHERE Name = '"+pp.name+"'";
                        //DatabaseHandler dbh = new DatabaseHandler()
                        try{
                            DatabaseHandler dbh = new DatabaseHandler(tempView.getContext());
                            SQLiteDatabase db = dbh.getWritableDatabase();
                            db.execSQL(sql);
                            db.execSQL(sql2);
                            pp.activePlayer=1;
                            Log.i(TAG,"new player: "+pp.name);
                            CurrentPlayerData.setActivePlayer(pp);//set new player actvie
                            killParent();//kills parent activity and thus this too

                        }catch (Exception e){
                            Log.e(TAG,"exception  occured: "+e.getMessage());
                        }

                    }
                }
            });

        }catch (Exception e){}
        return convertView;
    }
    private void killParent(){
        parentActivity.finish();
        //kill parent and set it to null and wait for this to be garbagecollected
        this.parentActivity=null;
    }

}
