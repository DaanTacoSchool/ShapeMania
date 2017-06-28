package trdaan.shapemania.game_helper_classes;

import android.content.Context;
import android.content.Intent;
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

import trdaan.shapemania.GameActivity;
import trdaan.shapemania.R;
import trdaan.shapemania.SelectLevelActivity;
import trdaan.shapemania.SelectPlayerActivity;
import trdaan.shapemania.architecture_enforcement_classes.GameState;
import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.CurrentPlayerData;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 16-1-2017.
 */

public class SelectLevelAdapter extends ArrayAdapter<Level> {
    private String TAG="levelAdap";
    public ArrayList<Level> arl = new ArrayList<>();
    private FrameLayout main;
    private SelectLevelActivity parentActivity;
    private PlayerProfile pp;

    public SelectLevelAdapter(Context context, ArrayList<Level> arl, SelectLevelActivity parentActivity,PlayerProfile pp) {
        super(context,0, arl);
        this.parentActivity = parentActivity;
        this.pp=pp;
    }
    @Override
    public int getCount() {
        return arl.size();
    }

    @Override
    public Level getItem(int pos) {
        return arl.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        try {

            final Level l = this.arl.get(pos);
            Log.i(TAG,"getView, levelnum: "+l.levelNum);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row_select_level, parent, false);
            Button btnSelectLevel= (Button)convertView.findViewById(R.id.btnSelectThisLevel);

            btnSelectLevel.setText(""+l.levelNum);
            Log.i(TAG,"level selected: "+btnSelectLevel.getText());
            btnSelectLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //intent to level
                    Intent i = new Intent(parentActivity, GameActivity.class);
                    i.putExtra("PLAYER", pp);
                    i.putExtra("LEVEL",l);
                    i.putExtra("GAMESTATE", GameState.START);
                    parentActivity.startActivity(i);
                    //parentActivity.finish();//not clean
                    //killParent();//might mess with gameactivity
                }
            });

        }catch (Exception e){}
        return convertView;
    }
    private void killParent(){
        parentActivity.finish();
        this.parentActivity=null;
    }

}
