package trdaan.shapemania;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;

import trdaan.shapemania.architecture_enforcement_classes.GameState;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 15-1-2017.
 */

public class SettingsActivity extends Activity {
    private String TAG = "SettingsActivity";
    private PlayerProfile pp;
    private FrameLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.main = (FrameLayout) findViewById(R.id.masterLayout);
        init();
    }

    private void init(){
        Bundle extras = getIntent().getExtras();
        try {

            this.pp = (PlayerProfile) extras.get("PLAYER");
            Log.i(TAG,"intent received");
        }catch (Exception e){}//empty

        Button btnPlayer;//goes to player main activity
        btnPlayer = (Button) findViewById(R.id.btnPlayer);
        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(main.getContext(),PlayerProfileMainActivity.class);
                i.putExtra("PLAYER", pp);
                startActivity(i);
                Log.i(TAG, "player main launched");


            }
        });
        Button btnAbout;//goes to about activity
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(main.getContext(),AboutActivity.class);
                startActivity(i);
                Log.i(TAG, "about launched");
            }
        });
    }

}
