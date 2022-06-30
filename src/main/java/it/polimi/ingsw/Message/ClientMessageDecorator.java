package it.polimi.ingsw.Message;

/**
 * the decorator of the messages
 * @param message: message to decorate
 * @param playerId: the player id
 */
public record ClientMessageDecorator(ClientMessage message, int playerId) {
}
