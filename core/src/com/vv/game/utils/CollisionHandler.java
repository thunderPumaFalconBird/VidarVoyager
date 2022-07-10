package com.vv.game.utils;

import com.badlogic.gdx.physics.box2d.*;

/**
 * This is the collision handler class.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class CollisionHandler implements ContactListener {
    Object outOfBounds; // Object that should be deleted once fully off-screen

    /**
     * Called when collision is detected but prior to the collision being processed. Allows for the changing of the
     * characteristics of the contact before the collision is handled.
     * @param contact information of the objects which are colliding
     * @param oldManifold the manifold from the previous contact
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    /**
     * Handles what happens when two objects come into contact.
     * @param contact information of the objects which are colliding
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

    }

    /**
     * Handles collision between an object and the world border
     * @param border the world border
     * @param obj the object colliding with the border
     */
    private void borderContact(Object border, Object obj) {

    }

    /**
     * Handles what happens when two objects lose contact with each other.
     * @param contact information of the objects which are colliding
     */
    @Override
    public void endContact(Contact contact) {

    }

    /**
     * Called after the collision has been handled. Allows for determining the collision results.
     * @param contact information of the objects which are colliding
     * @param impulse the impulse of the objects after the collision was processed
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
