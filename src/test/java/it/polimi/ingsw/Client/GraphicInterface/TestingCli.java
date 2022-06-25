package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;

public class TestingCli extends Cli {

    static Integer wI = 0;
    static Integer uI = 0;

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
    }
}
