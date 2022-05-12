package it.polimi.ingsw.Enum;

/**
 * Enum of color for the students of the game
 */
public enum Color {
    /**
     * Blue
     */
    Blue (0),
    /**
     * Green
     */
    Green (1),
    /**
     * Purple
     */
    Purple (2),
    /**
     * Red
     */
    Red (3),
    /**
     * Yellow
     */
    Yellow (4);

    /**
     * Index used in array for catalog the color
     */
    private final int index;

    /**
     * Enum Constructor
     * @param index index
     */
    Color(int index) {
        this.index = index;
    }

    /**
     *
     * @return the index of the color
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @return the number of color that exists in this game
     */
    public static int getNumberOfColors (){
        return Color.values().length;
    }

    /**
     *
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
     *
     * @param colorId the color index to check the validity
     * @return true if the index is valid false otherwise (if this method return true, the method {@link #getColorById(int) getColorById} never return null if the same index value is used)
     */
    public static boolean isColorIdValid (int colorId){
        return (getColorById(colorId) != null);
    }
}
