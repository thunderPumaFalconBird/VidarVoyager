package com.vv.game.rescueMission;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.vv.game.rescueMission.entities.collectable.Collectable;

import java.io.File;
import java.util.Vector;

public class Inventory {
    private final float INVENTORY_HIGHLIGHT_OFFSET = 21;
    private final float INVENTORY_OFFSET_X = 96;
    private final float INVENTORY_BAR_OFFSET_X = 60;
    private final float INVENTORY_BAR_OFFSET_Y = 81;
    private final int INVENTORY_MAX = 5;
    private final Texture highlight;
    private final Texture inventoryOxygenBar;
    private final Array<Collectable> inventory;

    public Inventory(){
        highlight = new Texture("screens" + File.separator + "Highlight2.png");
        inventoryOxygenBar = new Texture("screens" + File.separator + "InventoryOxygenBar.png");
        inventory = new Array<>();
    }

    public void addItem(Collectable item){
        inventory.add(item);
    }

    public boolean isEmpty(){ return inventory.isEmpty(); }

    public boolean isFull(){
        return inventory.size >= INVENTORY_MAX;
    }

    public void dropItem(int index, Vector2 position){
        inventory.get(index).putDownItem(position);
        inventory.removeIndex(index);
    }

    public Array<Collectable> getInventory() { return inventory; }

    public void draw(Batch batch, float x, float y, float highlightX){

        batch.draw(inventoryOxygenBar, x, y);

        //player.getBody().getPosition().x * VidarVoyager.PPM  - ((float)VidarVoyager.APP_HEIGHT/2)
        for(int i = 0; i < inventory.size; i++){
            batch.draw(inventory.get(i).getTextureRegion(),
                    x + i* INVENTORY_OFFSET_X + INVENTORY_BAR_OFFSET_X,
                    y + INVENTORY_BAR_OFFSET_Y);
        }
        batch.draw(highlight,
                x + highlightX*100,
                y + INVENTORY_HIGHLIGHT_OFFSET);
    }

    public void dispose(){
        highlight.dispose();
    }

}
