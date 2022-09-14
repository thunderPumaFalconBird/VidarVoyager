package com.vv.game.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.VidarVoyager;

import java.util.EnumMap;
import java.util.Vector;

/**
 * This is the Astronaut Class. It extends the actor class so that it can be added to a stage. The stage will then
 * call the astronauts draw method to draw its current animation based on it current state. The current state is
 * set in the update method based on user input.
 *
 * Note: there is no walkingLeft animation. The walkingRight animation must be flipped.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */

public class Astronaut  extends Actor {
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
    private EnumMap<STATE, Animation<TextureRegion>> animations;
    private float stateTime = 0f;
    private STATE currentState = STATE.idleFront;
    private TextureRegion currentFrame;
    private Body body;

    public Astronaut(Stage stage, World world, Vector2 startPosition){
        currentFrame = new TextureRegion();
        setStage(stage);
        initAnimations();
        setBounds(currentFrame.getRegionX(), currentFrame.getRegionY(),
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        setX(startPosition.x/VidarVoyager.PPM);
        setY(startPosition.y/VidarVoyager.PPM);
        defineAstronautBody(world);
    }

    public void defineAstronautBody(World world) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth()/2)/VidarVoyager.PPM, (getHeight()/2)/VidarVoyager.PPM);
        //TODO add maskBits for contactListener
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    public void destroyAstronautBody(World world){ world.destroyBody(body);}

    public void update(float deltaTime, Vector<Integer> keyInput){

        //reset velocity
        body.setLinearVelocity(0,0);

        //reset frame if it's flipped
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

        stateTime += deltaTime;

        if(currentState != STATE.dead){

            for (Integer integer : keyInput) {
                switch (integer) {
                    case Input.Keys.UP:
                        body.setLinearVelocity(0,0); //this will make sure player only moves in one direction
                        currentState = STATE.walkingBack;
                        body.applyLinearImpulse(new Vector2(0, 2f), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.DOWN:
                        body.setLinearVelocity(0,0);
                        currentState = STATE.walkingFront;
                        body.applyLinearImpulse(new Vector2(0, -2f), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.LEFT:
                        body.setLinearVelocity(0,0);
                        currentState = STATE.walkingLeft;
                        currentFrame = animations.get(STATE.walkingRight).getKeyFrame(stateTime, true);
                        currentFrame.flip(true, false);
                        body.applyLinearImpulse(new Vector2(-2f, 0), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.RIGHT:
                        body.setLinearVelocity(0,0);
                        currentState = STATE.walkingRight;
                        body.applyLinearImpulse(new Vector2(2f, 0), body.getWorldCenter(), true);
                        break;
                }
            }
            if(currentState == STATE.idleLeft){
                currentFrame = animations.get(STATE.idleRight).getKeyFrame(stateTime, true);
                currentFrame.flip(true, false);
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

    public Body getBody(){ return body; }

    private void initAnimations(){
        animations = new EnumMap<>(STATE.class);
        TextureAtlas textureAtlas = new TextureAtlas("astronaut/Astronaut.txt");

        Array<TextureRegion> atlasTemp = new Array<TextureRegion>(textureAtlas.getRegions());
        Array<TextureRegion> animationTemp = new Array<>();

        //ADDING TEXTURE REGIONS TO ANIMATIONS ENUM MAP

        // adding right facing idle
        animationTemp.addAll(atlasTemp, 0, 10);
        animations.put(STATE.idleRight, new Animation<>(.1f, animationTemp));
        animationTemp.clear();

        // adding Back facing idle
        animationTemp.addAll(atlasTemp, 10, 9);
        animations.put(STATE.idleBack, new Animation<>(.1f, animationTemp));
        animationTemp.clear();

        // adding Front facing idle
        animationTemp.addAll(atlasTemp, 19, 10);
        animations.put(STATE.idleFront, new Animation<>(.1f, animationTemp));
        animationTemp.clear();

        // adding right facing Walking
        animationTemp.addAll(atlasTemp, 29, 12);
        animations.put(STATE.walkingRight, new Animation<>(.1f, animationTemp));
        animationTemp.clear();

        // adding Back facing Walking
        animationTemp.addAll(atlasTemp, 41, 12);
        animations.put(STATE.walkingBack, new Animation<>(.1f, animationTemp));
        animationTemp.clear();

        // adding Front facing Walking
        animationTemp.addAll(atlasTemp, 53, 12);
        animations.put(STATE.walkingFront, new Animation<>(.1f, animationTemp));
        animationTemp.clear();

        //SET CURRENT FRAME TO CURRENT STATE
        currentFrame.setRegion(animations.get(currentState).getKeyFrame(stateTime, true));
    }
}
