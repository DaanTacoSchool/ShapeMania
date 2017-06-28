package trdaan.shapemania;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import trdaan.shapemania.database.AsyncLoadHighscores;
import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.HighScore;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;
import trdaan.shapemania.game_helper_classes.HighscoreAdapter;
import trdaan.shapemania.game_helper_classes.LevelManager;
import trdaan.shapemania.game_helper_classes.SelectLevelAdapter;

/**
 * Created by D2110175 on 16-1-2017.
 */

public class HighscoreActivity extends Activity implements AsyncLoadHighscores.CallbackHighscore ,View.OnClickListener, AdapterView.OnItemClickListener {
    private String TAG = "highscoreActivity";
    private FrameLayout main;
    private PlayerProfile pp;
    private ListView hsListView;
    private HighscoreAdapter hsAdap;
    private ArrayList<HighScore> hsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        Bundle extras = getIntent().getExtras();
        try {
            this.pp = (PlayerProfile)extras.get("PLAYER");
            Log.i(TAG,"intent received: "+pp.activePlayer);
        }catch (Exception e){}//empty
        init();
    }



    private void init(){
        this.main = (FrameLayout)findViewById(R.id.masterLayout);
        hsListView = (ListView)main.findViewById(R.id.listViewHighscores);

        hsListView.setOnItemClickListener(this);
        //somehow it wouldnt compile unless i implemented the interface.. really wierd..
     /*   hsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
              //nothin
            }
        });*/


        hsAdap=new HighscoreAdapter(main.getContext(),hsArray);
        hsListView.setAdapter(hsAdap);

       // levelAdapter.arl=levelArray;//do this on callback
        DatabaseHandler dbh = new DatabaseHandler(main.getContext());
        AsyncLoadHighscores alh = new AsyncLoadHighscores(dbh,main.getContext());
        alh.execute();//loads the highscores then on callback sets them as array
        hsAdap.notifyDataSetChanged();

    }
    public void onClick(View view) {
        Log.i(TAG,"click");//do nothin

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        //nothin
    }

    @Override
    public void retrieveHS(ArrayList<HighScore> clbArrHS) {

        //callback set arrays
        this.hsArray = clbArrHS;
        hsAdap.hsl = hsArray;
        hsAdap.notifyDataSetChanged();
    }
}


