package sokoban.Model.game;

import javafx.scene.effect.Light;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.level.J_Level;
import sokoban.Model.level.J_LevelRecord;

import java.awt.*;
import java.io.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *  <p>
 *      This class is responsible to load the game file and write the message into game engine
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
public class J_LoadGame {


    /**
     * Show the interface to the user to select the place to save file and return the file.
     *
     * @param primaryStage  The interface show to the user to select the place to save file
     * @return saveFile  The file player choose to save
     * @since 1.1
     * @see FileChooser
     */
    public File loadGameFile(Stage primaryStage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        File saveFile = fileChooser.showOpenDialog(primaryStage);
        try {
            InputStream input = new FileInputStream(saveFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            if(!line.contains("Sokoban"))
                return null;
        } catch (Exception e) {
            return null;
        }
        return saveFile;
    }


    /**
     * Write the message into the file.
     *
     * @param saveFile  The file player choose to save
     * @param m_gameEngine  Game engine
     * @since 1.1
     * @see InputStream
     * @see BufferedReader
     * @see J_LevelRecord
     * @see String#split(String)
     */
    public void loadSaveMessage(File saveFile, J_GameEngine m_gameEngine) {
        try {
            InputStream input = new FileInputStream(saveFile);
            byte[] in = input.readAllBytes();

            input = new ByteArrayInputStream(in);
            m_gameEngine.setLevels(m_gameEngine.getLevelSetting()
                    .loadGameFile(new ByteArrayInputStream(in)));;

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            LinkedHashMap<String, J_LevelRecord> levelRecords = new LinkedHashMap<>();
            String currentLevelName = "";
            String  userName = "";
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else if (!line.contains("LevelRecords")){
                    continue;
                }

                String[] split = line.split(":");

                userName = split[4];
                J_LevelRecord jr = new J_LevelRecord(split[1], Long.parseLong(split[8]), split[4]);
                int move = Integer.parseInt(split[5]);
                if (!split[3].equals("null")) {
                    jr.setEndTime(new Date(Long.parseLong(split[3])));
                    jr.setMove(move);
                    jr.setNewRecord(split[6]);
                    jr.setComplete(split[7].equals("true"));
                }
                jr.setStartTime(new Date());

                m_gameEngine.getMove().getMovesCount().put(split[1], move);
                levelRecords.put(jr.getLevelName(), jr);
                currentLevelName = jr.getLevelName();
            }

            J_LevelRecord lr = levelRecords.remove(currentLevelName);
            m_gameEngine.setLevelRecords(levelRecords);
            m_gameEngine.setLr(lr);
            m_gameEngine.setUserName(userName);

            J_Level currentLevel = null;
            for (J_Level level : m_gameEngine.getLevels()) {
                if (level.getName().equals(currentLevelName)) {
                    currentLevel = level;
                    break;
                }
            }
            m_gameEngine.setCurrentLevel(currentLevel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
