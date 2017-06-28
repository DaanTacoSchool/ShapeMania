package trdaan.shapemania.entity_classes;

import java.io.Serializable;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class HighScore implements Serializable {
    public String playerName;//player that got the high score
    public long score;//score itself
    public int level;//level the score was achieved on
    //note to self: i forgot the uid column(update, it does exist in db, just not here)
}
