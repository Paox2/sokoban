package sokoban.Model.gameEngine.object;

import java.util.HashMap;

/**
 * This class saves the color of each object.
 *
 * @author Wendi Han
 * @version 1.1
 * @see HashMap
 * @see E_GameObject
 */
public class J_Color {
    private HashMap<E_GameObject, String> objects = new HashMap<>();

    /**
     * Initialize the color of each object and save in the list.
     *
     * @since 1.1
     * @see HashMap
     */
    public J_Color() {
        objects.put(E_GameObject.WALL, "GREY");
        objects.put(E_GameObject.CRATE, "BROWN");
        objects.put(E_GameObject.CRATE_ON_DIAMOND, "BROWN");
        objects.put(E_GameObject.DIAMOND, "GREY");
        objects.put(E_GameObject.FLOOR, "GREY");

        objects.put(E_GameObject.PLAYER_ON_WALL, "GREY");
        objects.put(E_GameObject.PLAYER_ON_CRATE, "BROWN");
        objects.put(E_GameObject.PLAYER_ON_CRATE_ON_DIAMOND, "BROWN");
        objects.put(E_GameObject.PLAYER_ON_DIAMOND, "GREY");
        objects.put(E_GameObject.PLAYER_ON_FLOOR, "GREY");
        objects.put(E_GameObject.KEEPER, "D");
    }

    /**
     * Change the color of certain object.
     *
     * @param obj  Object
     * @param color New Color
     * @since 1.1
     */
    public void changeColor(E_GameObject obj, String color) {
        switch (obj) {
            case CRATE -> {
                objects.put(E_GameObject.PLAYER_ON_CRATE_ON_DIAMOND, color);
                objects.put(E_GameObject.PLAYER_ON_CRATE, color);
                objects.put(E_GameObject.CRATE_ON_DIAMOND, color);
                objects.put(obj, color);
            }
            case FLOOR -> {
                objects.put(E_GameObject.DIAMOND, color);
                objects.put(E_GameObject.PLAYER_ON_FLOOR, color);
                objects.put(E_GameObject.PLAYER_ON_DIAMOND, color);
                objects.put(obj, color);
            }
            case WALL -> {
                objects.put(E_GameObject.PLAYER_ON_DIAMOND, color);
                objects.put(obj, color);
            }
            default -> objects.put(obj, color);
        }
    }

    /**
     * Return the color list.
     *
     * @return The color list
     * @since 1.1
     */
    public HashMap<E_GameObject, String> getObjects() {
        return objects;
    }
}
