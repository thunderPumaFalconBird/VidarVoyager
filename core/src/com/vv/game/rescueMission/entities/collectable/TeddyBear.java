package com.vv.game.rescueMission.entities.collectable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.vv.game.VidarVoyager;

import java.io.File;

public class TeddyBear extends Collectable{
    public enum COLOR {
        BLUE,
        GREEN,
        ORANGE,
        PINK,
        RED
    }
    private COLOR color;
    private int item_index;

    public TeddyBear(World world, RectangleMapObject object, String name) {
        super(world);
        setTeddyColor(name);

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
        if(!collected) {
            batch.draw(textureRegion, body.getPosition().x * VidarVoyager.PPM - (getWidth() / 2),
                    body.getPosition().y * VidarVoyager.PPM - (getHeight() / 2));
        }
    }

    /**
     * this method is used to set the teddy bear color. The map tmx file has a string that is passed in.
     * @param name
     */
    private void setTeddyColor(String name) {
        switch (name){
            case "BLUE_TEDDY":
                item_index = 2;
                color = COLOR.BLUE;
                break;
            case "GREEN_TEDDY":
                item_index = 4;
                color = COLOR.GREEN;
                break;
            case "ORANGE_TEDDY":
                item_index = 5;
                color = COLOR.ORANGE;
                break;
            case "PINK_TEDDY":
                item_index = 6;
                color = COLOR.PINK;
                break;
            case "RED_TEDDY":
                item_index = 7;
                color = COLOR.RED;
                break;
        }
    }

    public COLOR getTeddyColor(){ return color;}
}
