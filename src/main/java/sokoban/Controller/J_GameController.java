package sokoban.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sokoban.GUI.J_GameView;
import sokoban.Model.game.*;
import sokoban.Model.gameEngine.debug.J_Debug;
import sokoban.Model.gameEngine.move.J_Movement;
import sokoban.Model.gameEngine.music.J_MusicSingleton;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.level.J_Level;
import sokoban.Model.level.J_LevelRecord;
import sokoban.Model.log.J_GameLogger;

import java.io.*;
import java.net.URL;
import java.util.Date;


/**
 * The controller response to the request from game page.<br>
 * It implements the function in game page and also initialize the game engine and game grid.
 *
 * @author Wendi Han - edited
 * @version 1.5
 * @see J_GameEngine
 * @see J_MenuController
 * @see J_MusicSingleton#setVolume(Double)
 * @see sokoban.Model.gameEngine.object.J_Color#changeColor(E_GameObject, String)
 * @see J_Movement#undoMove()
 * @see J_GameEngine#resetLevel()
 * @see J_Tooltip
 * @see J_LoadGame
 * @see J_SaveGame
 */
public class J_GameController{

    private final Stage m_PrimaryStage;
    private J_GameEngine m_GameEngine;
    private final GridPane m_GameGrid;

    /**
     * Get the game grid and stage from game view.<br>
     * Also, if user is back to menu from last game and login again, the variable backToMenu in game view
     * will be true, then it will not initialize game engine again.
     *
     * @param gameView  The view of game controller
     * @param primaryStage  Stage
     * @author Wendi Han
     * @since 1.1
     */
    public J_GameController(Stage primaryStage, J_GameView gameView) {
        m_PrimaryStage = primaryStage;
        m_GameGrid = gameView.getM_GameGrid();
        J_InitializeGame m_InitializeGame = new J_InitializeGame(m_PrimaryStage, m_GameGrid);
        if (!gameView.isM_ContinueLastGame()) {
            m_GameEngine = m_InitializeGame.loadDefaultSaveFile();
        } else {
            m_GameEngine = gameView.getM_GameEngine();
            m_GameEngine.setPrimaryStage(m_PrimaryStage);
            m_InitializeGame.loadDefaultSaveFile(m_GameEngine);
        }
        J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
    }


    /**
     * Response the request to back to menu.
     *
     * @since 1.1
     * @see J_MenuController
     * @see FXMLLoader
     */
    public void backToMenu() {
        try {
            m_GameEngine.getLr().setEndTime(new Date());
            m_GameEngine.getLr().setM_Time(m_GameEngine.getLr().getTimeDiff());
            m_GameEngine.getLr().setEndTime(null);

            Parent roots;
            FXMLLoader fxmlLoader = new FXMLLoader(new URL("file:src/main/java/sokoban/GUI/Menu.fxml"));
            roots = fxmlLoader.load();

            m_PrimaryStage.close();
            Stage stage = new Stage();
            m_GameEngine.setPrimaryStage(stage);
            J_MenuController controller = fxmlLoader.getController();
            controller.setBackToMenu(m_GameEngine);

            stage.setTitle("Sokoban");
            stage.setScene(new Scene(roots, 943, 448));
            stage.sizeToScene();
            stage.show();
            stage.setMaxHeight(stage.getHeight());
            stage.setMaxWidth(stage.getWidth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adjust the volume of the sound.
     *
     * @param value  The volume of sound
     * @since 1.1
     * @see J_MusicSingleton#setVolume(Double)
     */
    public void setVolume(double value) {
        m_GameEngine.getMusic().setVolume(value);
        J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
    }

    /**
     * Adjust the color of object.
     *
     * @param obj  Represent the object in the map.
     * @param color  The new color of the object
     * @since 1.1
     * @see sokoban.Model.gameEngine.object.J_Color#changeColor(E_GameObject, String)
     */
    public void toggleColor(E_GameObject obj, String color) {
        m_GameEngine.getColor().changeColor(obj, color);
        J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
    }

    /**
     * Close the game.
     *
     * @since 1.1
     */
    public void closeGame() {
        System.exit(0);
    }

    /**
     * Response to the request to back to last position.
     *
     * @since 1.3
     * @see J_Movement#undoMove()
     */
    public void undo() {
        m_GameEngine.getMove().undoMove();
        J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
    }

    /**
     * Response to the request to reset current level.
     *
     * @since 1.2
     * @see J_GameEngine#resetLevel()
     */
    public void resetLevel() {
        m_GameEngine.resetLevel();
        J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
    }

    /**
     * Show the message about game.
     *
     * @since 1.1
     * @see J_Tooltip
     */
    public void showAbout() {
        String message = "'LEFT' to move left\n'RIGHT' to move right\n" +
                "'UP' to move up\n'DOWN' to move down" +
                "\nPut All The Crate On The Diamond To Win The Game";

        J_Tooltip tooltip = new J_Tooltip(message, m_PrimaryStage);
        tooltip.show(Duration.seconds(4));
    }

    /**
     * According to the current state of music decide to start the music or close it.
     *
     * @since 1.1
     * @see J_MusicSingleton#toggleMusic()
     */
    public void toggleMusic() {
        m_GameEngine.getMusic().toggleMusic();
        J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
    }

    /**
     * According to the current state of debug decide to start the debug pattern or close it.
     *
     * @since 1.1
     * @see J_Debug#toggleDebug()
     */
    public void toggleDebug() {
        m_GameEngine.getDebug().toggleDebug();
        J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
    }

    /**
     * Set the username of player used to save new record.
     *
     * @param userName  The username of player
     * @since 1.1
     * @see J_GameEngine#setUserName(String)
     */
    public void setUserName(String userName) {
        m_GameEngine.setUserName(userName);
    }

    /**
     * Load the game user have saved before.<br>
     * Also set the game engine with the data saved in the file.
     *
     * @author Wendi Han -edited
     * @since 1.5
     * @see J_LoadGame
     * @see J_Tooltip
     */
    public void loadGame() {
        File saveFile;
        J_LoadGame loadGame = new J_LoadGame();
        saveFile = loadGame.loadGameFile(m_PrimaryStage);
        if (saveFile != null) {
            if (J_Debug.getInstance().isDebugActive()) {
                J_GameLogger.getInstance().info("Loading save file: " + saveFile.getName());
            }
            loadGame.loadSaveMessage(saveFile, m_GameEngine);
            J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
        } else {
            J_Tooltip tooltip = new J_Tooltip("Fail to load file", m_PrimaryStage);
            tooltip.show(Duration.seconds(2));
        }
    }

    /**
     * Save the game in the file player selected. Also save the game state(game engine).
     *
     * @since 1.5
     * @see J_SaveGame
     */
    public void saveGame() {
        J_SaveGame saveGame = new J_SaveGame();
        String saveText = saveGame.getSaveMessage(m_GameEngine);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SKB files (*.skb)", "*.skb");
        fileChooser.getExtensionFilters().add(extFilter);
        File save = fileChooser.showSaveDialog(m_PrimaryStage);
        if (save != null) {
            saveGame.saveGame(saveText, save);
        } else {
            J_Tooltip tooltip = new J_Tooltip("Fail to save game", m_PrimaryStage);
            tooltip.show(Duration.seconds(2));
        }
    }

    /**
     * Response to the request to get next level. It will set the completeness of
     * current level to false and move to the next level.
     *
     * @since 1.4
     * @see J_GameEngine
     * @see J_Tooltip
     */
    public void nextLevel() {
        int levelNumber = m_GameEngine.getCurrentLevel().getIndex();
        if (levelNumber != (m_GameEngine.getLevels().size() - 1)) {
            J_Level level = m_GameEngine.getNextLevel();
            m_GameEngine.getLevelRecords()
                    .put(m_GameEngine.getCurrentLevel().getName(), m_GameEngine.getLr());
            m_GameEngine.setLr(new J_LevelRecord(level.getName()
                    , new Date(), m_GameEngine.getUserName()));

            int index = m_GameEngine.getCurrentLevel().getIndex();
            m_GameEngine.getLevels().set(index,
                    m_GameEngine.getOriginalLevels().get(index));
            m_GameEngine.getMove().getMovesCount().put(m_GameEngine.getCurrentLevel().getName(),
                    0);
            m_GameEngine.setCurrentLevel(level);
            J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
        } else {
            String info = "This is the last level";
            J_Tooltip tooltip = new J_Tooltip(info, m_PrimaryStage);
            tooltip.show(Duration.seconds(1.5));
        }
    }

    /**
     * Response to the request to get last level. It will clear the record
     * of current level and move to the last level.
     *
     * @since 1.4
     * @see J_Tooltip
     * @see J_GameEngine
     */
    public void lastLevel() {
        int levelNumber = m_GameEngine.getCurrentLevel().getIndex();
        if (levelNumber != 0) {
            m_GameEngine.setIsFly(false);
            m_GameEngine.setCanUseFly(false);
            J_Level level = m_GameEngine.getLevels().get(levelNumber - 1);
            int index = m_GameEngine.getCurrentLevel().getIndex();
            m_GameEngine.getLevels().set(index,
                    m_GameEngine.getOriginalLevels().get(index));
            m_GameEngine.getMove().getMovesCount().put(m_GameEngine.getCurrentLevel().getName(),
                    0);
            m_GameEngine.setCurrentLevel(level);
            m_GameEngine.getMove().getMovesCount().put(m_GameEngine.getCurrentLevel().getName(),
                    0);
            m_GameEngine.setLr(new J_LevelRecord
                    (level.getName(), new Date(), m_GameEngine.getUserName()));
            J_ReloadGird.reloadGrid(m_GameEngine, m_GameGrid, m_PrimaryStage);
        } else {
            String info = "This is the first level";
            J_Tooltip tooltip = new J_Tooltip(info, m_PrimaryStage);
            tooltip.show(Duration.seconds(1.5));
        }
    }

    /**
     * Get the gameEngine of current game.
     *
     * @return J_GameEngine
     * @author Wendi Han
     * @since 1.3
     */
    public J_GameEngine getGameEngine() {
        return m_GameEngine;
    }

    /**
     * Get the game Grid of game map.
     *
     * @return GridPane - grid pane of game map
     * @author Wendi Han
     * @since 1.3
     */
    public GridPane getM_GameGrid() {
        return m_GameGrid;
    }
}