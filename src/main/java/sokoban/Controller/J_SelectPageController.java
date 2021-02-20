package sokoban.Controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sokoban.GUI.J_GameView;
import sokoban.Model.game.J_ReloadGird;
import sokoban.Model.gameEngine.object.J_ImageSingleton;
import sokoban.Model.level.J_Level;
import sokoban.Model.level.J_LevelRecord;
import sokoban.Model.level.J_LevelSetting;

import java.io.InputStream;
import java.net.URL;
import java.util.*;


/**
 * <p>
 *      Give player the permission to choose the level to start. All the levels before the level player choosing
 *      will be seen as unfinished. After finishing the level they have been chosen, they can choose to continue
 *      to play next level or back to menu.
 *      Also, the level record will be compared with highest level.
 * </p>
 *
 * @author Wendi Han
 * @version 1.1
 * @see Initializable
 * @see Application
 * @see J_ImageSingleton
 * @see J_GameController
 * @see sokoban.Model.game.J_GameEngine
 */
public class J_SelectPageController implements Initializable {
    @FXML private Label levelName;
    @FXML private TextField userName;
    @FXML private ImageView levelImage;
    @FXML private ImageView leftButton;
    @FXML private ImageView rightButton;
    @FXML private ScrollPane scrollPane;
    private List<J_Level> levelList;
    private J_Level currentShow;

    /**
     * Initialize the first scene show to the player with level name and image. Also, set the image of left
     * and right button
     *
     * @param url  The root of file
     * @param resourceBundle  Resource bundle
     * @since 1.1
     * @see InputStream
     * @see J_ImageSingleton
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftButton.setImage(J_ImageSingleton.getInstance().makeTransparent(new Image("LEFT.png")));
        leftButton.setPreserveRatio(true);
        rightButton.setImage(J_ImageSingleton.getInstance().makeTransparent(new Image("RIGHT.png")));
        rightButton.setPreserveRatio(true);
        InputStream input = getClass().getClassLoader().getResourceAsStream("SampleGame.skb");
        J_LevelSetting levelSetting = new J_LevelSetting();
        levelList = levelSetting.loadGameFile(input);
        currentShow = levelList.get(0);
        levelName.setText(currentShow.getName());
        levelImage.setImage(J_ImageSingleton.getInstance().getList().get(currentShow.getName()));
    }

    /**
     * Response to the request to start game. It will set the initialize game engine
     * (include {@code currentLevel} and {@code level record}). The player can play the level they choose.
     *
     * @since 1.1
     * @see sokoban.Model.game.J_GameEngine
     * @see J_GameController
     * @see J_LevelRecord
     * @see J_ReloadGird
     */
    @FXML
    void startGame() {
        try {
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            stage.close();
            J_GameView gameView = new J_GameView(null);
            gameView.start(new Stage());
            J_GameController game = gameView.getGameController();
            game.setUserName(userName.getText());
            game.getGameEngine().setCurrentLevel(currentShow);
            new Thread(() -> {
                LinkedHashMap<String, J_LevelRecord> recordList = new LinkedHashMap<>();
                for (J_Level level : levelList) {
                    if (level.getName().equals(currentShow.getName())) {
                        J_LevelRecord lr = new J_LevelRecord(currentShow.getName()
                                , new Date(), userName.getText());
                        game.getGameEngine().setLr(lr);
                        break;
                    }
                    J_LevelRecord lr = new J_LevelRecord(level.getName()
                            , new Date(), userName.getText());
                    recordList.put(lr.getLevelName(), lr);
                }
                game.getGameEngine().setLevelRecords(recordList);
            }).start();

            J_ReloadGird.reloadGrid(game.getGameEngine(), game.getM_GameGrid(),
                    stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Response to the request to back to menu.
     *
     * @since 1.1
     * @see J_MenuController
     */
    @FXML
    void backToMenu() {
        try {
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            Parent roots;
            roots = FXMLLoader.load(new URL("file:src/main/java/sokoban/GUI/Menu.fxml"));

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
     * Response to the request of scanning the last level. If current level is the first level,
     * it will show the level at the end of the level list.
     *
     * @since 1.1
     */
    @FXML
    void leftLevel() {
        int currentShowIndex = currentShow.getIndex();
        if (currentShowIndex == 0) {
            currentShowIndex = levelList.size() - 1;
        } else {
            currentShowIndex--;
        }
        currentShow = levelList.get(currentShowIndex);
        levelName.setText(currentShow.getName());
        levelImage.setImage(J_ImageSingleton.getInstance().getList().get(currentShow.getName()));
    }

    /**
     * Response to the request of scanning the next level. If current level is the last level,
     *      * it will show the level at the start of the level list.
     *
     * @since 1.1
     */
    @FXML
    void rightLevel() {
        int currentShowIndex = currentShow.getIndex();
        if (currentShowIndex == levelList.size() - 1) {
            currentShowIndex = 0;
        } else {
            currentShowIndex++;
        }
        currentShow = levelList.get(currentShowIndex);
        levelName.setText(currentShow.getName());
        levelImage.setImage(J_ImageSingleton.getInstance().getList().get(currentShow.getName()));
    }
}
