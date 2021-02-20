package sokoban.Model.gameEngine.object.ghost;

import sokoban.Model.game.J_GameEngine;

/**
 *  <p>
 *      This class implements the interface I_Ghost and creates a ghost which will
 *      give the player ability to fly.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_GhostFactory
 */
public class J_FlyGhost implements I_GhostFactory {
    /**
     * Return the type of this ghost.
     *
     * @return String - Give the ability to fly
     * @since 1.1
     */
    @Override
    public String type() {
        return "Player can fly";
    }

    /**
     * Give user the ability to fly.
     *
     * @since 1.1
     */
    @Override
    public void collision(J_GameEngine gameEngine) {
        gameEngine.setCanUseFly(true);
    }
}
