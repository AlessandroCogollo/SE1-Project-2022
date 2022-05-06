package it.polimi.ingsw.Enum;

public enum Move {
    PLAY_ASSISTANT (1, "Choose an Assistant not played by the other player, if all assistant that you have are played, you can play any assistant anyway."),
    MOVE_STUDENT (2, "Choose a students from your entrance and move it to your room or to an island."),
    MOVE_MOTHER_NATURE (3, "Choose the number of islands that Mother Nature should pass."),
    CHOOSE_CLOUDS (4, "Choose the clouds for add his students to your entrance"),
    PLAY_CHARACTER (5, "Play the character using your coins.");

    private final int code;
    private final String description;

    Move(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
