package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Phases.ActionPhase;
import it.polimi.ingsw.Enum.Phases.Phase;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.ChooseCloudMessage;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Message.MoveMotherNatureMessage;
import it.polimi.ingsw.Message.MoveStudentMessage;
import it.polimi.ingsw.Message.PlayAssistantMessage;

import java.util.Random;

public class TestingCli extends Cli {

    static Integer wI = 0;
    static Integer uI = 0;
    static final Random rand = new Random(System.currentTimeMillis());

    @Override
    public void startGraphic() {
        super.startGraphic();
        this.dC.setCallbackForModel(this::modelCallback);
    }

    static String getUsername() {
        synchronized (uI) {
            if (uI > 3)
                uI = 0;
            return "user" + uI++;
        }
    }

    static Wizard getWizard() {
        synchronized (wI) {
            if (wI > 4)
                wI = 0;

            int id = (wI * 10) + 1;

            for (Wizard w : Wizard.values()) {
                if (w.getId() == id) {
                    wI++;
                    return w;
                }
            }
            return Wizard.Flowers_Queen;
        }
    }

    @Override
    public void setInfo() {
        int first = this.dC.getFirst(null); //the value is not -1

        this.dC.setUsername(getUsername());

        this.dC.setWizard(getWizard());

        if (first == 0){
            this.dC.setGameMode(1);
            this.dC.setNumOfPlayers(4);
        }
    }

    @Override
    public void doneCallback() {
        int done = this.dC.getDone(null); //the value is not -1

        if (done == 0){
            displayInputError(this.dC.getErrorData());
            setInfo();
            return;
        }

        //info sent correctly

        this.dC.setCallbackForModel(this::modelCallback);
    }

    @Override
    public void modelCallback() {
        System.out.println("Testing CLi model Callback");

        ModelMessage model = this.dC.getModel();
        int myId = this.dC.getId();

        Phase p = Phase.valueOf(model.getActualPhase());
        ActionPhase aP = ActionPhase.valueOf(model.getActualActionPhase());

        if (Phase.Planning.equals(p)){
            this.dC.setNextMove(new PlayAssistantMessage(Errors.NO_ERROR, "Test CLi", rand.nextInt(1, 11)));
        }
        else {
            switch (aP) {
                case MoveStudent -> this.dC.setNextMove(new MoveStudentMessage(Errors.NO_ERROR, "Test Cli", rand.nextInt(Color.getNumberOfColors()), rand.nextInt(-1, 12)));
                case MoveMotherNature -> this.dC.setNextMove(new MoveMotherNatureMessage(Errors.NO_ERROR, "Test Cli", rand.nextInt(6)));
                case ChooseCloud -> this.dC.setNextMove(new ChooseCloudMessage(Errors.NO_ERROR, "Test Cli", myId));
            }
        }
    }
}
