package com.vv.game.entities;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.vv.game.VidarVoyager;
import com.vv.game.utils.CollisionHandler;

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
    private Array<MapObject> ammoStations;
    private Array<MapObject> lifeSupports;
    private Array<MapObject> oxygenStations;
    private Array<MapObject> cannons;
    private Array<MapObject> doors;
    private Array<MapObject> openDoors;
    private MapObject mineSweeper;

    public Level(int levelNumber){
        world = new World(new Vector2(0f, 0f), false);
        this.levelNumber = levelNumber;
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(("maps/Level" + levelNumber + ".tmx"));
        //TODO This will have to change based on the different maps.
        // Possibly create object in map to grab the x and y from.
        playerStartPosition = new Vector2(1000, 1000);

        for (int i = 0; i < map.getLayers().size(); i++) {

            switch (map.getLayers().get(i).getName()){
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

    //TODO create classes for each map object rather than using MapObject class
    public void initAmmoStations(int index){
        ammoStations = new Array<>();
        for(MapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            ammoStations.add(object);
        }
    }

    private void initLifeSupports(int index) {
        lifeSupports = new Array<>();
        for(MapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            lifeSupports.add(object);
        }
    }

    private void initOxygenStations(int index) {
        oxygenStations = new Array<>();
        for(MapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            oxygenStations.add(object);
        }
    }

    private void initCannons(int index) {
        cannons = new Array<>();
        for(MapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            cannons.add(object);
        }
    }

    private void initDoors(int index) {
        doors = new Array<>();
        for(MapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            doors.add(object);
        }
    }

    private void initMineSweeper(int index) {
        if(map.getLayers().get(index).getObjects().get(0) != null) {
            mineSweeper = map.getLayers().get(index).getObjects().get(0);
        }
    }

    public void dispose(){
        world.dispose();
        map.dispose();
    }
}
