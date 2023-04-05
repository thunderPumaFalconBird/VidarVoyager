package com.vv.game.rescueMission.entities.movable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.VidarVoyager;
import com.vv.game.utils.GameInput;

import java.util.EnumMap;
import java.io.File;

/**
 * This is the Astronaut Class. It extends the actor class so that it can be added to a stage. The stage will then
 * call the astronauts draw method to draw its current animation based on it current state. The current state is
 * set in the update method based on user input handled by GameInput.
 *
 * Note: there is no walkingLeft animation. The walkingRight animation must be flipped.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */

public class Astronaut  extends Movable {
    public enum STATE {
        idleFront,
        idleBack,
        idleLeft,
        idleRight,
        walkingFront,
        walkingBack,
        walkingLeft,
        walkingRight,
        stalled,
        dead
    }
    private final float PLAYER_VELOCITY = 1.2f;
    private final float IDLE_FRAME_RATE = 0.1f;
    private final float WALKING_FRAME_RATE = 0.055f;
    private final GameInput gameInput = GameInput.getInstance();
    private EnumMap<STATE, Animation<TextureRegion>> animations;
    private float stateTime = 0f;
    private STATE currentState = STATE.idleFront;
    private TextureRegion currentFrame;

    public Astronaut(Stage stage, World world, Vector2 startPosition){
        super(world);
        currentFrame = new TextureRegion();
        setStage(stage);
        initAnimations();
        setBounds(currentFrame.getRegionX(), currentFrame.getRegionY(),
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        setX(startPosition.x/VidarVoyager.PPM);
        setY(startPosition.y/VidarVoyager.PPM);
        createBody();
        body.setUserData(this);
    }

    @Override
    public void update(){

        //reset velocity
        body.setLinearVelocity(0,0);

        //reset frame if it's flipped from walking left or idle left
        if(currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        //reset to idle state
        switch (currentState){
            case walkingBack:
                currentState = STATE.idleBack;
                break;
            case walkingFront:
                currentState = STATE.idleFront;
                break;
            case walkingLeft:
                currentState = STATE.idleLeft;
                break;
            case walkingRight:
                currentState = STATE.idleRight;
                break;
        }

        stateTime += Gdx.graphics.getDeltaTime();

        if(currentState != STATE.dead){

            for (Integer integer : gameInput.getKeyInputs()) {
                switch (integer) {
                    case Input.Keys.UP:
                        body.setLinearVelocity(0,0); //this will make sure player only moves in one direction
                        currentState = STATE.walkingBack;
                        body.applyLinearImpulse(new Vector2(0, PLAYER_VELOCITY), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.DOWN:
                        body.setLinearVelocity(0,0); //this will make sure player only moves in one direction
                        currentState = STATE.walkingFront;
                        body.applyLinearImpulse(new Vector2(0, -PLAYER_VELOCITY), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.LEFT:
                        body.setLinearVelocity(0,0); //this will make sure player only moves in one direction
                        currentState = STATE.walkingLeft;
                        // walking left uses the walking right animation flipped on the x-axis
                        currentFrame = animations.get(STATE.walkingRight).getKeyFrame(stateTime, true);
                        if(!currentFrame.isFlipX()) {
                            currentFrame.flip(true, false);
                        }
                        body.applyLinearImpulse(new Vector2(-PLAYER_VELOCITY, 0), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.RIGHT:
                        body.setLinearVelocity(0,0); //this will make sure player only moves in one direction
                        currentState = STATE.walkingRight;
                        // this if statement prevents moon walking
                        if(currentFrame.isFlipX()) {
                            currentFrame.flip(true, false);
                        }
                        body.applyLinearImpulse(new Vector2(PLAYER_VELOCITY, 0), body.getWorldCenter(), true);
                        break;
                }
            }
            if(currentState == STATE.idleLeft){
                currentFrame = animations.get(STATE.idleRight).getKeyFrame(stateTime, true);
                if(!currentFrame.isFlipX()) {
                    currentFrame.flip(true, false);
                }
            }
            else if(currentState != STATE.walkingLeft){ //Walking left is handled in the above switch statement
                currentFrame = animations.get(currentState).getKeyFrame(stateTime, true);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(currentFrame, body.getPosition().x*VidarVoyager.PPM - (getWidth()/2),
                body.getPosition().y*VidarVoyager.PPM - (getHeight()/2));
    }

    public STATE getCurrentState() {
        return currentState;
    }

    private void initAnimations(){
        animations = new EnumMap<>(STATE.class);
        TextureAtlas textureAtlas = new TextureAtlas("astronaut" + File.separator + "astronaut2.txt");

        Array<TextureRegion> atlasTemp = new Array<TextureRegion>(textureAtlas.getRegions());
        Array<TextureRegion> animationTemp = new Array<>();

        //ADDING TEXTURE REGIONS TO ANIMATIONS ENUM MAP

        // adding right facing idle
        animationTemp.addAll(atlasTemp, 0, 10);
        animations.put(STATE.idleRight, new Animation<>(IDLE_FRAME_RATE, animationTemp));
        animationTemp.clear();

        // adding Back facing idle
        animationTemp.addAll(atlasTemp, 10, 9);
        animations.put(STATE.idleBack, new Animation<>(IDLE_FRAME_RATE, animationTemp));
        animationTemp.clear();

        // adding Front facing idle
        animationTemp.addAll(atlasTemp, 19, 10);
        animations.put(STATE.idleFront, new Animation<>(IDLE_FRAME_RATE, animationTemp));
        animationTemp.clear();

        // adding right facing Walking
        animationTemp.addAll(atlasTemp, 29, 12);
        animations.put(STATE.walkingRight, new Animation<>(WALKING_FRAME_RATE, animationTemp));
        animationTemp.clear();

        // adding Back facing Walking
        animationTemp.addAll(atlasTemp, 41, 12);
        animations.put(STATE.walkingBack, new Animation<>(WALKING_FRAME_RATE, animationTemp));
        animationTemp.clear();

        // adding Front facing Walking
        animationTemp.addAll(atlasTemp, 53, 12);
        animations.put(STATE.walkingFront, new Animation<>(WALKING_FRAME_RATE, animationTemp));
        animationTemp.clear();

        //SET CURRENT FRAME TO CURRENT STATE
        currentFrame.setRegion(animations.get(currentState).getKeyFrame(stateTime, true));
    }
}