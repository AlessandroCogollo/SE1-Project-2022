package it.polimi.ingsw.Enum;

/**
 * The possible move that a player can do
 */
public enum Move {
    /**
     * Play Assistant: Choose an Assistant not played by the other player, if all assistant that you have are played, you can play any assistant anyway.
     */
    PLAY_ASSISTANT (1, "Choose an Assistant not played by the other player, if all assistant that you have are played, you can play any assistant anyway."),
    /**
     * Move Students: Choose a students from your entrance and move it to your room or to an island. You can do it 3 times in a turn in a 2 or 4 game, 4 times in a turn in a 3 game.
     */
    MOVE_STUDENT (2, "Choose a students from your entrance and move it to your room or to an island."),
    /**
     * Move Mother Nature: Choose the number of islands that Mother Nature should pass.
     */
    MOVE_MOTHER_NATURE (3, "Choose the number of islands that Mother Nature should pass."),
    /**
     * Choose Clouds: Choose the clouds for add his students to your entrance
     */
    CHOOSE_CLOUDS (4, "Choose the clouds for add his students to your entrance"),
    /**
     * Play Character: Play the character using your coins. Can only be used in an advanced Game, if used in a normal game return an error.
     */
    PLAY_CHARACTER (5, "Play the character using your coins.");

    /**
     * Code of the move
     */
    private final int code;
    /**
     * Description of the move
     */
    private final String description;

    /**
     * Enum Constructor
     * @param code code
     * @param description description
     */
    Move(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     *
     * @return the code of the move
     */
    public int getCode() {
        return code;
    }

    /**
     *
     * @return the description of the move
     */
    public String getDescription() {
        return description;
    }
}
