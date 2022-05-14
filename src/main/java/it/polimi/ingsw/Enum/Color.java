package it.polimi.ingsw.Enum;

/**
 * Enum of color for the students of the game
 */
public enum Color {

    Blue (0),
    Green (1),
    Purple (2),
    Red (3),
    Yellow (4);


    private final int index;

    /**
     * Enum Constructor
     * @param index index
     */
    Color(int index) {
        this.index = index;
    }

    /**
     * Getter of Index
     * @return the index of the color
     */
    public int getIndex() {
        return index;
    }

    /**
     * Return the number of Colors
     * @return the number of color that exists in this game
     */
    public static int getNumberOfColors (){
        return Color.values().length;
    }

    /**
     * Return the color with index passed
     * @param index the index of the color needed
     * @return the corresponding color or null if the index does not exists (see {@link #isColorIdValid(int) isColorIdValid} for further details)
     */
    public static Color getColorById (int index){
        for (Color c: Color.values()){
            if (c.getIndex() == index)
                return c;
        }
        return null;
    }

    /**
     * Check if the index of the colo exists
     * @param colorId the color index to check the validity
     * @return true if the index is valid false otherwise (if this method return true, the method {@link #getColorById(int) getColorById} never return null if the same index value is used)
     */
    public static boolean isColorIdValid (int colorId){
        return (getColorById(colorId) != null);
    }
}
