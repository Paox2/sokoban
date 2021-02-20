package sokoban.Model.gameEngine.object.ghost;

import sokoban.Model.game.J_GameEngine;

import java.util.Date;

/**
 *  <p>
 *      This class implements the interface I_Ghost and creates a ghost which will
 *      set the start time of current level to now.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_GhostFactory
 */
public class J_ResetTimeGhost implements I_GhostFactory {

    /**
     * Return the type of this ghost.
     *
     * @return String - Reset time ghost
     * @since 1.1
     */
    @Override
    public String type() {
        return "Reset time";
    }

    /**
     * Reset the start time of current level (The special function of this ghost).
     *
     * @since 1.1
     */
    @Override
    public void collision(J_GameEngine gameEngine) {
        gameEngine.getLr().setStartTime(new Date());
    }
}
