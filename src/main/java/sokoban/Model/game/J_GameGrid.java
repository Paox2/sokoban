package sokoban.Model.game;

import sokoban.Model.gameEngine.debug.J_Debug;
import sokoban.Model.gameEngine.object.E_GameObject;
import java.awt.*;
import java.util.Iterator;

/**
 *  <p>
 *      This class contains the map of one special level. Also, it includes
 *      lots of method to access the object in the map or change it.
 *  </p>
 *
 * @author Wendi Han - edited
 * @version 1.1
 * @see E_GameObject
 * @see Point
 */
public class J_GameGrid implements Iterable {

    private final int m_COLUMNS;
    private final int m_ROWS;
    private E_GameObject[][] m_GameObjects;

    /**
     * Constructor is used to get the rows and columns and initialize the map.
     *
     * @param columns  Columns of map
     * @param rows  Rows of map
     * @since 1.1
     */
    public J_GameGrid(int columns, int rows) {
        m_COLUMNS = columns;
        m_ROWS = rows;

        m_GameObjects = new E_GameObject[m_COLUMNS][m_ROWS];
    }

    /**
     * Get the value of the number of columns in the map.
     *
     * @return {@code m_COLUMNS}
     * @since 1.1
     */
    public int getCOLUMNS() {
        return m_COLUMNS;
    }

    /**
     * Get the value of the number of rows in the map.
     *
     * @return {@code m_ROWS}
     * @since 1.1
     */
    public int getROWS() {
        return m_ROWS;
    }

    /**
     * Get the position after translate without modify the position of origin source.
     *
     * @param sourceLocation  The location of source.
     * @param delta  The direction it need to move.
     * @return Point - New position.
     * @since 1.1
     */
    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     * Get the source at the new position based on the position of the {@code source}
     * after translating {@code delta} direction.
     *
     * @param source  The position of source.
     * @param delta  The relative position of the source.
     * @return {@code E_GameObject} - The source on the relative position.
     * @since 1.1
     */
    public E_GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * Return the object at certain point and throw exception when the point out of bounds.
     *
     * @param col  The column of object.
     * @param row  The row of object.
     * @return {@code E_GameObejct} - The object at certain point.
     * @throws ArrayIndexOutOfBoundsException - Detect exception when index over the length of list.
     * @see J_GameGrid#isPointOutOfBounds(int, int)
     * @since 1.1
     */
    public E_GameObject getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(col, row)) {
            if (J_Debug.getInstance().isDebugActive()) {
                System.out.printf("Trying to get null GameObject from COL: %d  ROW: %d", col, row);
            }
            return null;
        }

        return m_GameObjects[col][row];
    }

    /**
     * Return the object at the point and throw exception if {@code Point p} is null.
     *
     * @param p  The point of object
     * @return {@code E_GameObject} - Object
     * @since 1.1
     */
    public E_GameObject getGameObjectAt(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }

    /**
     * Return {@code true} if operation is successful.
     *
     * @param gameObject  Object
     * @param x  The row in the map
     * @param y  The column in the map
     * @return {@code boolean} - Return {@code true} if operation is successful
     * @see J_GameGrid#isPointOutOfBounds(int, int)
     * @since 1.1
     */
    public boolean putGameObjectAt(E_GameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        m_GameObjects[x][y] = gameObject;
        return m_GameObjects[x][y] == gameObject;
    }

    /**
     * Return {@code true} if operation is successful.
     *
     * @param gameObject Object
     * @param p  Position in the map
     * @return {@code boolean} - Return {@code true} if operation is successful
     * @see J_GameGrid#isPointOutOfBounds(int, int)
     * @since 1.1
     */
    public boolean putGameObjectAt(E_GameObject gameObject, Point p) {
        return p != null && putGameObjectAt(gameObject, (int) p.getX(), (int) p.getY());
    }

    /**
     * Return {@code true} if position is out of bounds.
     *
     * @param x  Row
     * @param y  Column
     * @return {@code boolean} - Return {@code true} if position is out of bounds.
     * @since 1.1
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= m_COLUMNS || y >= m_ROWS);
    }

    /**
     * Return the map of certain level.
     *
     * @return String - Map
     * @see StringBuilder
     * @see Object#toString()
     * @since 1.1
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(m_GameObjects.length);

        for (E_GameObject[] gameObject : m_GameObjects) {
            for (E_GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = E_GameObject.DEBUG_OBJECT;
                }
                sb.append(aGameObject.getCharSymbol());
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * Return the iterator of game grid.
     *
     * @return Iterator
     * @see Iterator
     * @see GridIterator
     * @since 1.1
     */
    @Override
    public Iterator<E_GameObject> iterator() {
        return new GridIterator();
    }

    /**
     * This class overrides the method {@code hasNext()} and {@code next()} in the interface Iterator.
     *
     * @see Iterator#hasNext()
     * @see Iterator#next()
     * @since 1.1
     */
    public class GridIterator implements Iterator<E_GameObject> {
        int row = 0;
        int column = 0;

        @Override
        public boolean hasNext() {
            return !(row == m_ROWS && column == m_COLUMNS);
        }

        @Override
        public E_GameObject next() {
            if (column >= m_COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }

    /**
     * Get all the object in the map.
     *
     * @return E_GameObject[][] - All the object in the map.
     */
    public E_GameObject[][] getM_GameObjects() {
        return m_GameObjects;
    }
}