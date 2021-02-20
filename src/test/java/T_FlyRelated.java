import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sokoban.Model.game.J_GameEngine;
import sokoban.Model.gameEngine.move.J_Movement;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.gameEngine.object.J_Color;
import sokoban.Model.level.J_Level;
import sokoban.Model.level.J_LevelRecord;
import sokoban.Model.level.J_LevelSetting;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Wendi Han
 */
public class  T_FlyRelated {
    private static J_GameEngine gameEngine;
    private static List<J_Level> list;
    private static J_Movement move;
    private static J_Level level;
    private static J_LevelRecord jr;
    private static InputStream in;
    private static J_LevelSetting levelSetting;

    @BeforeEach
    void setup() {
        level = null;
        levelSetting = new J_LevelSetting();
        in = T_Map.class.getClassLoader().getResourceAsStream("debugGame.skb");
        list = levelSetting.loadGameFile(in);
        gameEngine = new J_GameEngine(list);
        gameEngine.setColor(new J_Color());
        level = gameEngine.getNextLevel();
        move = new J_Movement(gameEngine);
        gameEngine.setMove(move);
        gameEngine.setCurrentLevel(level);
        jr = new J_LevelRecord(level.getName(), new Date(), "unknown");
        gameEngine.setLr(jr);
        gameEngine.setPrimaryStage(null);
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
    }

//   Test this part need to commit the else statement about call J_Tooltip
//   in function executeFly() in J_Movement
//    @Test
//    void testCannotFly() {
//        move.handleKey(KeyCode.SPACE);
//        assertEquals(false, gameEngine.isCanUseFly());
//        assertEquals(false, gameEngine.isIsFly());
//    }

    @Test
    void testCanFly() {
        gameEngine.setCanUseFly(true);
        move.handleKey(KeyCode.F);
        assertEquals(true, gameEngine.isCanUseFly());
        assertEquals(true, gameEngine.isIsFly());
    }

    @Test
    void testPlayerOnWall() {
        gameEngine.setCanUseFly(true);
        move.handleKey(KeyCode.F);
        for (int i = 0; i < 8; i++) {
            move.handleKey(KeyCode.LEFT);
        }
        E_GameObject object = level.getObjectAt(new Point(2,5));
        assertEquals(E_GameObject.PLAYER_ON_WALL, object);
        assertEquals(level.getKeeperPosition(), new Point(2,5));
    }

    @Test
    void testUndo1() {
        gameEngine.setCanUseFly(true);
        move.handleKey(KeyCode.F);
        move.undoMove();
        assertEquals(false, gameEngine.isIsFly());
    }

    @Test
    void testUndo2() {
        gameEngine.setCanUseFly(true);
        move.handleKey(KeyCode.F);
        for (int i = 0; i < 8; i++) {
            move.handleKey(KeyCode.LEFT);
        }
        move.undoMove();
        E_GameObject object = level.getObjectAt(new Point(2,6));
        assertEquals(E_GameObject.PLAYER_ON_FLOOR, object);
    }

    @Test
    void testCannotFlyWhenChangeLevel() {
        gameEngine.setCanUseFly(true);
        level = gameEngine.getNextLevel();
        gameEngine.setCurrentLevel(level);
        assertEquals(false, gameEngine.isCanUseFly());
        assertEquals(false, gameEngine.isIsFly());
    }
}