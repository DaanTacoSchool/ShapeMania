package trdaan.shapemania;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 15-1-2017.
 */

public class AboutActivity extends Activity {
    private String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Log.i(TAG,"mind the copyright notice :)");
        //if you dont know that happens in this class then you should close your pc and follow some classes first..
    }

}
