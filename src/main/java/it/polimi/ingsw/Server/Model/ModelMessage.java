package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ModelMessage {

    private class PlayerSerializable{
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
            for (Assistant a: p)
                i++;
            this.assistantDeck = new int[i];
            i = 0;
            for (Assistant a: p)
                this.assistantDeck[i++] = a.getValue();
            if (mode == 0)
                this.coins = 0;
            else
                this.coins = ((AdvancedPlayer)p).getCoins();
        }
    }

    private class CloudSerializable{

        private final int id;
        private final int[] drawnStudents;

        CloudSerializable(Cloud c) {
            this.id = c.getId();
            this.drawnStudents = c.getCopyOfDrawnStudents();
        }
    }

    private class normalCharacterSerializable{
        private final int id;
        private final int cost;
        private final boolean used;

        normalCharacterSerializable(Character c) {
            this.id = c.id;
            this.cost = c.cost;
            this.used = c.used;
        }


    }

    private final class ApothecarySerializable extends normalCharacterSerializable{
        private final int banCard;

        ApothecarySerializable(Apothecary c) {
            super(c);
            this.banCard = c.getBanCard();
        }
    }

    private final class CookSerializable extends normalCharacterSerializable{
        private final int colorId;

        public CookSerializable(Cook c) {
            super(c);
            Color temp = c.getColor();
            if (temp == null)
                this.colorId = -1;
            else
                this.colorId = temp.getIndex();
        }
    }

    private final class ClericSerializable extends normalCharacterSerializable{
        private final int[] students;

        ClericSerializable(Cleric c) {
            super(c);
            this.students = c.getStudentsCopy();
        }
    }

    private final class JesterSerializable extends normalCharacterSerializable{
        private final int[] students;

        JesterSerializable(Jester c) {
            super(c);
            this.students = c.getStudentsCopy();
        }
    }

    private final class PrincessSerializable extends normalCharacterSerializable{
        private final int[] students;

        PrincessSerializable(Princess c) {
            super(c);
            this.students = c.getStudentsCopy();
        }
    }


    private final String time;
    private final int errorCode;
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

    private final List<normalCharacterSerializable> characterList;

    private final int activeCharacterId;

    private ModelMessage(GameInitializer g, Errors er) {
        Islands s = g.getIslands();
        GameBoard b = g.getBoard();
        RoundHandler r = g.getRoundHandler();
        this.winnerId = g.getWinningPlayerId();
        this.errorCode = er.getCode();
        this.time = Instant.now().toString();
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
                    if (tempId == 0)
                        this.characterList.add(new ApothecarySerializable((Apothecary) temp));
                    else if (tempId == 3)
                        this.characterList.add(new CookSerializable((Cook) temp));
                    else if (tempId == 2)
                        this.characterList.add(new ClericSerializable((Cleric) temp));
                    else if (tempId == 6)
                        this.characterList.add(new JesterSerializable((Jester) temp));
                    else if (tempId == 10)
                        this.characterList.add(new PrincessSerializable((Princess) temp));
                    else
                        this.characterList.add(new normalCharacterSerializable(temp));
                }
            Character t = b.getActiveCharacter();
            if (t != null)
                this.activeCharacterId = t.getId();
            else
                this.activeCharacterId = -1;
        }
        else{
            this.characterList = null;
            this.activeCharacterId = -1;
        }
    }

    static ModelMessage modelMessageBuilder (GameInitializer g, Errors er){
        return new ModelMessage(g, er);
    }
}