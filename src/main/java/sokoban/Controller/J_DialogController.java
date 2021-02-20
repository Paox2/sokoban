package sokoban.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import sokoban.GUI.J_GameView;
import sokoban.Model.game.J_GameEngine;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * The controller of the dialog page.<br>
 * It is mainly divided into three different page: Level up, Victory and highest record.<br>
 * Aim at different page, it contains different layout and buttons.
 *
 * @author Wendi HAN - Edited
 * @version 1.3
 */
public class J_DialogController implements Initializable {
    private J_GameEngine m_GameEngine;

    @FXML private ScrollPane scrollPane;
    @FXML private Label title1;
    @FXML private Label title2;
    @FXML private Label title3;
    @FXML private Label highestNameTitle;
    @FXML private Button nextLevel;

    @FXML private Label levelName;
    @FXML private Label divide1;
    @FXML private Label levelMove;
    @FXML private Label divide2;
    @FXML private Label levelTime;
    @FXML private Label divide3;
    @FXML private Label levelRecord;

    @FXML private Label highestLevelName;
    @FXML private Label highestLevelTime;
    @FXML private Label divide4;
    @FXML private Label highestLevelTimeUsername;
    @FXML private Label highestLevelMove;
    @FXML private Label divide5;
    @FXML private Label highestLevelMoveUsername;

    /**
     * Initialize the stage and set action.
     *
     * @author Wendi Han
     * @version 1.1
     * @param url url/location
     * @param resourceBundle set of resource
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        m_GameEngine = null;
    }

    /**
     * When player finish current level(not the last level), or game is over
     * it will use {@code gameEngine} to save the record of last game, and
     * use the {@code String} in the {@code info} to decide the message shown to the player
     *
     * @param gameEngine  Game engine contains game record
     * @param info  Information about level message
     * @since 1.1
     */
    public void setContinueGame(J_GameEngine gameEngine, List<String> info) {
        m_GameEngine = gameEngine;

        title1.setVisible(true);
        levelName.setVisible(true);
        levelTime.setVisible(true);
        divide1.setVisible(true);
        divide2.setVisible(true);
        divide3.setVisible(true);
        levelRecord.setVisible(true);
        levelMove.setVisible(true);
        title1.setText(info.get(1));
        levelName.setText(info.get(2));
        levelMove.setText(info.get(3));
        levelTime.setText(info.get(4));
        levelRecord.setText(info.get(5));
        divide1.setText(info.get(6));
        divide2.setText(info.get(6));
        divide3.setText(info.get(6));

        if(info.get(0).equals("victoryMessage"))
            nextLevel.setVisible(false);
    }

    /**
     * When player choose to see the highest scores,
     * it will use {@code gameEngine} to save the record of last game(if player back to menu before)
     * and use the {@code String} in the {@code info} to decide the message shown to the player
     *
     * @param gameEngine  Game engine contains game record
     * @param info  Information about level message
     * @since 1.3
     */
    public void setHighestShow(J_GameEngine gameEngine, List<String> info) {
        m_GameEngine = gameEngine;
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        stage.setY(Double.parseDouble(info.get(3)));
        stage.setX(Double.parseDouble(info.get(2)));

        highestLevelName.setText(info.get(4));
        highestLevelTime.setText(info.get(5));
        highestLevelTimeUsername.setText(info.get(6));
        highestLevelMove.setText(info.get(7));
        highestLevelMoveUsername.setText(info.get(8));
        divide4.setText(info.get(9));
        divide5.setText(info.get(9));

        title2.setVisible(true);
        title3.setVisible(true);
        highestLevelName.setVisible(true);
        highestLevelTime.setVisible(true);
        highestLevelTimeUsername.setVisible(true);
        highestLevelMove.setVisible(true);
        highestLevelMoveUsername.setVisible(true);
        divide4.setVisible(true);
        divide5.setVisible(true);
        highestNameTitle.setVisible(true);
        nextLevel.setVisible(false);
    }

    /**
     * Response to the request when user want to continue the game
     *
     * @see J_GameView#setContinueLastGame(Boolean)
     * @see J_GameView#start(Stage)
     * @since 1.1
     */
    @FXML
    void nextLevel() {
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        J_GameView game = new J_GameView(m_GameEngine);
        game.setContinueLastGame(true);
        try {
            stage.close();
            Stage newStage = new Stage();
            stage.setX(stage.getX());
            stage.setY(stage.getY());
            game.start(newStage);
            m_GameEngine.setPrimaryStage(newStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Response to the request when user want to back to the menu
     * @throws Exception - Detect the exception when J_MenuController get the roots of fxml file
     * @since 1.2
     */
    @FXML
    void backToMenu() throws Exception {
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        Parent roots;
        FXMLLoader fxmlLoader = new FXMLLoader(new URL("file:src/main/java/sokoban/GUI/Menu.fxml"));
        roots = fxmlLoader.load();

        if (m_GameEngine.getCurrentLevel() != null && !m_GameEngine.isGameComplete()) {
            J_MenuController controller = fxmlLoader.getController();
            controller.setBackToMenu(m_GameEngine);
        }

        stage.setTitle("Sokoban");
        stage.setScene(new Scene(roots, 943, 448));
        stage.sizeToScene();
        stage.show();
        stage.setMaxHeight(stage.getHeight());
        stage.setMaxWidth(stage.getWidth());
    }
}