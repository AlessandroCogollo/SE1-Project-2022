package it.polimi.ingsw.Enum;

/**
 * Errors code for the whole project divided in:
 * <ul>
 * <li> Global Code (various code for various scope) (code from 0 to 100)</li>
 * <li>Model Error (code from 201 to 300)</li>
 * <li>Server Error (code from 301 to 400)</li>
 * </ul>
 * Duplicated code are not valid
 */
public enum Errors {



    /*
      List of Global Error (code from 0 to 100)
     */



    /**
     * No error code
     */
    NO_ERROR (0, "No Error"),

    /**
     * Null pointer Code
     */
    NULL_POINTER (1, "Null Pointer"),



    /*
      List of Model Error (code from 201 to 300)
     */



    /**
     * Error description: The player who call this method isn't the current one
     */
    NOT_CURRENT_PLAYER(201, "The player who call this method isn't the current one"),
    /**
     * Error description: The call was made in the wrong phase
     */
    NOT_RIGHT_PHASE(202, "The call was made in the wrong phase"),
    /**
     * Error description: The player doesn't have this assistant
     */
    NO_SUCH_ASSISTANT (203, "The player doesn't have this assistant"),
    /**
     * Error description: The assistant was already played by another player and you have some playable card
     */
    ASSISTANT_ALREADY_PLAYED (204, "The assistant was already played by another player and you have some playable card"),
    /**
     * Error description: You can't move other student
     */
    NO_MORE_MOVEMENT(205, "You can't move other student"),
    /**
     * Error description: The Player doesn't have enough coins to play the character"
     */
    NOT_ENOUGH_COINS (206, "The Player doesn't have enough coins to play the character"),
    /**
     * Error description: Mother nature cannot move this much times
     */
    MOVEMENTS_TOO_HIGH (207, "Mother nature cannot move this much times"),
    /**
     * Error description: The player id doesn't exist
     */
    PLAYER_NOT_EXIST (208, "The player id doesn't exist"),
    /**
     * Error description: The number of movements aren't correct
     */
    MOVEMENT_ERROR (209, "The number of movements aren't correct"),
    /**
     * Error description: No more student of that color in the entrance or in the character card
     */
    NO_STUDENT (210, "No more student of that color in the entrance or in the character card"),
    /**
     * Error description: Max student of that color in the room
     */
    MAX_STUDENT_ROOM (211, "Max student of that color in the room"),
    /**
     * Error description: The player has used a character yet
     */
    CHARACTER_YET_PLAYED (212, "The player has used a character yet"),
    /**
     * Error description: The Assistant value doesn't exist
     */
    NOT_VALID_ASSISTANT (213, "The Assistant value doesn't exist"),
    /**
     * Error description: The color id isn't a correct type of color for students
     */
    NOT_VALID_COLOR (214, "The color id isn't a correct type of color for students"),
    /**
     * Error description: The destination is not valid
     */
    NOT_VALID_DESTINATION (215, "The destination is not valid"),
    /**
     * Error description: The Cloud doesn't exists
     */
    NO_SUCH_CLOUD (216, "The Cloud doesn't exists"),
    /**
     * Error description: The Character doesn't exists
     */
    NO_SUCH_CHARACTER (217, "The Character doesn't exists"),
    /**
     * Error description: There are not enough token to perform this action
     */
    NOT_ENOUGH_TOKEN (218, "There are not enough token to perform this action"),
    /**
     * Error description: The parameter for character passed aren't correct
     */
    NOT_RIGHT_PARAMETER (219, "The parameter for character passed aren't correct"),
    /**
     * Error description: The number of position moved by mother nature must be over or equal to 1 and less or equal than the value on the assistant card
     */
    MOVEMENT_NOT_VALID (220, "The number of position moved by mother nature must be over or equal to 1 and less or equal than the value on the assistant card"),
    /**
     * Error description: Expected an array with certain length, received an odd lenght/empty array/array with more than 4 elements
     */
    ILLEGAL_INPUT(221, "Expected an array with certain length, received an odd lenght/empty array/array with more than 4 elements"),
    /**
     * Error description: Cannot play a character in a normal game
     */
    INVALID_MOVE (222, "Cannot play a character in a normal game"),



    /*
      List of Server Error (code from 301 to 400)
     */



    /**
     * Error description: The server main thread has nothing to do
     */
    NOTHING_TODO (301, "The server main thread has nothing to do"),
    /**
     * Error description: All players have connected, the main server start the real game
     */
    CREATE_MODEL (302, "All players have connected, the main server start the real game"),
    /**
     * Error description: The game has to be stopped, due to a disconnection
     */
    PLAYER_DISCONNECTED (303, "The game has to be stopped, due to a disconnection"),
    /**
     * Error description: There is a winner and the server will be shutdown
     */
    GAME_OVER(304, "There is a winner and the server will be shutdown"),
    /**
     * Error description: There isn't any move in the model queue
     */
    MOVE_NOT_FIND (310, "There isn't any move in the model queue"),
    /**
     * Error description: Invalid number of player chosen
     */
    NUM_OF_PLAYER_ERROR (311, "Min 2 players, max 4 players");


    /**
     * Code of the error
     */
    private final int code;

    /**
     * Description of the error
     */
    private final String description;

    /**
     * Enum Constructor
     * @param code code
     * @param description description
     */
    Errors(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @return the description of the Error
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the code of the error
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code of the Error that is wanted
     * @return the Errors requested or null if the Error code is does not exists
     */
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
