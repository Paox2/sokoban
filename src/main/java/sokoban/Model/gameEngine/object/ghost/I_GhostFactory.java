package sokoban.Model.gameEngine.object.ghost;

import sokoban.Model.game.J_GameEngine;

/**
 *  <p>
 *      This class is a ghost factor, which contains the method about show ghost type and
 *      implements its special operation.<br>
 *  </p>
 *
 *  <ul>
 *       <li> I_Ghost allows user to get ghost type.
 *       <li> Each instance will implement its function by implementing {@code collision()}.
 *  </ul>
 *
 * @author Wendi Han
 * @version 1.1
 */
public interface I_GhostFactory {

    /**
     * Return the type of ghost.
     *
     * @return String - Type of ghost
     * @since 1.1
     */
    String type();

    /**
     * Each instance will implement its function by implementing this method.
     *
     * @since 1.1
     */
    void collision(J_GameEngine gameEngine);
}
