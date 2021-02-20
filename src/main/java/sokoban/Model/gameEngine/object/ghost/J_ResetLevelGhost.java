package sokoban.Model.gameEngine.object.ghost;

import sokoban.Model.game.J_GameEngine;

/**
 *  <p>
 *      This class implements the interface I_Ghost and creates a ghost which will
 *      reset current level when player touches it.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_GhostFactory
 */
public class J_ResetLevelGhost implements I_GhostFactory {

    /**
     * Return the type of this ghost.
     *
     * @return String - Reset level ghost
     * @since 1.1
     */
    @Override
    public String type() {
        return "Reset Level";
    }

    /**
     * Reset current level (The special function of this ghost).
     *
     * @since 1.1
     */
    @Override
    public void collision(J_GameEngine gameEngine) {
        gameEngine.resetLevel();
    }
}