package sokoban.Model.level;

import sokoban.Model.log.J_GameLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *  <p>
 *      This class is used to create a list of level(includes name, map and position of diamond).
 *  </p>
 *
 * @author Wendi Han - edited
 * @version 1.1
 * @see J_Level
 * @see StringBuilder
 * @see J_GameLogger
 */
public class J_LevelSetting {
    private J_GameLogger m_Logger;

    /**
     * Constructor is used to initialize the logger.
     *
     * @since 1.1
     */
    public J_LevelSetting(){
        m_Logger = J_GameLogger.getInstance();
    }

    /**
     * Load the game file and return a list of level(contains name, map and position of diamond).
     *
     * @param input - The input stream of game file
     * @return {@code List<J_Level>} - A list of level(contains name, map and position of diamond)
     * @see J_Level
     * @see StringBuilder
     * @see J_GameLogger
     * @since 1.1
     */
    public List<J_Level> loadGameFile(InputStream input) {
        List<J_Level> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";

            while (true) {
                String line = reader.readLine();

                // Break the loop if EOF is reached
                if (line == null) {
                    if (rawLevel.size() != 0) {
                        J_Level parsedLevel = new J_Level(levelName, levelIndex++, rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (line.contains("MapSetName")) {
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (parsedFirstLevel) {
                        J_Level parsedLevel = new J_Level(levelName, levelIndex++, rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        parsedFirstLevel = true;
                    }

                    levelName = line.replace("LevelName: ", "");
                    continue;
                }

                line = line.trim();
                line = line.toUpperCase();
                // If the line contains at least 2 WALLS, add it to the list
                if (line.matches(".*W.*W.*")) {
                    rawLevel.add(line);
                }
            }
        } catch (IOException e) {
            m_Logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            m_Logger.severe("Cannot open the requested file: " + e);
        }

        return levels;
    }
}
