package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Characters.*;
import it.polimi.ingsw.Server.Model.Characters.Character;

import java.util.ArrayList;
import java.util.List;

public class ModelMessage extends Message{

    public class PlayerSerializable {
        private final int id;
        private final int towerColor;
        private final int[] assistantDeck;
        private final int activeAssistant;
        private final School school;
        private final int coins;

        PlayerSerializable(Player p, int mode) {
            this.id = p.getId();
            this.towerColor = p.getTowerColor();
            this.school = p.getSchool();
            if (p.getActiveAssistant() == null)
                this.activeAssistant = -1;
            else
                this.activeAssistant = p.getActiveAssistant().getValue();

            int i = 0;
            for (Assistant ignored : p)
                i++;
            this.assistantDeck = new int[i];
            i = 0;
            for (Assistant a : p)
                this.assistantDeck[i++] = a.getValue();
            if (mode == 0)
                this.coins = 0;
            else
                this.coins = ((AdvancedPlayer) p).getCoins();
        }

        public int getId() {
            return id;
        }

        public int getTowerColor() {
            return towerColor;
        }

        public int[] getAssistantDeck() {
            return assistantDeck;
        }

        public int getActiveAssistant() {
            return activeAssistant;
        }

        public School getSchool() {
            return school;
        }

        public int getCoins() {
            return coins;
        }
    }

    public class CloudSerializable {

        private final int id;
        private final int[] drawnStudents;

        CloudSerializable(Cloud c) {
            this.id = c.getId();
            this.drawnStudents = c.getCopyOfDrawnStudents();
        }

        public int getId() {
            return id;
        }

        public int[] getDrawnStudents() {
            return drawnStudents;
        }
    }

    public class NormalCharacterSerializable {

        private final int id;
        private final int cost;
        private final boolean used;
        private final String name;

        public NormalCharacterSerializable(Character c) {
            this.id = c.getId();
            this.used = c.getUsed();
            this.cost = (this.used) ? (c.getCost() - 1) : c.getCost();
            this.name = c.getName();
        }

        public int getId() {
            return id;
        }

        public int getCost() {
            return cost;
        }

        public boolean isUsed() {
            return used;
        }

        public String getName() {
            return name;
        }
    }

    public final class ApothecarySerializable extends NormalCharacterSerializable {

        private final int banCard;

        ApothecarySerializable(Apothecary c) {
            super(c);
            this.banCard = c.getBanCard();
        }

        public int getBanCard() {
            return banCard;
        }
    }

    public final class CookSerializable extends NormalCharacterSerializable {
        private final int colorId;

        public CookSerializable(Cook c) {
            super(c);
            Color temp = c.getColor();
            if (temp == null)
                this.colorId = -1;
            else
                this.colorId = temp.getIndex();
        }

        public int getColorId() {
            return colorId;
        }
    }

    public final class ClericSerializable extends NormalCharacterSerializable {
        private final int[] students;

        ClericSerializable(Cleric c) {
            super(c);
            this.students = c.getStudentsCopy();
        }

        public int[] getStudents() {
            return students;
        }
    }

    public final class JesterSerializable extends NormalCharacterSerializable {
        private final int[] students;

        JesterSerializable(Jester c) {
            super(c);
            this.students = c.getStudentsCopy();
        }

        public int[] getStudents() {
            return students;
        }
    }

    public final class PrincessSerializable extends NormalCharacterSerializable {
        private final int[] students;

        PrincessSerializable(Princess c) {
            super(c);
            this.students = c.getStudentsCopy();
        }

        public int[] getStudents() {
            return students;
        }
    }

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

    private final List<NormalCharacterSerializable> characterList;

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
                        default -> this.characterList.add(new NormalCharacterSerializable(temp));
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
            if (c.id == cloudId)
                return true;
        }
        return false;
    }

    public List<NormalCharacterSerializable> getCharacterList() {
        return characterList;
    }

    public boolean isCharacterIdValid (int characterId){
        for (NormalCharacterSerializable c: this.characterList){
            if (c.id == characterId)
                return true;
        }
        return false;
    }

    public NormalCharacterSerializable getCharacterById (int characterId){
        for (NormalCharacterSerializable c: this.characterList){
            if (c.id == characterId)
                return c;
        }
        return null;
    }
    public int getWinnerId() {
        return winnerId;
    }

}