package sokoban.Model.level;

import sokoban.Model.gameEngine.debug.J_Debug;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.game.J_GameGrid;
import sokoban.Model.game.J_GameEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.random;
import static sokoban.Model.game.J_GameGrid.translatePoint;

/**
 *  <p>
 *      This class contains the detail of level.<br>
 *      {@code m_Name} represents level name.<br>
 *      {@code m_ObjectsGrid} represents level map.<br>
 *      {@code m_DiamondsGrid} represents diamond position in the map.<br>
 *      {@code m_PipelineTargetPoint} represents pipeline position in the map.<br>
 *      {@code m_Index} represents the index of level in the list.<br>
 *      {@code m_NumberOfDiamonds} represents the number of diamonds.<br>
 *      {@code m_KeeperPosition} represents the position of keeper.<br>
 *  </p>
 *
 * @author Wendi Han - edited
 * @version 1.2
 * @see J_GameGrid
 * @see E_GameObject
 */
public final class J_Level implements Iterable<E_GameObject>{

    private final String m_Name;
    private final J_GameGrid m_ObjectsGrid;
    private final J_GameGrid m_DiamondsGrid;
    private final ArrayList<Point> m_PipelineTargetPoint;
    private final int m_Index;
    private int m_NumberOfDiamonds = 0;
    private Point m_KeeperPosition = new Point(0, 0);
    private HashMap<Point, List<String>> poolType;

    /**
     * Constructor to initialize the game map, diamonds map and target position of target.
     *
     * @param levelName  Level name
     * @param levelIndex  The index of level in all level
     * @param raw_level  A list of rows of the map
     * @see J_GameGrid
     * @see E_GameObject
     * @since 1.1
     */
    public J_Level(String levelName, int levelIndex, List<String> raw_level) {
        poolType = new HashMap<>();
        if (J_Debug.getInstance().isDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }

        m_Name = levelName;
        m_Index = levelIndex;

        int poolNumber = 0;
        int rows = raw_level.size();
        int columns = raw_level.get(0).trim().length();

        m_ObjectsGrid = new J_GameGrid(rows, columns);
        m_DiamondsGrid = new J_GameGrid(rows, columns);
        m_PipelineTargetPoint = new ArrayList<>();

        for (int row = 0; row < raw_level.size(); row++) {

            // Loop over the string one char at a time because it should be the fastest way:
            // http://stackoverflow.com/questions/8894258/fastest-way-to-iterate-over-all-the-chars-in-a-string
            for (int col = 0; col < raw_level.get(row).length(); col++) {
                // The game object is null when the we're adding a floor or a diamond
                E_GameObject curTile = E_GameObject.fromChar(raw_level.get(row).charAt(col));

                if (curTile == E_GameObject.DIAMOND) {
                    m_NumberOfDiamonds++;
                    m_DiamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = E_GameObject.FLOOR;
                } else if (curTile == E_GameObject.KEEPER) {
                    m_KeeperPosition = new Point(row, col);
                } else if (curTile == E_GameObject.TARGET) {
                    m_PipelineTargetPoint.add(new Point(row, col));
                    curTile = E_GameObject.FLOOR;
                } else if (curTile == E_GameObject.POOL) {
                    poolType.put(new Point(row,col), null);
                    poolNumber++;
                }

                m_ObjectsGrid.putGameObjectAt(curTile, row, col);
            }
        }

        if (poolNumber > 0) {
            createPoolProposition(poolNumber);
        }
    }

    /**
     * Initialize the proposition list for pool. Create one shallow pool and several deep pool.
     * Only the shallow pool with the true proposition can be filled.
     *
     * @param poolNumber The number of pool in the map.
     * @since 1.2
     */
    private void createPoolProposition(int poolNumber) {
        J_PoolStatementSingleton poolStatementSingleton = J_PoolStatementSingleton.getInstance();
        List<String> trueList = poolStatementSingleton.getTureProposition();
        List<String> falseList = poolStatementSingleton.getFalseProposition();
        int trueListSize = trueList.size();
        int falseListSize = falseList.size();

        int i = (int) (random() * poolNumber);

        for (Point point : poolType.keySet()) {
            if (i-- == 0) {
                int j = (int) (random() * trueListSize);
                List<String> pool = new ArrayList<>();
                pool.add(trueList.get(j));
                pool.add("true");
                poolType.put(point, pool);
            } else {
                int j = (int) (random() * falseListSize);
                List<String> pool = new ArrayList<>();
                pool.add(falseList.get(j));
                pool.add("false");
                poolType.put(point, pool);
            }
        }
    }

    /**
     * Get the object in the {@code delta} position of the {@code source} position in the map.
     *
     * @param source  The position of source
     * @param delta  The position of movement
     * @return {@code E_GameObject} - The object at certain position.
     * @since 1.1
     */
    public E_GameObject getTargetObject(Point source, Point delta) {
        return m_ObjectsGrid.getTargetFromSource(source, delta);
    }

    /**
     * Get the object in the position{@code source} in the map.
     *
     * @param p  The position of source
     * @return {@code E_GameObject} - The object at certain position.
     * @since 1.1
     */
    public E_GameObject getObjectAt(Point p) {
        return m_ObjectsGrid.getGameObjectAt(p);
    }

    /**
     * Move {@code object} to the new position.
     *
     * @param object  Type of object
     * @param source  The position of object
     * @param delta  The position of movement
     * @since 1.1
     */
    public void moveGameObjectBy(E_GameObject object, Point source, Point delta) {
        moveGameObjectTo(object, source, translatePoint(source, delta));
    }

    /**
     * Move {@code object} to the {@code destination}.
     *
     * @param object  Type of object
     * @param source  The position of object
     * @param destination  The new position of object
     * @since 1.1
     */
    public void moveGameObjectTo(E_GameObject object, Point source, Point destination) {
        m_ObjectsGrid.putGameObjectAt(getObjectAt(destination), source);
        m_ObjectsGrid.putGameObjectAt(object, destination);
    }

    /**
     * Set certain position in the map to wall.
     *
     * @param source - The position in the map
     * @param object - Object at this point
     */
    public void setGameObjectTo(E_GameObject object, Point source) {
        m_ObjectsGrid.putGameObjectAt(object, source);
    }

    /**
     * Returning a string contains level name and level index.
     *
     * @return String
     * @see Object#toString()
     * @since 1.1
     */
    @Override
    public String toString() {
        return "J_Level{" +
                "name='" + m_Name + '\'' +
                ", index=" + m_Index +
                '}';
    }

    /**
     * Get the instance of level iterator.
     *
     * @return Iterator
     * @see Iterator
     * @see E_GameObject
     * @see Iterator
     * @see LevelIterator
     * @since 1.1
     */
    @Override
    public Iterator<E_GameObject> iterator() {
        return new LevelIterator();
    }

    /**
     * This class overrides the method {@code hasNext()} and {@code next()} in the interface Iterator.
     *
     * @see Iterator#hasNext()
     * @see Iterator#next()
     * @since 1.1
     */
    public class LevelIterator implements Iterator<E_GameObject> {

        int column = 0;
        int row = 0;

        @Override
        public boolean hasNext() {
            return !(row == m_ObjectsGrid.getROWS() - 1 && column == m_ObjectsGrid.getCOLUMNS());
        }

        @Override
        public E_GameObject next() {
            if (column >= m_ObjectsGrid.getCOLUMNS()) {
                column = 0;
                row++;
            }

            E_GameObject object = m_ObjectsGrid.getGameObjectAt(column, row);
            E_GameObject diamond = m_DiamondsGrid.getGameObjectAt(column, row);
            E_GameObject retObj = object;

            column++;

            if (diamond == E_GameObject.DIAMOND) {
                if (object == E_GameObject.CRATE) {
                    retObj = E_GameObject.CRATE_ON_DIAMOND;
                } else if (object == E_GameObject.FLOOR) {
                    retObj = diamond;
                } else {
                    retObj = object;
                }
            }

            return retObj;
        }

        public Point getCurrentPosition() {
            return new Point(column, row);
        }
    }

    /**
     * Return {@code true} if level has been completed by checking if all the crate on the diamond.
     *
     * @return {@code true} if level has been completed
     * @see E_GameObject
     * @since 1.1
     */
    public boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < m_ObjectsGrid.getROWS(); row++) {
            for (int col = 0; col < m_ObjectsGrid.getCOLUMNS(); col++) {
                if (m_ObjectsGrid.getGameObjectAt(col, row) == E_GameObject.CRATE
                        && m_DiamondsGrid.getGameObjectAt(col, row) == E_GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }

        return cratedDiamondsCount >= m_NumberOfDiamonds;
    }

    /**
     * Get the name of level
     *
     * @return {@code m_Name} - Level name
     * @since 1.1
     */
    public String getName() {
        return m_Name;
    }

    /**
     * Get the index of level in the level list.
     *
     * @return {@code m_Index} - The index of level in the level list
     * @since 1.1
     */
    public int getIndex() {
        return m_Index;
    }

    /**
     * Get the position of keeper in the map.
     *
     * @return {@code J_KeeperPosition} - The position of keeper in the map
     * @since 1.1
     */
    public Point getKeeperPosition() {
        return m_KeeperPosition;
    }

    /**
     * Get the position of target of pipeline in the map.
     *
     * @return {@code m_PipelineTargetPoint} - A list of points represent the position of pipeline.
     * @since 1.1
     */
    public ArrayList<Point> getPipelineTargetPoint() {
        return m_PipelineTargetPoint;
    }

    /**
     * Get the number of diamonds.
     *
     * @return int - The number of diamond.
     * @since 1.1
     */
    public int getM_NumberOfDiamonds() {
        return m_NumberOfDiamonds;
    }

    /**
     * Get the map.
     *
     * @return J_GameGrid - Map.
     * @since 1.1
     */
    public J_GameGrid getM_ObjectsGrid() {
        return m_ObjectsGrid;
    }

    /**
     * Get the map of diamond.
     *
     * @return J_GameGrid - Map of diamond.
     * @since 1.1
     */
    public J_GameGrid getM_DiamondsGrid() {
        return m_DiamondsGrid;
    }

    /**
     * Get the Pool proposition list to judge if it is the shallow pool.
     *
     * @return Pool proposition list
     * @since 1.2
     */
    public HashMap<Point, List<String>> getPoolType() {
        return poolType;
    }

    /**
     * Set the keeper position.
     *
     * @param m_KeeperPosition Keeper position
     * @since 1.2
     */
    public void setM_KeeperPosition(Point m_KeeperPosition) {
        this.m_KeeperPosition = m_KeeperPosition;
    }
}