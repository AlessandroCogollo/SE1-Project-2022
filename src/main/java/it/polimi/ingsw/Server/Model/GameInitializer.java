package it.polimi.ingsw.Server.Model;

import java.util.Collection;
import java.util.Iterator;

// this class create all the model and keep the register of the players
class GameInitializer implements Iterable<Player>{

    private final Collection<Player> players;
    private final GameBoard board;
    private final int gameMode;


    GameInitializer(int[] ids, int gameMode) {
        this.gameMode = gameMode;

        this.board = new GameBoard(this, ids.length);

        this.players = Player.factoryPlayers(ids, gameMode, this.board, this.board.getBag());
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
