package it.polimi.ingsw.Enum;

//todo upscale error code and description
public enum Errors {
    NO_ERROR (0, "No Error"),
    NULL_POINTER (100, "Null Pointer"),
    NOT_CURRENT_PLAYER(1, "The player who call this method isn't the current one"),
    NOT_RIGHT_PHASE(2, "The call was made in the wrong phase"),
    NO_SUCH_ASSISTANT (3, "The player doesn't have this assistant"),
    ASSISTANT_ALREADY_PLAYED (4, "The assistant was already played by another player and you have some playable card"),
    NO_MORE_MOVEMENT(5, "You can't move other student"),
    NOT_ENOUGH_COINS (6, "the Player doesn't have enough coins to play the character"),
    MOVEMENTS_TOO_HIGH (7, "Mother nature cannot move this much times"),
    PLAYER_NOT_EXIST (8, "The player id doesn't exist"),
    MOVEMENT_ERROR (9, "The number of movements aren't correct"),
    NO_STUDENT (10, "No more student of that color in the entrance or in the character card"),
    MAX_STUDENT_ROOM (11, "Max student of that color in the room"),
    CHARACTER_YET_PLAYED (12, "The player has used a character yet"),
    NOT_VALID_ASSISTANT (13, "The Assistant value doesn't exist"),
    NOT_VALID_COLOR (14, "The color id isn't a correct type of color for students"),
    NOT_VALID_DESTINATION (15, "The destination is not valid"),
    NO_SUCH_CLOUD (16, "The Cloud doesn't exists"),
    NO_SUCH_CHARACTER (17, "The Character doesn't exists"),
    NOT_ENOUGH_TOKEN (18, "There are not enough token to perform this action"),
    NOT_RIGHT_PARAMETER (19, "The parameter for character passed aren't correct"),
    MOVEMENT_NOT_VALID (20, "the number of position moved by mother nature must be over or equal to 1 and less or equal than the value on the assistant card"),
    ILLEGAL_INPUT(21, "Expected an array with certain length, received an odd lenght/empty array/array with more than 4 elements"),
    INVALID_MOVE (22, "Cannot play a character in a normal game"),
    MOVE_NOT_FIND (201, "There isn't any move in the model queue");

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

    public static Errors getErrorsByCode(int code){
        for (Errors e: Errors.values())
            if (e.getCode() == code)
                return e;
        return null;
    }

    @Override
    public String toString() {
        return "Error " + code + ": " + description;
    }
}
