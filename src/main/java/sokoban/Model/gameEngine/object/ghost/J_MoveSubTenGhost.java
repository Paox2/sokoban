package sokoban.Model.gameEngine.object.ghost;

import sokoban.Model.game.J_GameEngine;
import sokoban.Model.gameEngine.move.J_Movement;

/**
 *  <p>
 *      This class implements the interface I_Ghost and creates a ghost which will
 *      let the count of moves of current level sub 10(if over then 10), otherwise to 0.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_GhostFactory
 */
public class J_MoveSubTenGhost implements I_GhostFactory {

    /**
     * Return the type of this ghost.
     *
     * @return String - Decrease the moves count of current level
     * @since 1.1
     */
    @Override
    public String type() {
        return "Step count sub 10";
    }

    /**
     * Decrease the moves count of current level by 10, if count less than ten then set to 0.
     *
     * @since 1.1
     */
    @Override
    public void collision(J_GameEngine gameEngine) {
        String levelName = gameEngine.getCurrentLevel().getName();
        int count = gameEngine.getMove().getMovesCount().get(levelName);
        if (count > 10) {
            gameEngine.getMove().getMovesCount().put(levelName, count - 10);
        } else {
            gameEngine.getMove().getMovesCount().put(levelName, 0);
        }
    }
}
