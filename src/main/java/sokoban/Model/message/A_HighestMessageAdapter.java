package sokoban.Model.message;

import javafx.stage.Stage;
import sokoban.Model.game.J_GameEngine;

import java.util.LinkedHashMap;

/**
 *  <p>
 *      This class implements the method {@code  showMessage(J_GameEngine gameEngine)} and
 *      creates a new method {@code showMessageWithPosition(LinkedHashMap<String, Integer> list, J_GameEngine gameEngine)}
 *      needs lower class to implement.
 *  </p>
 *
 *  <ul>
 *      <li>A_HighestMessageAdapter is a adapter that make the parameter of method {@code showMessage()} can be used
 *      by message type use {@code LinkedHashMap<String, Integer> list} as parameter.</li>
 *  </ul>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_MessageFactor
 */
public abstract class A_HighestMessageAdapter  implements I_MessageFactor{

    /**
     * Override and abandon the old method used to show message.
     *
     * @param gameEngine  Game engine
     * @since 1.1
     */
    @Override
    public void showMessage(J_GameEngine gameEngine, Stage stage) {
        showMessageWithPosition(new LinkedHashMap<>(), gameEngine, stage);
    }

    /**
     * New method is used to change the parameter of {@code showMessage()} and make it suitable for certain type
     * of message.
     *
     * @param gameEngine  Game engine
     * @param list  A list about the position of the stage
     * @param stage Stage
     * @since 1.1
     */
    public abstract void showMessageWithPosition(LinkedHashMap<String, Integer> list
            , J_GameEngine gameEngine, Stage stage);
}