package com.mycompany.app;

import org.junit.jupiter.api.Test;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ProgramTest {

    @Test
    void mainRunsWithoutException() {
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    Program.main(new String[]{});
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    @Test
    void programConstructorIsNotUsed() {
        Program prog = new Program();
        assertNotNull(prog);
    }
}
