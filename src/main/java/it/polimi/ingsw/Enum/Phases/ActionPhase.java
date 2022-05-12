package it.polimi.ingsw.Enum.Phases;

/**
 * Enum for The Action Phase of the Game
 */
public enum ActionPhase {
    /**
     * Planning Phase
     */
    NotActionPhase,
    /**
     * Player Can move Students between his entrance and the room / the island
     */
    MoveStudent,
    /**
     * Player can move Mother Nature up to the value of their Assistant
     */
    MoveMotherNature,
    /**
     * Player Choose Clouds
     */
    ChooseCloud
}
