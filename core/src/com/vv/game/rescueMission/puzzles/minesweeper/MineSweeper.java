package com.vv.game.rescueMission.puzzles.minesweeper;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vv.game.VidarVoyager;
import com.vv.game.rescueMission.puzzles.Puzzle;
import com.vv.game.utils.GameInput;

import java.util.HashMap;

public class MineSweeper extends Puzzle {
    private final World world;
    private Body body;
    private Stage stage;

    private final GameInput gameInput = GameInput.getInstance();
    private HashMap<Vector2, Square> squares;
    private boolean active = false;

    public MineSweeper(World world, RectangleMapObject object){
        this.world = world;
        createBody(object);
        squares = new HashMap<>();
    }

    public boolean isActive(){ return active; }

    public void addStage(Stage stage){
        this.stage = stage;
    }

    public void run(){
        active = true;
        //create mines
        //set mines in squares
        //add squares to stage
    }

    public void update(){
        //check input new vec2 - x%100 - y%100 to get vec2 of square.
        //set Texture based on type of square.
    }

    public void createBody(RectangleMapObject object){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

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

    public Body getBody(){ return body; }

    public void destroyBody() { world.destroyBody(body); }
}