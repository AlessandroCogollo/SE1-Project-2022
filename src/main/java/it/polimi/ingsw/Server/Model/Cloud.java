package it.polimi.ingsw.Server.Model;

class Cloud {
    private int[] students;

    public Cloud(){
        this.students = new int[5];
    }
    public void AddStudents(){
        this.students = Bag.DrawStudents(3);
    }

    public int[] getStudents() {
        return students;
    }
}
