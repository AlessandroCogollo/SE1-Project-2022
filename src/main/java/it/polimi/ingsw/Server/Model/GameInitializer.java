package it.polimi.ingsw.Server.Model;

import java.util.Collection;
import java.util.Iterator;

// this class create all the model and keep the register of the players
class GameInitializer implements Iterable{

    private final Collection<Player> players;


    GameInitializer(int[] ids, int gameMode) {
        //todo board and bag
        //todo change the constructor with the right one
        GameBoard board = new GameBoard();

        //todo to remove
        Bag bag = new Bag();
        //Bag bag = board.getBag();

        this.players = Player.factoryPlayers(ids, gameMode, board, bag);
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

    @Override
    public Iterator iterator() {
        return players.iterator();
    }

}
