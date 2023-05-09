package com.vv.game.rescueMission.puzzles.minesweeper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vv.game.rescueMission.puzzles.Puzzle;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * This is the MineSweeper class. It is a mini-game that is used as a puzzle for RescueMission.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class MineSweeper extends Puzzle {
    private Texture background = new Texture("minesweeper" + File.separator + "MineSweeperBackground.png");
    private final int ROWS = 16;
    private final int COLUMNS = 16;
    private final int NUM_OF_MINES = 30;
    private final int SQUARE_WIDTH = 50;
    private final int BOARD_OFFSET = 100;
    private Table table;
    private Label minesLabel;
    private Label timeLabel;
    private Stage stage;
    private HashMap<Vector2, Square> squares;
    private float stateTime = 0f;
    private int clockTime = 0;
    private int numOfMinesLeft;
    private boolean active = false;
    private boolean firstTouch = true;
    private int explosionX = -1;
    private int explosionY = -1;


    /**
     * The constructor sets up the square in the hashMap to all be zero. it also sets up the timeLabel and MinesLabel
     * for the scoreboard.
     */
    public MineSweeper(){
        squares = new HashMap<>();
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                int x = BOARD_OFFSET + j * SQUARE_WIDTH;
                int y = BOARD_OFFSET + i * SQUARE_WIDTH;
                squares.put(new Vector2(x,y) ,new Square(x,y));
            }
        }
        numOfMinesLeft = NUM_OF_MINES;

        table = new Table();
        table.top();
        table.setFillParent(true);

        timeLabel = new Label(String.format(Locale.getDefault(), "%03d", clockTime),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel.setFontScale(1.6f);
        table.add(timeLabel).expandX().padBottom(-115).padLeft(350);

        minesLabel = new Label(String.format(Locale.getDefault(), "%02d", numOfMinesLeft),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        minesLabel.setFontScale(1.6f);
        table.add(minesLabel).expandX().padBottom(-115).padRight(300);
    }

    /**
     * This method takes the players first click x and y as parameters and places mines in random squares except for the
     * square that the player clicked on first. If also adjusts the nearby squares mine count.
     *
     * @param player_x the x position of the players mouse click
     * @param player_y the y position of the players mouse click
     */
    private void initBoard(int player_x, int player_y){

        Random rand = new Random();
        int max = SQUARE_WIDTH + COLUMNS * SQUARE_WIDTH;
        int min = BOARD_OFFSET;

        for(int i = 0; i < NUM_OF_MINES; i++){
            int x = rand.nextInt(max - min + 1) + min;
            int y = rand.nextInt(max - min + 1) + min;

            x = x - x % SQUARE_WIDTH;
            y = y - y % SQUARE_WIDTH;


            // makes sure no mine is created in the square the player first clicks on.
            while(x == player_x && y == player_y){
                x = rand.nextInt(max - min + 1) + min;
                x = x - x % SQUARE_WIDTH;

                y = rand.nextInt(max - min + 1) + min;
                y = y - y % SQUARE_WIDTH;
            }


            if(!squares.get(new Vector2(x,y)).isMine()){
                //Set Square as mine
                squares.get(new Vector2(x,y)).setType(Square.TYPE.MINE);
                boolean X_lessThanBorderLimits = x < SQUARE_WIDTH + COLUMNS * SQUARE_WIDTH;


                //SET ALL SQUARES NEARBY MINE. While checking for the boards boundaries.
                if(y > BOARD_OFFSET){
                    //Set square on top middle
                    squares.get(new Vector2(x,y- SQUARE_WIDTH)).addMineNearBy();
                    if(x > BOARD_OFFSET){
                        // Set square on top left
                        squares.get(new Vector2(x - SQUARE_WIDTH,y - SQUARE_WIDTH)).addMineNearBy();
                    }
                    if(X_lessThanBorderLimits){
                        //Set square on top right
                        squares.get(new Vector2(x + SQUARE_WIDTH,y - SQUARE_WIDTH)).addMineNearBy();
                    }
                }
                if(y < SQUARE_WIDTH + ROWS * SQUARE_WIDTH){
                    //Set square on bottom middle
                    squares.get(new Vector2(x,y + SQUARE_WIDTH)).addMineNearBy();
                    if(x > BOARD_OFFSET){
                        //Set square on bottom left
                        squares.get(new Vector2(x - SQUARE_WIDTH,y + SQUARE_WIDTH)).addMineNearBy();
                    }
                    if(X_lessThanBorderLimits){
                        //Set square on bottom right
                        squares.get(new Vector2(x + SQUARE_WIDTH,y + SQUARE_WIDTH)).addMineNearBy();
                    }
                }
                if(x > BOARD_OFFSET){
                    //Set square on left middle
                    squares.get(new Vector2(x - SQUARE_WIDTH,y)).addMineNearBy();
                }
                if(X_lessThanBorderLimits){
                    //Set square on right middle
                    squares.get(new Vector2(x + SQUARE_WIDTH,y)).addMineNearBy();
                }
            }
            else{
                //If there is already a mine try again.
                i--;
            }
        }
    }

    /**
     * This method reveals the square that was clicked on. It also recursively checks the nearby squares if the square
     * has a zero value.
     *
     * @param x the x position of the square that was clicked on
     * @param y the y position of the square that was clicked on
     */
    private void revealSquares(int x, int y){
        if(squares.get(new Vector2(x,y)).isZero()){
            boolean X_lessThanBorderLimits = x < SQUARE_WIDTH  + COLUMNS * SQUARE_WIDTH;
            squares.get(new Vector2(x,y)).setTexture();

            if(y > BOARD_OFFSET){
                //top middle
                if(!squares.get(new Vector2(x,y- SQUARE_WIDTH)).isRevealed()) {
                    revealSquares(x, y - SQUARE_WIDTH);
                }
                if(x > BOARD_OFFSET){
                    //top left
                    if(!squares.get(new Vector2(x - SQUARE_WIDTH, y - SQUARE_WIDTH)).isRevealed()) {
                        revealSquares(x - SQUARE_WIDTH, y - SQUARE_WIDTH);
                    }
                }
                if(X_lessThanBorderLimits){
                    //Set square on top right
                    if(!squares.get(new Vector2(x + SQUARE_WIDTH, y - SQUARE_WIDTH)).isRevealed()) {
                        revealSquares(x + SQUARE_WIDTH, y - SQUARE_WIDTH);
                    }
                }
            }
            if(y < SQUARE_WIDTH + ROWS * SQUARE_WIDTH){
                //bottom middle
                if(!squares.get(new Vector2(x, y + SQUARE_WIDTH)).isRevealed()) {
                    revealSquares(x, y + SQUARE_WIDTH);
                }
                if(x > BOARD_OFFSET){
                    //bottom left
                    if(!squares.get(new Vector2(x - SQUARE_WIDTH, y + SQUARE_WIDTH)).isRevealed()) {
                        revealSquares(x - SQUARE_WIDTH, y + SQUARE_WIDTH);
                    }
                }
                if(X_lessThanBorderLimits){
                    //bottom right
                    if(!squares.get(new Vector2(x + SQUARE_WIDTH, y + SQUARE_WIDTH)).isRevealed()) {
                        revealSquares(x + SQUARE_WIDTH, y + SQUARE_WIDTH);
                    }
                }
            }
            if(x > BOARD_OFFSET){
                //Set square on left middle
                if(!squares.get(new Vector2(x - SQUARE_WIDTH, y)).isRevealed()) {
                    revealSquares(x - SQUARE_WIDTH, y);
                }
            }
            if(X_lessThanBorderLimits){
                //Set square on right middle
                if(!squares.get(new Vector2(x + SQUARE_WIDTH, y)).isRevealed()) {
                    revealSquares(x + SQUARE_WIDTH, y);
                }
            }
        }
        else if (!squares.get(new Vector2(x,y)).isMine()){
            squares.get(new Vector2(x,y)).setTexture();
        }
    }

    /**
     * This method checks if all the none mine square are revealed. If they are, it sets solved to true.
     */
    private void checkForWin(){
        boolean win = true;
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                int x = BOARD_OFFSET + j * SQUARE_WIDTH;
                int y = BOARD_OFFSET + i * SQUARE_WIDTH;
                if(!squares.get(new Vector2(x,y)).isMine() && !squares.get(new Vector2(x,y)).isRevealed()){
                    win = false;
                }
            }
        }
        solved = win;
    }

    /**
     * This method resets all the squares in the board to zeros and sets first touch to true so that the touchDown
     * method will initialize a new board when the player clicks on the first square. The clockTime is not reset.
     */
    private void resetBoard(){
        squares.clear();
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                int newX = BOARD_OFFSET + j * SQUARE_WIDTH;
                int newY = BOARD_OFFSET + i * SQUARE_WIDTH;
                squares.put(new Vector2(newX,newY) ,new Square(newX,newY));
            }
        }
        firstTouch = true;
        numOfMinesLeft = NUM_OF_MINES;
    }

    /**
     * This method is called when the Player collides with a door. The puzzle actors will get added to the puzzleScreen
     * stage.
     * @param stage May be null if the actor or any ascendant is no longer in a stage.
     */
    public void setStage(Stage stage){
        this.stage = stage;
        stage.addActor(this);
        stage.addActor(table);
    }


    /**
     * This method is called based on the APP_FPS. The clock is updated based on deltaTime and the labels are updated.
     */
    @Override
    public void update(){
        //StateTime is used to add up the deltaTime it adds up to 1 every second.
        stateTime += Gdx.graphics.getDeltaTime();
        if(stateTime >= 1){
            clockTime++;
            stateTime = 0;
        }

        minesLabel.setText(String.format(Locale.getDefault(), "%02d", numOfMinesLeft));
        timeLabel.setText(String.format(Locale.getDefault(), "%03d", clockTime));

        if(explosionX != -1){
            //TODO update explosion animation
            //TODO end animation sets explosionX and explostionY = -1 and resetBoard()
        }
    }

    /**
     * This method is called to draw the squares.
     * @param batch
     * @param parentAlpha The parent alpha, to be multiplied with this actor's alpha, allowing the parent's alpha to affect all
     *           children.
     */
    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(background, 0, 0);
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                squares.get(new Vector2(BOARD_OFFSET + j * SQUARE_WIDTH, BOARD_OFFSET + i * SQUARE_WIDTH)).draw(batch);
            }
        }
        if(explosionX != -1){
            //TODO draw explosion animation
        }
    }

    @Override
    public boolean isSolved(){ return solved; }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void setActive(boolean active) { this.active = active; }

    /**
     * This method handles mouse inputs.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean handled = false;

        //Check that the player is clicking on a square.
        if(screenX > BOARD_OFFSET && screenX < BOARD_OFFSET + COLUMNS * SQUARE_WIDTH &&
                screenY > BOARD_OFFSET && screenY < BOARD_OFFSET + ROWS * SQUARE_WIDTH) {


            /*The x and y are stored in a mashMap based on the upper left x and y. To get the x and y of the square that
            was clicked on, the mod of the square width is subtracted from the x and y*/
            int x = screenX - screenX % SQUARE_WIDTH;
            int y = screenY - screenY % SQUARE_WIDTH;

            //flip the input y. Screen y is 0 at the top, Input y is 0 at the bottom.
            y = 950 - y;

            // If this is the players first click on the board then initialize the board.
            if (!isSolved() && firstTouch) {
                initBoard(x, y);
                firstTouch = false;
            }

            if(button == Input.Buttons.LEFT && !squares.get(new Vector2(x, y)).isFlagged()) {
                if (!squares.get(new Vector2(x, y)).isMine()) {
                    revealSquares(x, y);
                } else {
                    explosionX = x;
                    explosionY = y;
                    resetBoard();
                }
            }
            else if(button == Input.Buttons.RIGHT){
                squares.get(new Vector2(x, y)).setFlagged();
                numOfMinesLeft--;
            }

            checkForWin();

            handled = true;
        }
        return handled;
    }

    @Override
    public boolean keyDown(int keycode) { return false; }

    @Override
    public boolean keyUp(int keycode) { return false; }

    @Override
    public boolean keyTyped(char character) { return false; }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

    @Override
    public boolean mouseMoved(int screenX, int screenY) { return false; }

    @Override
    public boolean scrolled(float amountX, float amountY) { return false; }
}