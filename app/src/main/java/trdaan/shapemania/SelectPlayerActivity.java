package trdaan.shapemania;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import trdaan.shapemania.database.AsyncLoadPlayer;
import trdaan.shapemania.database.DatabaseHandler;
import trdaan.shapemania.entity_classes.PlayerProfile;
import trdaan.shapemania.game_helper_classes.SelectPlayerAdapter;

/**
 * Created by D2110175 on 15-1-2017.
 */

public class SelectPlayerActivity extends Activity implements View.OnClickListener, AsyncLoadPlayer.CallbackPlayers {
    private String TAG = "SelectPlayer";
    private PlayerProfile pp;//ToDo#1
    private FrameLayout main;
    private ListView playerListView;
    private SelectPlayerAdapter playerAdapter;
    private ArrayList<PlayerProfile> profileArray = new ArrayList<PlayerProfile>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);
        init();
    }
    private void init(){

        Log.i(TAG, "init");
        this.main = (FrameLayout)findViewById(R.id.masterLayout);//set this to retrieve context
        playerListView = (ListView)main.findViewById(R.id.playerListView);//start setting up listview
        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG,"itemclick");//do nothing here, only on buttonclick, which is located in the adapter!
            }
        });

        playerAdapter=new SelectPlayerAdapter(main.getContext(),profileArray,this);
        playerListView.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();
        DatabaseHandler dbh = new DatabaseHandler(main.getContext());
        dbh.loadPlayer(main.getContext());//this will return an array of all users using a callback
    }

    @Override
    public void retrievePlayers(ArrayList<PlayerProfile> clbArrayPlayers) {
        //this is the callback
        profileArray = clbArrayPlayers;
        playerAdapter.arl=profileArray;//without this the list wouldnt update
        playerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG,"click");//because an empty method would be such a moodkill :p, for button onclick, go to the adapter
    }
}
