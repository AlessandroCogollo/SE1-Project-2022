package it.polimi.ingsw.Server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class QueueOrganizerTest {
    QueueOrganizer getClass (int playerNumber){
        int[] ids = null;
        switch (playerNumber) {
            case 2 -> {
                ids = new int[2];
                ids[1] = 1;
            }
            case 3 -> {
                ids = new int[3];
                ids[1] = 1;
                ids[2] = 2;
            }
            case 4 -> {
                ids = new int[4];
                ids[0] = 0;
                ids[1] = 1;
                ids[2] = 2;
                ids[3] = 3;
            }
        }
        if (ids == null)
            return null;

        return new QueueOrganizer(ids);
    }

    @Test
    void getModelQueue() {
        for (int i = 2; i <= 4; i++)
            assertNotNull(getClass(i).getModelQueue());
    }

    @Test
    void getPlayerQueue() {
        for (int i = 2; i <= 4; i++){
            QueueOrganizer q = getClass(i);
            for (int j = 0; j < i; j++)
                assertNotNull(q.getPlayerQueue(j));
        }
    }
}