package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;


// This class is the interface towards the controller. It also check if the move from player/controller are valid. The only methods tha can be invoked from the controller are the factory method for getting a Game Instance or and advanced one, and the method for the possible interactions of users to the model
public class Game{

    private final int numOfPlayer;

    protected final GameInitializer gameInit;
    protected final RoundHandler round;

    Game (int numOfPlayer, GameInitializer gameInit, RoundHandler round) {
        this.numOfPlayer = numOfPlayer;
        this.gameInit = gameInit;
        this.round = round;
    }

    public int playAssistant (int playerId, Assistant x){

        if (!gameInit.existsPlayer(playerId))
            return Errors.PLAYER_NOT_EXIST.getCode();
        if (x == null)
            return Errors.NULL_POINTER.getCode();

        Player p = gameInit.getPlayerById(playerId);

        // check if it's a possible moves
        if (!p.equals(round.getCurrent()))
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (!round.getPhase().equals(Phase.Planning))
            return Errors.NOT_RIGHT_PHASE.getCode();
        if (!p.hasAssistant(x))
            return Errors.NO_SUCH_ASSISTANT.getCode();
        if (!round.canPLayAssistant(p, x))
            return Errors.ASSISTANT_ALREADY_PLAYED.getCode();

        // call the correct method in player to modify the model
        p.playAssistant(x);

        // update the round handler
        round.next();

        return Errors.NO_ERROR.getCode();
    }

    public int moveStudents (int playerId, Movement m){

        if (!gameInit.existsPlayer(playerId))
            return Errors.PLAYER_NOT_EXIST.getCode();
        if (m == null)
            return Errors.NULL_POINTER.getCode();

        Player p = gameInit.getPlayerById(playerId);

        if (!p.equals(round.getCurrent()))
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (!round.getPhase().equals(Phase.Action) || !round.getActionPhase().equals(ActionPhase.MoveStudent))
            return Errors.NOT_RIGHT_PHASE.getCode();

        //check if the player can still move student
        int movementDone = round.getStudentMovedInThisTurn();
        if (((numOfPlayer == 2 || numOfPlayer == 4) && movementDone >= 3) || (numOfPlayer == 3 && movementDone >= 4))
            return Errors.NO_MORE_MOVEMENT.getCode();

        //check if the player has the student that he wants to move
        if (p.hasStudent(m.getColor()))
            return Errors.NO_STUDENT.getCode();

        //check if student has already 10 student of that color in the room
        if (p.getNumberOfStudentInRoomByColor(m.getColor()) == 10)
            return Errors.MAX_STUDENT_ROOM.getCode();

        //todo check for Island, need GameBoard

        // call the player for move the student
        p.moveStudent(m);

        round.next();

        return Errors.NO_ERROR.getCode();
    }

    public int moveMotherNature (int playerId, int position){

        if (!gameInit.existsPlayer(playerId))
            return Errors.PLAYER_NOT_EXIST.getCode();

        Player p = gameInit.getPlayerById(playerId);

        if (!p.equals(round.getCurrent()))
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (!round.getPhase().equals(Phase.Action) || !round.getActionPhase().equals(ActionPhase.MoveMotherNature))
            return Errors.NOT_RIGHT_PHASE.getCode();
        if (position > p.getActiveAssistant().getMaxMovement())
            return Errors.MOVEMENTS_TOO_HIGH.getCode();

        p.moveMotherNature(position);

        round.next();

        return Errors.NO_ERROR.getCode();
    }

    public int chooseCloud (int playerId, Cloud c){

        if (!gameInit.existsPlayer(playerId))
            return Errors.PLAYER_NOT_EXIST.getCode();
        //todo check exist cloud, need Cloud class

        Player p = gameInit.getPlayerById(playerId);

        // check if it's a possible moves
        if (!p.equals(round.getCurrent()))
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (!round.getPhase().equals(Phase.Action) || !round.getActionPhase().equals(ActionPhase.ChooseCloud))
            return Errors.NOT_RIGHT_PHASE.getCode();

        p.chooseCloud(c);

        round.next();

        return Errors.NO_ERROR.getCode();
    }

    //return the correct interface for the parameter passed, in case of four player the teammates need to have the same final bit (in decimal same team all odds or all even, and the other team the opposite)
    static public Game getGameModel (int[] ids, int gameMode){

        //check game mode and number of player
        if (gameMode < 0 || gameMode > 1 || ids.length < 2 || ids.length > 4)
            return null;

        //check same id
        boolean sameId = false;
        for (int i = 0; i < ids.length && !sameId; i++) {
            for (int j = i + 1; j < ids.length && !sameId; j++){
                if (ids[i] == ids[j])
                    sameId = true;
            }
        }
        if (sameId){
            return null;
        }

        //check odd and even for 4 player game
        if (ids.length == 4){
            int teamA = 0, teamB = 0;
            for (int i = 0; i < 4; i++){
                if (ids[i] % 2 == 0){
                    teamA++;
                }
                else
                    teamB++;
            }
            if (teamA != 2 || teamB != 2)
                return null;
        }

        //game initializer create all the model
        GameInitializer gInit = new GameInitializer(ids, gameMode);

        //round handler is used for track the phase, the round, and the cycle of them
        RoundHandler roundHandler = new RoundHandler(gInit);

        //advanced or normal game
        if (gameMode == 0)
            return new Game(ids.length, gInit, roundHandler);
        else
            return new AdvancedGame(ids.length, gInit, roundHandler);
    }
}
