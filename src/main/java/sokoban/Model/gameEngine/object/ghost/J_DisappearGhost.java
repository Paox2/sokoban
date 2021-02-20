package sokoban.Model.gameEngine.object.ghost;

import sokoban.Model.game.J_GameEngine;
import sokoban.Model.gameEngine.object.E_GameObject;

import java.awt.*;

/**
 *  <p>
 *      This class implements the interface I_Ghost and creates a ghost which will
 *      disappear after attaching.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_GhostFactory
 */
public class J_DisappearGhost implements I_GhostFactory {

    /**
     * Return the type of this ghost.000000000
     *
     * @return String - Reset time ghost
     * @since 1.1
     */
    @Override
    public String type() {
        return "Ghost disappear";
    }

    /**
     * Ghost will disappear.
     *
     * @since 1.1
     */
    @Override
    public void collision(J_GameEngine gameEngine) {
        Point ghostPosition = gameEngine.getMove().getTargetObjectPoint();
        gameEngine.getCurrentLevel().setGameObjectTo(E_GameObject.FLOOR, ghostPosition);
    }

}
