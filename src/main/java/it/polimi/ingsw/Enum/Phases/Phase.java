package it.polimi.ingsw.Enum.Phases;

/**
 * The major phase of the Game
 */
public enum Phase {
    /**
     * During this Phase the player are one after the other and they only have to choose their Assistant
     */
    Planning,
    /**
     * During this Phase , first the player pass through all the actionPhase, then pass to the next player
     */
    Action
}
