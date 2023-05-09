package com.vv.game.rescueMission.entities.immovable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.vv.game.VidarVoyager;
import com.vv.game.rescueMission.puzzles.Puzzle;
import com.vv.game.rescueMission.puzzles.TestPuzzle;
import com.vv.game.rescueMission.puzzles.minesweeper.MineSweeper;

import java.io.File;

/**
 * This is the door class. Doors are locked until a puzzle is completed by the player.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class Door extends Immovable {
    private final Texture doorTexture = new Texture("maps" + File.separator + "doorClosed300.png");
    private Puzzle puzzle = new MineSweeper();

    /**
     * The door constructor is used to call the immovable constructor. Then the box2d body is created.
     * @param world
     * @param object
     */
    public Door(World world, RectangleMapObject object){
        super(world);
        Rectangle rectangle = object.getRectangle();
        setX((rectangle.getX() + rectangle.getWidth()/2) / VidarVoyager.PPM);
        setY((rectangle.getY() - 25 + rectangle.getHeight()/2) / VidarVoyager.PPM);

        createBody(object);
        body.setUserData(this);
    }

    public Puzzle getPuzzle() { return puzzle; }

    /**
     * This method is called when a player collides with the door.
     * @param active
     */
    public void setActive(boolean active){ puzzle.setActive(active); }

    /**
     * This method is called to check if the puzzle is being used.
     * @return
     */
    public boolean isActive(){ return puzzle.isActive(); }

    /**
     * This method is called to draw doors if the door has not been unlocked.
     * @param batch
     * @param parentAlpha The parent alpha, to be multiplied with this actor's alpha, allowing the parent's alpha to affect all
     *           children.
     */
    @Override
    public void draw(Batch batch, float parentAlpha){
        if(!puzzle.isSolved()) {
            batch.draw(doorTexture, body.getPosition().x * VidarVoyager.PPM - ((float) doorTexture.getWidth() / 2),
                    body.getPosition().y * VidarVoyager.PPM - ((float) doorTexture.getHeight() / 2));
        }
    }

    /**
     * This method is called when the game is destroyed.
     */
    public void dispose(){ doorTexture.dispose(); }
}