package it.polimi.ingsw.Server.Model;

import java.util.Collection;
import java.util.Iterator;

// this class create all the model and keep the register of the players
class GameInitializer implements Iterable<Player>{

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

    void createAllGame (int[] ids, RoundHandler roundHandler){
        this.bag = new Bag(this);
        this.professors = new Professors(this);
        this.islands = new Islands();
        this.board = new GameBoard(this);
        this.players = Player.factoryPlayers(ids, this);
        this.roundHandler = roundHandler;
    }

    int getGameMode() {
        return gameMode;
    }

    int getPlayersNumber() {
        return playerNumber;
    }

    Bag getBag() {
        return bag;
    }

    Professors getProfessors() {
        return professors;
    }

    Islands getIslands() {
        return islands;
    }

    GameBoard getBoard() {
        return board;
    }

    RoundHandler getRoundHandler() {
        return roundHandler;
    }

    Player getPlayerById (int id){
        for (Player x: players){
            if (x.getId() == id)
                return x;
        }
        return null;
    }

    boolean existsPlayer (int id){
        boolean exists = false;
        for (Player p : players)
            if (p.getId() == id) {
                exists = true;
                break;
            }
        return exists;
    }

    void checkWin(){
        //todo
    }

    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }
}
