package trdaan.shapemania;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;
import trdaan.shapemania.game_helper_classes.LevelManager;
import trdaan.shapemania.game_helper_classes.SelectLevelAdapter;
import trdaan.shapemania.game_helper_classes.SelectPlayerAdapter;

/**
 * Created by D2110175 on 15-1-2017.
 */

public class SelectLevelActivity extends Activity {
    private String TAG = "SelectLevelActivity";
    private ImageView triangle;//ToDo#2
    private ImageView square;//ToDo#3
    private FrameLayout main;
    private PlayerProfile pp;
    private ListView levelListView;
    private SelectLevelAdapter levelAdapter;
    private ArrayList<Level> levelArray = new ArrayList<Level>();//holds the levels the player has unlocked
    private ArrayList<Level> allLevelsArray = new ArrayList<>();//holds all levels, currently it isnt really necessary, but there are planned
    //features that will need this //ToDo#4
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);

        Bundle extras = getIntent().getExtras();
        try {
            this.pp = (PlayerProfile)extras.get("PLAYER");
            this.allLevelsArray= (ArrayList<Level>)extras.get("LEVELLIST");
            Log.i(TAG,"intent received: "+pp.activePlayer);
            Log.i(TAG,"levels in array before init : "+allLevelsArray.size());

        }catch (Exception e){}//empty
        initAnimations();//sets the animations, originally only a test,
        // but eventually there will be hidden bonuslevels accesible as easteregg here //ToDo#5
        init();//inits all other stuff like lists
    }

    private void initAnimations(){

        this.main= (FrameLayout) findViewById(R.id.masterLayout);
        //this.triangle = (ImageView)main.findViewById(R.id.redTriangle);//imageview would not play animation somehow
        final Animation rot = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_triangle_select_level);
         rot.setRepeatCount(Animation.INFINITE);
        final Animation blink = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink_circle_select_level);
        rot.setRepeatCount(Animation.INFINITE);
        blink.setRepeatCount(Animation.INFINITE);
        final TextView tr = (TextView)findViewById(R.id.txtTriangle);
        final TextView ty = (TextView)findViewById(R.id.yellowCircle);

        tr.startAnimation(rot);
        ty.startAnimation(blink);
        //imageviews would not animate somehow. hacked together a workaround but is still marked as //Bug#2 or FIXME#2


        Log.i(TAG,"initAnimations completed");
    }

    private void init(){

        levelListView = (ListView)main.findViewById(R.id.listViewSelectLevel);
        levelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                //interesting things here, since only the allLevels array is filled and
                //the list does not yet have an adapter, it'll sometimes(?) give a nullpointer if i use the other array
                // using this one however gives the same results.
                //first a new instance of the levelmanager is created, then the selected level is retreived from the list
                //then the function retrievecurrentlevel is called, this actually is one of the callback functions from loadlevels.
                //this function starts up the gameactivity with the supplied level. as it would have when it was called from loadlevels.
                LevelManager lman = new LevelManager(main.getContext(),pp);
                Level lx= allLevelsArray.get(pos);
                Log.i(TAG,"level retrieved:"+lx.levelNum);
                lman.retrieveCurrentLevel(lx);
                Log.i(TAG,"click");
                finish();//this should fix the bug when levels arent updated after playing a level from select level and then presed go to main on lvlfinished
                //now player will go to the gamemenu activity instead of select level, forcing them to either
                //continue or restrt select level (which then has the updated level list)
            }
        });


        //set list
        levelAdapter=new SelectLevelAdapter(main.getContext(),levelArray,this,pp);
        levelListView.setAdapter(levelAdapter);
        for(int i=0; i<pp.currentLevel;i++){
            levelArray.add(allLevelsArray.get(i));
            Log.i(TAG,"level added at pos: "+i);
            Log.i(TAG, "should be lvel: "+allLevelsArray.get(i));
            //to check
        }
        levelAdapter.arl=levelArray;
        levelAdapter.notifyDataSetChanged();

    }
    public void onClick(View view) {
        Log.i(TAG,"click");//empty

    }
}
