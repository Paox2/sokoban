package sokoban.Model.message;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
 *      <li>It will show the victory message to the player using certain format.</li>
 *      <li>It will read and modify the highest score</li>
 *  </ul>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_MessageFactor
 * @see J_LevelRecord
 * @see StringBuilder
 */
public class J_VictoryMessage implements I_MessageFactor{
    private J_GameEngine m_GameEngine = null;
    private String messageType = "victoryMessage";
    private boolean allComplete;

    /**
     * Implement the {@code getMessage()} method and return a list of message contains :
     * <ul>
     *     <li>Level name</li>
     *     <li>Level record</li>
     * </ul>
     *
     * @return A list of message
     * @see J_LevelRecord
     * @see StringBuilder
     * @since 1.1
     */
    @Override
    public List<String> getMessage() {
        LinkedHashMap<String, J_LevelRecord> levelList = m_GameEngine.getLevelRecords();
        int totalMove = 0;
        long totalTime = 0;
        allComplete = true;
        for(String key : levelList.keySet()){
            if (!levelList.get(key).isLevelComplete()) {
                allComplete = false;
                continue;
            }
            totalMove+= levelList.get(key).getMove();
            totalTime += levelList.get(key).getTime();
        }

        Integer high = judgeTotalLevelAndRecord(totalMove, totalTime);

        StringBuilder name = new StringBuilder("Level Name\nTotal Level\n");
        StringBuilder divide1 = new StringBuilder("|\n|\n");
        StringBuilder move = new StringBuilder("Moves Count\n" + totalMove + "\n");
        StringBuilder time = new StringBuilder("Time Spend\n" + totalTime / 1000 + "." + totalTime % 1000 + " s\n");
        StringBuilder record = new StringBuilder("New Record\n");
        List<String> scoreList = new ArrayList<>();

        if (allComplete) {
            switch (high) {
                case 0 -> record.append("No Record\n");
                case 1 -> record.append("Less Time\n");
                case 2 -> record.append("Less Move\n");
                case 3 -> record.append("Less Time/Move\n");
            }
        } else {
            record.append("-\n");
        }

        for (String level : levelList.keySet()) {
            name.append(levelList.get(level).getLevelName()).append("\n");
            divide1.append("|\n");
            if (levelList.get(level).isLevelComplete()) {
                move.append(levelList.get(level).getMove()).append("\n");
                time.append(levelList.get(level).getTime() / 1000).append(".").
                        append(levelList.get(level).getTime() % 1000).append("s\n");
                record.append(levelList.get(level).getNewRecord()).append("\n");
            } else {
                move.append("-\n");
                time.append("-\n");
                record.append("-\n");
            }
        }
        scoreList.add(name.toString());
        scoreList.add(move.toString());
        scoreList.add(time.toString());
        scoreList.add(record.toString());
        scoreList.add(divide1.toString());
        return scoreList;
    }

    /**
     * Judge if player creates a new record. There are four type of return value :
     * <ul>
     *     <li>0 represents no new record</li>
     *     <li>1 represents new time record</li>
     *     <li>2 represents new move record</li>
     *     <li>3 represents new time and move record</li>
     * </ul>
     *
     * @param totalMove - The moves count of all the level the player has been completed.
     * @return The type of new record if have, otherwise return 0
     * @see J_LevelRecord
     * @since 1.1
     */
    private Integer judgeTotalLevelAndRecord(int totalMove, long totalTime) {
        int i = 0;
        Integer high;
        J_LevelRecord lr = m_GameEngine.getLr();
        do{
            high = J_HighestDataSingleton.getInstance().judgeNewRecordSpecialLevel(lr);
            switch (high) {
                case 1 -> {
                    J_HighestDataSingleton.getInstance().setNewMoveRecord(lr.getLevelName(), lr);
                    lr.setNewRecord("Less Time");
                }
                case 2 -> {
                    J_HighestDataSingleton.getInstance().setNewTimeRecord(lr.getLevelName(), lr);
                    lr.setNewRecord("Less Move");
                }
                case 3 -> {
                    J_HighestDataSingleton.getInstance().setNewTimeRecord(lr.getLevelName(), lr);
                    J_HighestDataSingleton.getInstance().setNewMoveRecord(lr.getLevelName(), lr);
                    lr.setNewRecord("Less Time/Move");
                }
            }
            if (!allComplete)
                break;

            lr = new J_LevelRecord("Total", totalTime, totalMove, m_GameEngine.getUserName());
            i++;
        }while(i < 2);
        return high;
    }

    /**
     * Show the message about each level player have been played.
     *
     * @param gameEngine - Game engine
     * @since 1.1
     */
    @Override
    public void showMessage(J_GameEngine gameEngine, Stage stage) {
        m_GameEngine = gameEngine;
        List<String> info = new ArrayList<>();
        String title = "Game Over";
        List<String> message = getMessage();

        info.add(messageType);
        info.add(title);
        info.addAll(message);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader
                    (new URL("file:src/main/java/sokoban/GUI/Dialog.fxml"));
            Parent roots = fxmlLoader.load();
            J_DialogController dialogController = fxmlLoader.getController();
            stage.setTitle(info.get(1));
            stage.setScene(new Scene(roots, 943, 448));

            //m_GameEngine.setPrimaryStage(stage);
            dialogController.setContinueGame(gameEngine, info);
            stage.sizeToScene();
            stage.show();
            stage.setMaxHeight(stage.getHeight());
            stage.setMaxWidth(stage.getWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
