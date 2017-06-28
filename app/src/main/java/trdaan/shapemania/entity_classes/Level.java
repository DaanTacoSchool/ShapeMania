package trdaan.shapemania.entity_classes;

import java.io.Serializable;

/**
 * Created by D2110175 on 14-1-2017.
 */

public class Level implements Serializable{
    public int uid;//database primary key
    public int amountOfTargets;//the predefined amount of targets (this amount wil be guaranteed)
    public int amountOfDuds;//the amount of other shapes (these are randomly generated and it is possible that some of these will become targets)
    public int levelNum;//indicator what level this is
    public long time;//time allocated to finish the level
}
