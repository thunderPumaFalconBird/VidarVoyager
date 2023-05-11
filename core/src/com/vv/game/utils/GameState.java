package com.vv.game.utils;

import com.badlogic.gdx.utils.Array;
import com.vv.game.rescueMission.entities.immovable.Door;

public class GameState {
    private int levelNumber;
    private float playerPositionX;
    private float playerPositionY;
    private float oxygenLevel;
    private String object1;
    private String object2;
    private String object3;
    private String object4;
    private String object5;
    private Array<Door> doors = new Array<>();
    private static GameState instance = new GameState();

    private GameState(){}

    public static GameState getInstance() { return instance;}

    public int getLevelNumber() { return levelNumber; }

    public void setLevelNumber(int levelNumber) { this.levelNumber = levelNumber; }

    public float getPlayerPositionX() { return playerPositionX; }

    public void setPlayerPositionX(float playerPositionX) { this.playerPositionX = playerPositionX; }

    public float getPlayerPositionY() { return playerPositionY; }

    public void setPlayerPositionY(float playerPositionY) { this.playerPositionY = playerPositionY; }

    public float getOxygenLevel() { return oxygenLevel; }

    public void setOxygenLevel(float oxygenLevel) { this.oxygenLevel = oxygenLevel; }

    public String getObject1() { return object1; }

    public void setObject1(String object1) { this.object1 = object1; }

    public String getObject2() { return object2; }

    public void setObject2(String object2) { this.object2 = object2; }

    public String getObject3() { return object3; }

    public void setObject3(String object3) { this.object3 = object3; }

    public String getObject4() { return object4; }

    public void setObject4(String object4) { this.object4 = object4; }

    public String getObject5() { return object5; }

    public void setObject5(String object5) { this.object5 = object5; }

    public Array<Door> getDoors() { return doors; }

    public void setDoors(Array<Door> doors) { this.doors = doors; }
}
