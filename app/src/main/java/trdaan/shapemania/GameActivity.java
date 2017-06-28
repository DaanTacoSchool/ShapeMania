package trdaan.shapemania;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import trdaan.shapemania.architecture_enforcement_classes.GameObjectState;
import trdaan.shapemania.architecture_enforcement_classes.GameState;
import trdaan.shapemania.architecture_enforcement_classes.GraphicGameObjectInterface;
import trdaan.shapemania.database.AsyncAddHighScore;
import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.CircleObject;
import trdaan.shapemania.entity_classes.HighScore;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;
import trdaan.shapemania.entity_classes.SquareObject;


import trdaan.shapemania.architecture_enforcement_classes.PossibleColors;
import trdaan.shapemania.game_helper_classes.RandomGameShapeGenerator;

public class GameActivity extends AppCompatActivity{
    private GameState currentState;
    private String TAG = "GameActivity";
    //private LevelSurfaceView lsv;
    private SurfaceView rsv;
    private SurfaceHolder sh;
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private ArrayList<GraphicGameObjectInterface> objList ;
    private GraphicGameObjectInterface animTest; //ToDo#8
    private FrameLayout main ;
    private int lvlCount;//ToDo#9
    //GameThread gameThread;
    private int targets=0;
    private int claimed=0;
    private int targetsToWin=0;
    private CountDownTimer timer;
    private long score=0;
    //private long timeToFinishLevel=30000;//30k 30sec
    private long timeToFinishLevel;
    private long remainingTime=0;
    private TextView txtScore;
    private Level l;
    private PlayerProfile pp;
    private ArrayList<GraphicGameObjectInterface> argo = new ArrayList<GraphicGameObjectInterface>();
    private Animator testx;//ToDo#10


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        try{
            init();
        }catch (Exception e){
            finish();//if game could not render then close activity
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }




    }
    public void onBackPressed() {
        timer.cancel();
        //cancel timer when player cancels out of game activity, otherwise player will get a gameover activity shoved in its face
        Log.i(TAG,"timer cancelled player backs out of game");
        super.onBackPressed();
    }
    public void init(){
        this.main= (FrameLayout) findViewById(R.id.activity_game);
        Bundle extras = getIntent().getExtras();
        try {
            currentState = (GameState) extras.get("GAMESTATE");
            this.l = (Level)extras.get("LEVEL");
            this.pp=(PlayerProfile)extras.get("PLAYER");
            Log.i(TAG,"intent received: "+currentState.toString());
            TextView txtLvl = (TextView)findViewById(R.id.txtCurrLvl);
            txtLvl.setText("lvl: "+l.levelNum);//set current level
            timeToFinishLevel = ((l.time)+1000);//sets time given to player + extra time to render game when countdown has already begun
        }catch (Exception e){}//empty

        rsv=(SurfaceView) findViewById(R.id.surfaceView2);
        sh=rsv.getHolder();
        lvlCount = 45;//for testing
        final TextView txtRemainingTime = (TextView)main.findViewById(R.id.txtRemainingTime);
        final int safeHeight=txtRemainingTime.getHeight();//gets height where shapes arent supposed to be drawn (logic is in generator)
        final int safeWidth=0;
        txtScore= (TextView)main.findViewById(R.id.txtScore);


        //this waits for 4 seconds before starting the countdown timer, during this time
        //the countdow to start the game will be shown (for five seconds, this is in another delaytimer)
        // the last second of the countdown will be used to render the game on slower devices
        //this time is compensated for
        //this is the timer visible in the upper left corner of the screen
        final Handler delayTmer = new Handler();
        delayTmer.postDelayed(new Runnable() {
            @Override
            public void run() {

                timer = new CountDownTimer(timeToFinishLevel, 1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txtRemainingTime.setText("" + millisUntilFinished / 1000);
                        remainingTime=(millisUntilFinished/100);
                    }
                    @Override
                    public void onFinish() {
                        //when time is up you lost
                        Intent i = new Intent(getApplication(),GameOverActivity.class);
                        i.putExtra("GAMESTATE", GameState.LEVEL_FAILED);
                        Log.e(TAG,"thescore to be send:"+score);
                        i.putExtra("SCORE",score);
                        //i.putExtra("")
                        startActivity(i);
                        finish();
                    }

                }.start();
                         }
                        }, 4000);



        //surfaceholder callback
        sh.addCallback(new SurfaceHolder.Callback(){

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {


                Canvas canvas = rsv.getHolder().lockCanvas();
                if( canvas != null ) {
                    synchronized (rsv.getHolder()) {

                        //get generator
                        final RandomGameShapeGenerator rgen = new RandomGameShapeGenerator(canvas,rsv.getContext(),safeHeight,safeWidth,l);
                        argo = rgen.generateArrayOfShapes();//the objects to be drawn
                        Log.i(TAG,"shapes retrieved");
                        TextView txtTargets = (TextView)main.findViewById(R.id.txtTarget);
                        txtTargets.setText(rgen.getTargetString());//above in screen display the targets incase someone wasnt paying atention
                        //ToDo#11 display target shape as well in box or something
                        setTouchListener(argo);//set touchlistener and pass the array along to be able to compare touch and bounds
                        final TextView txtCD = (TextView)findViewById(R.id.txtCountDownTillStart);//countdown till game starts

                        //tillstart is used, it displays the countdown before the game starts.
                        //which clearly is there..
                        //even though android studio says it is unused.. very very typical
                        Log.i(TAG,"countdown started");
                        CountDownTimer tillStart = new CountDownTimer(5000, 1000){
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(millisUntilFinished>3000){
                                    txtCD.setText("Your targets are the "+rgen.getTargetString()+"S");//when time is above 3000ms
                                }else{
                                    txtCD.setText(millisUntilFinished/1000 +"..");//when time is below 3000ms
                                }
                            }
                            @Override
                            public void onFinish() {
                                txtCD.setVisibility(View.GONE);
                            }//when timer finishes
                        }.start();

                        final Handler handler = new Handler();
                        //this is a new runnable, also another thread?
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                        targets=0;
                        claimed=0;
                        targetsToWin=0;
                                //set vars for logging and scoremanaging
                                Log.i(TAG,"score vars set");

                        for(GraphicGameObjectInterface ggo:argo){

                            View vr = (View)((View)ggo).findViewById(((View)ggo).getId());
                            //i could pass ggo in stead of parsing it to view, but tried this because maybe, just maybe the animations would work
                            //spoiler alert, they didnt.
                            //benefit of this is that each shape does have an id and only the shapes that have an id will be rendered (ie. ones
                            //that were generated with errors will not be shown)
                            main.addView(vr);//add shape to view
                            if(ggo.getState()==GameObjectState.TARGET){
                                targets++;
                                targetsToWin++;
                            }//setting amount of targets needed to win, this will be displayed in future version
                            //ToDo#12
                        }

                    }
                }, 5000);

                    }
                    rsv.getHolder().unlockCanvasAndPost(canvas);//unlocks canvas for other methods etc.
                }

            }


            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                //unused
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                //unused
            }
        });

        Log.i(TAG,"game begun");
    }
    protected void updateCanvas(Canvas canvas){
        //maybe build animation here?
        //call from surface changed
    }

    private void setTouchListener(final ArrayList<GraphicGameObjectInterface> argo){
        //set mediaplayers
        final MediaPlayer correct = MediaPlayer.create(this, R.raw.misc_menu);
        final MediaPlayer wrong = MediaPlayer.create(this,R.raw.negative_2);
        Log.i(TAG,"made mediaplayers");
        Log.i(TAG,"these mediaplayers have their own logging somewhere that clogs the whole thing..");
        //this touchlistener listens for ANY touch on the entire screen! it is build on the layoutmanager. this way,
        //even when a shape is drawn outside of its designated area it will be clickable, and thus the level can be finished
        main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                float x = e.getX();
                float y = e.getY();

                int action = e.getAction();
                Log.i(TAG,"action="+e.toString());
                if(action == MotionEvent.ACTION_UP) {//if finger lifted from screen
                    for(GraphicGameObjectInterface ggo:argo) {
                        if (ggo.getBoundingBox().contains(x, y) == true) {
                            Log.i(TAG,"touched something");

                           //uncomment this to have yellow circles drawn on the spot where you touched.
                           //if the circle touches a shape that shapes touchevent would be triggered.
                           //if a shape touches multiple shapes then multiple shapes wiill be triggered,
                           //this leads to a decrease of score gain on higher levels, to make things more difficult yknow
                           /*
                            GraphicGameObjectInterface testx = new TestObject(v.getContext(), x, y, GameObjectState.ACTIVE);
                            testx.setColor(PossibleColors.YELLOW);
                            main.addView((View) testx);*/
                            if(ggo.getState()==GameObjectState.TARGET){//iff pressed thing is target
                                ggo.setState(GameObjectState.CLAIMED);//set claimed
                                //ggo.refresh();
                                correct.start();//play sound
                                ggo.setGone();//let it vanish (actually it is invisible rather than gone!), it can popup again in new gammodes
                                targets=targets-1;
                                claimed++;
                                score=score+10;
                                txtScore.setText(score+"");
                                Log.e(TAG,"targets, claimed, totaal targets, score"+targets+","+claimed+","+targetsToWin+","+score);

                              /*  Toast.makeText(main.getContext(), "+10 points",
                                        Toast.LENGTH_SHORT).show();*/
                                //toast was quite annoying actually
                                if(claimed==targetsToWin){//when the amount of targets clicked and the amount of targets to win are the same:

                                    timer.cancel();//disable timer
                                    Log.e(TAG,"time remaining:"+remainingTime);
                                    score=score+remainingTime;//add remaining time to score (milis/100)
                               /*     Toast.makeText(main.getContext(), "Je hebt gewonnen! Score: "+score,
                                            Toast.LENGTH_LONG).show();*/
                                    //for debugging^
                                    txtScore.setText(score+"");
                                    Intent i = new Intent(getApplication(),LevelFinishedActivity.class);
                                    i.putExtra("GAMESTATE", GameState.LEVEL_COMPLETED);
                                    i.putExtra("SCORE",score);
                                    i.putExtra("PLAYER",pp);
                                    //i.putExtra("")
                                    startActivity(i);
                                    //launches level finished activity

                                    //make new highscore and add to db.
                                    //ToDo#13
                                    HighScore hs = new HighScore();
                                    hs.playerName=pp.name;
                                    hs.score=score;
                                    hs.level=pp.currentLevel;
                                    AsyncAddHighScore addHs = new AsyncAddHighScore(new DatabaseHandler(main.getContext()),hs);
                                    addHs.execute();
                                    //add to db

                                    finish();

                                }
                            }//end if target
                            else{
                                //execute this for clicks on wrong ones
                                //setgone to let the wrong ones disappear, due to feedback from my testplatform i decided is was better
                                //to not let them disappear.
                                //also score was decreased to -1 on a faulty click.
                                //used to be -5, might be upped to -3 to make it more difficult, but still fair
                                //(multiple difficulties aside from levels?)
                                //ggo.setGone();
                                score = score-1;
                                wrong.start();

                            }//endese
                            //always execute whatever is here
                            //cuurently that is nothing


                        }
                    }
                }
                return true;
            }
        });
        Log.i(TAG,"ontouch main set");
    }


    //below here are testmethods used for developing. I did not remove this because i need them later
    // when i will expand the app to be playstore ready.






/*
            //either rotates only the last object, or everything around the center of the screen. excluding the objects boundingboxes
            //update.. after 2days still unable to find a way to rotate object from -60deg to +60deg on its own centerpivot..
            //really fucking wierd..
            private void doExecuteRotate(){
                 testx.start();
                testx.setDuration((l.time+2000));
                AnimatorSet animSet = new AnimatorSet();
                 animSet.play(testx);
                animSet.start();
                    //((View)v).invalidate();
                }
    private void initRotate(){
        testx = AnimatorInflater.loadAnimator(this,R.animator.test);
    }

    private void doRotate(GraphicGameObjectInterface v){
      //  TextView tempView = (TextView)v;
     //   Animation aro = AnimationUtils.loadAnimation(this,R.anim.rotate_triangle_select_level);
      //  v.startAnimation(aro);
     //   v.invalidate();


        testx.setTarget(v);
        doExecuteRotate();
       // testx.start();

    }*/


    private boolean isViewContains(View v, int rx,int ry) {
        int[] l = new int[2];
        v.getLocationOnScreen(l);
        Rect rect = new Rect(l[0], l[1], l[0] + v.getWidth(), l[1] + v.getHeight());
        Log.i(TAG,"v.height and with"+v.getHeight()+","+v.getWidth());
        return rect.contains(rx, ry);
    }
    private void initSomeOtherStuff(Canvas canvas){

        this.SCREEN_WIDTH=(canvas.getWidth()-60);
        this.SCREEN_HEIGHT=(canvas.getHeight()-60);
        Log.i(TAG,"width and height: "+SCREEN_WIDTH+", "+SCREEN_HEIGHT);
        Random r = new Random();


      //  final GraphicGameObjectInterface testObj =  new TestObject(this,50,50,GameObjectState.ACTIVE);
       // testObj.setColor(PossibleColors.GREEN);
        objList = new ArrayList<>();
        for(int i=0;i<15;i++){
            float y = r.nextInt(SCREEN_HEIGHT - 50) + 50;
            float x = r.nextInt(SCREEN_WIDTH-50)+50;

            //and smaller radius for smaller/older devices, on which the shapes will be bigger, make radius relative to screensize!
            GraphicGameObjectInterface testx =  new CircleObject(this,x,y,GameObjectState.ACTIVE);
            testx.setColor(PossibleColors.RED);
            objList.add(testx);
            Log.i(TAG,"set: "+i);
        }
        Log.i(TAG,"okay so far");

    }



}
