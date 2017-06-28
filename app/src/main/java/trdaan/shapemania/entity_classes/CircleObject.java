package trdaan.shapemania.entity_classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import trdaan.shapemania.architecture_enforcement_classes.Animations;
import trdaan.shapemania.architecture_enforcement_classes.Direction;
import trdaan.shapemania.architecture_enforcement_classes.GameObjectState;
import trdaan.shapemania.architecture_enforcement_classes.GraphicGameObjectInterface;
import trdaan.shapemania.architecture_enforcement_classes.PossibleColors;
import trdaan.shapemania.architecture_enforcement_classes.PossibleShapes;

/**
 * Created by D2110175 on 11-1-2017.
 * note: no logging due to the large amount of times this is called
 */

public class CircleObject extends View implements GraphicGameObjectInterface {
    private Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int theColor;//marked
    private float xPos;
    private float yPos;
    private int radius=25;
    private PossibleColors c;
    private PossibleShapes s;
    private GameObjectState gos;
    RectF boundingBox;
    private Animation anim;

    public CircleObject(Context context,float xPos, float yPos,GameObjectState gos){
        super(context);
        //set vars
        this.xPos=xPos;
        this.yPos=yPos;
        this.gos=gos;
        this.boundingBox = new RectF();
        boundingBox.left = xPos-(radius*2);
        boundingBox.top = yPos-(radius*2);
        boundingBox.right = xPos + (radius*2);
        boundingBox.bottom = yPos + (radius*2);
    }
    public void setColor(PossibleColors c){
        this.c=c;
        tPaint.setColor(c.getValue());
    }

    @Override
    public void setShape(PossibleShapes s) {
        this.s=s;
    }

    @Override
    public int getColor() {
    return c.getValue();
    }

    @Override
    public RectF getBoundingBox() {
        return boundingBox;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw the circle
        canvas.drawCircle(xPos, yPos, radius, tPaint);
    }
    public void setGone(){
        this.setVisibility(INVISIBLE);
    }
    public void setState(GameObjectState gos){
        this.gos=gos;
    }
    public void refresh(){
        if(gos!=null){
            if(gos==GameObjectState.CLAIMED){
                this.setVisibility(GONE);
                this.boundingBox=null;
                Log.i("gameobject","isclaimed");
            }
        }
    }
    public GameObjectState getState(){
        return gos;
    }

    @Override
    public void setDirection(Direction direction) {
        //do nothing
    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public Animation getAnimationTag() {
        return anim;
    }

    @Override
    public void setAnimation(Animation anim) {
        this.anim = anim;
    }

    //HACK: for testing
    public int getThisViewID(){
       return ((View)this).getId();
    }

}
