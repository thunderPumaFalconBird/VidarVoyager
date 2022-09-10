package com.vv.game.entities;

import com.badlogic.gdx.Gdx;
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
    private final World world;
    private Body body;

    public Astronaut(Stage stage, World world){
        currentFrame = new TextureRegion();
        this.world = world;
        setStage(stage);
        initAnimations();
        setBounds(currentFrame.getRegionX(), currentFrame.getRegionY(),
                currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        setX(1000/VidarVoyager.PPM);
        setY(1000/VidarVoyager.PPM);
        defineAstronautBody();
    }

    public void defineAstronautBody() {
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

    public void destroyAstronautBody(){ world.destroyBody(body);}

    public void update(float deltaTime){ //TODO fix the flip of left facing animations or make new spritesheet with left facing animation
        //reset velocity
        body.setLinearVelocity(0,0);
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
        }
        if(currentState == STATE.idleLeft){
            currentFrame = animations.get(STATE.idleRight).getKeyFrame(stateTime, true);
            currentFrame.flip(true, false);
        }
        else {
            currentFrame = animations.get(currentState).getKeyFrame(stateTime, true);
        }

        stateTime += deltaTime;

        if(currentState != STATE.dead){
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                currentState = STATE.walkingBack;
                currentFrame = animations.get(currentState).getKeyFrame(stateTime, true);
                body.applyLinearImpulse(new Vector2(0, .5f), body.getWorldCenter(), true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                currentState = STATE.walkingFront;
                currentFrame = animations.get(currentState).getKeyFrame(stateTime, true);
                body.applyLinearImpulse(new Vector2(0, -.5f), body.getWorldCenter(), true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                currentState = STATE.walkingLeft;
                currentFrame = animations.get(STATE.walkingRight).getKeyFrame(stateTime, true);
                currentFrame.flip(true,false);
                body.applyLinearImpulse(new Vector2(-.5f, 0), body.getWorldCenter(), true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                currentState = STATE.walkingRight;
                currentFrame = animations.get(currentState).getKeyFrame(stateTime, true);
                body.applyLinearImpulse(new Vector2(.5f, 0), body.getWorldCenter(), true);
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

    public TextureRegion getCurrentFrame() { return currentFrame; }

    public void setCurrentState(STATE currentState){
        this.currentState = currentState;
    }

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
