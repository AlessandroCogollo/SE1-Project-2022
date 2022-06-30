package it.polimi.ingsw.Message;

/**
 * the decorator of the messages used by the client handler for add the information of the player id
 * @param message: message to decorate
 * @param playerId: the player id
 */
public record ClientMessageDecorator(ClientMessage message, int playerId) {}
