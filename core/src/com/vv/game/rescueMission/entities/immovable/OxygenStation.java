package com.vv.game.rescueMission.entities.immovable;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.vv.game.VidarVoyager;

public class OxygenStation extends Immovable {
    private int refills = 2;

    public OxygenStation(World world, RectangleMapObject object){
        super(world);
        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM);

        createBody(object);
        body.setUserData(this);
    }

    public int getRefills() { return refills; }

    public void setRefills(int refills) { this.refills = refills; }
}