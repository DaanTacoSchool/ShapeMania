package trdaan.shapemania.entity_classes;

import java.io.Serializable;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class PlayerProfile implements Serializable {

    public int uid;//database primary key
    public String name;//playername
    public long totalScore;//total score all levels combined, more info underneath
    public int currentLevel;//the highest available level the player has unlocked
    public int activePlayer;//identifier to check if the player is currently active

    /*
    the totalscore will be a total of all the scores from all the levels. this is currently not implemented yet.
    the reason for this is that it is of no use now. this will be used in the future as a form of "exp" points to unlock new levels and gamemodes
    i cannot tell anything about those gamemodes or system because it is not yet relevant
     */
}
