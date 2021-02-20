package sokoban.Model.gameEngine.object.ghost;

import sokoban.Model.game.J_GameEngine;
import sokoban.Model.gameEngine.move.J_Movement;

/**
 *  <p>
 *      This class implements the interface I_Ghost and creates a ghost which will
 *      let the count of moves of current level add 10.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_GhostFactory
 */
public class J_MoveAddTenGhost implements I_GhostFactory {

    /**
     * Return the type of this ghost.
     *
     * @return String - Increase the moves count of current level
     * @since 1.1
     */
    @Override
    public String type() {
            return "Step count add 10";
        }

    /**
     * Increase the moves count of current level by 10.
     *
     * @since 1.1
     */
    @Override
    public void collision(J_GameEngine gameEngine) {
        String levelName = gameEngine.getCurrentLevel().getName();
        int count = gameEngine.getMove().getMovesCount().get(levelName);
        gameEngine.getMove().getMovesCount().put(levelName, count + 10);
    }
}
