package trdaan.shapemania;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import trdaan.shapemania.architecture_enforcement_classes.GameState;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class PlayerProfileMainActivity extends Activity {
    private String TAG = "ppActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile_main);

        //go to new user
        Button newProfile;
        newProfile = (Button) findViewById(R.id.btnNewProfile);
        newProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplication(),NewUserActivity.class);
                i.putExtra("GAMESTATE", GameState.PAUSE);
                startActivity(i);
                Log.i(TAG, "new user fired");


            }
        });
        //go to selectplayer
        Button selectProfile;
        selectProfile = (Button) findViewById(R.id.btnSelectProfile);
        selectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplication(),SelectPlayerActivity.class);
                i.putExtra("GAMESTATE", GameState.PAUSE);
                startActivity(i);
                Log.i(TAG, "select player fired");


            }
        });
    }
}
