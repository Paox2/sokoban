package sokoban.Model.gameEngine.object;

import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.util.Duration;
import sokoban.Model.game.J_GameEngine;
import sokoban.Model.gameEngine.debug.J_Debug;
import sokoban.Model.log.J_GameLogger;

/**
 * This class create a 3D shape to the object in the map.
 *
 * @author Wendi Han
 * @version 1.2
 * @see HBox
 * @see E_GameObject
 * @see J_Color
 * @see J_ImageSingleton
 */
public class J_GraphicObject extends Rectangle {
    public HBox pictureRegion = new HBox();
    private final J_Color color;

    /**
     * Initialize color list and image list.
     *
     * @param color - A list of objects' color
     * @since 1.1
     */
    public J_GraphicObject(J_Color color) {
        this.color = color;
    }

    /**
     * Get the object of certain point in the map and return the 3D shape of this object.
     *
     * @param obj A type of E_GameObject
     * @return HBox - A 3D shape of this object
     * @since 1.1
     */
    public HBox getGraphicObject(E_GameObject obj) {
        Image image;

        try {
            if (obj == E_GameObject.DIAMOND) {
                if (J_Debug.getInstance().isDebugActive()) {
                    FadeTransition ft = new FadeTransition(Duration.seconds(1), this);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.2);
                    ft.setCycleCount(2);
                    ft.setAutoReverse(true);
                    ft.play();
                }
            }

            switch (obj) {
                case WALL:
                case PLAYER_ON_WALL:
                case PLAYER_ON_CRATE:
                case PLAYER_ON_DIAMOND:
                case PLAYER_ON_CRATE_ON_DIAMOND:
                case PLAYER_ON_FLOOR:
                case FLOOR:
                case CRATE:
                case DIAMOND:
                case KEEPER:
                case CRATE_ON_DIAMOND :
                case DEBUG_OBJECT:
                    image = J_ImageSingleton.getInstance()
                            .getList().get(obj.toString() + "_" + color.getObjects().get(obj));
                    break;

                case TARGET:
                    image = J_ImageSingleton.getInstance().getList().get(E_GameObject.FLOOR.toString()
                            + "_" + color.getObjects().get(E_GameObject.FLOOR));
                    break;

                case PIPELINE:
                case GHOST:
                case POOL:
                case PLAYER_ON_PIPELINE:
                    image = J_ImageSingleton.getInstance().getList().get(obj.toString());
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + obj);
            }
        } catch (Exception e) {
            String message = "Error in Level constructor. Object not recognized.";
            J_GameLogger.getInstance().severe(message);
            throw new AssertionError(message);
        }

        StackPane pane = get3DShape(obj, image);
        pictureRegion.getChildren().add(pane);

        return pictureRegion;
    }

    /**
     * Get 3D shape using image to represent object in the map.<br>
     * For the ghost and keeper we use sphere and for wall, floor, and other things we use box.<br>
     * If player is flying than add the keeper and shadow on the top of the object.
     *
     * @param image  Image is used to cover the object
     * @param obj  The object type we need to create.
     * @since 1.2
     */
    private StackPane get3DShape(E_GameObject obj, Image image) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        pictureRegion.getChildren().add(imageView);
        int depth = 7;
        StackPane pane = new StackPane();
        PhongMaterial material = new PhongMaterial();

        switch (obj) {
            case WALL:
            case CRATE:
            case DIAMOND:
            case DEBUG_OBJECT:
            case CRATE_ON_DIAMOND:
            case PLAYER_ON_CRATE_ON_DIAMOND:
            case PLAYER_ON_DIAMOND:
            case PLAYER_ON_WALL:
            case PLAYER_ON_CRATE:
                depth = 25;
            case FLOOR:
            case PLAYER_ON_FLOOR:
            case PIPELINE:
            case PLAYER_ON_PIPELINE:
            case TARGET:
            case POOL:
                if (obj == E_GameObject.WALL || obj == E_GameObject.PLAYER_ON_WALL) {
                    depth = 30;
                }
                Box box = new Box(25, 25, depth);

                material.setDiffuseMap(image);
                box.setMaterial(material);
                box.setTranslateZ(depth / 2 * -1);
                if (obj == E_GameObject.DIAMOND || obj == E_GameObject.PLAYER_ON_DIAMOND) {
                    ImageView floor = new ImageView();
                    floor.setImage(J_ImageSingleton.getInstance().getList()
                            .get("FLOOR_" + color.getObjects().get(obj)));
                    floor.setFitHeight(25);
                    floor.setFitWidth(25);
                    floor.setTranslateZ(-1);
                    pane.getChildren().addAll(floor, box, imageView);
                } else if(obj == E_GameObject.POOL) {
                    pane.getChildren().addAll(imageView);
                } else {
                    pane.getChildren().addAll(box, imageView);
                }
                break;

            case KEEPER:
            case GHOST:
                depth = 25;
                Sphere sphere = new Sphere(12);

                material.setDiffuseMap(image);
                sphere.setMaterial(material);
                sphere.setTranslateZ(depth / 2 * -1);
                sphere.setRotate(40);
                sphere.setRotationAxis(new Point3D(1,0,0));
                ImageView floor = new ImageView();
                floor.setImage(J_ImageSingleton.getInstance().getList()
                        .get("FLOOR_" + color.getObjects().get(E_GameObject.FLOOR)));
                floor.setFitHeight(25);
                floor.setFitWidth(25);
                floor.setTranslateZ(-1);
                pane.getChildren().addAll(floor, sphere, imageView);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + obj);
        }

        if(obj.isKeeperFlyObject()) {
            StackPane player = getPlayerShapeOnObject(depth);
            pane.getChildren().add(player);
        }

        return pane;
    }

    /**
     * Get the 3D player flying on the top of the object
     *
     * @param depth  The depth of 3D shape below the player.
     * @since 1.2
     */
    private StackPane getPlayerShapeOnObject(int depth) {
        StackPane pane = new StackPane();
        Image imagePlayer = J_ImageSingleton.getInstance()
                .getList().get("KEEPER_" + color.getObjects().get(E_GameObject.KEEPER));
        ImageView shadow = new ImageView(J_ImageSingleton.getInstance().getList().get("SHADOW"));
        shadow.setTranslateZ(-depth-1);
        depth = 25;
        Sphere sphere = new Sphere(12);
        PhongMaterial material1 = new PhongMaterial();
        material1.setDiffuseMap(imagePlayer);
        sphere.setMaterial(material1);
        sphere.setTranslateZ(depth / 2 * -1);
        sphere.setRotate(40);
        sphere.setRotationAxis(new Point3D(1,0,0));
        sphere.setTranslateZ(-40);
        pane.getChildren().addAll(shadow,sphere);
        return pane;
    }
}