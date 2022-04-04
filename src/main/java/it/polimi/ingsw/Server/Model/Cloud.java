package it.polimi.ingsw.Server.Model;

class Cloud {
    private int id;
    private int[] drawnStudents;
    private int numOfPlayer;
    private Bag bag;


    public Cloud(Bag bag, int numOfPlayer){
        this.bag = bag;
        this.numOfPlayer = numOfPlayer;

    }
    public void AddStudents(){
        this.drawnStudents = new int[Color.getNumberOfColors()];
        if(numOfPlayer == 3){
            drawnStudents = bag.DrawStudents(4);
        }
        else this.drawnStudents = bag.DrawStudents(3);
    }

    public int[] getStudents() {
        return drawnStudents;
    }
}
