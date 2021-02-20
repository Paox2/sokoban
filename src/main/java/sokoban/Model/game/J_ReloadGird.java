package sokoban.Model.game;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sokoban.Model.level.J_Level;
import sokoban.Model.gameEngine.object.E_GameObject;
import sokoban.Model.gameEngine.object.J_Color;
import sokoban.Model.gameEngine.object.J_GraphicObject;
import sokoban.Model.message.I_MessageFactor;
import sokoban.Model.message.J_VictoryMessage;

import java.awt.*;
import java.util.Date;

/**
 *  <p>
 *      This class refreshes the stage while the player performs some actions.
 *  </p>
 *
 * @author Wendi Han - edited
 * @version 1.1
 * @see J_GameEngine
 * @see J_VictoryMessage#showMessage(J_GameEngine, Stage)
 * @see J_ReloadGird#addObjectToGrid(E_GameObject, Point, GridPane, J_Color)
 * @see J_GraphicObject#getGraphicObject(E_GameObject)
 */
public class J_ReloadGird {

    /**
     * <p>
     *     If game completes, set the end time and close the stage. Otherwise, update the pane.
     * </p>
     *
     * @param gameEngine  Game engine
     * @param gameGrid  A map of game show to the player
     * @param primaryStage A stage show to the player
     * @see J_VictoryMessage#showMessage(J_GameEngine, Stage)
     * @see J_ReloadGird#addObjectToGrid(E_GameObject, Point, GridPane, J_Color)
     * @since 1.1
     */
    public static void reloadGrid(J_GameEngine gameEngine, GridPane gameGrid, Stage primaryStage) {

        if (gameEngine.isGameComplete()) {
            gameEngine.setEndTime(new Date());
            try {
                I_MessageFactor message = new J_VictoryMessage();
                Stage stage = new Stage();
                stage.setX(primaryStage.getX());;
                stage.setY(primaryStage.getY());
                primaryStage.close();
                message.showMessage(gameEngine, stage);
                primaryStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        J_Level currentLevel = gameEngine.getCurrentLevel();
        J_Level.LevelIterator levelGridIterator = (J_Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition(), gameGrid, gameEngine.getColor());
        }
        gameGrid.autosize();
        primaryStage.sizeToScene();
    }

    /**
     * Add object to grid one by one.
     * @param gameObject  Object
     * @param location  Next position
     * @param gameGrid  Game grid
     * @param color  A list of color of different object
     * @see J_GraphicObject#getGraphicObject(E_GameObject)
     */
    public static void addObjectToGrid(E_GameObject gameObject, Point location, GridPane gameGrid, J_Color color) {
        J_GraphicObject graphicObject = new J_GraphicObject(color);
        gameGrid.add(graphicObject.getGraphicObject(gameObject), location.y, location.x);
    }

}
