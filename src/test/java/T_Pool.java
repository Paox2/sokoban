import javafx.scene.input.KeyCode;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class T_Pool {
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

    @Test
    void testOnlyOneShallowPool() {
        int numberTruth = 0;
        int numberFalse = 0;
        for (Point point : level.getPoolType().keySet()) {
            if(level.getPoolType().get(point).get(1).equals("true")) {
                numberTruth++;
            } else if(level.getPoolType().get(point).get(1).equals("false")) {
                numberFalse++;
            }
        }
        assertEquals(1, numberTruth);
        assertEquals(2, numberFalse);
    }


    /** Running this test need to commit line 296, 297 which is the use of tooltip in J_Movement
    @Test
    void testWrongPoolCannotPass() {
        Point truePool = new Point(2,17);
        level.getPoolType().get(truePool).set(1, "false");

        move.handleKey(KeyCode.RIGHT);
        move.handleKey(KeyCode.RIGHT);
        move.handleKey(KeyCode.RIGHT);
        E_GameObject object = level.getObjectAt(truePool);
        assertEquals(E_GameObject.POOL, object);
    }
    */

     /** Running this test need to commit line 290,291 which is the use of tooltip in J_Movement
     @Test
     void testTruePoolCanPass() {
         Point truePool = new Point(2,17);
         level.getPoolType().get(truePool).set(1, "true");

         move.handleKey(KeyCode.RIGHT);
         move.handleKey(KeyCode.RIGHT);
         move.handleKey(KeyCode.RIGHT);
         E_GameObject object = level.getObjectAt(truePool);
         assertEquals(E_GameObject.FLOOR, object);
     }
     */


    /** Running this test need to commit line 296, 297 which is the use of tooltip in J_Movement
     @Test
     void testTruePoolCanPass() {
         Point truePool = new Point(2,17);
         level.getPoolType().get(truePool).set(1, "true");

         move.handleKey(KeyCode.RIGHT);
         move.handleKey(KeyCode.RIGHT);
         move.handleKey(KeyCode.RIGHT);
         E_GameObject object = level.getObjectAt(truePool);
         assertEquals(E_GameObject.FLOOR, object);
     }
     */

    /** Running this test need to commit line 290, 291 which is the use of tooltip in J_Movement
     @Test
     void testTruePoolUndo() {
         Point truePool = new Point(2,17);
         level.getPoolType().get(truePool).set(1, "true");

         move.handleKey(KeyCode.RIGHT);
         move.handleKey(KeyCode.RIGHT);
         move.handleKey(KeyCode.RIGHT);
         move.undoMove();

         E_GameObject object = level.getObjectAt(truePool);
         assertEquals(E_GameObject.POOL, object);
         object = level.getObjectAt(new Point(2,16));
         assertEquals(E_GameObject.CRATE, object);
         object = level.getObjectAt(new Point(2,15));
         assertEquals(E_GameObject.KEEPER, object);

     }
     */

    /** Running this test need to commit line 296, 297 which is the use of tooltip in J_Movement
     @Test
     void testFalsePoolUndo() {
         Point truePool = new Point(2,17);
         level.getPoolType().get(truePool).set(1, "false");

         move.handleKey(KeyCode.RIGHT);
         move.handleKey(KeyCode.RIGHT);
         move.handleKey(KeyCode.RIGHT);
         move.undoMove();

         E_GameObject object = level.getObjectAt(truePool);
         assertEquals(E_GameObject.POOL, object);
         object = level.getObjectAt(new Point(2,16));
         assertEquals(E_GameObject.CRATE, object);
         object = level.getObjectAt(new Point(2,15));
         assertEquals(E_GameObject.KEEPER, object);
     }
    */
}
