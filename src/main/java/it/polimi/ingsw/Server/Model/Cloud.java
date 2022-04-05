package it.polimi.ingsw.Server.Model;

class Cloud {
    private final int id;
    private final Bag bag;
    private final int numOfPlayer;
    private int[] drawnStudents;


    Cloud(int id, int numOfPlayer, Bag bag){
        this.id = id;
        this.bag = bag;
        this.numOfPlayer = numOfPlayer;
        setStudents();
    }

    void setStudents(){
        if (numOfPlayer == 3)
            this.drawnStudents = bag.DrawStudents(4);
        else
            this.drawnStudents = bag.DrawStudents(3);
    }

    int[] getStudents() {
        return drawnStudents;
    }
}
