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
import java.util.*;
import java.util.List;

/**
 *  <p>
 *      This class implements the show method and get method of the interface I_MessageFactor.
 *  </p>
 *
 *  <ul>
 *      <li>It will show the level up message to the player using certain format.</li>
 *      <li>It will read and modify the highest score</li>
 *  </ul>
 *
 * @author Wendi Han
 * @version 1.1
 * @see I_MessageFactor
 */
public class J_LevelUpMessage implements I_MessageFactor {
    private String m_MessageType = "levelUpMessage";
    private J_GameEngine m_GameEngine;

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
        List<String> list = new ArrayList<>();
        int totalMove = 0;
        long totalTime = 0;
        for(String key : levelList.keySet()){
            if (!levelList.get(key).isLevelComplete()) {
                continue;
            }
            totalMove+= levelList.get(key).getMove();
            totalTime += levelList.get(key).getTime();
        }

        StringBuilder name = new StringBuilder("Level Name\nTotal Level\n");
        StringBuilder divide1 = new StringBuilder("|\n|\n");
        StringBuilder move = new StringBuilder("Moves Count\n" + totalMove + "\n");
        StringBuilder time = new StringBuilder("Time Spend\n" + totalTime / 1000 + "." + totalTime % 1000 + " s\n");
        StringBuilder record = new StringBuilder("New Record\n-\n");

        List<Map.Entry<String, J_LevelRecord>> sortMovesCountList = new ArrayList<>(levelList.entrySet());
        ValueComparator vc= new ValueComparator();
        Collections.sort(sortMovesCountList,vc);

        for (Map.Entry<String, J_LevelRecord> stringJ_levelRecordEntry : sortMovesCountList) {
            if (!stringJ_levelRecordEntry.getValue().isLevelComplete()) {
                name.append(stringJ_levelRecordEntry.getValue().getLevelName()).append("\n");
                divide1.append("|\n");
                move.append("-\n");
                time.append("-\n");
                record.append("-\n");
                continue;
            }
            name.append(stringJ_levelRecordEntry.getValue().getLevelName()).append("\n");
            divide1.append("|\n");
            move.append(stringJ_levelRecordEntry.getValue().getMove()).append("\n");
            time.append(stringJ_levelRecordEntry.getValue().getTime()/1000).append(".").
                    append(stringJ_levelRecordEntry.getValue().getTime()%1000).append("s\n");
            if (stringJ_levelRecordEntry.getValue().getLevelName().equals(m_GameEngine.getLastLevelName())) {
                Integer recordType;
                if ((recordType = J_HighestDataSingleton.getInstance().judgeNewRecordSpecialLevel(stringJ_levelRecordEntry.getValue())) > 0){
                    String levelName = m_GameEngine.getLastLevelName();
                    J_LevelRecord lr = stringJ_levelRecordEntry.getValue();
                    switch (recordType) {
                        case 1 -> {
                            m_GameEngine.getLr().setNewRecord("Less Time");
                            record.append("Less Time\n");
                            J_HighestDataSingleton.getInstance().setNewTimeRecord(levelName
                                    , new J_LevelRecord(levelName, lr.getTime(),lr.getUserName()));
                        }
                        case 2 -> {
                            m_GameEngine.getLr().setNewRecord("Less Move");
                            record.append("Less Move\n");
                            J_HighestDataSingleton.getInstance().setNewMoveRecord(levelName
                                    , new J_LevelRecord(levelName, lr.getMove(),lr.getUserName()));
                        }
                        case 3 -> {
                            m_GameEngine.getLr().setNewRecord("Less Time/Move");
                            record.append("Less Time/Move\n");
                            J_HighestDataSingleton.getInstance().setNewTimeRecord(levelName
                                    , new J_LevelRecord(levelName, lr.getTime(),lr.getUserName()));
                            J_HighestDataSingleton.getInstance().setNewMoveRecord(levelName
                                    , new J_LevelRecord(levelName, lr.getMove(),lr.getUserName()));
                        }
                    }
                } else {
                    record.append(stringJ_levelRecordEntry.getValue().getNewRecord()).append("\n");
                }
            } else {
                record.append(stringJ_levelRecordEntry.getValue().getNewRecord()).append("\n");
            }
        }
        list.add(name.toString());
        list.add(move.toString());
        list.add(time.toString());
        list.add(record.toString());
        list.add(divide1.toString());
        return list;
    }

    /**
     * Show the message about each level player have been played.
     *
     * @param gameEngine  Game engine
     * @since 1.1
     */
    @Override
    public void showMessage(J_GameEngine gameEngine, Stage stage) {
        m_GameEngine = gameEngine;
        String title = "Level Up";
        List<String> info = new ArrayList<>();
        List<String> message = getMessage();
        info.add(m_MessageType);
        info.add(title);
        info.addAll(message);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader
                    (new URL("file:src/main/java/sokoban/GUI/Dialog.fxml"));
            Parent roots = fxmlLoader.load();
            J_DialogController dialogController = fxmlLoader.getController();

            stage.setTitle(info.get(1));
            stage.setScene(new Scene(roots, 943, 448));
            //m_GameEngine.setPrimaryStage(stage)

            dialogController.setContinueGame(gameEngine, info);
            stage.sizeToScene();
            stage.show();
            stage.setMaxHeight(stage.getHeight());
            stage.setMaxWidth(stage.getWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implement a self-define comparator of list, the level with less moves come first.
     *
     * @see Comparable
     * @since 1.1
     */
    private static class ValueComparator implements Comparator<Map.Entry<String,J_LevelRecord>> {
        @Override
        public int compare(Map.Entry<String, J_LevelRecord> o1, Map.Entry<String, J_LevelRecord> o2) {
            return o1.getValue().getMove() - o2.getValue().getMove();
        }
    }
}
