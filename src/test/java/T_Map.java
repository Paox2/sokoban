import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.level.J_Level;
import sokoban.Model.level.J_LevelSetting;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class T_Map {
    private static InputStream in;
    private static J_Level level;
    private static List<J_Level> list;
    private E_GameObject object;
    private String name;
    private int index;
    private ArrayList<Point> pipelineTarget;
    private static ArrayList<Point> expectTarget;
    private static Point keeperPosition;
    J_LevelSetting levelSetting;

    @BeforeEach
    void setup() {
        in = T_Map.class.getClassLoader().getResourceAsStream("debugLevel.skb");
        levelSetting = new J_LevelSetting();
        list = levelSetting.loadGameFile(in);
        level = list.get(0);
        expectTarget = new ArrayList<>();
        expectTarget.add(new Point(1,10));
        expectTarget.add(new Point(1,13));
        keeperPosition = level.getKeeperPosition();
    }

    @Test
    public void testObjectInMap1() {
        object = level.getObjectAt(new Point(0, 1));
        assertEquals(E_GameObject.WALL, object);
    }

    @Test
    public void testObjectInMap2() {
        object = level.getObjectAt(new Point(1, 1));
        assertEquals(E_GameObject.KEEPER, object);
    }

    @Test
    public void testObjectInMap3() {
        object = level.getObjectAt(new Point(1, 3));
        assertEquals(E_GameObject.CRATE, object);
    }

    @Test
    public void testLevelName() {
        name = level.getName();
        assertEquals("Just this level", name);
    }

    @Test
    public void testLevelIndex() {
        index = level.getIndex();
        assertEquals(0, index);
    }

    @Test
    public void testPipelineTargetPoint() {
        pipelineTarget = level.getPipelineTargetPoint();
        assertEquals(expectTarget, pipelineTarget);
    }

    @Test
    public void testMove() {
        level.moveGameObjectBy(E_GameObject.KEEPER, keeperPosition, new Point(0,1));
        object = level.getObjectAt(new Point(1,2));
        assertEquals(E_GameObject.KEEPER,object);

        keeperPosition.translate(0, 1);
        keeperPosition = level.getKeeperPosition();
        assertEquals(new Point(1, 2), keeperPosition);
    }
}
