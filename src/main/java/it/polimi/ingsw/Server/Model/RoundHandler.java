package it.polimi.ingsw.Server.Model;

import java.util.*;


//this class cycle throw the phase and the player to keep track of all moves done, needs to run start() in order to start the game
class RoundHandler {

    private final GameInitializer gInit;

    //except for the 1' round the planning phase is always equal to the last action phase but considering that the queue uses a destructive getters we need one queue for all the 2 phase
    private final Queue<Player> actionOrder; // the order during the action phase
    private final Queue<Player> planningOrder; // the order during the planning phase
    private final Queue<Player> specialOrder; // for track the case when a player has all the assistant equals to the others

    // the current state of the game
    private Phase phase;
    private ActionPhase actionPhase;
    private Player current;
    private int studentMovedInThisTurn;
    private boolean isFinalRound;

    // random for the initial player
    private final Random rand;

    RoundHandler(GameInitializer gameInitializer) {
        this.current = null;
        this.rand = new Random(System.currentTimeMillis());

        this.actionOrder = new LinkedList<>();
        this.planningOrder = new LinkedList<>();
        this.specialOrder = new LinkedList<>();
        this.gInit = gameInitializer;
        this.studentMovedInThisTurn = 0;
        this.isFinalRound = false;
    }

    Player getCurrent() {
        return current;
    }

    Phase getPhase() {
        return phase;
    }

    ActionPhase getActionPhase() {
        return actionPhase;
    }

    int getStudentMovedInThisTurn() {
        return studentMovedInThisTurn;
    }

    boolean getIsFinalRound() {
        return isFinalRound;
    }

    void setFinalRound() {
        isFinalRound = true;
    }

    //return true if the player can play the assistant considering all the exception
    boolean canPLayAssistant (Player p, Assistant x){

        boolean canPlay = true;
        Collection<Assistant> assistantPlayed = new ArrayList<>();

        // iterating over all player != p
        for (Player player : gInit) {
            if (!p.equals(player)) {

                Assistant temp = player.getActiveAssistant();

                //setting up a temporary list for later
                assistantPlayed.add(temp);


                // if one other player has a not null assistant that is equal to the one that the player p wants to play (x) the boolean is set to false for now
                if (temp != null && temp.equals(x))
                    canPlay = false;
            }
        }

        //if the assistant x is unique return true
        if (canPlay)
            return true;

        // test for all equals assistant
        boolean allEqual = true;

        //cycle over all assistants in p's deck
        for (Assistant a: p){

            //if there is an assistant in p's deck that isn't played he can play that one instead of x Assistant
            if (!assistantPlayed.contains(a)){
                allEqual = false;
                break;
            }
        }

        //if we found that the player can actually play the assistant x we add the player in the special order, and we return true
        if (allEqual) {
            specialOrder.add(p);
            return true;
        }
        return false;
    }

    //set the initial state
    void start (){
        //initial phase
        phase = Phase.Planning;
        actionPhase = ActionPhase.NotActionPhase;

        //set the initial order

        //get a random player
        int randomId = getRandomPlayerId();
        Player p = gInit.getPlayerById(randomId);


        LinkedList<Player> temp = new LinkedList<>();

        //we insert first the one extracted, and all the player before him in the iterator gInit will be added to the temp queue in the correct order
        boolean insertedFirst = false;
        for (Player t: gInit){
            if (!insertedFirst) {
                if (t.equals(p)) {
                    planningOrder.add(t);
                    insertedFirst = true;
                } else {
                    temp.add(t);
                }
            }
            else
                planningOrder.add(t);
        }

        // now we add all the player in temp to the right queue
        while (!temp.isEmpty()){
            planningOrder.add(temp.poll());
        }

        //we set the current player with the one extracted
        current = planningOrder.poll();
    }

    //cycle between the phase
    void next (){

        //planning phase
        if (phase.equals(Phase.Planning)){

            //during the planning phase we iterate before throw player and then to the next phase

            //change to action phase
            if (planningOrder.isEmpty()) {
                setOrder();
                phase = Phase.Action;
                actionPhase = ActionPhase.MoveStudent;
                current = actionOrder.poll();
            }
            //or next player
            else
                current = planningOrder.poll();
        }

        //action phase
        else {

            //during the action phase we iterate before throw action phase and then to the next player
            switch (actionPhase){
                case NotActionPhase:
                    //win
                    break;
                case MoveStudent:
                    studentMovedInThisTurn++;
                    int playerNum = gInit.getPlayersNumber();
                    if (((playerNum == 2 || playerNum == 4) && studentMovedInThisTurn == 3) || (playerNum == 3 && studentMovedInThisTurn == 4)){
                        actionPhase = ActionPhase.MoveMotherNature;
                        studentMovedInThisTurn = 0;
                    }
                    break;
                case MoveMotherNature:
                    actionPhase = ActionPhase.ChooseCloud;
                    break;
                case ChooseCloud:
                    gInit.getBoard().playCharacter(null, null); //reset active character

                    //return to planning
                    if (actionOrder.isEmpty()) {
                        if (isFinalRound){
                            phase = Phase.Action;
                            actionPhase = ActionPhase.NotActionPhase;
                            gInit.checkWin();
                            break;
                        }
                        phase = Phase.Planning;
                        actionPhase = ActionPhase.NotActionPhase;
                        resetActiveAssistants();
                        gInit.getBoard().populateClouds();
                        current = planningOrder.poll();
                    }
                    //or to the next player and the next action phase
                    else {
                        current = actionOrder.poll();
                        actionPhase = ActionPhase.MoveStudent;
                    }
                    break;
            }
        }
    }

    //select a random player id
    private int getRandomPlayerId (){
        int[] ids = new int[gInit.getPlayersNumber()];
        int i = 0;
        Player p;
        for (Player player : gInit) {
            p = player;
            ids[i] = p.getId();
            i++;
        }
        return ids[rand.nextInt(gInit.getPlayersNumber())];
    }

    private void resetActiveAssistants() {
        for (Player p: gInit)
            p.playAssistant(null);
    }

    //set the order of the player during the next action phase and the following planning phase thanks to the active assistant of the players
    private void setOrder(){
        final int id = 0;
        final int value = 1;

        //we create a matrix 2xNumberOfPlayers with in the first row all the ids and in the second row alla the value of their active assistant
        int[][] arr = new int[2][gInit.getPlayersNumber()];
        int i = 0;
        Player x;
        for (Player player : gInit) {
            x = player;
            arr[id][i] = x.getId();
            arr[value][i] = x.getActiveAssistant().getValue();
            i++;
        }

        //we sort the column of the matrix  comparing the value of the active assistant
        sortMatrixColumn(arr, value);

        //special case when a player has the same assistant as the others
        if (!specialOrder.isEmpty()){
            Player temp;
            int tempId;
            while (!specialOrder.isEmpty()){

                //we select the one in the special queue
                temp = specialOrder.poll();
                tempId = temp.getId();

                //and we search his id in the matrix saving his index i
                i = 0;
                while (arr[id][i] != tempId){
                    i++;
                }

                //considering that the matrix is ordered from the one with the smallest value to the one with the max value, player with the same value are in a random order, so to make that the first one who has played the assistant is the first, for all the others players in this special queue (the first that has play the assistant isn't in the queue) we shift them to the last position with the same value. So, int this way also for multiple player with the same assistant we keep the correct order
                while (arr[value][i] == arr[value][i + 1] && i < gInit.getPlayersNumber() - 1){
                    swap (arr, i, i + 1);
                    i++;
                }
            }
        }

        //now in all cases in the matrix there's the correct order, so we insert it in each queue
        for (i = 0; i < gInit.getPlayersNumber(); i++){
            actionOrder.add(gInit.getPlayerById(arr[id][i]));
            planningOrder.add(gInit.getPlayerById(arr[id][i]));
        }
    }

    //sort int ascending order the matrix column considering only the index passed as parameter in O(n^2)
    static void sortMatrixColumn (int[][] matrix, int rowToCompare){
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = i + 1; j < matrix[0].length; j++) {
                if (matrix[rowToCompare][i] > matrix[rowToCompare][j]) {
                    swap(matrix, i, j);
                }
            }
        }
    }

    //swap two column of a matrix with 2 row
    static void swap (int[][] matrix, int index1, int index2){
        int tmp0, tmp1;
        tmp0 = matrix[0][index1];
        tmp1 = matrix[1][index1];
        matrix[0][index1] = matrix[0][index2];
        matrix[1][index1] = matrix[1][index2];
        matrix[0][index2] = tmp0;
        matrix[1][index2] = tmp1;
    }

    //revert the column order of a matrix 2xn
    static void revertMatrix (int[][] matrix, int columnNumber){
        for (int i = 0; i < (columnNumber / 2); i++){
            RoundHandler.swap(matrix, i, (columnNumber - 1) - i);
        }
    }
}
