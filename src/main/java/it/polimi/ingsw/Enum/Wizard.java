package it.polimi.ingsw.Enum;

/**
 * Class used for enumerate the possible wizard that the player can choose and that determinate the back of assistant card
 */
public enum Wizard {

    Sorcerer (1),
    King (11),
    Witch (21),
    Wise (31),
    Flowers_Queen (41);

    private final int id;

    /**
     * Enum Constructor
     * @param id the index of the asset for the wizard
     */
    Wizard(int id) {
        this.id = id;
    }

    /**
     * Getter
     * @return the id of wizard
     */
    public int getId() {
        return id;
    }

    /**
     * Retrun the name of file with the image of the wizard
     * @return a String with the name of the image file for the wizard
     */
    public String getFileName(){
        return "CarteTOT_back_" + id + "@3x.png";
    }
}
