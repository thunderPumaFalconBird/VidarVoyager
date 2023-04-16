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

/**
 * This is the collision handler class.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class CollisionHandler implements ContactListener {

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

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
        }
        else if(fixA.getBody().getUserData() instanceof Astronaut && fixB.getBody().getUserData() instanceof Door){
            Door door = (Door) fixA.getBody().getUserData();
            door.setActive(true);
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

    private void handleItem(Fixture fixA, Fixture fixB){
        Astronaut player = (Astronaut) fixB.getBody().getUserData();
        Collectable item = (Collectable) fixA.getBody().getUserData();
        if(player.pickUpItem(item)) {
            item.setCollected(true);
        }
    }

    private void handleOxygenRefill(Fixture fixA, Fixture fixB) {
        OxygenStation oxy = (OxygenStation) fixA.getBody().getUserData();
        Astronaut player = (Astronaut) fixB.getBody().getUserData();
        //The refilling logic is in the astronauts update method. If refilling is true the level increments else it decrements.
        if(oxy.getRefills() > 0) {
            player.setRefillingOxygen(true);
            oxy.setRefills(oxy.getRefills() - 1);
        }
    }

    private void handleOxygenRefillEnd(Fixture fix) {
        Astronaut player = (Astronaut) fix.getBody().getUserData();
        //The refilling logic is in the astronauts update method. If refilling is true the level increments else it decrements.
        player.setRefillingOxygen(false);
    }

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
