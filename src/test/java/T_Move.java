import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sokoban.Model.game.J_GameEngine;
import sokoban.Model.gameEngine.move.J_Movement;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.level.J_Level;
import sokoban.Model.level.J_LevelRecord;
import sokoban.Model.level.J_LevelSetting;
import sokoban.Model.gameEngine.object.J_Color;
import sokoban.Model.game.J_Tooltip;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class T_Move {
    private static J_GameEngine gameEngine;
    private static List<J_Level> list;
    private static List<J_Level> olist;
    private static J_Movement move;
    private static InputStream in;
    private static InputStream in2;
    private String name;
    private static J_Level level;
    private static J_LevelRecord jr;
    private int moves;
    private static LinkedHashMap<String, J_LevelRecord> levelRecords;
    private boolean isComplete;
    private E_GameObject object;
    private J_LevelSetting levelSetting;

    @BeforeEach
    void setup() {
        level = null;
        levelSetting = new J_LevelSetting();
        in = T_Map.class.getClassLoader().getResourceAsStream("debugGame.skb");
        in2 = T_Map.class.getClassLoader().getResourceAsStream("debugGame.skb");
        list = levelSetting.loadGameFile(in);
        olist = levelSetting.loadGameFile(in2);
        gameEngine = new J_GameEngine(list);
        gameEngine.setOriginalLevels(olist);
        gameEngine.setColor(new J_Color());
        move = new J_Movement(gameEngine);
        gameEngine.setMove(move);
        level = gameEngine.getNextLevel();
        gameEngine.setCurrentLevel(level);
        jr = new J_LevelRecord(level.getName(), new Date(), "unknown");
        gameEngine.setLr(jr);
        levelRecords = new LinkedHashMap<>();
        gameEngine.setLevelRecords(levelRecords);
    }

    @AfterEach
    void finialize() {
        level = null;
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.clear();
        gameEngine.setCurrentLevel(null);
        move = null;
        jr = null;
        levelRecords = null;
    }

    @Test
    public void testStart() {
        name = gameEngine.getCurrentLevel().getName();
        assertEquals("Just this level1", name);
    }

    @Test
    public void testMoveCount() {
        move.handleKey(KeyCode.LEFT);
        move.handleKey(KeyCode.LEFT);
        move.handleKey(KeyCode.LEFT);
        move.handleKey(KeyCode.LEFT);
        moves = move.getMovesCount().get(gameEngine.getCurrentLevel().getName());
        assertEquals(4,moves);
    }

    @Test
    public void testLevelUp() {
        level = gameEngine.getNextLevel();
        gameEngine.setCurrentLevel(level);
        name = gameEngine.getCurrentLevel().getName();
        assertEquals("Just this level2", name);

        level = gameEngine.getNextLevel();
        gameEngine.setCurrentLevel(level);
        gameEngine.getNextLevel();
        isComplete = gameEngine.isGameComplete();
        assertEquals(true, isComplete);
    }

    @Test
    public void testOnlyPartOfCrateOnDiamond() {
        level = gameEngine.getNextLevel();
        gameEngine.setCurrentLevel(level);
        move.handleKey(KeyCode.LEFT);
        move.handleKey(KeyCode.LEFT);
        move.handleKey(KeyCode.LEFT);
        move.handleKey(KeyCode.LEFT);
        move.handleKey(KeyCode.LEFT);
        isComplete = gameEngine.getCurrentLevel().isComplete();
        assertEquals(false, isComplete);
    }

    @Test
    public void testResetLevel() {
        move.handleKey(KeyCode.LEFT);
        move.handleKey(KeyCode.LEFT);
        assertEquals(2, move.getMovesCount().get(gameEngine.getCurrentLevel().getName()));
        try {
            sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameEngine.resetLevel();
        assertEquals(0, move.getMovesCount().get(gameEngine.getCurrentLevel().getName()));
        assertTrue(Math.abs((new Date()).getTime() - jr.getStartTime().getTime()) < 1000);
    }

    @Test
    public void testUndo() {
        move.handleKey(KeyCode.LEFT);
        move.handleKey(KeyCode.LEFT);
        object = level.getObjectAt(new Point(2, 11));
        assertEquals(2, move.getMovesCount().get(gameEngine.getCurrentLevel().getName()));
        assertEquals(E_GameObject.KEEPER, object);
        move.undoMove();
        assertEquals(1, move.getMovesCount().get(gameEngine.getCurrentLevel().getName()));
        object = level.getObjectAt(new Point(2, 12));
        assertEquals(E_GameObject.KEEPER, object);
    }
}