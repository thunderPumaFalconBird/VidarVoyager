package com.vv.game.rescueMission.entities.collectable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.vv.game.VidarVoyager;

import java.io.File;

public class C4 extends Collectable{
    private final int item_index = 3;
    private TextureRegion textureRegion;

    public C4(World world, RectangleMapObject object) {
        super(world);
        TextureAtlas textureAtlas = new TextureAtlas("collectables" + File.separator + "items.txt");
        textureRegion = textureAtlas.getRegions().get(item_index);

        setWidth(textureRegion.getRegionWidth());
        setHeight(textureRegion.getRegionHeight());

        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() + getHeight()/2) / VidarVoyager.PPM);

        createBody();
        body.setUserData(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(textureRegion, getX() * VidarVoyager.PPM - (getWidth() / 2),
                getY() * VidarVoyager.PPM - (getHeight() / 2));
    }


}