package trdaan.shapemania;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import trdaan.shapemania.architecture_enforcement_classes.GameState;
import trdaan.shapemania.database.AsyncVerifyUsername;
import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.CurrentPlayerData;
import trdaan.shapemania.entity_classes.PlayerProfile;

/**
 * Created by D2110175 on 15-1-2017.
 */

public class NewUserActivity extends Activity implements AsyncVerifyUsername.CallbackCheckName{
    private String TAG = "newUserActivity";
    private TextView txtName;
    private DatabaseHandler dbh;
    private FrameLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        this.main = (FrameLayout)findViewById(R.id.masterLayout);
        dbh = new DatabaseHandler(main.getContext());

        txtName = (TextView) findViewById(R.id.input_name);

        Button btnConfirm;
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate user here
                AsyncVerifyUsername avu = new AsyncVerifyUsername(dbh,main.getContext(),txtName.getText().toString());
                avu.execute();

            }
        });
    }


    @Override
    public boolean nameIsTaken(boolean isTaken) {//this is the callback method
        if(isTaken){
            //name not available
            txtName.setBackgroundColor(Color.RED);
            Log.e(TAG,"name already taken");
            //toast?
        }else{
            //its okay go add it
            //if players are loaded set current active to inactive
            //after that add new player to db and then load it in memory
            PlayerProfile oldP;
            if(CurrentPlayerData.arePlayersLoaded()){//check if players are loaded
                CurrentPlayerData.loadActivePlayer();//load current player
                oldP= CurrentPlayerData.getActivePlayer();//get current active player
                String sql = "UPDATE PlayerNames SET activePlayer = 0 WHERE Name = '"+oldP.name+"'";//set current active user to  inactive
                SQLiteDatabase db = dbh.getWritableDatabase();
                db.execSQL(sql);
            }
            PlayerProfile pp = new PlayerProfile();
            pp.activePlayer=1;
            pp.name = txtName.getText().toString();
            pp.totalScore=0;
            pp.currentLevel=2;
            dbh.addPlayer(pp);
            dbh.loadPlayer(main.getContext());

            //load player and finish activity, player might need to select player in list
            //FIXME#3
            finish();
        }
        return false;
    }
}
