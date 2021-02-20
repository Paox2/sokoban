package sokoban.Model.gameEngine.move;

import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import sokoban.Model.game.J_GameEngine;
import sokoban.Model.game.J_Tooltip;
import sokoban.Model.gameEngine.debug.J_Debug;
import sokoban.Model.gameEngine.object.J_Color;
import sokoban.Model.gameEngine.object.ghost.E_EnumGhostFactory;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.gameEngine.object.ghost.I_GhostFactory;
import sokoban.Model.level.J_Level;
import sokoban.Model.log.J_GameLogger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.random;
import static sokoban.Model.game.J_GameGrid.translatePoint;

/**
 *  <p>
 *      This class is used to process event about moving.<br>
 *      {@code handleKey(Keycode code)} function recognize the keycode user pressed.<br>
 *      {@code movePipeline(E_GameObject keeper, Point keeperPosition) } show the detail when player uses pipeline<br>
 *      {@code move(Point delta)} process the request of move and judge if level has been finished.
 *      {@code undoMove()} gives player the chance to back to last position
 *  </p>
 *
 * @author Wendi Han - edited
 * @version 1.4
 * @see J_Color#getObjects()
 * @see J_GameLogger#severe(String)
 * @see J_Level
 * @see Duration
 * @see J_Tooltip
 * @see Point#setLocation(int, int)
 * @see J_Level#moveGameObjectTo(E_GameObject, Point, Point)
 * @see J_Level#getKeeperPosition()
 * @see J_Level#moveGameObjectBy(E_GameObject, Point, Point)
 * @see J_GameEngine#setLevelUp()
 * @see J_Tooltip
 * @see #movePipeline(E_GameObject, Point)
 */
public class J_Movement {
    private J_GameEngine m_GameEngine;
    private J_GameLogger m_Logger;
    private LinkedHashMap<String,Integer> m_MovesCountList;
    private Point m_LastKeeperMove;
    private Point m_CratePosition;
    private Boolean m_LastTouchCrate;
    private Boolean m_HaveUndo;
    private ArrayList<Point> m_PipelineTargetPoint;
    private Boolean m_LastTimeFly;
    private Point m_TargetObjectPoint;
    private GaussianBlur gaussianBlur = new GaussianBlur();

    /**
     * Initialize the game engine, logger and move count list.
     * Set {@code m_HaveUndo} to {@code false}
     *
     * @param gameEngine - Game engine
     * @since 1.1
     */
    public J_Movement(J_GameEngine gameEngine){
        m_GameEngine = gameEngine;
        m_Logger = m_GameEngine.getLogger();
        m_MovesCountList = new LinkedHashMap<>();
        List<J_Level> levels = m_GameEngine.getLevels();
        for (J_Level level : levels){
            m_MovesCountList.put(level.getName(),0);
        }
        m_LastKeeperMove = new Point(0,0);
        m_HaveUndo = false;
        m_LastTimeFly = false;
        m_PipelineTargetPoint = new ArrayList<>();
    }

    /**
     * Recognize the key code player pressed and print the keycode if debug pattern is opening.
     *
     * @param code  The key code player pressed
     * @see J_Color#getObjects()
     * @see J_GameLogger#severe(String)
     * @since 1.1
     */
    public void handleKey(KeyCode code) {
        if (m_GameEngine.isStop()) {
            if (code == KeyCode.SPACE) {
                m_GameEngine.setStop(false);
                m_GameEngine.getPrimaryStage().getScene().getRoot().setEffect(null);
                m_GameEngine.getLr().setStartTime(new Date());
            }
            return;
        }

        switch (code) {
            case UP:
                move(new Point(-1, 0));
                m_GameEngine.getColor().getObjects().put(E_GameObject.KEEPER,"U");
                break;

            case RIGHT:
                move(new Point(0, 1));
                m_GameEngine.getColor().getObjects().put(E_GameObject.KEEPER,"R");
                break;

            case DOWN:
                move(new Point(1, 0));
                m_GameEngine.getColor().getObjects().put(E_GameObject.KEEPER,"D");
                break;

            case LEFT:
                move(new Point(0, -1));
                m_GameEngine.getColor().getObjects().put(E_GameObject.KEEPER,"L");
                break;

            case F:
                executeFly();
                break;

            case SPACE:
                m_GameEngine.setStop(true);
                m_GameEngine.getLr().setEndTime(new Date());
                gaussianBlur.setRadius(20.0D);
                m_GameEngine.getPrimaryStage().getScene().getRoot().setEffect(gaussianBlur);
                m_GameEngine.getLr().setM_Time(m_GameEngine.getLr().getTimeDiff());
                m_GameEngine.getLr().setEndTime(null);
                break;

            default:
                break;
        }

        if (m_GameEngine.getDebug().isDebugActive()) {
            System.out.println(code);
        }
    }

    /**
     * Response to the player to fly if they have the ability to fly.
     *
     * @since 1.4
     * @see J_Level
     * @see Duration
     * @see J_Tooltip
     */
    private void executeFly() {
        J_Level current = m_GameEngine.getCurrentLevel();
        Point keeperPosition = current.getKeeperPosition();
        if(!m_GameEngine.isIsFly() && m_GameEngine.isCanUseFly()) {
            m_GameEngine.setIsFly(true);
            current.setGameObjectTo(E_GameObject.PLAYER_ON_FLOOR, keeperPosition);
            m_HaveUndo = false;
            m_LastTimeFly = true;
        } else if (m_GameEngine.isIsFly()){
            E_GameObject keeper = current.getObjectAt(keeperPosition);
            if(keeper ==  E_GameObject.PLAYER_ON_FLOOR) {
                m_GameEngine.setIsFly(false);
                current.setGameObjectTo(E_GameObject.KEEPER, keeperPosition);
                m_LastTimeFly = true;
                m_HaveUndo = false;
            } else {
                new J_Tooltip("Only can land on the floor", m_GameEngine.getPrimaryStage())
                        .show(Duration.seconds(2));
            }
        } else {
            new J_Tooltip("You cannot fly now\n"
                    + "You can gain this ability through ghosts"
                    , m_GameEngine.getPrimaryStage())
                    .show(Duration.seconds(2));
        }
    }

    /**
     * Finish the move part if player uses pipeline and record the last position of player.
     * If the target point is crate, then user cannot move to the target.
     *
     * @param keeper  The object : keeper.
     * @param keeperPosition   The position of keeper in the map
     * @see Point#setLocation(int, int)
     * @see J_Level#moveGameObjectTo(E_GameObject, Point, Point)
     * @since 1.3
     */
    private void movePipeline(E_GameObject keeper, Point keeperPosition) {
        m_PipelineTargetPoint = m_GameEngine.getCurrentLevel().getPipelineTargetPoint();
        Point target = new Point();
        for (Point p : m_PipelineTargetPoint) {
            if (abs(p.getY() - keeperPosition.getY()) + abs(p.getX() - keeperPosition.getX()) > 2) {
                target = p;
            }
        }
        E_GameObject targetObject = m_GameEngine.getCurrentLevel().getObjectAt(target);
        if(targetObject == E_GameObject.CRATE)
            return;
        m_GameEngine.getCurrentLevel().moveGameObjectTo(keeper, keeperPosition, target);

        m_LastKeeperMove = new Point(keeperPosition.x - target.x, keeperPosition.y - target.y);
        keeperPosition.setLocation(target.x, target.y);
    }

    /**
     * Execute the task of moving and judge if the player has passed the level.<br>
     * If true, set level up, otherwise continue current level.
     *
     * @param delta  The direction player moved
     * @see J_Level#getKeeperPosition()
     * @see J_Level#moveGameObjectBy(E_GameObject, Point, Point)
     * @see J_GameEngine#setLevelUp()
     * @see J_Tooltip
     * @see #movePipeline(E_GameObject, Point)
     * @since 1.1
     */
    public void move(Point delta) {
        if (m_GameEngine.isGameComplete()) {
            return;
        }

        J_Level currentLevel = m_GameEngine.getCurrentLevel();
        Point keeperPosition = currentLevel.getKeeperPosition();
        E_GameObject keeper = currentLevel.getObjectAt(keeperPosition);
        m_TargetObjectPoint = translatePoint(keeperPosition, delta);
        E_GameObject keeperTarget = currentLevel.getObjectAt(m_TargetObjectPoint);
        if(keeperTarget == null) {
            new J_Tooltip("Cannot access the outside of the map", m_GameEngine.getPrimaryStage())
                    .show(Duration.seconds(2));
        }

        if (J_Debug.getInstance().isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget, m_TargetObjectPoint);
        }

        boolean keeperMoved = false;
        boolean isFly = m_GameEngine.isIsFly();

        if (isFly) {
            E_GameObject targetObject;
            E_GameObject keeperObject;
            if (keeperTarget == E_GameObject.GHOST) {
                int i = (int) (random() * 6);
                System.out.println(E_EnumGhostFactory.fromInt(i));
                I_GhostFactory ghost = E_EnumGhostFactory.fromInt(i).create();
                ghost.collision(m_GameEngine);
                J_Tooltip tooltip = new J_Tooltip(ghost.type(), m_GameEngine.getPrimaryStage());
                tooltip.show(Duration.seconds(1.5));
            } else {
                targetObject = keeperTarget.getPlayerOnObject();
                keeperObject = keeper.getPlayerOnObject();
                currentLevel.setGameObjectTo(targetObject, m_TargetObjectPoint);
                currentLevel.setGameObjectTo(keeperObject, keeperPosition);
                keeperMoved = true;
            }
        } else {
            switch (keeperTarget) {
                case POOL:
                    String proposition = currentLevel.getPoolType().get(m_TargetObjectPoint).get(0);
                    new J_Tooltip(proposition, m_GameEngine.getPrimaryStage())
                            .show(Duration.seconds(2));
                case WALL:
                    break;

                case CRATE:
                    E_GameObject crateTarget = currentLevel.getTargetObject(m_TargetObjectPoint, delta);
                    if (crateTarget != E_GameObject.FLOOR && crateTarget != E_GameObject.POOL) {
                        break;
                    }

                    m_LastTouchCrate = true;
                    m_CratePosition = translatePoint(m_TargetObjectPoint, delta);
                    currentLevel.moveGameObjectBy(keeperTarget, m_TargetObjectPoint, delta);
                    currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);

                    if(crateTarget == E_GameObject.POOL) {
                        String propositionType = currentLevel.getPoolType().get(m_CratePosition).get(1);
                        switch (propositionType) {
                            case "true" -> {
                                currentLevel.setGameObjectTo(E_GameObject.FLOOR, m_CratePosition);
                                currentLevel.setGameObjectTo(E_GameObject.FLOOR, keeperPosition);
                                new J_Tooltip("Choose right!!!!!", m_GameEngine.getPrimaryStage())
                                        .show(Duration.seconds(1.5));
                            }
                            case "false" -> {
                                currentLevel.setGameObjectTo(E_GameObject.POOL, m_CratePosition);
                                currentLevel.setGameObjectTo(E_GameObject.FLOOR, keeperPosition);
                                new J_Tooltip("Bad choose!!!!!", m_GameEngine.getPrimaryStage())
                                        .show(Duration.seconds(1.5));
                            }
                            default -> throw new AssertionError("This should not have happened" +
                                    ". Report this problem to the developer.");
                        }
                    }
                    keeperMoved = true;
                    break;

                case FLOOR:
                    m_LastTouchCrate = false;
                    currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                    keeperMoved = true;
                    break;

                case PIPELINE:
                    m_LastTouchCrate = false;
                    movePipeline(keeper, keeperPosition);
                    keeperMoved = true;
                    break;

                case GHOST:
                    int i;
                    if (m_GameEngine.isCanUseFly()) {
                        i = (int) (random() * 6);
                    } else {
                        i = (int) (random() * 7);
                    }
                    I_GhostFactory ghost = E_EnumGhostFactory.fromInt(i).create();
                    System.out.println(ghost.type());
                    ghost.collision(m_GameEngine);
                    J_Tooltip tooltip = new J_Tooltip(ghost.type(), m_GameEngine.getPrimaryStage());
                    tooltip.show(Duration.seconds(1.5));
                    break;

                default:
                    m_Logger.severe("The object to be moved was not a recognised GameObject.");
                    throw new AssertionError("This should not have happened" +
                            ". Report this problem to the developer.");
            }
        }

        if (keeperMoved) {
            m_LastTimeFly = false;
            m_HaveUndo = false;
            if(keeperTarget != E_GameObject.PIPELINE || m_GameEngine.isIsFly()) {
                m_LastKeeperMove = new Point((int)delta.getX() * -1, (int)delta.getY() * -1);
                keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            }
            int count = m_MovesCountList.get(currentLevel.getName());
            m_MovesCountList.put(currentLevel.getName(), ++count);
            if (currentLevel.isComplete()) {
                if (m_GameEngine.getDebug().isDebugActive()) {
                    System.out.println("Level complete!");
                }
                m_GameEngine.setLevelUp();
            }
        }
    }

    /**
     * <p>
     * Execute the operation of back to last position.<br>
     * If user have executed {@code undo} before and do not move again, this method do nothing.<br>
     * If user successfully fly or land last time, it will back to the last state.
     * </p>
     *
     * @see J_Level#moveGameObjectBy(E_GameObject, Point, Point)
     * @see J_Tooltip
     * @since 1.2
     */
    public void undoMove() {
        if(m_LastTimeFly && !m_HaveUndo) {
            executeFly();
            m_HaveUndo = true;
            return;
        }
        J_Level currentLevel = m_GameEngine.getCurrentLevel();
        if (! m_HaveUndo && m_MovesCountList.get(currentLevel.getName()) > 0) {
            Point keeperPosition = currentLevel.getKeeperPosition();;
            E_GameObject keeper = currentLevel.getObjectAt(keeperPosition);
            int lastX = keeperPosition.x + (int)m_LastKeeperMove.getX();
            int lastY = keeperPosition.y + (int)m_LastKeeperMove.getY();
            Point lastPosition = new Point(lastX, lastY);
            E_GameObject lastObject = currentLevel.getObjectAt(lastPosition);

            if (!m_GameEngine.isIsFly()) {
                currentLevel.moveGameObjectBy(keeper, keeperPosition, m_LastKeeperMove);
                if (m_LastTouchCrate) {
                    E_GameObject crate = currentLevel.getObjectAt(m_CratePosition);
                    if (crate == E_GameObject.FLOOR || crate == E_GameObject.POOL) {
                        currentLevel.setGameObjectTo(E_GameObject.POOL, m_CratePosition);
                        currentLevel.setGameObjectTo(E_GameObject.CRATE, keeperPosition);
                    } else {
                        currentLevel.moveGameObjectBy(crate, m_CratePosition, m_LastKeeperMove);
                    }
                }
            } else {
                lastObject = lastObject.getPlayerOnObject();
                keeper = keeper.getPlayerOnObject();
                currentLevel.setGameObjectTo(lastObject, lastPosition);
                currentLevel.setGameObjectTo(keeper, keeperPosition);
            }

            keeperPosition.translate((int) m_LastKeeperMove.getX(), (int) m_LastKeeperMove.getY());
            int count = m_MovesCountList.get(currentLevel.getName());
            m_MovesCountList.put(currentLevel.getName(), count - 1);
            m_HaveUndo = true;
        } else {
            String message = "Please move again, then can use undo";
            J_Tooltip tooltip = new J_Tooltip(message, m_GameEngine.getPrimaryStage());
            tooltip.show(Duration.seconds(1.5));
        }
    }

    /**
     * Get the value of {@code m_MovesCountList}, which is a list of moves count.
     *
     * @return {m_MovesCountList} - A linked hash map saves level name and moves count.
     * @since 1.1
     */
    public LinkedHashMap<String, Integer> getMovesCount() {
        return m_MovesCountList;
    }

    /**
     * Return the position of target object.
     *
     * @return Point - The position of target object.
     * @since 1.3
     */
    public Point getTargetObjectPoint() {
        return m_TargetObjectPoint;
    }

    /**
     * Get the position of target object.
     *
     * @param targetObjectPoint  Position of target object
     * @since 1.3
     */
    public void setTargetObjectPoint(Point targetObjectPoint) {
        m_TargetObjectPoint = targetObjectPoint;
    }
}
