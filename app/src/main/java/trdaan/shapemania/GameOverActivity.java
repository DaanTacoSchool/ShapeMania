package trdaan.shapemania;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import trdaan.shapemania.architecture_enforcement_classes.GameState;

/**
 * Created by D2110175 on 13-1-2017.
 */

public class    GameOverActivity extends Activity {
    private FrameLayout main;
    private View v;
    private GameState gs;
    private long score;
    private String TAG="GameOverActivity";

    public GameOverActivity(){

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        init();
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

    private void init(){

        this.main= (FrameLayout) findViewById(R.id.masterLayout);
        this.v = (View)this.findViewById(android.R.id.content);//was used in previous version and will be used for indev features

        Bundle extras = getIntent().getExtras();
            this.gs = (GameState) extras.get("GAMESTATE");
            Log.i(TAG,"intent received: "+gs.toString());
            this.score = (long)extras.get("SCORE");
            Log.i(TAG,"intent received: "+score);

        Button btnMain = (Button)main.findViewById(R.id.btnBackToMain);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//goes to main game menu, not mainactivity

            }
        });

        TextView txtScore= (TextView)main.findViewById(R.id.txtFadeInScore);//fadein animations will be added TODO#7
        txtScore.setText(score+"");
        Log.i(TAG,"score:"+score);
    }

    //test method to test animations, this will be refactured to play the fade in animation later on, see todo7
    public void playRotate(){
       // TextView vr = (TextView)findViewById(R.id.testTextView);
      //  Animation vxa = (Animation) AnimationUtils.loadAnimation(main.getContext(),R.anim.rotate_triangle_select_level);
        // vr.setAnimation(vxa);
        // vxa.setStartTime(0);
        // vxa.setDuration(3000);
       // vr.startAnimation(vxa);

    }
}