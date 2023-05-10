package com.vv.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.io.File;

public class LogInEntry {
    private final float PADDING_INBETWEEN = 15.5f;
    private Texture background;
    private Table table;
    private TextField userName;
    private TextField password;
    private Stage stage;
    private boolean active = false;

    public LogInEntry(Stage stage){
        this.stage = stage;
        background = new Texture("screens" + File.separator + "LogInEntry.png");

        table = new Table();
        table.bottom().padBottom(550).padLeft(148);
        table.setFillParent(true);

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();
        style.cursor = new TextureRegionDrawable(new Texture("screens" + File.separator + "cursor.png"));
        style.fontColor = Color.WHITE;

        userName = new TextField("",style);
        userName.setSize(190, 20);
        userName.setMaxLength(32);

        password = new TextField("", style);
        password.setSize(190, 20);
        password.setPasswordMode(true);
        password.setMaxLength(32);

        stage.addActor(table);
    }

    public void setActive(boolean active) {
        if (active) {
            table.add(userName);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(password);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
        }
        else {
            table.removeActor(userName);
            table.removeActor(password);
        }
        this.active = active;
    }

    public boolean isActive(){ return active; }

    public void draw(Batch batch){
        batch.draw(background, 250, 500);
    }

}
