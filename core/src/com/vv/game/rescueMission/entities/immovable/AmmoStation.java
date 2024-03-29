package com.vv.game.rescueMission.entities.immovable;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.vv.game.VidarVoyager;

/**
 * This is the ammoStation class. Currently, the player can not collect ammo.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class AmmoStation extends Immovable {

    public AmmoStation(World world, RectangleMapObject object){
        super(world);
        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM);

        createBody(object);
        body.setUserData(this);
    }
}
