package trdaan.shapemania.entity_classes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import trdaan.shapemania.R;
import trdaan.shapemania.architecture_enforcement_classes.Animations;
import trdaan.shapemania.architecture_enforcement_classes.Direction;
import trdaan.shapemania.architecture_enforcement_classes.GameObjectState;
import trdaan.shapemania.architecture_enforcement_classes.GraphicGameObjectInterface;
import trdaan.shapemania.architecture_enforcement_classes.PossibleColors;
import trdaan.shapemania.architecture_enforcement_classes.PossibleShapes;

/**
 * Created by D2110175 on 12-1-2017.
 */
//no logging due to the large amount of times this is called
public class StarObject extends View implements GraphicGameObjectInterface {

    private static final Direction DEFAULT_DIRECTION = Direction.LEFT;
    private static final int DEFAULT_COLOR = 0xff757575;

    // private Paint mPaint;
    //private Path mTrianglePath;
    private Direction mDirection;
    private int mColor;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int theColor;
    private float xPos;
    private float yPos;
    private int radius=50;
    private PossibleColors c;
    private PossibleShapes s;
    private GameObjectState gos;
    RectF boundingBox;
    private int width;
    private int height;
    private Animation anim;


    public StarObject(Context context,float xPos, float yPos,GameObjectState gos){
        super(context);
        this.xPos=xPos;
        this.yPos=yPos;
        this.gos=gos;
        this.boundingBox = new RectF();
        boundingBox.left = xPos-(radius*2);
        boundingBox.top = yPos-(radius*2);
        boundingBox.right = xPos + (radius*2);
        boundingBox.bottom = yPos + (radius*2);

        //for testing sakes
        this.width=radius;
        this.height=radius;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);

    }



    public void setDirection(Direction direction) {
       this.mDirection=direction;
    }

    @Override
    public Direction getDirection() {
        return this.mDirection;
    }





    @Override
    public void setColor(PossibleColors c) {
        if (mColor != c.getValue()) {
            mColor = c.getValue();
            if (mPaint != null) {
                mPaint.setColor(c.getValue());
            }
            invalidate();
        }
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


    private Path createStarBySize(float width, int steps) {//width is diameter of star, steps is amount of arms
        float halfWidth = width / 2.0F;
        float bigRadius = halfWidth;
        float radius = halfWidth / 2.0F;
        float degreesPerStep = (float) Math.toRadians(360.0F / (float) steps);
        float halfDegreesPerStep = degreesPerStep / 2.0F;
        Path ret = new Path();
        ret.setFillType(Path.FillType.EVEN_ODD);
        float max = (float) (2.0F* Math.PI);
        ret.moveTo(width, halfWidth);
        for (double step = 0; step < max; step += degreesPerStep) {
            ret.lineTo((float)(halfWidth + bigRadius * Math.cos(step)), (float)(halfWidth + bigRadius * Math.sin(step)));
            ret.lineTo((float)(halfWidth + radius * Math.cos(step + halfDegreesPerStep)), (float)(halfWidth + radius * Math.sin(step + halfDegreesPerStep)));
        }
        ret.close();
        return ret;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path p = createStarBySize(radius,5);//create star
        p.offset(xPos,yPos);//move star to location
        canvas.drawPath(p,mPaint);//draw it

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
                Log.i("gameobject","isclaimed");//unused due to the aggressive nature of it
            }
        }
    }
    public GameObjectState getState(){
        return gos;
    }

    public Animation getAnimationTag() {
        return anim;
    }

    @Override
    public void setAnimation(Animation anim) {
        this.anim = anim;
    }


}