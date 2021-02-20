package sokoban.Model.gameEngine.debug;

import sokoban.Model.gameEngine.music.J_MusicSingleton;

/**
 *  <p>
 *      This class is used to open or close debug pattern.
 *      When {@code m_Debug} is {@code true}, it will show the message after player moving or other operation.
 *  </p>
 *
 * @author Wendi Han - edited
 * @version 1.1
 */
public class J_Debug {
    private boolean m_Debug = false;
    private static J_Debug instance;

    /**
     * Get the instance of debug.
     *
     * @return Instance of debug
     */
    public static J_Debug getInstance(){
        if(instance == null) {
            synchronized (J_MusicSingleton.class) {
                if(instance == null) {
                    instance = new J_Debug();
                }
            }
        }
        return instance;
    }

    /**
     * Return {@code true} if debug is opening.
     *
     * @return {@code true} - if debug is opening.
     * @since 1.1
     */
    public boolean isDebugActive() {
        return m_Debug;
    }

    /**
     * Open or close the debug.
     *
     * @since 1.1
     */
    public void toggleDebug() {
        m_Debug = !m_Debug;
    }
}
