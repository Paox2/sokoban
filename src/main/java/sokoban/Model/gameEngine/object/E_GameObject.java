package sokoban.Model.gameEngine.object;

import sokoban.Model.gameEngine.object.ghost.I_GhostFactory;

/**
 * This class contains a list of object in the map and show the relation between object and character in the map.
 *
 * @author Wendi Han - edited
 * @version 1.2
 * @see I_GhostFactory
 */
public enum E_GameObject{
    /** The wall in the map*/
    WALL('W'),
    /** The floor in the map*/
    FLOOR(' '),
    /** The crate in the map*/
    CRATE('C'),
    /** The diamond(put the crate on it) in the map*/
    DIAMOND('D'),
    /** The keeper(player)*/
    KEEPER('S'),
    /** Can teleport to another place*/
    PIPELINE('P'),
    /** The diamond has already put on the diamond*/
    CRATE_ON_DIAMOND('O'),
    /** Use this pattern under debug pattern*/
    DEBUG_OBJECT('='),
    /** The target of teleport when player uses pipeline*/
    TARGET('T'),
    /** A type of ghost*/
    GHOST('G'),
    /** PLAYER fly on the wall*/
    PLAYER_ON_WALL('L'),
    /** PLAYER fly on the crate*/
    PLAYER_ON_CRATE('A'),
    /** Player fly on the diamond*/
    PLAYER_ON_DIAMOND('Y'),
    /** Player fly on the crate_on_diamond*/
    PLAYER_ON_CRATE_ON_DIAMOND('E'),
    /** Player fly on the pipeline*/
    PLAYER_ON_PIPELINE('R'),
    /** Player fly on the floor*/
    PLAYER_ON_FLOOR('N'),
    /** Player fly on the poor*/
    PLAYER_ON_POOL('*'),
    /** POOL*/
    POOL('-');

    private final char symbol;

    /**
     * Constructor is used to assign a value to symbol.
     * @param symbol  the symbol of object in the map file
     * @since 1.1
     */
    E_GameObject(final char symbol) {
        this.symbol = symbol;
    }

    /**
     * Get the object from the character.
     * @param c  the char used to represent related object
     * @return E_GameObject
     * @since 1.1
     */
    public static E_GameObject fromChar(char c) {
        for (E_GameObject t : E_GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {
                return t;
            }
        }

        return WALL;
    }

    /**
     * Get the character from the object.
     *
     * @return char
     * @since 1.1
     */
    public char getCharSymbol() {
        return symbol;
    }

    /**
     * Return the name of object based on its symbol.
     *
     * @author Wendi Han
     * @return String
     * @since 1.1
     * @see Object#toString()
     */
    @Override
    public String toString() {
        String obj = "";
        switch (symbol){
            case 'W' :
            case 'L' :
                obj = "WALL";
                break;
            case ' ' :
            case 'N' :
                obj = "FLOOR";
                break;
            case 'C' :
            case 'A' :
                obj = "CRATE";
                break;
            case 'D' :
            case 'Y' :
                obj = "DIAMOND";
                break;
            case 'S' :
                obj = "KEEPER";
                break;
            case 'P' :
            case 'R' :
                obj = "PIPELINE";
                break;
            case 'T' :
                obj = "TARGET";
                break;
            case 'O' :
            case 'E' :
                obj = "CRATE_ON_DIAMOND";
                break;
            case 'G' :
                obj = "GHOST";
                break;
            case '=' :
                obj = "DEBUG_OBJECT";
                break;
            case '-' :
            case '*' :
                obj = "POOL";
                break;
            default:
                break;
        }
        return obj;
    }

    /**
     * Return {@code ture} if object is player flying on it,
     *
     * @return {@code ture} if object is player flying on it
     * @since 1.2
     */
    public boolean isKeeperFlyObject() {
        switch (this) {
            case PLAYER_ON_CRATE:
            case PLAYER_ON_CRATE_ON_DIAMOND:
            case PLAYER_ON_DIAMOND:
            case PLAYER_ON_PIPELINE:
            case PLAYER_ON_WALL:
            case PLAYER_ON_FLOOR:
            case PLAYER_ON_POOL:
                return true;
            default:
                return false;
        }
    }

    /**
     * Return the object that player fly on it or not, based on the state of player
     *
     * @return The object that player fly on it or not, based on the state of player
     * @since 1.2
     */
    public E_GameObject getPlayerOnObject() {
        switch (this) {
            case PLAYER_ON_CRATE:
                return E_GameObject.CRATE;
            case PLAYER_ON_CRATE_ON_DIAMOND:
                return E_GameObject.CRATE_ON_DIAMOND;
            case PLAYER_ON_DIAMOND:
                return E_GameObject.DIAMOND;
            case PLAYER_ON_PIPELINE:
                return E_GameObject.PIPELINE;
            case PLAYER_ON_WALL:
                return E_GameObject.WALL;
            case PLAYER_ON_FLOOR:
                return E_GameObject.FLOOR;
            case PLAYER_ON_POOL:
                return E_GameObject.POOL;
            case CRATE:
                return E_GameObject.PLAYER_ON_CRATE;
            case CRATE_ON_DIAMOND:
                return E_GameObject.PLAYER_ON_CRATE_ON_DIAMOND;
            case DIAMOND:
                return E_GameObject.PLAYER_ON_DIAMOND;
            case PIPELINE:
                return E_GameObject.PLAYER_ON_PIPELINE;
            case WALL:
                return E_GameObject.PLAYER_ON_WALL;
            case FLOOR:
                return E_GameObject.PLAYER_ON_FLOOR;
            case POOL:
                return E_GameObject.PLAYER_ON_POOL;
        }
        return null;
    }
}