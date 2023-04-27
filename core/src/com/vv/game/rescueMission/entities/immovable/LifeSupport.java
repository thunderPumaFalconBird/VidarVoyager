package com.vv.game.rescueMission.entities.immovable;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.vv.game.VidarVoyager;

/**
 * This is the lifesupport class. It is used in the main task of level 1. The lifesupport is considered fixed is the
 * player collects 5 different color teddy bears.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class LifeSupport extends Immovable {
    private boolean fixed = false;

    /**
     * The lifesupport constructor is used to call the immovable constructor and then it creates the box2d body.
     * @param world
     * @param object
     */
    public LifeSupport(World world, RectangleMapObject object){
        super(world);
        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM);

        createBody(object);
        body.setUserData(this);
    }

    /**
     * this method checks to see if the lifesupport has been fixed.
     * @return
     */
    public boolean getFixed() { return fixed; }

    /**
     * This method is called by collision handler when the player collides with the lifesupport and has 5 different
     * color teddy bears.
     * @param fixed
     */
    public void setFixed(boolean fixed) { this.fixed = fixed; }
}