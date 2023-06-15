package com.vv.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
    private ImageButton enterButton;
    private ImageButton backButton;
    private Stage stage;
    private boolean active = false;

    public LogInEntry(Stage stage){
        this.stage = stage;
        background = new Texture("screens" + File.separator + "LogInEntry.png");

        table = new Table();
        table.bottom().padBottom(520);
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

        Texture textureUp = new Texture("buttons" + File.separator + "EnterButton.png");
        TextureRegion textureRegionUp = new TextureRegion(textureUp);
        enterButton = new ImageButton(new TextureRegionDrawable(textureRegionUp));

        textureUp = new Texture("buttons" + File.separator + "BackButton.png");
        textureRegionUp = new TextureRegion(textureUp);
        backButton = new ImageButton(new TextureRegionDrawable(textureRegionUp));

        stage.addActor(table);
    }

    public void setActive(boolean active) {
        if (active) {
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(userName).colspan(2).padLeft(135);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(password).colspan(2).padLeft(135);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(backButton);
            table.add(enterButton);
        }
        else {
            table.removeActor(userName);
            table.removeActor(password);
            table.removeActor(enterButton);
            table.removeActor(backButton);
        }
        this.active = active;
    }

    public boolean isActive(){ return active; }

    public void draw(Batch batch){
        batch.draw(background, 250, 500);
    }

    public String getUserInputUsername() { return userName.getText(); }

    public String getUserInputPassword() { return password.getText(); }

    public String getButtonsPressed(){
        String temp = "none";
        if(backButton.isPressed()){
            temp = "back";
        }
        if(enterButton.isPressed()){
            temp = "back";
        }
        return temp;
    }

    public void failedLogIn(){
        userName.setColor(Color.RED);
        password.setColor(Color.RED);
    }

}
