package it.polimi.ingsw.Server.Model;

import java.util.Optional;

public enum Color {
    Blue (0),
    Green (1),
    Purple (2),
    Red (3),
    Yellow (4);

    private final int index;

    Color(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static int getNumberOfColors (){
        return Color.values().length;
    }

    public static Color getColorById (int index){
        for (Color c: Color.values()){
            if (c.getIndex() == index)
                return c;
        }
        return null;
    }

    public static boolean isColorIdValid (int colorId){
        return (getColorById(colorId) != null);
    }
}
