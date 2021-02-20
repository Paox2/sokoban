import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import sokoban.Model.gameEngine.object.E_GameObject;

import static org.junit.jupiter.api.Assertions.*;
import static sokoban.Model.gameEngine.object.E_GameObject.fromChar;

public class T_GameObject {
    private static char wall;
    private static char floor;
    private static char crate;
    private static char diamond;
    private static char keeper;
    private static char pipeline;
    private static char crate_on_diamond;
    private static char other;
    private static E_GameObject object;

    @BeforeAll
    static void setup(){
        wall = 'w';
        floor = ' ';
        crate = 'c';
        diamond = 'd';
        keeper = 's';
        pipeline = 'p';
        crate_on_diamond = 'o';
        other = 'q';
    }

    @Test
    public void test1() {
        object = fromChar(wall);
        assertEquals(E_GameObject.WALL, object);
    }

    @Test
    public void test2() {
        object = fromChar(floor);
        assertEquals(E_GameObject.FLOOR, object);
    }

    @Test
    public void test3() {
        object = fromChar(crate);
        assertEquals(E_GameObject.CRATE, object);
    }

    @Test
    public void test4() {
        object = fromChar(diamond);
        assertEquals(E_GameObject.DIAMOND, object);
    }

    @Test
    public void test5() {
        object = fromChar(keeper);
        assertEquals(E_GameObject.KEEPER, object);
    }

    @Test
    public void test6() {
        object = fromChar(pipeline);
        assertEquals(E_GameObject.PIPELINE, object);
    }

    @Test
    public void test7() {
        object = fromChar(crate_on_diamond);
        assertEquals(E_GameObject.CRATE_ON_DIAMOND, object);
    }

    @Test
    public void test8() {
        object = fromChar(other);
        assertEquals(E_GameObject.WALL, object);
    }
}
