package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// this class create all the model and keep the register of the players
class GameInitializer implements Iterable<Player>{

    private Collection<Player> players;
    private GameBoard board;
    private final int gameMode;


    GameInitializer(int[] ids, int gameMode) {
        this.gameMode = gameMode;
        //todo use the right constructor, need GameBoard class and Bag class
        GameBoard board = new GameBoard(this, ids.length);
        this.board = board;
        Bag bag = new Bag();
        //Bag bag = board.getBag();

        this.players = Player.factoryPlayers(ids, gameMode, board, bag);
    }

    protected GameBoard getGameBoard(){
        return this.board;
    }

    int getGameMode() {
        return gameMode;
    }

    Player getPlayerById (int id){
        for (Player x: players){
            if (x.getId() == id)
                return x;
        }
        return null;
    }

    int getNumberOfPlayers(){
        return players.size();
    }



    ArrayList<Player> getPlayers (){
        ArrayList<Player> temp = new ArrayList<>();
        for (Player p : players) temp.add(p);
        return temp;
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

    @Override
    public Iterator<Player> iterator() {
        return players.iterator();
    }

}
