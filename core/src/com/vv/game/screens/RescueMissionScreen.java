package com.vv.game.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;
import com.vv.game.rescueMission.entities.collectable.Collectable;
import com.vv.game.rescueMission.entities.movable.Astronaut;

import java.io.File;

/**
 * This is the Rescue Mission Class. It handles rendering the map and stage. It also renders box2d shapes when debugging.
 * It also updates the camera position based on the player's location.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class RescueMissionScreen extends AbstractScreen {
    private final float INVENTORY_OFFSET_X = 96;
    private final float INVENTORY_BAR_OFFSET_X = 60;
    private final float INVENTORY_BAR_OFFSET_Y = 81;
    private final float OXYGEN_OFFSET_X = 796;
    private final float OXYGEN_OFFSET_Y = 86;
    private final float OXYGEN_BAR_WIDTH = 15;
    private final float INVENTORY_HIGHLIGHT_OFFSET = 21;
    private final Stage stage;
    private final OrthographicCamera cam;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Texture inventoryOxygenBar = new Texture("screens" + File.separator + "InventoryOxygenBar.png");
    private final Texture oxygenBarTexture = new Texture("screens" + File.separator + "OxygenLevelBar.png");
    private final Texture highlight = new Texture("screens" + File.separator + "Highlight2.png");
    private final Array<TextureRegion> inventoryTextures = new Array<>();
    private float inventoryX, inventoryY;
    private int oxygenBarsNumber = 10;
    private int inventoryHighlightX = 0;

    /**
     * The Rescue Mission Screen constructor sets up the camera and stage which are used to render textures. It also
     * sets up the map render so that the map will be rendered.
     *
     * @param map
     * @param world
     */
    public RescueMissionScreen(TiledMap map, World world){
        super();

        this.world = world;
        cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        stage = new Stage(new FitViewport((float) VidarVoyager.APP_WIDTH,
                (float)VidarVoyager.APP_HEIGHT,
                this.cam));
        cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.update();

        mapRenderer = new OrthogonalTiledMapRenderer(map);
    }

    public Stage getStage(){ return this.stage; }

    @Override
    public void initMultiplexer(InputMultiplexer multiplexer){

    }

    @Override
    public void removeMultiplexer(InputMultiplexer multiplexer){

    }

    @Override
    public String getButtonPressed(){
        String temp = "none";

        return temp;
    }

    /**
     * This method is called and the player's position is used to update the camera position.
     * @param x
     * @param y
     */
    public void updateCam(float x, float y){
        cam.position.set(x * VidarVoyager.PPM, y * VidarVoyager.PPM, 0);
        cam.update();
    }

    /**
     * This method is called to update camera and act on the stage. It also updates the inventory textures that need to
     * be displayed and the oxygen level of the player.
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        cam.update();
        mapRenderer.setView(cam);
        stage.act(deltaTime);

        //Find the actor in the stage that is an astronaut.
        Array<Actor> temp = stage.getActors();
        inventoryTextures.clear();
        for(int i = 0; i < temp.size; i++){
            if(temp.get(i) instanceof Astronaut){
                Astronaut player = (Astronaut) ((Astronaut) temp.get(i)).getBody().getUserData();

                //update astronaut's inventory
                Array<Collectable> inventory = player.getInventory();
                for (int j = 0; j < inventory.size; j++){
                    inventoryTextures.add(inventory.get(j).getTextureRegion());
                }

                //update astronaut's oxygen level
                float oxygenLevel = player.getOxygenLevel();
                oxygenBarsNumber = ((int) oxygenLevel / 10) + 1;

                //update highlight item in inventory
                inventoryHighlightX = player.getCurrentItem();
            }
        }

        //Update the inventory position based on camera position which changes based on Astronaut position.
        inventoryX = cam.position.x - ((float)VidarVoyager.APP_HEIGHT/2);
        inventoryY = cam.position.y - ((float)VidarVoyager.APP_HEIGHT/2);
    }

    /**
     * This is the render method. The order that things get rendered in matters. Everything draws over the previous thing.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime){
        super.render(deltaTime);

        batch.begin();

        mapRenderer.render();

        batch.draw(inventoryOxygenBar, inventoryX, inventoryY);

        stage.draw();

        for(int i = 0; i < inventoryTextures.size; i++){
            batch.draw(inventoryTextures.get(i),
                    inventoryX + i* INVENTORY_OFFSET_X + INVENTORY_BAR_OFFSET_X,
                    inventoryY + INVENTORY_BAR_OFFSET_Y
                    );
        }
        for(int i = 0; i < oxygenBarsNumber; i++){
            batch.draw(oxygenBarTexture,
                    inventoryX + i* OXYGEN_BAR_WIDTH + OXYGEN_OFFSET_X,
                    inventoryY + OXYGEN_OFFSET_Y);
        }

        batch.draw(highlight,
                inventoryX + inventoryHighlightX*100,
                inventoryY + INVENTORY_HIGHLIGHT_OFFSET);
        if(VidarVoyager.debugging){
            b2dr.render(world, cam.combined.cpy().scl(VidarVoyager.PPM));
        }

        batch.end();
    }

    /**
     * This method is called when the game is destroyed.
     */
    @Override
    public void dispose(){
        super.dispose();
        stage.dispose();
        mapRenderer.dispose();
        inventoryOxygenBar.dispose();
        mapRenderer.dispose();
        highlight.dispose();
    }

    /**
     * Called when this screen becomes the current screen for the Game.
     */

    public void show() { batch.setProjectionMatrix(cam.combined); }

    /**
     * This method is called when the screen is paused.
     */

    public void pause() { }

    /**
     * This method is called when resuming the screen from a paused state.
     */

    public void resume() { }

    /**
     * This method is called when the screen is no longer the current screen for the game.
     */

    public void hide() { }

    /**
     * Called when a key was pressed.
     * @param keycode one of the constants in {@link Input.Keys}
     * @return
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * Called when a key was released.
     * @param keycode one of the constants in {@link Input.Keys}
     * @return
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed.
     * @param character The character
     * @return
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be Input.Buttons.LEFT
     * on iOS. This is used to set UI buttons pressed.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be Input.Buttons.LEFT
     * on iOS.
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @return
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     * @param screenX
     * @param screenY
     * @return
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     * @param amountX the horizontal scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @param amountY the vertical scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @return
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

