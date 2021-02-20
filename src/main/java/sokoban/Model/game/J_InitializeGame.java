package sokoban.Model.game;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.InputStream;

/**
 *  <p>
 *      This class initialize the game (gameEngine, map, grid) and
 *      set an event filter response to the key event.
 *  </p>
 *
 * @author Wendi Han - edited
 * @version 1.2
 * @see J_ReloadGird#reloadGrid(J_GameEngine, GridPane, Stage)
 */
public class J_InitializeGame {
    private J_GameEngine m_GameEngine;
    private final Stage m_PrimaryStage;
    private final GridPane m_GameGrid;

    /**
     * Initialize the value of {@code primaryStage}, {@code gridPane} and {@code imageList} using parameters.
     *
     * @param primaryStage  Stage show to the user
     * @param gridPane  The pane of game
     * @since 1.1
     */
    public J_InitializeGame(Stage primaryStage, GridPane gridPane) {
        m_PrimaryStage = primaryStage;
        m_GameGrid = gridPane;
    }

    /**
     * Initialize and return the game engine and set the event filter.
     *
     * @return {@code J_GameEngine} - Game Engine
     * @since 1.1
     */
    public J_GameEngine loadDefaultSaveFile() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("SampleGame.skb");
        m_GameEngine = new J_GameEngine(in, m_PrimaryStage);
        setEventFilter();
        return m_GameEngine;
    }

    /**
     * Using existing game engine and setting the event filter.
     *
     * @param gameEngine Game engine
     * @since 1.2
     */
    public void loadDefaultSaveFile(J_GameEngine gameEngine) {
        m_GameEngine = gameEngine;
        setEventFilter();
    }

    /**
     * Set on action response to the key event.
     * @since 1.1
     */
    public void setEventFilter() {
        m_PrimaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            m_GameEngine.getMove().handleKey(event.getCode());
            J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
        });
    }
}
