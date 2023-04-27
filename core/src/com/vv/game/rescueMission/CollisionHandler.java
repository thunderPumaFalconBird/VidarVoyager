package com.vv.game.rescueMission;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.vv.game.rescueMission.entities.collectable.Collectable;
import com.vv.game.rescueMission.entities.collectable.TeddyBear;
import com.vv.game.rescueMission.entities.immovable.LifeSupport;
import com.vv.game.rescueMission.entities.immovable.OxygenStation;
import com.vv.game.rescueMission.entities.movable.Astronaut;
import com.vv.game.rescueMission.entities.immovable.Cannon;
import com.vv.game.rescueMission.entities.immovable.Door;
import com.vv.game.screens.ScreenController;
import com.vv.game.utils.GameController;

/**
 * This is the collision handler class. The Level class sets a new collisionHandler for each level.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class CollisionHandler implements ContactListener {
    private ScreenController screenController = ScreenController.getInstance();

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    /**
     * This method is called whenever two bodies in the box2d world collide.
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() instanceof Cannon){
            Cannon cannon = (Cannon) fixB.getBody().getUserData();
            cannon.fireCannon();
        }
        else if(fixA.getBody().getUserData() instanceof Cannon && fixB.getBody().getUserData() instanceof Astronaut){
            Cannon cannon = (Cannon) fixA.getBody().getUserData();
            cannon.fireCannon();
        }
        else if(fixA.getBody().getUserData() instanceof Door && fixB.getBody().getUserData() instanceof Astronaut){
            Door door = (Door) fixA.getBody().getUserData();
            door.setActive(true);
            screenController.setScreen(ScreenController.SCREEN_STATE.PUZZLE_SCREEN);
            door.getPuzzle().setStage(screenController.getScreenStage(ScreenController.SCREEN_STATE.PUZZLE_SCREEN));
        }
        else if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() instanceof Door){
            Door door = (Door) fixA.getBody().getUserData();
            door.setActive(true);
            screenController.setScreen(ScreenController.SCREEN_STATE.PUZZLE_SCREEN);
            door.getPuzzle().setStage(screenController.getScreenStage(ScreenController.SCREEN_STATE.PUZZLE_SCREEN));
        }
        else if(fixA.getBody().getUserData() instanceof Collectable && fixB.getBody().getUserData() instanceof Astronaut){
            handleItem(fixA, fixB);
        }
        else if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() instanceof Collectable){
            handleItem(fixB, fixA);
        }
        else if(fixA.getBody().getUserData() instanceof OxygenStation && fixB.getBody().getUserData() instanceof Astronaut){
            handleOxygenRefill(fixA, fixB);
        }
        else if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() instanceof OxygenStation){
            handleOxygenRefill(fixB, fixA);
        }
        else if(fixA.getBody().getUserData() instanceof LifeSupport && fixB.getBody().getUserData() instanceof Astronaut){
            handleLifeSupport(fixA, fixB);
        }
        else if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() instanceof LifeSupport){
            handleLifeSupport(fixB, fixA);
        }
    }

    /**
     * This method is called whenever two bodies are no longer in contact.
     * @param contact
     */
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if(fixA.getBody().getUserData() instanceof OxygenStation && fixB.getBody().getUserData() instanceof Astronaut){
            handleOxygenRefillEnd(fixB);
        }
        else if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() instanceof OxygenStation){
            handleOxygenRefillEnd(fixA);
        }
    }

    /**
     * This method is called when an astronaut collides with a collectable item. The item will be picked up if the
     * astronauts inventory is not full. If the player's inventory is full pickUpItem(item) will return false.
     * @param fixA
     * @param fixB
     */
    private void handleItem(Fixture fixA, Fixture fixB){
        Astronaut player = (Astronaut) fixB.getBody().getUserData();
        Collectable item = (Collectable) fixA.getBody().getUserData();
        if(player.pickUpItem(item)) {
            item.setCollected(true);
        }
    }

    /**
     * This method is called when an astronaut collides with an oxygen station. As long as the oxygen station still has
     * a refill it will set the player's refilling boolean to true. The player's update method will increment the oxygen
     * level while refilling is true.
     * @param fixA
     * @param fixB
     */
    private void handleOxygenRefill(Fixture fixA, Fixture fixB) {
        OxygenStation oxy = (OxygenStation) fixA.getBody().getUserData();
        Astronaut player = (Astronaut) fixB.getBody().getUserData();
        //The refilling logic is in the astronauts update method. If refilling is true the level increments else it decrements.
        if(oxy.getRefills() > 0) {
            player.setRefillingOxygen(true);
            oxy.setRefills(oxy.getRefills() - 1);
        }
    }

    /**
     * This method sets the player's refilling boolean to false. The player's update method will no longer increment the
     * oxygen level when refilling is false.
     * @param fix
     */
    private void handleOxygenRefillEnd(Fixture fix) {
        Astronaut player = (Astronaut) fix.getBody().getUserData();
        //The refilling logic is in the astronauts update method. If refilling is true the level increments else it decrements.
        player.setRefillingOxygen(false);
    }

    /**
     * This method is called when the astronaut collides with the life support structure. It check's the player's
     * inventory for one of each color bear. If the player has all the bears it will set the life support structure to
     * fixed.
     * @param fixA
     * @param fixB
     */
    private void handleLifeSupport(Fixture fixA, Fixture fixB){
        Astronaut player = (Astronaut) fixB.getBody().getUserData();
        LifeSupport lifeSupport = (LifeSupport) fixA.getBody().getUserData();
        boolean[] bears = {false, false, false, false, false};

        Array<Collectable> temp = player.getInventory();
        for (int i = 0; i < temp.size; i++){
            if(temp.get(i).getBody().getUserData() instanceof TeddyBear){
                TeddyBear tempBear = (TeddyBear) temp.get(i).getBody().getUserData();
                TeddyBear.COLOR tempColor = tempBear.getTeddyColor();
                if(tempColor == TeddyBear.COLOR.BLUE){
                    bears[0] = true;
                }
                else if(tempColor == TeddyBear.COLOR.GREEN){
                    bears[1] = true;
                }
                else if(tempColor == TeddyBear.COLOR.ORANGE){
                    bears[2] = true;
                }
                else if(tempColor == TeddyBear.COLOR.PINK){
                    bears[3] = true;
                }
                else if(tempColor == TeddyBear.COLOR.RED){
                    bears[4] = true;
                }
            }
        }
        if(bears[0] && bears[1] && bears[2] && bears[3] && bears[4]){
            lifeSupport.setFixed(true);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
