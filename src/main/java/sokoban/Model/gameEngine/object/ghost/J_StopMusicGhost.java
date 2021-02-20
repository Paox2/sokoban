package sokoban.Model.gameEngine.object.ghost;

import sokoban.Model.game.J_GameEngine;

/**
 *  <p>
 *      This class implements the interface I_Ghost and creates a ghost which will
 *      stop background music when player touches it.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_GhostFactory
 */
public class J_StopMusicGhost implements I_GhostFactory {

    /**
     * Return the type of this ghost.
     *
     * @return String - Stop music ghost
     * @since 1.1
     */
    @Override
    public String type() {
        return "Stop music";
    }

    /**
     * Stop the music (The special function of this ghost).
     *
     * @since 1.1
     */
    @Override
    public void collision(J_GameEngine gameEngine) {
        gameEngine.getMusic().stopMusic();
    }
}