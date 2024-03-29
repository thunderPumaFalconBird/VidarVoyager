package com.vv.game.rescueMission.levels;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.VidarVoyager;
import com.vv.game.rescueMission.CollisionHandler;
import com.vv.game.rescueMission.entities.collectable.*;
import com.vv.game.rescueMission.entities.immovable.*;
import com.vv.game.rescueMission.puzzles.Puzzle;

import java.io.File;
import java.util.HashMap;

/**
 * This is the Level Class. It contains the map and any objects that are in the map. The map is loaded based
 * on the level number. Maps will be named after the level they belong to. Each level may have different objects.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */

public class Level1 implements Level {
    private final TiledMap map;
    private final World world;
    private Vector2 playerStartPosition;
    private Array <AmmoStation> ammoStations;
    private LifeSupport lifeSupports;
    private Array<OxygenStation> oxygenStations;
    private Array<Collectable> collectables;
    private HashMap<Vector2, Cannon> cannons;
    private Array<Door> doors;

    private Array<TextureRegion> instructions;

    /**
     * The constructor initializes the level based on the levelNumber.
     */
    public Level1(){
        world = new World(new Vector2(0f, 0f), false);
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(("maps" + File.separator + "Level1.tmx"));

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
                case "Collectables" :
                    initCollectables(i);
                    break;
                default:
                    break;
            }
        }

        initInstructions();

        world.setContactListener(new CollisionHandler());
    }

    /**
     * This method checks if the main task for the level is completed.
     * @return
     */
    @Override
    public boolean checkForWin() {
        boolean temp = false;
        temp = lifeSupports.getFixed();
        return temp;
    }

    @Override
    public TiledMap getMap() { return map; }

    @Override
    public World getWorld() { return world; }

    @Override
    public Array<TextureRegion> getInstructions() { return instructions; }

    @Override
    public Vector2 getPlayerStartPosition() { return playerStartPosition; }

    /**
     * This method checks all the doors for an active puzzle and returns it. If there is no puzzle it will return null.
     * @return
     */
    public Puzzle getActivePuzzle(){
        Puzzle temp = null;
        for(int i = 0; i < doors.size; i++){
            if(doors.get(i).isActive()){
                temp = doors.get(i).getPuzzle();
            }
        }
        return temp;
    }

    /**
     * This method adds any drawable actors to the stage. Immovable map objects are not added to the stage.
     * @param stage
     */
    @Override
    public void addActors(Stage stage){
        for(int i = 0; i < doors.size; i++){
            stage.addActor(doors.get(i));
        }
        for(int i = 0; i < collectables.size; i++){
            stage.addActor(collectables.get(i));
        }
        //The cannons have a draw method for the laser
        for (Cannon cannon : cannons.values()) {
            stage.addActor(cannon);
        }

    }

    /**
     * This method is called based on the APP_FPS (frames per second). It steps the world forward which applies any
     * box2d physics changes to bodies in the World. It also updated collectable items.
     */
    @Override
    public void update(){
        world.step(1f / VidarVoyager.APP_FPS, VidarVoyager.VELOCITY_ITERATIONS,
                VidarVoyager.POSITION_ITERATIONS);

        for(int i = 0; i < collectables.size; i++){
            if(collectables.get(i).isCollected()){
                collectables.get(i).pickUpItem();
            }
        }
        for(int i = 0; i < doors.size; i++){
            if(doors.get(i).getPuzzle().isSolved()){
                doors.get(i).getBody().setActive(false);
            }
        }
    }

    /**
     * This method is used to start the players position based on an object name startPosition in the level tmx file.
     * @param index
     */
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

    /**
     * This initializes ammoStations. The index indicates the current map layer.
     * @param index
     */
    public void initAmmoStations(int index){
        ammoStations = new Array<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            ammoStations.add(new AmmoStation(world, object));
        }
    }

    /**
     * This initializes cannons. The index indicates the current map layer. This also initializes th ranges for cannons.
     * The ranges are used in the collision handler to activate the cannons.
     * @param index
     */
    private void initCannons(int index) {
        cannons = new HashMap<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            Cannon temp = new Cannon(world, object);
            cannons.put(new Vector2(temp.getX(), temp.getY()), temp);
        }
        for (RectangleMapObject object : map.getLayers().get("CannonRange").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = object.getRectangle();
            float x = (rectangle.getX() + rectangle.getWidth() / 2) / VidarVoyager.PPM;
            float y = (rectangle.getY() + rectangle.getHeight() / 2) / VidarVoyager.PPM;
            float rangeOffset = 2;

            if (cannons.containsKey(new Vector2(x, y - rangeOffset))) {
                cannons.get(new Vector2(x, y - rangeOffset)).setRangeBody(object, Cannon.Orientation.DOWN);
            } else if (cannons.containsKey(new Vector2(x, y + rangeOffset))) {
                cannons.get(new Vector2(x, y + rangeOffset)).setRangeBody(object, Cannon.Orientation.UP);
            } else if (cannons.containsKey(new Vector2(x + rangeOffset, y))) {
                //The cannon range at x = 3600, y = 4100 gets assigned to the cannon at x = 3700, y = 4300.
                //no reason to fix it, cannon is blocked in by other cannons.
                cannons.get(new Vector2(x + rangeOffset, y)).setRangeBody(object, Cannon.Orientation.RIGHT);
            } else if (cannons.containsKey(new Vector2(x - rangeOffset, y))) {
                cannons.get(new Vector2(x - rangeOffset, y)).setRangeBody(object, Cannon.Orientation.LEFT);
            }
        }
    }

    /**
     * This initializes the lifeSupport object. The index indicates the current map layer.
     * @param index
     */
    private void initLifeSupports(int index) {
        RectangleMapObject object = map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class).get(0);
        lifeSupports = new LifeSupport(world, object);
    }

    /**
     * This initializes oxygen stations. The index indicates the current map layer.
     * @param index
     */
    private void initOxygenStations(int index) {
        oxygenStations = new Array<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            oxygenStations.add(new OxygenStation(world, object));
        }
    }

    /**
     * This initializes doors. The index indicates the current map layer.
     * @param index
     */
    private void initDoors(int index) {
        doors = new Array<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            doors.add(new Door(world, object));
        }
    }

    /**
     * This initializes collectable items. The index indicates the current map layer.
     * @param index
     */
    private void initCollectables(int index) {
        collectables = new Array<>();
        for(RectangleMapObject object : map.getLayers().get(index).getObjects().getByType(RectangleMapObject.class)){
            switch (object.getName()){
                case "Banana": collectables.add(new Banana(world, object));
                    break;
                case "Battery": collectables.add(new Battery(world, object));
                    break;
                case "C4": collectables.add(new C4(world, object));
                    break;
                case "SodaCan": collectables.add(new SodaCan(world, object));
                    break;
                case "ToDoList": collectables.add(new ToDoList(world, object));
                    break;
                case "Tube": collectables.add(new Tube(world, object));
                    break;
                case "BLUE_TEDDY": collectables.add(new TeddyBear(world, object, "BLUE_TEDDY"));
                    break;
                case "GREEN_TEDDY": collectables.add(new TeddyBear(world, object, "GREEN_TEDDY"));
                    break;
                case "ORANGE_TEDDY": collectables.add(new TeddyBear(world, object, "ORANGE_TEDDY"));
                    break;
                case "PINK_TEDDY": collectables.add(new TeddyBear(world, object, "PINK_TEDDY"));
                    break;
                case "RED_TEDDY": collectables.add(new TeddyBear(world, object, "RED_TEDDY"));
                    break;
            }
        }
    }

    public void initInstructions(){
        instructions = new Array<>();

        TextureAtlas textureAtlas = new TextureAtlas("instructions" + File.separator + "Level1Instructions.txt");
        Array<TextureRegion> atlasTemp = new Array<TextureRegion>(textureAtlas.getRegions());

        instructions.addAll(atlasTemp, 0, 2);
    }

    /**
     * this method is called when the game is destroyed.
     */
    public void dispose(){
        world.dispose();
        map.dispose();
        for(int i = 0; i < doors.size; i++) {
            doors.get(i).dispose();
        }
    }
}
