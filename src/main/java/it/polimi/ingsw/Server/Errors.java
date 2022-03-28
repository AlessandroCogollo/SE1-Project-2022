package it.polimi.ingsw.Server;

public enum Errors {
    NO_ERROR (0, "No Error"),
    NOT_CURRENT_PLAYER(1, "The player who call this method isn't the current one"),
    NOT_RIGHT_PHASE(2, "The call was made in the wrong phase"),
    NO_SUCH_ASSISTANT (3, "The player doesn't have this assistant"),
    ASSISTANT_ALREADY_PLAYED (4, "The assistant was already played by another player and you have some playable card"),
    NOT_ENOUGH_STUDENT(5, "You don't have the student that you move"),
    NOT_ENOUGH_COINS (6, "the Player doesn't have enough coins to play the character"),
    MOVEMENTS_TOO_HIGH (7, "Mother nature cannot move this much times"),
    MOVEMENT_ERROR (8, "The number of movements aren't correct");


    private final int code;
    private final String description;

    private Errors(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Error " + code + ": " + description;
    }
}
