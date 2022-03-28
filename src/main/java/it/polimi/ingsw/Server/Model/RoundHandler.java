package it.polimi.ingsw.Server.Model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


//this class cycle throw the phase and the player to keep track of all moves done, needs to run start() in order to start the game
class RoundHandler {

    private final GameInitializer gInit; //for have the pointers to player

    //except for the 1 round the planning phase is always equal to the las action phase but considering that the queue uses a destructive getters we need one queue for all the 2 phase
    private final Queue<Player> actionOrder; // the order during the action phase
    private final Queue<Player> planningOrder; // the order during the planning phase
    private final Queue<Player> specialOrder; // for track the case when a player has all the assistant equals to the others

    // the current state of the game
    private Phase phase;
    private ActionPhase actionPhase;
    private Player current;

    // random for the initial player
    private Random rand;

    RoundHandler(GameInitializer gameInitializer) {
        this.current = null;
        this.rand = new Random(System.currentTimeMillis());
        this.actionOrder = new LinkedList<>();
        this.planningOrder = new LinkedList<>();
        this.specialOrder = new LinkedList<>();
        this.gInit = gameInitializer;
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

    boolean canPLayAssistant (Player p, Assistant x){
        boolean canPlay = true;
        Player i;
        Iterator<Player> iterator = gInit.iterator();
        while (iterator.hasNext()){
            i = iterator.next();
            if (!p.equals(i)){
                Assistant temp = i.getActiveAssistant();
                if (temp != null && temp.equals(x)){
                    canPlay = false;
                }
            }
        }
        if (canPlay)
            return true;

        boolean allEqual = true;
        for (Assistant t: Assistant.values()){
            if (p.hasAssistant(t) && !x.equals(t)){
                iterator = gInit.iterator();
                while (iterator.hasNext()){
                    i = iterator.next();
                    if (!p.equals(i)){
                        Assistant temp = i.getActiveAssistant();
                        if (temp != null && !temp.equals(t)){
                            allEqual = false;
                            break;
                        }
                    }
                }
            }
            if (!allEqual)
                break;
        }
        if (allEqual) {
            specialOrder.add(p);
            return true;
        }
        return false;
    }

    void start (){
        phase = Phase.Planning;
        actionPhase = ActionPhase.NotActionPhase;

        int randomId = getRandomPlayerId();
        Player p = gInit.getPlayerById(randomId);
        LinkedList<Player> temp = new LinkedList<>();
        Iterator<Player> iterator = gInit.iterator();
        while (iterator.hasNext()){
            Player t = iterator.next();
            if (t.equals(p)){
                planningOrder.add(t);
                break;
            }
            else{
                temp.add(t);
            }
        }
        while (iterator.hasNext()){
            planningOrder.add(iterator.next());
        }
        while (!temp.isEmpty()){
            planningOrder.add(temp.poll());
        }
        current = planningOrder.poll();
    }

    void next (){
        if (phase.equals(Phase.Planning)){
            if (actionOrder.isEmpty()) {
                setOrder();
                phase = Phase.Action;
                actionPhase = ActionPhase.MoveStudent;
            }
            current = actionOrder.poll();
        }
        else {
            switch (actionPhase){
                case NotActionPhase:
                    //error not possible
                    break;
                case MoveStudent:
                    actionPhase = ActionPhase.MoveMotherNature;
                    break;
                case MoveMotherNature:
                    actionPhase = ActionPhase.ChooseCloud;
                    break;
                case ChooseCloud:
                    if (actionOrder.isEmpty()) {
                        phase = Phase.Planning;
                        actionPhase = ActionPhase.NotActionPhase;
                    }
                    current = planningOrder.poll();
                    break;
            }
        }
    }

    private void setOrder(){
        final int id = 0;
        final int value = 1;

        int[][] arr = new int[2][gInit.getNumberOfPlayers()];
        int i = 0;
        Player x;
        Iterator<Player> iter = gInit.iterator();
        while (iter.hasNext()){
            x = iter.next();
            arr[id][i] = x.getId();
            //todo assistant
            //arr[value][i] = x.getActiveAssistant().getValue();
            i++;
        }
        sortMatrixColumn(arr, 1);
        if (!specialOrder.isEmpty()){
            Player temp;
            int tempId;
            while (!specialOrder.isEmpty()){
                temp = specialOrder.poll();
                tempId = temp.getId();
                i = 0;
                while (arr[id][i] != tempId){
                    i++;
                }
                while (arr[value][i] == arr[value][i + 1] && i < gInit.getNumberOfPlayers() - 1){
                    swap (arr, i, i + 1);
                    i++;
                }
            }
        }
        for (i = 0; i < gInit.getNumberOfPlayers(); i++){
            actionOrder.add(gInit.getPlayerById(arr[id][i]));
            planningOrder.add(gInit.getPlayerById(arr[id][i]));
        }
    }

    private static void sortMatrixColumn (int[][] matrix, int rowToCompare){
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = i + 1; j < matrix[0].length; j++) {
                if (matrix[rowToCompare][i] > matrix[rowToCompare][j]) {
                    swap(matrix, i, j);
                }
            }
        }
    }

    private static void swap (int[][] matrix,int index1, int index2){
        int tmp0, tmp1;
        tmp0 = matrix[0][index1];
        tmp1 = matrix[1][index1];
        matrix[0][index1] = matrix[0][index2];
        matrix[1][index1] = matrix[1][index2];
        matrix[0][index2] = tmp0;
        matrix[1][index2] = tmp1;
    }

    private int getRandomPlayerId (){
        int[] ids = new int[gInit.getNumberOfPlayers()];
        int i = 0;
        Player p;
        Iterator<Player> iterator = gInit.iterator();
        while (iterator.hasNext()){
            p = iterator.next();
            ids[i] = p.getId();
            i++;
        }
        return ids[rand.nextInt(gInit.getNumberOfPlayers())];
    }

}
