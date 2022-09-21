package com.vv.game.level;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.vv.game.VidarVoyager;
import com.vv.game.puzzles.MineSweeper;
import com.vv.game.utils.CollisionHandler;

import java.io.File;
import java.util.HashMap;

/**
 * This is the Level Class. It contains the map and any objects that are in the map. The map is loaded based
 * on the level number. Maps will be named after the level they belong to. Each level may have different objects.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */

public class Level {
    private final int levelNumber;
    private final TiledMap map;
    private final World world;
    private Vector2 playerStartPosition;
    private Array <AmmoStation> ammoStations;
    private Array<LifeSupport> lifeSupports;
    private Array<OxygenStation> oxygenStations;
    private HashMap<Vector2, Cannon> cannons;
    private Array<Door> doors;
    private MineSweeper mineSweeper;

    public Level(int levelNumber){
        world = new World(new Vector2(0f, 0f), false);
        this.levelNumber = levelNumber;
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(("maps" + File.separator + "Level" + levelNumber + ".tmx"));

        for (int i = 0; i < map.getLayers().size(); i++) {

            switch (map.getLayers().get(i).getName()){
                case "StartPosition" :
                    setPlayerStartPosition(i);
                    break;
                case "AmmoStation" :
                    initAmmoStations(i);
                    break;
                case "Wall" :
                    initWalls(i);
                    break;
                case "LifeSupport" :
                    initLifeSupports(i);
                    break;
                case "OxygenStation" :
                    initOxygenStations(i);
                    break;
                case "Cannon" :
                    initCannons(i);
                    break;
                case "Door" :
                    initDoors(i);
                    break;
                case "MineSweeper" :
                    initMineSweeper(i);
                    break;
                default:
                    break;
            }
        }
        world.setContactListener(new CollisionHandler());
    }


    public TiledMap getMap() { return map; }

    public World getWorld() { return world; }

    public Vector2 getPlayerStartPosition() { return playerStartPosition; }

    public void setPlayerStartPosition(int index) {
        if(map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class).get(0) != null) {
            RectangleMapObject object = map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class).get(0);
            Rectangle rectangle = object.getRectangle();
            playerStartPosition = new Vector2(rectangle.getX(),
                    rectangle.getY());
        }
    }

    //walls will not change and will not have any functionality. create bodies in the world and forget about them.
    private void initWalls(int index) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = object.getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM,
                    (rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rectangle.getWidth() / 2 / VidarVoyager.PPM,
                    rectangle.getHeight() / 2 / VidarVoyager.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

    public void initAmmoStations(int index){
        ammoStations = new Array<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            ammoStations.add(new AmmoStation(world, object));
        }
    }

    private void initCannons(int index) {
        cannons = new HashMap<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            Cannon temp = new Cannon(world, object);
            cannons.put(new Vector2(temp.getX(), temp.getY()), temp);
        }
        for(RectangleMapObject object : map.getLayers().get("CannonRange").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = object.getRectangle();
            float x = (rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM;
            float y = (rectangle.getY() + rectangle.getHeight()/2) / VidarVoyager.PPM;
            float rangeOffset = 2;

            if(cannons.containsKey(new Vector2(x,y - rangeOffset))){
                cannons.get(new Vector2(x, y - rangeOffset)).setRangeBody(object);
            }
            else if(cannons.containsKey(new Vector2(x,y + rangeOffset))){
                cannons.get(new Vector2(x, y + rangeOffset)).setRangeBody(object);
            }
            else if(cannons.containsKey(new Vector2(x + rangeOffset,y))){
                cannons.get(new Vector2(x + rangeOffset, y)).setRangeBody(object);
            }
            else if(cannons.containsKey(new Vector2(x - rangeOffset,y))){
                cannons.get(new Vector2(x - rangeOffset, y)).setRangeBody(object);
            }
        }
    }

    private void initLifeSupports(int index) {
        lifeSupports = new Array<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            lifeSupports.add(new LifeSupport(world, object));
        }
    }

    private void initOxygenStations(int index) {
        oxygenStations = new Array<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            oxygenStations.add(new OxygenStation(world, object));
        }
    }

    private void initDoors(int index) {
        doors = new Array<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            doors.add(new Door(world, object));
        }
    }

    private void initMineSweeper(int index) {
        if(map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class).get(0) != null) {
            RectangleMapObject object = map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class).get(0);
            mineSweeper = new MineSweeper(world, object);
        }
    }

    public void dispose(){
        world.dispose();
        map.dispose();
    }
}
