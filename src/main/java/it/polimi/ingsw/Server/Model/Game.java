package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

import java.util.Iterator;
import java.util.Optional;


// This class is the interface towards the controller. The only methods tha can be invoked from the controller are the factory method for getting a Game Instance or and advanced one, and the method for the possible interactions of users to the model
public class Game{

    private final int numOfPlayer;

    protected final GameInitializer gameInit;
    protected final RoundHandler round;

    Game(int numOfPlayer, GameInitializer gameInit, RoundHandler round) {
        this.numOfPlayer = numOfPlayer;
        this.gameInit = gameInit;
        this.round = round;
    }

    public int playAssistant (int playerId, Assistant x){

        Player p = gameInit.getPlayerById(playerId);

        // check if it's a possible moves
        if (p != round.getCurrent())
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (!round.getPhase().equals(Phase.Planning))
            return Errors.NOT_RIGHT_PHASE.getCode();
        if (p.hasAssistant(x))
            return Errors.NO_SUCH_ASSISTANT.getCode();
        if (round.canPLayAssistant(p, x))
            return Errors.ASSISTANT_ALREADY_PLAYED.getCode();

        // call the correct method in player to modify the model
        p.playAssistant(x);

        // update the round handler
        round.next();

        return Errors.NO_ERROR.getCode();
    }

    public int moveStudents (int playerId, StudentsMovements s){

        Player p = gameInit.getPlayerById(playerId);

        if (p != round.getCurrent())
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (!round.getPhase().equals(Phase.Action) || !round.getActionPhase().equals(ActionPhase.MoveStudent))
            return Errors.NOT_RIGHT_PHASE.getCode();
        
        //check if there are the right number of movements
        if (numOfPlayer == 3)
            if (s.getNumberOfMoves() != 4)
                return Errors.MOVEMENT_ERROR.getCode();
        else
            if (s.getNumberOfMoves() != 3)
                return Errors.MOVEMENT_ERROR.getCode();

        //check if the player has all the student that he wants to move
        Iterator<StudentsMovements.Movement> iterator = s.iterator();
        StudentsMovements.Movement move;
        Color color;
        Optional<Cloud> cloud;
        boolean haveAllColor = true;
        while (iterator.hasNext() && haveAllColor){
            move = iterator.next();
            color = move.getColor();
            if (p.hasStudent(color))
                haveAllColor = false;
        }
        if (!haveAllColor)
            return Errors.NOT_ENOUGH_STUDENT.getCode();

        // call the player for move all the students one on one
        iterator = s.iterator();
        while (iterator.hasNext()){
            move = iterator.next();
            p.moveStudent(move);
        }

        round.next();

        return Errors.NO_ERROR.getCode();
    }

    public int moveMotherNature (int playerId, int position){

        Player p = gameInit.getPlayerById(playerId);

        if (p != round.getCurrent())
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (!round.getPhase().equals(Phase.Action) || !round.getActionPhase().equals(ActionPhase.MoveMotherNature))
            return Errors.NOT_RIGHT_PHASE.getCode();

        //todo assistant
        /*
        if (position > p.getActiveAssistant().getMaxMovement())
            return Errors.MOVEMENTS_TOO_HIGH.getCode();
        */

        //todo check that is a valid Island

        p.moveMotherNature(position);

        round.next();

        return Errors.NO_ERROR.getCode();
    }

    public int chooseCloud (int playerId, Cloud c){

        Player p = gameInit.getPlayerById(playerId);

        // check if it's a possible moves
        if (p != round.getCurrent())
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (!round.getPhase().equals(Phase.Action) || !round.getActionPhase().equals(ActionPhase.ChooseCloud))
            return Errors.NOT_RIGHT_PHASE.getCode();

        //todo check that is a valid Cloud

        p.chooseCloud(c);

        round.next();

        return Errors.NO_ERROR.getCode();
    }

    /*
    @ ensures (* if ids.length == 4 teammates has the same terminal bit in ids (codified in binary) and this final bit is different from the other team *);
    */
    static public Game getGameModel (int[] ids, int gameMode){

        //todo check ensures

        if (gameMode < 0 || gameMode > 1 || ids.length < 2 || ids.length > 4)
            return null;
        else {

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
}
