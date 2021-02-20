package sokoban.Model.game;

import javafx.stage.FileChooser;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.level.J_Level;
import sokoban.Model.level.J_LevelRecord;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *  <p>
 *      This class contains the relative method to load game with user own map.
 *  </p>
 *
 * @author Wendi Han
 * @version 1.1
 * @see J_InitializeGame
 * @see J_GameEngine
 * @see FileChooser
 * @see InputStream
 * @see BufferedReader
 * @see J_LevelRecord
 * @see String#split(String)
 */
public class J_SaveGame {

    /**
     * Get the message saved in the file (includes map, game record).
     *
     * @param m_gameEngine  Game engine
     * @return String contains the state of current prograss of game.
     * @since 1.1
     * @see StringBuilder
     * @see LinkedHashMap
     * @see E_GameObject
     * @see J_Level
     * @see J_GameEngine
     */
    public String getSaveMessage(J_GameEngine m_gameEngine) {
        StringBuilder saveMessage = new StringBuilder("MapSetName : Sokoban\n");
        LinkedHashMap<String, J_LevelRecord> levelRecords = m_gameEngine.getLevelRecords();
        List<J_Level> levels = new ArrayList<>(m_gameEngine.getOriginalLevels());

        for (J_Level level : levels) {
            saveMessage.append("LevelName: ").append(level.getName()).append("\n");

            ArrayList<Point> pipelineTarget = new ArrayList<>(level.getPipelineTargetPoint());
            E_GameObject[][] gameObjects = level.getM_ObjectsGrid().getM_GameObjects();
            E_GameObject[][] diamondPosition = level.getM_DiamondsGrid().getM_GameObjects();

            for (int i = 0; i < gameObjects.length; i++) {
                for (int j = 0; j < gameObjects[0].length; j++) {
                    if(diamondPosition[i][j] == E_GameObject.DIAMOND) {
                        saveMessage.append("D");
                    } else if (pipelineTarget.contains(new Point(i,j))) {
                        saveMessage.append("T");
                    } else {
                        saveMessage.append(gameObjects[i][j].getCharSymbol());
                    }
                }
                saveMessage.append("\n");
            }
            saveMessage.append("\n\n");
        }

        m_gameEngine.getLr().setEndTime(new Date());
        m_gameEngine.getLr().setM_Time(m_gameEngine.getLr().getTimeDiff());
        m_gameEngine.getLr().setStartTime(new Date());
        m_gameEngine.getLr().setMove(m_gameEngine.getMove().getMovesCount()
                .get(m_gameEngine.getCurrentLevel().getName()));
        levelRecords.put(m_gameEngine.getCurrentLevel().getName()
                , m_gameEngine.getLr());

        for (J_LevelRecord lr : levelRecords.values()) {
            saveMessage.append("LevelRecords:")
                    .append(lr.getLevelName()).append(":")
                    .append(lr.getStartTime().getTime()).append(":")
                    .append(lr.getEndTime() == null?"null" : lr.getEndTime().getTime()).append(":")
                    .append(lr.getUserName()).append(":")
                    .append(lr.getMove()).append(":")
                    .append(lr.getNewRecord()).append(":")
                    .append(lr.isLevelComplete()).append(":")
                    .append(lr.getTime()).append("\n");
        }

        return saveMessage.toString();
    }

    /**
     * Save the {@code saveText} (game map and game record) into the file {@code save}.
     *
     * @param saveText  The text contains game map and game record used to save in the file
     * @param save  The file user want to save the message in.
     * @since 1.1
     * @see PrintWriter
     */
    public void saveGame(String saveText, File save) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(save);
            writer.println(saveText);
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
    }
}
