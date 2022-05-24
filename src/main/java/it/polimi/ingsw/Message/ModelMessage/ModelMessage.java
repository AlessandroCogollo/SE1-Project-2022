package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.Message;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Characters.*;
import it.polimi.ingsw.Server.Model.Characters.Character;

import java.util.ArrayList;
import java.util.List;

public class ModelMessage extends Message {

    private final int gameMode;
    private final int playerNumber;
    private final int winnerId;

    private final int currentPlayerId;

    private final String actualPhase;
    private final String actualActionPhase;
    private final int studentsMoved;

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
        this.motherNatureIslandId = s.getMotherNature().getId();
        this.professorsList = g.getProfessors().getProfessorsCopy();
        this.bag = g.getBag().getStudentsCopy();


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
                    int tempId = temp.getId();
                    switch (tempId){
                        case 0 -> this.characterList.add(new ApothecarySerializable((Apothecary) temp));
                        case 3 -> this.characterList.add(new CookSerializable((Cook) temp));
                        case 2 -> this.characterList.add(new ClericSerializable((Cleric) temp));
                        case 6 -> this.characterList.add(new JesterSerializable((Jester) temp));
                        case 10 -> this.characterList.add(new PrincessSerializable((Princess) temp));
                        default -> this.characterList.add(new CharacterSerializable(temp));
                    }
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

}