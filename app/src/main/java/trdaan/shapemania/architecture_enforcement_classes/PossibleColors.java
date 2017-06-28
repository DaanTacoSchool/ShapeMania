package trdaan.shapemania.architecture_enforcement_classes;

import android.graphics.Color;

/**
 * Created by D2110175 on 11-1-2017.
 */

public enum PossibleColors {

    //also bind actual color value to enum
    YELLOW(Color.YELLOW),BLUE(Color.BLUE),RED(Color.RED),GREEN(Color.GREEN);
    private final int value;
    PossibleColors(int value) {
        this.value=value;
    }
    public int getValue(){
        return value;
    }



}
