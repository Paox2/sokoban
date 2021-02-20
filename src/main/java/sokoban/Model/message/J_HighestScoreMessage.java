package sokoban.Model.message;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import sokoban.Controller.J_DialogController;
import sokoban.Model.game.J_GameEngine;
import sokoban.Model.level.J_LevelRecord;
import sokoban.Model.record.J_HighestDataSingleton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *  <p>
 *      This class implements the show method and get method of the interface I_MessageFactor.
 *  </p>
 *
 *  <ul>
 *      <li>It will show the highest of each level to the player using certain format.</li>
 *      <li>The score divides into time record and move record</li>
 *  </ul>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_MessageFactor
 */
public class J_HighestScoreMessage extends A_HighestMessageAdapter{
    private String messageType = "highestScore";

    /**
     * Implement the {@code getMessage()} method and return a list of message contains :
     * <ul>
     *     <li>Level name</li>
     *     <li>Level record</li>
     *     <li>User name of player created highest record</li>
     * </ul>
     *
     * @return A list of message
     * @see J_LevelRecord
     * @see StringBuilder
     * @see LinkedHashMap
     * @since 1.1
     */
    @Override
    public List<String> getMessage() {
        LinkedHashMap<String, J_LevelRecord> timeRecord = J_HighestDataSingleton.getInstance().getTimeRecord();
        LinkedHashMap<String, J_LevelRecord> moveRecord = J_HighestDataSingleton.getInstance().getMoveRecord();
        StringBuilder highestLevelName = new StringBuilder();
        StringBuilder highestLevelTime = new StringBuilder();
        StringBuilder highestLevelTimeUsername = new StringBuilder();
        StringBuilder highestLevelMove = new StringBuilder();
        StringBuilder highestLevelMoveUsername = new StringBuilder();
        StringBuilder divide = new StringBuilder();

        if (timeRecord == null && moveRecord == null) {
            highestLevelName.append("No Record");
            highestLevelTimeUsername.append("No Record");
            highestLevelMove.append("No Record");
            highestLevelMoveUsername.append("No Record");
            highestLevelTime.append("No Record");
            divide.append("|");
        } else {
            J_LevelRecord jr;
            for (String level : timeRecord.keySet()) {
                jr = timeRecord.get(level);
                highestLevelName.append(jr.getLevelName()).append("\n");
                highestLevelTime.append(jr.getTime() / 1000).append(".").append(jr.getTime() % 1000).append("s\n");
                highestLevelTimeUsername.append(jr.getUserName()).append("\n");
                divide.append("|\n");
            }
            for (String level : moveRecord.keySet()) {
                jr = moveRecord.get(level);
                highestLevelMove.append(jr.getMove()).append("\n");
                highestLevelMoveUsername.append(jr.getUserName()).append("\n");
            }
        }
        List<String> message = new ArrayList<>();
        message.add(highestLevelName.toString());
        message.add(highestLevelTime.toString());
        message.add(highestLevelTimeUsername.toString());
        message.add(highestLevelMove.toString());
        message.add(highestLevelMoveUsername.toString());
        message.add(divide.toString());
        return message;
    }

    /**
     * Show the message about highest score of each level and the score of total level(if have).
     *
     * @param gameEngine Game engine
     * @since 1.1
     */
    @Override
    public void showMessageWithPosition(LinkedHashMap<String,  Integer> list, J_GameEngine gameEngine, Stage stage) {
        String title = "Highest Score";
        List<String> info = new ArrayList<>();
        info.add(messageType);
        info.add(title);
        info.add(String.valueOf(list.get("x")));
        info.add(String.valueOf(list.get("y")));
        info.addAll(getMessage());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader
                    (new URL("file:src/main/java/sokoban/GUI/Dialog.fxml"));
            Parent roots = fxmlLoader.load();
            J_DialogController dialogController = fxmlLoader.getController();

            stage.setTitle(info.get(1));
            stage.setScene(new Scene(roots, 943, 448));

            KeyFrame start = new KeyFrame(Duration.ZERO,
                    new KeyValue(roots.translateXProperty(), -stage.getWidth()));
            KeyFrame end = new KeyFrame(Duration.seconds(1),
                    new KeyValue(roots.translateXProperty(), 0));
            Timeline slide = new Timeline(start, end);
            slide.play();

            dialogController.setHighestShow(gameEngine, info);
            gameEngine.setPrimaryStage(stage);
            stage.sizeToScene();
            stage.show();
            stage.setMaxHeight(stage.getHeight());
            stage.setMaxWidth(stage.getWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
