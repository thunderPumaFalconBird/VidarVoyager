package com.vv.game.screens;

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
    private final float inventoryOffsetX = 96;
    private final float inventoryOffsetY = 60;
    private final float oxygenOffsetX = 796;
    private final float oxygenOffsetY = 85;
    private final float oxygenBarWidth = 15;
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
     * This method will return the name of a button as a string if there are any buttons being pressed. Currently,
     * there are no buttons.
     * @param x
     * @param y
     * @return
     */
    @Override
    public String getButtonPressed(float x, float y){
        String temp = "none";

        return temp;
    }

    @Override
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    @Override
    public void setGameWon(boolean gameWon) { this.gameWon = gameWon; }

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
                    inventoryX + i*inventoryOffsetX + inventoryOffsetY,
                    inventoryY + inventoryOffsetY
                    );
        }
        for(int i = 0; i < oxygenBarsNumber; i++){
            batch.draw(oxygenBarTexture,
                    inventoryX + i*oxygenBarWidth + oxygenOffsetX,
                    inventoryY + oxygenOffsetY);
        }

        batch.draw(highlight,
                inventoryX + inventoryHighlightX*100,
                inventoryY);
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
    @Override
    public void show() { batch.setProjectionMatrix(cam.combined); }

    /**
     * This method is called when the screen is paused.
     */
    @Override
    public void pause() { }

    /**
     * This method is called when resuming the screen from a paused state.
     */
    @Override
    public void resume() { }

    /**
     * This method is called when the screen is no longer the current screen for the game.
     */
    @Override
    public void hide() { }
}

