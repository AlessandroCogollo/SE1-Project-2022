package it.polimi.ingsw.Server.Model;

import java.util.Collection;
import java.util.Iterator;

// this class create all the model and keep the register of the players
class GameInitializer implements Iterable<Player>{

    private final Collection<Player> players;
    private final int gameMode;


    GameInitializer(int[] ids, int gameMode) {
        this.gameMode = gameMode;
        //todo use the right constructor, need GameBoard class and Bag class
        GameBoard board = new GameBoard();
        Bag bag = new Bag();
        //Bag bag = board.getBag();

        this.players = Player.factoryPlayers(ids, gameMode, board, bag);
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

    int getNumberOfPlayers (){
        return players.size();
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
