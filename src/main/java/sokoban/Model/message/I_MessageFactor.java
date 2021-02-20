package sokoban.Model.message;

import javafx.stage.Stage;
import sokoban.Model.game.J_GameEngine;

import java.util.List;

/**
 *  <p>
 *      This class is a message factor, which contains the method about getting and showing the message.<br>
 *  </p>
 *
 *  <ul>
 *       <li> I_MessageFactor allows user to get message from certain file and uses certain format.
 *       <li> I_MessageFactor allows user to show message in the stage.
 *  </ul>
 *
 * @author Wendi Han
 * @version 1.1
 */
public interface I_MessageFactor {

    /**
     * Allow user to get message from certain file and uses certain format.
     *
     * @return {@code List<String>} - A list of string/message
     */
    List<String> getMessage();

    /**
     * Allows user to show message in the stage.
     *
     * @param gameEngine  Game engine
     * @param stage Stage
     */
    void showMessage(J_GameEngine gameEngine, Stage stage);
}
