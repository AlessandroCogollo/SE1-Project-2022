package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Message.ModelMessage.ModelMessage;

import java.util.Collection;
import java.util.Iterator;

// this class create all the model and keep the register of the players
public class GameInitializer implements Iterable<Player>{

    private final int gameMode;
    private final int playerNumber;

    private Collection<Player> players = null;
    private Bag bag = null;
    private Professors professors = null;
    private Islands islands = null;
    private GameBoard board = null;
    private RoundHandler roundHandler;
    private int winningPlayerId;




    GameInitializer(int gameMode, int playerNumber) {
        this.gameMode = gameMode;
        this.playerNumber = playerNumber;
        this.winningPlayerId = -1;
    }

    /**
     * creates the game board, the professors, the islands
     * @param ids
     * @param roundHandler
     */
    void createAllGame (int[] ids, RoundHandler roundHandler){
        this.bag = new Bag(this);
        this.professors = new Professors(this);
        this.islands = new Islands();
        this.board = new GameBoard(this);
        this.players = Player.factoryPlayers(ids, this);
        this.roundHandler = roundHandler;
    }

    void createAllGame (RoundHandler roundHandler, ModelMessage resumedModel){
        this.bag = new Bag(this, resumedModel.getBag());
        this.professors = new Professors(this, resumedModel.getProfessorsList());
        this.islands = new Islands(resumedModel.getIslandList(), resumedModel.getMotherNatureIslandId());
        this.board = new GameBoard(this, resumedModel);
        this.players = Player.factoryPlayers(this, resumedModel.getPlayerList());
        this.roundHandler = roundHandler;
    }

    public int getGameMode() {
        return gameMode;
    }

    public int getPlayersNumber() {
        return playerNumber;
    }

    public Bag getBag() {
        return bag;
    }

    public Professors getProfessors() {
        return professors;
    }

    public Islands getIslands() {
        return islands;
    }

    public GameBoard getBoard() {
        return board;
    }

    public RoundHandler getRoundHandler() {
        return roundHandler;
    }

    public int getWinningPlayerId() {
        return winningPlayerId;
    }

    public Player getPlayerById (int id){
        for (Player x: players){
            if (x.getId() == id)
                return x;
        }
        return null;
    }

    public boolean existsPlayer (int id){
        boolean exists = false;
        for (Player p : players)
            if (p.getId() == id) {
                exists = true;
                break;
            }
        return exists;
    }

    /**
     * checks if the player with id = winner has won
     * @param winner
     */
    void checkWin(Player winner){
        this.winningPlayerId = winner.getId();
    }

    /**
     * checks if somebody has won
     */
    void checkWin (){

        int length;
        if (playerNumber == 3)
            length = 3;
        else
            length = 2;

        int[][] originalTowerMap = new int[2][length];

        final int id = 0;
        final int remainingTowers = 1;

        int i = 0;
        for(Player p : this){
            int tempTowers = p.getSchool().getTowers();
            if (tempTowers != 0) {//player with no tower win automatically using the other method and this removed the player with 0 towers (mate in 4 players game)
                originalTowerMap[id][i] = p.getId();
                originalTowerMap[remainingTowers][i] = p.getSchool().getTowers();
                i++;
            }
        }

        //ascending order
        RoundHandler.sortMatrixColumn(originalTowerMap, remainingTowers);


        /*String[] name = {"id", "towers"};
        printMatrix(originalTowerMap, 2, length, name);*/

        if (originalTowerMap[remainingTowers][0] == originalTowerMap[remainingTowers][1]){
            checkWin(getPlayerById(equalCase()));
        }
        else {
            checkWin(getPlayerById(originalTowerMap[id][0]));
        }
    }

    private int equalCase() {
        final int id = 0;
        final int professorNumber = 1;

        int[][] professorMap = new int [2][playerNumber];
        int i = 0;
        for (Player p: this){
            professorMap[id][i] = p.getId();
            professorMap[professorNumber][i] = getProfessors().getNumberOfProfessorOfPlayer(p);
            i++;
        }

        int length;
        if (playerNumber == 4){
            //In 4 players game we have to count the sum of professor of the team
            professorMap = teamProfessors(professorMap); //we remove 2 player adding their professor to the mate
            length = 2;
        }
        else
            length = playerNumber;

        //ascending order
        RoundHandler.sortMatrixColumn(professorMap, professorNumber);
        //descending order
        RoundHandler.revertMatrix(professorMap, length);

        //other case case are not covered in rules, if the player has the same number of tower used and the same number of professor (an odd number of professor must be not took), or the same with 3 players
        return professorMap[id][0];
    }

    int[][] teamProfessors(int[][] professorMap) {
        //only case 4 players

        int[] teamA = new int[2];
        int teamAProfessors = 0;
        int[] teamB = new int[2];
        int teamBProfessors = 0;
        int a = 0, b = 0;
        for (int i = 0; i < 4; i++){
            if (professorMap[0][i] % 2 == 0){
                teamA[a] = professorMap[0][i];
                teamAProfessors += professorMap[1][i];
                a++;
            }
            else{
                teamB[b] = professorMap[0][i];
                teamBProfessors += professorMap[1][i];
                b++;
            }
        }

        int[][] newProfessorMap = new int[2][2];
        newProfessorMap[0][0] = teamA[0];
        newProfessorMap[1][0] = teamAProfessors;

        newProfessorMap[0][1] = teamB[0];
        newProfessorMap[1][1] = teamBProfessors;

        return newProfessorMap;
    }

    void printMatrix (int[][] matrix, int row, int column, String[] rawName){
        boolean use = rawName.length == row;
        for (int r = 0; r < row; r++) {
            System.out.print("[ ");
            for (int c = 0; c < column; c++) {
                System.out.print( matrix[r][c] + " , ");
            }
            System.out.print( "\b\b] " + (use ? rawName[r] : "" + "\n"));
        }
    }

    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }
}
