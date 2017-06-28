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
 * Created by D2110175 on 11-1-2017.
 *NOTE: no logging here because of the large amount of times this is called. th sheer amount that has to be written to the log will create lag
 * getTrianglePath and init retrieved from: (those functions served as basis for generating the starpath)
 * https://github.com/itaihanski/triangle-view/blob/master/LICENSE -> Free to use MIT license. (including modification and commercial purposes)
 *
 */

public class TriangleObject extends View implements GraphicGameObjectInterface{

    private static final Direction DEFAULT_DIRECTION = Direction.LEFT;
    private static final int DEFAULT_COLOR = 0xff757575;

   // private Paint mPaint;
    private Path mTrianglePath;
    private Direction mDirection;
    private int mColor;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int theColor;//replaced by mcolor
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

    //first three constructors are required, 4th is called
    public TriangleObject(Context context) {
        this(context, null);
    }

    public TriangleObject(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleObject(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public TriangleObject(Context context,float xPos, float yPos,GameObjectState gos){
        super(context);//sets in motion chain of constructors that accomplish shit
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

    }


    private void init(AttributeSet attrs) {
        if (attrs != null) {
            //retrieve values from xml file
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TriangleObject);
            switch (a.getInt(R.styleable.TriangleObject_tr_direction, 0)) {
                case 0:
                    mDirection = Direction.LEFT;
                    break;
                case 1:
                    mDirection = Direction.UP;
                    break;
                case 2:
                    mDirection = Direction.RIGHT;
                    break;
                case 3:
                default:
                    mDirection = Direction.DOWN;
            }
            mColor = a.getColor(R.styleable.TriangleObject_tr_color, DEFAULT_COLOR);
            a.recycle();
        } else {
            mDirection = DEFAULT_DIRECTION;
            mColor = DEFAULT_COLOR;
        }

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        //set other required vars
    }


    public void setDirection(Direction direction) {//setting directions
        if (direction != mDirection) {
            mDirection = direction;
            mTrianglePath = null;
        }
        invalidate();
    }

    @Override
    public Direction getDirection() {
        return this.mDirection;
    }


    private Path getTrianglePath() {
        if (mTrianglePath == null) {
            mTrianglePath = new Path();
            Point p1, p2, p3;
            int x = (int)xPos;
            int y= (int)yPos;
            //setting different points according to direction
            //including own modifications to set position of shape (calculations were a bitch)
            switch (mDirection) {
                case LEFT:
                    p1 = new Point(width+x, 0+y);
                    p2 = new Point(width+x, height+y);
                    p3 = new Point(0+x, (height / 2)+y);
                    break;
                case UP:
                    p1 = new Point(0+x, height+y);
                    p2 = new Point(width+x, height+y);
                    p3 = new Point((width / 2)+x, 0+y);
                    break;
                case RIGHT:
                    p1 = new Point(0+x, 0+y);
                    p2 = new Point(0+x, height+y);
                    p3 = new Point(width+x, (height / 2)+y);
                    break;
                case DOWN:
                default:
                    p1 = new Point(0+x, 0+y);
                    p2 = new Point(width+x, 0+y);
                    p3 = new Point((width / 2)+x, height+y);
            }
            mTrianglePath.moveTo(p1.x, p1.y);
            mTrianglePath.lineTo(p2.x, p2.y);
            mTrianglePath.lineTo(p3.x, p3.y);
        }
        return mTrianglePath;
    }

    @Override
    public void setColor(PossibleColors c) {
        if (mColor != c.getValue()) {
            mColor = c.getValue();
            if (mPaint != null) {
                mPaint.setColor(c.getValue());
            }
            mTrianglePath = null;
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
        // return this.c.getValue();
    }

    @Override
    public RectF getBoundingBox() {
        return boundingBox;
    }

/*
    private Path createStarBySyze(float width, int steps) {
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
*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // Path p = createStarBySyze(radius,5);
      //  p.offset(xPos,yPos);
       // canvas.drawPath(p,mPaint);
        canvas.drawPath(getTrianglePath(), mPaint);
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

    public Animation getAnimationTag() {
        return anim;
    }

    @Override
    public void setAnimation(Animation anim) {
        this.anim = anim;
    }

    //
    // Direction
    //


}