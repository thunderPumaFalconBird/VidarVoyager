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

public class SignUpEntry {
    private final float PADDING_INBETWEEN = 15.5f;
    private Texture background;
    private Table table;
    private TextField userName;
    private TextField firstName;
    private TextField lastName;
    private TextField middleInitial;
    private TextField dateOfBirth;
    private TextField email;
    private TextField password;
    private TextField passwordVerified;
    private Stage stage;
    private boolean active = false;

    public SignUpEntry(Stage stage){
        this.stage = stage;
        background = new Texture("screens" + File.separator + "SignUpEntry.png");

        table = new Table();
        table.bottom().padBottom(356).padLeft(148);
        table.setFillParent(true);

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();
        style.cursor = new TextureRegionDrawable(new Texture("screens" + File.separator + "cursor.png"));
        style.fontColor = Color.WHITE;

        userName = new TextField("",style);
        userName.setSize(190, 20);
        userName.setMaxLength(32);

        firstName = new TextField("", style);
        firstName.setSize(190, 20);
        firstName.setMaxLength(32);

        lastName = new TextField("", style);
        lastName.setSize(190, 20);
        lastName.setMaxLength(32);

        middleInitial = new TextField("", style);
        middleInitial.setSize(190, 20);
        middleInitial.setMaxLength(4);

        dateOfBirth = new TextField("yyy-mm-dd", style);
        dateOfBirth.setSize(190, 20);
        dateOfBirth.setMaxLength(12);

        email = new TextField("", style);
        email.setSize(190, 20);
        email.setMaxLength(52);

        password = new TextField("", style);
        password.setSize(190, 20);
        password.setPasswordMode(true);
        password.setMaxLength(32);

        passwordVerified = new TextField("", style);
        passwordVerified.setSize(190, 20);
        passwordVerified.setPasswordMode(true);
        passwordVerified.setMaxLength(32);

        stage.addActor(table);
    }

    public void setActive(boolean active){
        if(active){
            table.add(userName);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(firstName);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(lastName);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(middleInitial);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(dateOfBirth);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(email);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(password);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
            table.add(passwordVerified);
            table.row().padBottom(PADDING_INBETWEEN).padTop(PADDING_INBETWEEN);
        }
        else{
            table.removeActor(userName);
            table.removeActor(firstName);
            table.removeActor(lastName);
            table.removeActor(middleInitial);
            table.removeActor(dateOfBirth);
            table.removeActor(email);
            table.removeActor(password);
            table.removeActor(passwordVerified);
        }
        this.active = active;
    }

    public boolean isActive(){ return active; }

    public void draw(Batch batch){
        batch.draw(background, 250, 300);
    }

    public String getUserInputUsername() { return userName.getText(); }

    public String getUserInputFirstName() { return firstName.getText(); }

    public String getUserInputLastName() { return lastName.getText(); }

    public String getUserInputMiddleInitial() { return middleInitial.getText(); }

    public String getUserInputDateOfBirth() { return dateOfBirth.getText(); }

    public String getUserInputEmail() { return email.getText(); }

    public String getUserInputPassword() { return password.getText(); }

    public boolean verifyPassword(){
        boolean result = password.getText().equals(passwordVerified.getText());
        if(!result){
            password.setText("");
            passwordVerified.setText("");
        }
        return result;
    }

    public void userNameTaken(){
        userName.setText("This user name is taken");
    }

    public void failedSignUp(){
        userName.setColor(Color.RED);
        firstName.setColor(Color.RED);
        lastName.setColor(Color.RED);
        middleInitial.setColor(Color.RED);
        dateOfBirth.setColor(Color.RED);
        email.setColor(Color.RED);
        password.setColor(Color.RED);
        passwordVerified.setColor(Color.RED);
    }
}
