package com.vv.game.rescueMission.entities.movable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.VidarVoyager;
import com.vv.game.rescueMission.entities.collectable.Collectable;

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

public class Astronaut  extends Movable implements InputProcessor {

    /**
     * This enumeration is used as keys for the animations enumMap of the astronaut.
     */
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
    private final int INVENTORY_MAX = 5;
    private final float OXYGEN_DEPLETION_AMOUNT = 0.008f;
    private EnumMap<STATE, Animation<TextureRegion>> animations;
    private float stateTime = 0f;
    private STATE currentState = STATE.idleFront;
    private TextureRegion currentFrame;
    private final Array<Collectable> inventory = new Array<>();;
    //Using an array of keyInputs allows to check for keys that are being held down. see keyUp and keyDown methods.
    private final Array<Integer> keyInputs = new Array<>();
    private int currentItem = 0;
    private float oxygenLevel = 100;
    private boolean refillingOxygen = false;

    /**
     * The astronaut constructor calls the movable constructor to set the world. It then initializes the animations and
     * world body.
     * @param stage
     * @param world
     * @param startPosition
     */
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

    /**
     * This method is called when the astronaut collides with a collectable item.
     * @param item
     * @return
     */
    public boolean pickUpItem(Collectable item){
        boolean temp = false;
        if(inventory.size < INVENTORY_MAX) {
            inventory.add(item);
            temp = true;
        }
        return temp;
    }

    public STATE getCurrentState() { return currentState; }

    public float getOxygenLevel(){ return oxygenLevel; }

    public void setRefillingOxygen(boolean refillingOxygen) { this.refillingOxygen = refillingOxygen; }

    public Array<Collectable> getInventory() { return inventory; }

    public int getCurrentItem() { return currentItem; }

    /**
     * This is used to remove the multiplexer as well as take away any key inputs the player was pressing before this
     * method was called.
     * @param multiplexer
     */
    public void removeMultiplexer(InputMultiplexer multiplexer){
        multiplexer.removeProcessor(this);
        for(int i = 0; i < keyInputs.size; i++){
            keyUp(keyInputs.get(i));
        }
    }

    /**
     * This method is called based on APP_FPS (frames per second) and updates the player based on user input. The oxygen
     * is updated based on refilling boolean.
     */
    @Override
    public void update(){

        stateTime += Gdx.graphics.getDeltaTime();

        if(currentState == STATE.idleLeft){
            currentFrame = animations.get(STATE.idleRight).getKeyFrame(stateTime, true);
            if(!currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }
        else if(currentState == STATE.walkingLeft){
            currentFrame = animations.get(STATE.walkingRight).getKeyFrame(stateTime, true);
            if (!currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }
        else {
            currentFrame = animations.get(currentState).getKeyFrame(stateTime, true);
            if (currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }

        if(currentState != STATE.dead) {
            for(int i = 0; i < keyInputs.size; i++) {
                switch (keyInputs.get(i)) {
                    case Input.Keys.UP:
                        body.setLinearVelocity(0, 0); //this will make sure player only moves in one direction
                        currentState = STATE.walkingBack;
                        body.applyLinearImpulse(new Vector2(0, PLAYER_VELOCITY), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.DOWN:
                        body.setLinearVelocity(0, 0); //this will make sure player only moves in one direction
                        currentState = STATE.walkingFront;
                        body.applyLinearImpulse(new Vector2(0, -PLAYER_VELOCITY), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.LEFT:
                        body.setLinearVelocity(0, 0); //this will make sure player only moves in one direction
                        currentState = STATE.walkingLeft;
                        body.applyLinearImpulse(new Vector2(-PLAYER_VELOCITY, 0), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.RIGHT:
                        body.setLinearVelocity(0, 0); //this will make sure player only moves in one direction
                        currentState = STATE.walkingRight;
                        body.applyLinearImpulse(new Vector2(PLAYER_VELOCITY, 0), body.getWorldCenter(), true);
                        break;
                    case Input.Keys.D:
                        keyUp(Input.Keys.D); //Remove key to avoid dropping multiple items
                        if (!inventory.isEmpty()) {
                            inventory.get(currentItem).putDownItem(body.getPosition());
                            inventory.removeIndex(currentItem);
                            if (currentItem != 0) {
                                currentItem--;
                            }
                        }
                        break;
                    case Input.Keys.I:
                        keyUp(Input.Keys.I); //Remove key to avoid incrementing multiple times
                        if (currentItem < inventory.size - 1) {
                            currentItem++;
                        } else {
                            currentItem = 0;
                        }
                        break;
                }
            }
        }

        //Update inventory position
        for(int i = 0; i < inventory.size; i++){
            inventory.get(i).setInventoryPosition(new Vector2(
                    body.getPosition().x * VidarVoyager.PPM- ((float)VidarVoyager.APP_HEIGHT/2),
                    body.getPosition().y * VidarVoyager.PPM- ((float)VidarVoyager.APP_HEIGHT/2)),
                    i);
        }

        //deplete the Oxygen Level if the astronaut is not in contact with an oxygen station
        if(!refillingOxygen) {
            oxygenLevel -= OXYGEN_DEPLETION_AMOUNT;
        }
        else if(oxygenLevel < 100) {
            oxygenLevel += OXYGEN_DEPLETION_AMOUNT * 100;
        }

        //Kill astronaut if oxygen runs out.
        if(oxygenLevel < 0){
            currentState = STATE.dead;
        }
    }

    /**
     * This method is called to display the animation on the screen.
     * @param batch
     * @param parentAlpha The parent alpha, to be multiplied with this actor's alpha, allowing the parent's alpha to affect all
     *           children.
     */
    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(currentFrame, body.getPosition().x*VidarVoyager.PPM - (getWidth()/2),
                body.getPosition().y*VidarVoyager.PPM - (getHeight()/2));
    }

    /**
     * This method checks input for keys that are used by the astronaut and adds those keys to an array. It returns true
     * if a key is added to the array.
     * @param keycode one of the constants in {@link Input.Keys}
     * @return
     */
    @Override
    public boolean keyDown(int keycode) {
        boolean returnValue = false;
        if(keycode == Input.Keys.UP || keycode == Input.Keys.DOWN
                || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT  || keycode == Input.Keys.D
                || keycode == Input.Keys.I){
            keyInputs.add(keycode);
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * This method checks input for keys that are used by the astronaut and removes those keys from the array. It
     * returns true if a key is removed from the array.
     * @param keycode one of the constants in {@link Input.Keys}
     * @return
     */
    @Override
    public boolean keyUp(int keycode) {
        boolean returnValue = false;
        switch (keycode) {
            case Input.Keys.UP:
                body.setLinearVelocity(0, 0);
                currentState = STATE.idleBack;
                keyInputs.removeValue(keycode, true);
                returnValue = true;
                break;
            case Input.Keys.DOWN:
                body.setLinearVelocity(0, 0);
                currentState = STATE.idleFront;
                keyInputs.removeValue(keycode, true);
                returnValue = true;
                break;
            case Input.Keys.LEFT:
                body.setLinearVelocity(0, 0);
                currentState = STATE.idleLeft;
                keyInputs.removeValue(keycode, true);
                returnValue = true;
                break;
            case Input.Keys.RIGHT:
                body.setLinearVelocity(0, 0);
                currentState = STATE.idleRight;
                keyInputs.removeValue(keycode, true);
                returnValue = true;
                break;
            case Input.Keys.D:
            case Input.Keys.I:
                keyInputs.removeValue(keycode, true);
                returnValue = true;
                break;
        }
        return returnValue;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    /**
     * This method initializes the astronaut animations.
     */
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
