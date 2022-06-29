package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Server.Model.Characters.Character;
import it.polimi.ingsw.Server.Model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ModelMessage extends Message {

    private final int gameMode;
    private final int playerNumber;
    private final int winnerId;

    private final int currentPlayerId;

    private final String actualPhase;
    private final String actualActionPhase;
    private final List<Integer> actionOrder;
    private final List<Integer> planningOrder;
    private final List<Integer> specialOrder;
    private final int studentsMoved;
    private final boolean finalRound;

    private final int motherNatureIslandId;

    private final int[] professorsList;

    private final int[] bag;

    private final List<Island> islandList;

    private final List<CloudSerializable> cloudList;

    private final List<PlayerSerializable> playerList; //or advanced player if game mode 1

    private final List<CharacterSerializable> characterList;

    private final int activeCharacterId;

    protected ModelMessage(GameInitializer g, Errors er) {
        super(er, "Model Message");

        Islands s = g.getIslands();
        GameBoard b = g.getBoard();
        RoundHandler r = g.getRoundHandler();

        this.winnerId = g.getWinningPlayerId();
        this.gameMode = g.getGameMode();
        this.playerNumber = g.getPlayersNumber();
        this.currentPlayerId = r.getCurrent().getId();
        this.actualPhase = r.getPhase().toString();
        this.actualActionPhase = r.getActionPhase().toString();
        this.studentsMoved = r.getStudentMovedInThisTurn();
        this.finalRound = r.getIsFinalRound();
        this.motherNatureIslandId = s.getMotherNature().getId();
        this.professorsList = g.getProfessors().getProfessorsCopy();
        this.bag = g.getBag().getStudentsCopy();

        Queue<Player> tempQ = r.getSpecialOrder();
        if (tempQ == null || tempQ.isEmpty())
            this.specialOrder = null;
        else {
            this.specialOrder = new ArrayList<>(tempQ.size());
            for (Player p: tempQ){
                this.specialOrder.add(p.getId());
            }
        }

        tempQ = r.getPlanningOrder();
        if (tempQ == null || tempQ.isEmpty())
            this.planningOrder = null;
        else {
            this.planningOrder = new ArrayList<>(tempQ.size());
            for (Player p: tempQ){
                this.planningOrder.add(p.getId());
            }
        }

        tempQ = r.getActionOrder();
        if (tempQ == null || tempQ.isEmpty())
            this.actionOrder = null;
        else {
            this.actionOrder = new ArrayList<>(tempQ.size());
            for (Player p: tempQ){
                this.actionOrder.add(p.getId());
            }
        }


        this.islandList = new ArrayList<>(s.getIslandsNumber());
        for (Island island : s)
            this.islandList.add(island);

        this.cloudList = new ArrayList<>(this.playerNumber);
        for (Cloud cloud : b)
            this.cloudList.add(new CloudSerializable(cloud));

        this.playerList = new ArrayList<>(this.playerNumber);
        for (Player player : g)
            this.playerList.add(new PlayerSerializable(player, this.gameMode));

        if (this.gameMode == 1) {
            this.characterList = new ArrayList<>(3);
            for (int i = 0; i < 12; i++)
                if (b.existsCharacter(i)) {
                    Character temp = b.getCharacterById(i);
                    this.characterList.add (new CharacterSerializable(temp));
                }
            Character t = b.getActiveCharacter();

            if (t != null)
                this.activeCharacterId = t.getId();
            else
                this.activeCharacterId = -1;
        } else {
            this.characterList = null;
            this.activeCharacterId = -1;
        }
    }

    //all getter


    public int getPlayerNumber() {
        return playerNumber;
    }

    public int[] getIds () {
        int size = playerList.size();
        int[] ids = new int[size];
        for (int i = 0; i < size; i++){
            ids[i] = playerList.get(i).getId();
        }
        return ids;
    }

    public List<Integer> getActionOrder() {
        return actionOrder;
    }

    public List<Integer> getPlanningOrder() {
        return planningOrder;
    }

    public List<Integer> getSpecialOrder() {
        return specialOrder;
    }

    public boolean isFinalRound() {
        return finalRound;
    }

    public boolean gameIsOver() {
        return this.winnerId != -1;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public String getActualPhase() {
        return actualPhase;
    }

    public String getActualActionPhase() {
        return actualActionPhase;
    }

    public int getStudentsMoved() {
        return studentsMoved;
    }

    public int getStudentsToMove() {
        int max = (this.playerNumber == 3) ? 4 : 3;
        return max - this.studentsMoved;
    }

    public List<Island> getIslandList() {
        return islandList;
    }

    public boolean isIslandIdValid(int islandId) {
        for (Island i : this.islandList) {
            if (i.getId() == islandId)
                return true;
        }
        return false;
    }

    public int getMotherNatureIslandId() {
        return motherNatureIslandId;
    }

    public int[] getProfessorsList() {
        return professorsList;
    }

    public int[] getBag() {
        return bag;
    }

    public List<PlayerSerializable> getPlayerList() {
        return playerList;
    }

    public PlayerSerializable getPlayerById(int playerId) {
        for (PlayerSerializable p : this.playerList) {
            if (p.getId() == playerId)
                return p;
        }
        return null;
    }

    public int getGameMode() {
        return gameMode;
    }

    public int getActiveCharacterId() {
        return activeCharacterId;
    }

    public List<CloudSerializable> getCloudList() {
        return cloudList;
    }

    public boolean isCloudValid (int cloudId){
        for (CloudSerializable c : this.cloudList){
            if (c.getId() == cloudId)
                return true;
        }
        return false;
    }

    public List<CharacterSerializable> getCharacterList() {
        return characterList;
    }

    public boolean isCharacterIdValid (int characterId){
        for (CharacterSerializable c: this.characterList){
            if (c.getId() == characterId)
                return true;
        }
        return false;
    }

    public CharacterSerializable getCharacterById (int characterId){
        for (CharacterSerializable c: this.characterList){
            if (c.getId() == characterId)
                return c;
        }
        return null;
    }
    public int getWinnerId() {
        return winnerId;
    }

    public int getCharacterIdFromName (String name){

        String characterName = name.toLowerCase();

        int characterId;

        switch (characterName){
            case "apothecary" -> characterId = 0;
            case "bard" -> characterId = 1;
            case "cleric" -> characterId = 2;
            case "cook" -> characterId = 3;
            case "drunkard" -> characterId = 4;
            case "herald" -> characterId = 5;
            case "jester" -> characterId = 6;
            case "knight" -> characterId = 7;
            case "minotaur" -> characterId = 8;
            case "postman" -> characterId = 9;
            case "princess" -> characterId = 10;
            case "thief" -> characterId = 11;
            default -> {
                System.out.println("name not valid character activated " + name);
                return -1;
            }
        }
        return characterId;
    }

    public Island getIslandFromId (int id){
        for (Island i: islandList)
            if (i.getId() == id)
                return i;

        return null;
    }

    public int calcIslandDistance(Island src, Island dest){

        if (!isIslandIdValid(src.getId()) || !isIslandIdValid(dest.getId()))
            return -1;

        int indexSrc = islandList.indexOf(src);
        int indexDest = islandList.indexOf(dest);

        int distance;
        if (indexDest >= indexSrc)
            distance = indexDest - indexSrc;
        else
            distance = indexDest + islandList.size() - indexSrc;

        return distance;
    }

    public int calcIslandDistance(Island dest){
        return calcIslandDistance(getIslandFromId(motherNatureIslandId), dest);
    }

}