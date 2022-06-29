package it.polimi.ingsw.Enum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorsTest {

    @Test
    void getDescription() {
        for (Errors er : Errors.values()){
            String s = er.getDescription();
            assertNotNull(s);
        }
    }

    @Test
    void getCode() {
        for (Errors er : Errors.values()){
            int c = er.getCode();
            assertTrue(c >= 0);
        }
    }

    @Test
    void getErrorsByCode() {
        for (Errors er : Errors.values()){
            assertNotNull(Errors.getErrorsByCode(er.getCode()));
        }
        assertNull(Errors.getErrorsByCode(-10));
    }

    @Test
    void testToString() {
        for (Errors er : Errors.values()){
            assertEquals("Error " + er.getCode() + ": " + er.getDescription(), er.toString());
        }
    }

    @Test
    void values() {
        // enum specific
        Errors.values();
        assertTrue(true);
    }

    @Test
    void valueOf() {
        // enum specific
        Errors.valueOf("NO_ERROR");
        assertTrue(true);
    }
}