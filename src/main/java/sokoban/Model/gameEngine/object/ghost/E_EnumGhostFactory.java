package sokoban.Model.gameEngine.object.ghost;

/**
 *  <p>
 *      A ghost factory contains different type of ghost.
 *      All the ghost need to implement their own {@code create()} method.
 *  </p>
 *
 * @author Wendi Han - edited
 * @version 1.1
 */
public enum E_EnumGhostFactory {
    /** Ghost disappear */
    DISAPPEARGHOST(0) {
        @Override
        public I_GhostFactory create() {
            return new J_DisappearGhost();
        }
    },
    /** A ghost will reset current level */
    RESETLEVELGHOST(1){
        @Override
        public I_GhostFactory create() {
            return new J_ResetLevelGhost();
        }
    },
    /** A ghost will reset the start time of current level */
    RESETTIMEGHOST(2) {
         @Override
        public I_GhostFactory create() {
             return new J_ResetTimeGhost();
         }
    },
    /** A ghost will decrease moves count by 10*/
    MOVESUBTENGHOST(3) {
        @Override
        public I_GhostFactory create() {
            return new J_MoveSubTenGhost();
        }
    },
    /** A ghost will increase moves count by 10*/
    MOVEADDTENGHOST(4) {
        @Override
        public I_GhostFactory create() {
            return new J_MoveAddTenGhost();
        }
    },
    /** A ghost will close the music */
    CLOSEMUSICGHOST(5){
        @Override
        public I_GhostFactory create() {
            return new J_StopMusicGhost();
        }
    },
    /** A ghost can give the player the ability to fly*/
    FLYGHOST(6){
        @Override
        public I_GhostFactory create() {
            return new J_FlyGhost();
        }
    };

    private final int symbol;

    /**
     * Constructor is used to assign a value to symbol.
     * @param symbol  the symbol represent a ghost
     * @since 1.1
     */
    E_EnumGhostFactory(final int symbol) {
        this.symbol = symbol;
    }

    /**
     * Get the object from the int.
     * @param c  The symbol of the ghost
     * @return ghost
     * @since 1.1
     */
    public static E_EnumGhostFactory fromInt(int c) {
        for (E_EnumGhostFactory t : E_EnumGhostFactory.values()) {
            if(c == t.symbol) {
                return t;
            }
        }
        return RESETLEVELGHOST;
    }

    /**
     * Implemented by each ghost object to create a instance of certain type of ghost.
     * @return I_Ghost - A instance of certain type of ghost.
     * @since 1.1
     */
    public abstract I_GhostFactory create();
}
