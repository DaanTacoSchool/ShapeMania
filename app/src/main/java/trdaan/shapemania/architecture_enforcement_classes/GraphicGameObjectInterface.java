package trdaan.shapemania.architecture_enforcement_classes;

import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.animation.Animation;

/**
 * Created by D2110175 on 11-1-2017.
 */

public interface GraphicGameObjectInterface {

    void setColor(PossibleColors c);
    void setShape(PossibleShapes s);
    int getColor();
    RectF getBoundingBox();
    void setGone();
    void setState(GameObjectState gos);
    void refresh();//too agressive
    GameObjectState getState();
    void setDirection(Direction direction);
    Direction getDirection();
    Animation getAnimationTag();//marked
    void setAnimation(Animation anim);


}
