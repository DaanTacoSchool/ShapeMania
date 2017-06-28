package trdaan.shapemania.entity_classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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
 * Created by D2110175 on 12-1-2017.
 * NOTE: no logging because it will slow the app down too much due to the sheer amount of times this class is made
 */

public class SquareObject extends View implements GraphicGameObjectInterface {
    private Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int theColor;
    private float xPos;
    private float yPos;
    private int radius=50;
    private PossibleColors c;
    private PossibleShapes s;
    private GameObjectState gos;
    RectF boundingBox;
    private Animation anim;

    public SquareObject(Context context,float xPos, float yPos,GameObjectState gos) {
        super(context);
        this.xPos=xPos;
        this.yPos=yPos;
        this.gos=gos;
        this.boundingBox = new RectF();
        boundingBox.left = xPos-(radius*2);
        boundingBox.top = yPos-(radius*2);
        boundingBox.right = xPos + (radius*2);
        boundingBox.bottom = yPos + (radius*2);
        //warning: there are not many exciting things here, you might die of boredom
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

        //pretty clear what happens here
        Rect rect = new Rect();
        rect.left = (int)xPos;
        rect.top = (int)yPos;
        rect.right = (int)xPos + (int)radius;
        rect.bottom = (int)yPos + (int)radius;
        canvas.drawRect(rect,tPaint);

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
                Log.i("gameobject","isclaimed");//unused cause its too aggressive
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

}
