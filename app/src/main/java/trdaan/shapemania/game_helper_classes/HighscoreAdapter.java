package trdaan.shapemania.game_helper_classes;

import android.content.Context;
import android.content.Intent;
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
import trdaan.shapemania.architecture_enforcement_classes.GameState;
import trdaan.shapemania.entity_classes.HighScore;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 16-1-2017.
 */

public class HighscoreAdapter  extends ArrayAdapter<HighScore> {
    private String TAG="highAdap";
    public ArrayList<HighScore> hsl = new ArrayList<>();
    private FrameLayout main;

    public HighscoreAdapter(Context context, ArrayList<HighScore> hsl) {
        super(context,0);
        this.hsl=hsl;
    }
    @Override
    public int getCount() {
        return hsl.size();
    }

    @Override
    public HighScore getItem(int pos) {
        return hsl.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        try {


            HighScore hs = hsl.get(pos);
            Log.i(TAG,"getView");

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_row_highscores, parent, false);
            TextView txtName = (TextView)convertView.findViewById(R.id.txtHighscoreName);
            TextView txtScore = (TextView)convertView.findViewById(R.id.txttheHighscore);
            TextView txtLvl = (TextView)convertView.findViewById(R.id.txtAchievedOn);
            //pretty self explanatory
            txtLvl.setText("lvl "+hs.level);
            txtScore.setText("score "+hs.score);
            txtName.setText(" "+hs.playerName);

        }catch (Exception e){}
        return convertView;
    }


}
