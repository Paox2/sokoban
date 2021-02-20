package sokoban.Model.game;

import javafx.stage.Stage;
import sokoban.Model.gameEngine.debug.J_Debug;
import sokoban.Model.gameEngine.move.J_Movement;
import sokoban.Model.gameEngine.music.J_MusicSingleton;
import sokoban.Model.level.J_Level;
import sokoban.Model.level.J_LevelRecord;
import sokoban.Model.level.J_LevelSetting;
import sokoban.Model.log.J_GameLogger;
import sokoban.Model.gameEngine.object.J_Color;
import sokoban.Model.message.I_MessageFactor;
import sokoban.Model.message.J_LevelUpMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * The core of game, a combination of different part of game.<br>
 * It will be initialized when the game is first time loading, and then it will use the
 * same instance.<br>
 * Most part of code of game engine is the getter and setter of different instances of game engine.
 *
 * @author Wendi Han
 * @version 1.4
 * @see J_LevelRecord
 * @see J_Movement
 * @see sokoban.Model.gameEngine.object.E_GameObject
 * @see J_Debug
 * @see J_MusicSingleton
 */
public class J_GameEngine {

    private final String GAME_NAME = "Sokoban";
    private J_GameLogger logger;
    private J_Debug debug;
    private J_Level currentLevel;
    private List<J_Level> levels;
    private List<J_Level> originalLevels;
    private boolean gameComplete = false;
    private J_MusicSingleton music;
    private J_Movement move;
    private J_LevelSetting levelSetting;
    private J_Color color;
    private Stage primaryStage;
    private String userName;
    private Date startTime;
    private LinkedHashMap<String, J_LevelRecord> levelRecords;
    private J_LevelRecord lr;
    private Date endTime;
    private String lastLevelName;
    private byte[] in;
    private boolean isFly = false;
    private boolean canUseFly = false;
    private boolean stop = false;

    /**
     * Constructor is used when user want to back to last game and there is no need to initialize other things.
     *
     * @since 1.4
     */
    public J_GameEngine() {
        music = J_MusicSingleton.getInstance();
    }

    /**
     * Constructor is used when only levels need to be initialized.
     *
     * @param levels A list of level
     * @since 1.4
     */
    public J_GameEngine(List<J_Level> levels) {
        this.levels = levels;
        debug = new J_Debug();
    }

    /**
     * This constructor use to initialize the game, include the game map, level, start time and other parts.
     *
     * @param input        The input file (contains the map)
     * @param primaryStage the stage show to the user
     * @since 1.1
     */
    public J_GameEngine(InputStream input, Stage primaryStage) {
        try {
            levelRecords = new LinkedHashMap<>();
            this.primaryStage = primaryStage;
            logger = J_GameLogger.getInstance();
            levelSetting = new J_LevelSetting();
            in = input.readAllBytes();
            InputStream IN1 = new ByteArrayInputStream(in);
            InputStream IN2 = new ByteArrayInputStream(in);
            levels = levelSetting.loadGameFile(IN1);
            originalLevels = levelSetting.loadGameFile(IN2);
            currentLevel = null;
            currentLevel = getNextLevel();
            debug = new J_Debug();
            music = J_MusicSingleton.getInstance();
            color = new J_Color();
            move = new J_Movement(this);
            userName = "unknown";
            lastLevelName = "";
            startTime = new Date();
            lr = new J_LevelRecord(currentLevel.getName(), new Date(), userName);
        } catch (IOException x) {
            logger.warning("Cannot create logger.");
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default save file: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Return the next level of current level, and if it has been the last level return null
     * and set {@code gameComplete} to {@code true}.
     *
     * @return J_Level - Next level if have, otherwise null
     * @since 1.1
     */
    public J_Level getNextLevel() {
        isFly = false;
        canUseFly = false;
        int currentLevelIndex;
        if (currentLevel == null) {
            currentLevelIndex = 0;
        } else {
            currentLevelIndex = currentLevel.getIndex() + 1;
        }

        while (currentLevelIndex < levels.size()) {
            J_Level next = levels.get(currentLevelIndex);
            if (next.getM_NumberOfDiamonds() == 0) {
                currentLevelIndex++;
                continue;
            }
            return levels.get(currentLevelIndex);
        }

        gameComplete = true;
        return null;
    }

    /**
     * Once player finish one level, it will set the level up and check if game is over
     * (player has finished the last game).
     *
     * @author Wendi Han
     * @see J_LevelRecord
     * @see J_Level#getName()
     * @since 1.1
     */
    public void setLevelUp() {
        String name = lr.getLevelName();
        lr.setEndTime(new Date());
        lr.setMove(move.getMovesCount().get(name));
        lr.setComplete(true);
        levelRecords.put(name, lr);
        int index = currentLevel.getIndex();
        J_Level level = originalLevels.get(index);
        lastLevelName = currentLevel.getName();
        levels.set(index, level);
        lr.setM_Time(lr.getTimeDiff());

        new Thread(() -> {
            InputStream is = new ByteArrayInputStream(in);
            originalLevels = levelSetting.loadGameFile(is);
        }).start();
        currentLevel = getNextLevel();
        if (currentLevel != null) {
            I_MessageFactor mf = new J_LevelUpMessage();
            Stage stage = new Stage();
            stage.setX(primaryStage.getX());
            stage.setY(primaryStage.getY());
            primaryStage.close();
            mf.showMessage(this, stage);
            lr = new J_LevelRecord(currentLevel.getName(), new Date(), userName);
        }
    }

    /**
     * If user choose to reset the level, controller will use this function to reset the current live.
     *
     * @author Wendi Han
     * @since 1.2
     */
    public void resetLevel() {
        isFly = false;
        canUseFly = false;
        int index = currentLevel.getIndex();
        currentLevel = originalLevels.get(index);
        new Thread(() -> {
            InputStream is = new ByteArrayInputStream(in);
            originalLevels = levelSetting.loadGameFile(is);
        }).start();
        move.getMovesCount().put(currentLevel.getName(), 0);
        lr.setStartTime(new Date());
        lr.setTimeTo(0);
    }


    /* Following part is the getter and setter of the elements in this class */


    /**
     * Return {@code true} if game has been completed.
     *
     * @return {@code true} - If game has been completed.
     */
    public boolean isGameComplete() {
        return gameComplete;
    }

    /**
     * Return {@code music} to become accessible outside the class.
     *
     * @return {@code music} - Music element
     */
    public J_MusicSingleton getMusic() {
        return music;
    }

    /**
     * Return {@code debug} to become accessible outside the class.
     *
     * @return {@code debug} - Debug element
     */
    public J_Debug getDebug() {
        return debug;
    }

    /**
     * Return {@code move} to become accessible outside the class.
     *
     * @return {@code debug} - Contains the count of move of each level, and current state
     */
    public J_Movement getMove() {
        return move;
    }

    /**
     * Return {@code levelSetting} to become accessible outside the class.
     *
     * @return {@code levelSetting} - Contains the method to get a list of level(map,object)
     */
    public J_LevelSetting getLevelSetting() {
        return levelSetting;
    }

    /**
     * Return {@code levels} to become accessible outside the class.
     *
     * @return {@code levels} - A list of level(map, object)
     */
    public List<J_Level> getLevels() {
        return levels;
    }

    /**
     * Return {@code currentLevel} to become accessible outside the class.
     *
     * @return {@code currentLevel} - Current level
     */
    public J_Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Return {@code logger} to become accessible outside the class.
     *
     * @return {@code logger} - Logger element
     */
    public J_GameLogger getLogger() {
        return logger;
    }

    /**
     * Return {@code GAME_NAME} to become accessible outside the class.
     *
     * @return {@code GAME_NAME} - The name of game
     */
    public String getGameName() {
        return GAME_NAME;
    }

    /**
     * Return {@code color} to become accessible outside the class.
     *
     * @return {@code color} - Contain the color of each object
     */
    public J_Color getColor() {
        return color;
    }

    /**
     * Return {@code userName} to become accessible outside the class.
     *
     * @return {@code userName} - The name of user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Return {@code startTime} to become accessible outside the class.
     *
     * @return {@code startTime} - The time of game start.
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Return {@code primaryStage} to become accessible outside the class.
     *
     * @return {@code primaryStage} - Current stage show to the user
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Return {@code levelRecords} to become accessible outside the class.
     *
     * @return {@code levelRecords} - A list of list's record
     */
    public LinkedHashMap<String, J_LevelRecord> getLevelRecords() {
        return levelRecords;
    }

    /**
     * Return {@code endTime} to become accessible outside the class.
     *
     * @return {@code endTime} - The time of user finish the last level
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Return {@code lastLevelName} to become accessible outside the class.
     *
     * @return {@code lastLevelName} - The name of level before current level
     */
    public String getLastLevelName() {
        return lastLevelName;
    }

    /**
     * Return {@code lr} to become accessible outside the class.
     *
     * @return J_LevelRecord - The record of level
     */
    public J_LevelRecord getLr() {
        return lr;
    }

    /**
     * Return {@code originalLevels} to become accessible outside the class.
     *
     * @return originalLevels - A list of original level
     */
    public List<J_Level> getOriginalLevels() {
        List<J_Level> origin = originalLevels;

        new Thread(() -> {
            InputStream is = new ByteArrayInputStream(in);
            originalLevels = levelSetting.loadGameFile(is);
        }).start();

        return origin;
    }

    /**
     * Set {@code userName}.
     *
     * @param userName The name of user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Set {@code currentLevel}.
     *
     * @param currentLevel Current level
     */
    public void setCurrentLevel(J_Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    /**
     * Set {@code levels} and use when player restart the last game.
     *
     * @param levels A list of level(map, object)
     */
    public void setLevels(List<J_Level> levels) {
        this.levels = levels;
    }

    /**
     * Set {@code music} to make sure different page using the same music element.
     *
     * @param music The music element
     */
    public void setMusic(J_MusicSingleton music) {
        this.music = music;
    }

    /**
     * Set {@code move}.
     *
     * @param move The count of move of each level and current state.
     */
    public void setMove(J_Movement move) {
        this.move = move;
    }

    /**
     * Set {@code levelSetting}.
     *
     * @param levelSetting The initialize of a list of level
     * @deprecated
     */
    public void setLevelSetting(J_LevelSetting levelSetting) {
        this.levelSetting = levelSetting;
    }

    /**
     * Set {@code color} which contains the color of each object in the map.
     *
     * @param color Contains the color of each object in the map
     */
    public void setColor(J_Color color) {
        this.color = color;
    }

    /**
     * Set {@code startTime}.
     *
     * @param startTime The time of game start.
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * Set {@code endTime}.
     *
     * @param endTime The time when game finish
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Set {@code primaryStage} which is used to contain the element and show to the user.
     *
     * @param primaryStage The stage show to the user
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Set {@code lr} which contains the color of each object in the map.
     *
     * @param lr The record of level.
     */
    public void setLr(J_LevelRecord lr) {
        this.lr = lr;
    }

    /**
     * Set {@code levelRecords} which contains the color of each object in the map.
     *
     * @param levelRecords Contains the record of each level.
     */
    public void setLevelRecords(LinkedHashMap<String, J_LevelRecord> levelRecords) {
        this.levelRecords = levelRecords;
    }

    /**
     * Set {@code originalLevels} which contains original levels.
     *
     * @param originalLevels Contains all original levels.
     */
    public void setOriginalLevels(List<J_Level> originalLevels) {
        this.originalLevels = originalLevels;
    }


    /**
     * Check if user is fly.
     *
     * @return true if user is flying
     */
    public boolean isIsFly() {
        return isFly;
    }

    /**
     * Set {@code ifFly} which is used to judge if user is player.
     *
     * @param isFly Set the state of player : fly or not.
     */
    public void setIsFly(boolean isFly) {
        this.isFly = isFly;
    }

    /**
     * Check if user can fly.
     *
     * @return true if user can fly
     */
    public boolean isCanUseFly() {
        return canUseFly;
    }

    /**
     * Set {@code canUseFly} which is used to judge if user can fly.
     *
     * @param canUseFly Give user the ability of fly or do not give.
     */
    public void setCanUseFly(boolean canUseFly) {
        this.canUseFly = canUseFly;
    }

    /**
     * Set {@code true } if game complete or have a new game.
     *
     * @param b Game complete or not
     */
    public void setGameComplete(boolean b) {
        gameComplete = b;
    }

    /**
     * Set {@code in} which is the input stream of input file.
     *
     * @param in The input stream of input file
     */
    public void setIn(byte[] in) {
        this.in = in;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isStop() {
        return stop;
    }
}