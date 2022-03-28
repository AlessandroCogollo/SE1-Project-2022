package it.polimi.ingsw.Server.Model;

public enum Color {
    Blue (0),
    Green (1),
    Purple (2),
    Red (3),
    Yellow (4);

    private int index;

    Color(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static int getNumberOfColors (){
        return Color.values().length;
    }
}
