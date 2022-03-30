package it.polimi.ingsw.Server;

public enum Errors {
    NO_ERROR (0, "No Error"),
    NOT_CURRENT_PLAYER(1, "The player who call this method isn't the current one"),
    NOT_RIGHT_PHASE(2, "The call was made in the wrong phase"),
    NO_SUCH_ASSISTANT (3, "The player doesn't have this assistant"),
    ASSISTANT_ALREADY_PLAYED (4, "The assistant was already played by another player and you have some playable card"),
    NO_MORE_MOVEMENT(5, "You can't move other student"),
    NOT_ENOUGH_COINS (6, "the Player doesn't have enough coins to play the character"),
    MOVEMENTS_TOO_HIGH (7, "Mother nature cannot move this much times"),
    PLAYER_NOT_EXIST (8, "The player id doesn't exist"),
    MOVEMENT_ERROR (9, "The number of movements aren't correct"),
    NO_STUDENT (10, "No more student of that color in the entrance"),
    NULL_POINTER (11, "Null Pointer"),
    MAX_STUDENT_ROOM (12, "Max student of that color in the room"),
    CHARACTER_YET_PLAYED (13, "The player has used a character yet");


    private final int code;
    private final String description;

    Errors(int code, String description) {
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
