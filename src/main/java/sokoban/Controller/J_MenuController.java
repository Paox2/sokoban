package sokoban.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sokoban.GUI.J_GameView;
import sokoban.Model.ImageAnimation.J_ImageAnimation;
import sokoban.Model.game.J_GameEngine;
import sokoban.Model.game.J_Tooltip;
import sokoban.Model.gameEngine.music.J_MusicSingleton;
import sokoban.Model.message.A_HighestMessageAdapter;
import sokoban.Model.message.J_HighestScoreMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

/**
 * The controller response to the request from main page.<br>
 * It implements the function in main page.
 *
 * @author Wendi Han
 * @version 1.4
 * @see J_ImageAnimation
 * @see J_GameView#start(Stage)
 * @see J_HighestScoreMessage#showMessage(J_GameEngine, Stage)
 * @see Scene#getWindow()
 * @see J_MusicSingleton#toggleMusic()
 * @see J_MusicSingleton#setVolume(Double)
 * @see System#exit(int)
 * @see J_MusicSingleton
 * @see J_GameView#start(Stage)
 * @see J_GameController#setUserName(String)
 */
public class J_MenuController implements Initializable {
    private J_GameEngine m_GameEngine;

    @FXML private ScrollPane scrollPane;
    @FXML private AnchorPane anchorPane;
    @FXML private Button startGame;
    @FXML private Button continueGame;
    @FXML private Button newGame;
    @FXML private Slider volume;
    @FXML private TextField userName;
    @FXML private Button startButton;
    @FXML private Button cancelButton;
    @FXML private Button selectLevel;

    /**
     * Add the image animation in the anchor pane which is separated from the fxml code.
     *
     * @param url URL
     * @param resourceBundle Resource
     * @since 1.1
     * @see J_ImageAnimation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        m_GameEngine = new J_GameEngine();
        J_ImageAnimation ia = new J_ImageAnimation();
        Pane pane = ia.getSlideShow();
        AnchorPane.setTopAnchor(pane, 140.0);
        AnchorPane.setLeftAnchor(pane, 80.0);
        anchorPane.getChildren().add(pane);
    }

    /**
     * If user back to menu from game, save the record of last game and show
     * the continue game and new game button.
     *
     * @param m_GameEngine  Game engine from last game.
     * @since 1.1
     */
    public void setBackToMenu(J_GameEngine m_GameEngine) {
        startGame.setVisible(false);
        continueGame.setVisible(true);
        newGame.setVisible(true);
        selectLevel.setVisible(false);
        this.m_GameEngine = m_GameEngine;
    }

    /**
     * Response to the request to start game button to let user input username.
     *
     * @since 1.1
     */
    @FXML
    void startGameButton() {
        userName.setVisible(true);
        startButton.setVisible(true);
        cancelButton.setVisible(true);
        startGame.setVisible(false);
        selectLevel.setVisible(false);
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        new J_Tooltip("Make sure you have read the instruction\n"
            + "in the top left of main page", stage).show(Duration.seconds(2));
    }

    /**
     * Response to the request to cancel game.
     *
     * @since 1.1
     */
    @FXML
    void cancelButton() {
        userName.setVisible(false);
        startButton.setVisible(false);
        cancelButton.setVisible(false);
        startGame.setVisible(true);
        selectLevel.setVisible(true);
    }

    /**
     * Response to the request to continue the game.
     *
     * @since 1.2
     * @see J_GameView#start(Stage)
     */
    @FXML
    void continueGame(){
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.close();
        J_GameView game = new J_GameView(m_GameEngine);
        m_GameEngine.getLr().setStartTime(new Date());
        game.setContinueLastGame(true);
        try {
            game.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Response to the request to create new game and cancel the last game.
     *
     * @since 1.2
     */
    @FXML
    void newGame(){
        startGame.setVisible(true);
        continueGame.setVisible(false);
        newGame.setVisible(false);
        selectLevel.setVisible(true);
        m_GameEngine.setGameComplete(true);
    }

    /**
     * Response to the request to show the highest score.
     *
     * @since 1.1
     * @see J_HighestScoreMessage#showMessage(J_GameEngine, Stage)
     * @see Scene#getWindow()
     */
    @FXML
    void showHighest() {
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        LinkedHashMap<String, Integer> list = new LinkedHashMap<>();
        list.put("x", (int) stage.getX());
        list.put("y", (int) stage.getY());
        A_HighestMessageAdapter message = new J_HighestScoreMessage();
        message.showMessageWithPosition(list, m_GameEngine, stage);
    }

    /**
     * Response to the request to start or close the music.
     *
     * @since 1.1
     * @see J_MusicSingleton#toggleMusic()
     */
    @FXML
    void toggleMusic() {
        m_GameEngine.getMusic().toggleMusic();
    }

    /**
     * Response to the request to adjust the sound of music.
     *
     * @since 1.1
     * @see J_MusicSingleton#setVolume(Double)
     */
    @FXML
    void setVolume() {
        m_GameEngine.getMusic().setVolume(volume.getValue());
    }

    /**
     * Response to the request to close the game.
     *
     * @since 1.1
     * @see System#exit(int)
     */
    @FXML
    void closeGame() {
        System.exit(0);
    }

    /**
     * Response to the request to start a new  game.
     *
     * @author Wendi Han
     * @since 1.1
     * @see J_MusicSingleton
     * @see J_GameView#start(Stage)
     * @see J_GameController#setUserName(String)
     */
    @FXML
    void startButtonAction() {
        try {
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            J_GameView game = new J_GameView(null);
            stage.close();
            Stage newStage = new Stage();
            newStage.setX(stage.getX());
            newStage.setY(stage.getY());
            game.start(newStage);
            game.getGameController().setUserName(userName.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Limit number of words of username and set the position of caret.
     *
     * @since 1.3
     */
    @FXML
    void inputTextLimit() {
        if ( userName.getText().length() > 10 ) {
            userName.setText(userName.getText().substring(0,10));
            userName.positionCaret(10);
        }
    }


    /**
     * Move to the select page where player can select the level to play.
     *
     * @since 1.4
     * @see FXMLLoader
     * @see J_SelectPageController
     */
    @FXML
    void selectLevel() {
        try {
            Parent roots;
            roots = FXMLLoader.load(new URL("file:src/main/java/sokoban/GUI/SelectPage.fxml"));
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            stage.setTitle("Select level");
            stage.setScene(new Scene(roots, 943, 448));

            KeyFrame start = new KeyFrame(Duration.ZERO,
                    new KeyValue(roots.translateXProperty(), -stage.getWidth()));
            KeyFrame end = new KeyFrame(Duration.seconds(1),
                    new KeyValue(roots.translateXProperty(), 0));
            Timeline slide = new Timeline(start, end);
            slide.play();
            stage.sizeToScene();

            stage.show();
            stage.setMaxHeight(stage.getHeight());
            stage.setMaxWidth(stage.getWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
