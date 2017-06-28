package trdaan.shapemania.game_helper_classes;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Random;

import trdaan.shapemania.R;
import trdaan.shapemania.architecture_enforcement_classes.Direction;
import trdaan.shapemania.architecture_enforcement_classes.GameObjectState;
import trdaan.shapemania.architecture_enforcement_classes.GraphicGameObjectInterface;
import trdaan.shapemania.architecture_enforcement_classes.PossibleColors;
import trdaan.shapemania.architecture_enforcement_classes.PossibleShapes;
import trdaan.shapemania.entity_classes.CircleObject;
import trdaan.shapemania.entity_classes.Level;
import trdaan.shapemania.entity_classes.SquareObject;
import trdaan.shapemania.entity_classes.StarObject;
import trdaan.shapemania.entity_classes.TriangleObject;

/**
 * Created by D2110175 on 11-1-2017.
 */

public class RandomGameShapeGenerator {
    private Random r = new Random();
    private Canvas canvas;
    private PossibleShapes targetShape;
    private PossibleColors targetColor;
    private  Context context;
    private int safeAreaHeight;
    private int safeAreaWidth;
    private Level l;
    private String TAG ="rgsGen";



    public RandomGameShapeGenerator(Canvas canvas, Context context,int heightDrawArea, int widthDrawArea,Level l){
        this.canvas=canvas;
        this.targetShape=getRandomShape();
        this.targetColor=getRandomColor();
        this.context =context;
        this.safeAreaHeight=heightDrawArea;
        this.safeAreaWidth=widthDrawArea;
        this.l=l;
        Log.i(TAG,"constructor finished initting vars");

        //minimum
        if(safeAreaWidth<70){
            this.safeAreaWidth=70;
        }
        if(this.safeAreaHeight<70){
            this.safeAreaHeight=70;
        }
        Log.i(TAG,"constructor finished");
        Log.e(TAG,"no further logging because it flooded the logfiles really quickly");
    }

    //basically combines everything and puts it in an array
    public ArrayList<GraphicGameObjectInterface> generateArrayOfShapes(){
        ArrayList<GraphicGameObjectInterface> argo = new ArrayList<>();

        //make the duds
        for(int i=0;i<l.amountOfDuds;i++){
            GraphicGameObjectInterface tObj= makeRandomShape();
            ((View)tObj).setId(((View)tObj).generateViewId());
            argo.add(tObj);
        }
        //make the targets
        for(int j=0;j<l.amountOfTargets;j++){
            GraphicGameObjectInterface targObj= makeTargetShape();
            Object obj = ((View)targObj).generateViewId();
            ((View)targObj).setId((Integer) obj);
            argo.add(targObj);
        }
        //(generate object then generate and set id on generated object in both the bodies)

        return argo;
    }
    //old versions but replaced by use of a variable
    public PossibleShapes whatIsTheTargetShape(){
        return this.targetShape;
    }
    public PossibleColors whatIsTheTargetColor(){
        return this.targetColor;
    }

    public GraphicGameObjectInterface makeTargetShape(){
        int SCREEN_WIDTH=(canvas.getWidth()-60);
        int SCREEN_HEIGHT=(canvas.getHeight()-60);
        float y = r.nextInt(SCREEN_HEIGHT - safeAreaHeight) + safeAreaHeight;
        float x = r.nextInt(SCREEN_WIDTH-safeAreaWidth)+safeAreaWidth;
        //above: get bounds where shape will be drawn within, and get random x and y positions
        GraphicGameObjectInterface theComponent=null;
        //gets new object that is a target
        switch (targetShape){
            case CIRCLE:
                theComponent = new CircleObject(context, x, y, GameObjectState.TARGET);
                break;
            case TRIANGLE:
                theComponent = new TriangleObject(context, x, y, GameObjectState.TARGET);
                break;
            case SQUARE:
                theComponent = new SquareObject(context, x, y, GameObjectState.TARGET);
                break;
            case STAR:
                theComponent = new StarObject(context, x, y, GameObjectState.TARGET);
                break;
        }
        //set other information
        theComponent.setShape(targetShape);
        theComponent.setDirection(Direction.LEFT);//to make things easier all predefined targets are set left oriented
        theComponent.setColor(targetColor);
        theComponent.setAnimation(getRandomAnimation());
        return theComponent;
    }
    public GraphicGameObjectInterface makeRandomShape(){

        int SCREEN_WIDTH=(canvas.getWidth()-60);
        int SCREEN_HEIGHT=(canvas.getHeight()-60);
        float y = r.nextInt(SCREEN_HEIGHT - safeAreaHeight) + safeAreaHeight;
        float x = r.nextInt(SCREEN_WIDTH-safeAreaWidth)+safeAreaWidth;
        //get bounds to draw shape in and random x and y positions inbetween those

        GraphicGameObjectInterface theComponent=null;
        PossibleShapes tempShape = getRandomShape();
        PossibleColors tempColor = getRandomColor();
        Direction direction;
        //get random shape and color and make object

        int intDir = r.nextInt(5-1)+1;//random direction


        //the secret to true randomness is that even though there are a minmum amount of targets, there is no maximum
        //a random shape can in fact become a target if it has the right shape and color,
        // these can be distinguished from predefined ones through orientation

        switch (tempShape){
            case CIRCLE:
                if(tempShape==targetShape && tempColor==targetColor) {
                    theComponent = new CircleObject(context, x, y, GameObjectState.TARGET);
                }else{
                    theComponent = new CircleObject(context, x, y, GameObjectState.ACTIVE);
                }
                break;
            case TRIANGLE:
                if(tempShape==targetShape && tempColor==targetColor) {
                    theComponent = new TriangleObject(context, x, y, GameObjectState.TARGET);
                }else{
                    theComponent = new TriangleObject(context, x, y, GameObjectState.ACTIVE);
                }
                break;
            case SQUARE:
                if(tempShape==targetShape && tempColor==targetColor) {
                    theComponent = new SquareObject(context, x, y, GameObjectState.TARGET);
                }else{
                    theComponent = new SquareObject(context, x, y, GameObjectState.ACTIVE);
                }
                break;
            case STAR:
                if(tempShape==targetShape && tempColor==targetColor) {
                    theComponent = new StarObject(context, x, y, GameObjectState.TARGET);
                }else{
                    theComponent = new StarObject(context, x, y, GameObjectState.ACTIVE);
                }
                break;
        }

        theComponent.setColor(tempColor);
        theComponent.setShape(tempShape);
        theComponent.setAnimation(getRandomAnimation());
        //set the color and shape and animation(currently not working)

        //get random direction. could maybe put this in seperate function to be used with the targets as well, but currently there are no
        //plans for that
        switch (intDir){
            case 1:
                theComponent.setDirection(Direction.UP);
                break;
            case 2:
                theComponent.setDirection(Direction.LEFT);
                break;
            case 3:
                theComponent.setDirection(Direction.DOWN);
                break;
            case 4:
                theComponent.setDirection(Direction.RIGHT);
                break;
        }
        if(theComponent.getDirection()==null){
            theComponent.setDirection(Direction.DOWN);
        }



      //no logging here because it will fill the log really quickly



        return theComponent;
    }

    //inDev

    public Animation getRandomAnimation(){
        //this is not finished due to a bug, the animations wont play due to some reason. I still havent
        //decided wether to fix this at all. On high levels this can put too much load on the phone.
        int iAnim = r.nextInt(4-1)+1;
       // Animations rAnim = null;
        Animation rAnim=null;
        switch(iAnim){
            case 1:
               // rAnim=Animations.BOUNCE;
                break;
            case 2:
                //rAnim=Animations.ROTATE;
               rAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_triangle_select_level);
                break;
            case 3:
               // rAnim=Animations.STROBE;
                break;
            case 4:
               // rAnim=Animations.ROTATE;//will never be called but can be used for future stuff
                break;
        }
        if(rAnim==null){
            rAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_triangle_select_level);
        }
        //rAnim.setRepeatCount(Animation.INFINITE);
        rAnim.setStartTime(0);
       // rAnim.start();

        return rAnim;

    }

    public PossibleShapes getRandomShape(){
        //retrieve random shape

        int iShape = r.nextInt(5-1)+1;
        PossibleShapes rShape=null;

        //determine shape
        switch(iShape){
            case 1:
                rShape=PossibleShapes.CIRCLE;
                break;
            case 2:
                rShape=PossibleShapes.TRIANGLE;
                break;
            case 3:
                rShape=PossibleShapes.SQUARE;
                break;
            case 4:
                rShape=PossibleShapes.STAR;
                break;
        }
        if(rShape==null){
            rShape=PossibleShapes.CIRCLE;//default
        }
        return rShape;

        //return null;
    }
    public PossibleColors getRandomColor(){
        //retrieve random color

        int iColor = r.nextInt(5-1)+1;
        PossibleColors rColor=null;

        //determine color
        switch(iColor){
            case 1:
                rColor=PossibleColors.BLUE;
                break;
            case 2:
                rColor=PossibleColors.RED;
                break;
            case 3:
                rColor=PossibleColors.GREEN;
                break;
            case 4:
                rColor=PossibleColors.YELLOW;
                break;

        }
        if(rColor==null){
            rColor=PossibleColors.RED;//default
        }
        return rColor;

        //return null;
    }
    public String getTargetString(){
        //return the target and color as string to display it
        String rVal = this.targetColor + " "+this.targetShape;
        return rVal;
    }
}
