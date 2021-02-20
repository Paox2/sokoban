import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sokoban.Model.game.J_GameEngine;
import sokoban.Model.gameEngine.move.J_Movement;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.gameEngine.object.ghost.E_EnumGhostFactory;
import sokoban.Model.gameEngine.object.ghost.I_GhostFactory;
import sokoban.Model.level.J_Level;
import sokoban.Model.level.J_LevelRecord;
import sokoban.Model.level.J_LevelSetting;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Wendi Han
 * @project comp2013_cw_scywh1
 */
public class T_Ghost {
    J_GameEngine gameEngine;
    InputStream in;
    List<J_Level> list;
    J_Movement move;
    J_Level level;
    I_GhostFactory ghost;
    J_LevelSetting levelSetting;

    @BeforeEach
    void setup() {
        in = T_Map.class.getClassLoader().getResourceAsStream("debugGame.skb");
        levelSetting = new J_LevelSetting();
        list = levelSetting.loadGameFile(in);
        gameEngine = new J_GameEngine(list);
        move = new J_Movement(gameEngine);
        move.setTargetObjectPoint(new Point(1,1));
        gameEngine.setMove(move);
        level = gameEngine.getNextLevel();
        gameEngine.setCurrentLevel(level);
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
    }

    @Test
    void testDisappear() {
        Point target = move.getTargetObjectPoint();
        assertEquals(E_GameObject.GHOST, level.getObjectAt(target));
        ghost = E_EnumGhostFactory.fromInt(0).create();
        ghost.collision(gameEngine);
        assertEquals(E_GameObject.FLOOR, level.getObjectAt(target));
    }

    @Test
    void testMoveAddAndSub() {
        ghost = E_EnumGhostFactory.fromInt(4).create();
        ghost.collision(gameEngine);
        assertEquals(10, move.getMovesCount().get(level.getName()));

        ghost = E_EnumGhostFactory.fromInt(3).create();
        ghost.collision(gameEngine);
        assertEquals(0, move.getMovesCount().get(level.getName()));
    }

    @Test
    void testResetTime() {
        J_LevelRecord jr = new J_LevelRecord(level.getName(),new Date(), "good");
        gameEngine.setLr(jr);
        ghost = E_EnumGhostFactory.fromInt(2).create();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        jr.setEndTime(new Date());
        ghost.collision(gameEngine);
        assertEquals(true, (jr.getTimeDiff() - 1000) < 100);
    }
}
