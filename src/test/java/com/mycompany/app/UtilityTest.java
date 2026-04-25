package com.mycompany.app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    private String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return baos.toString();
    }

    @Test
    void printCharBoard() {
        char[] board = {'X','O','X',' ','O',' ','X',' ','O'};
        String out = captureOutput(() -> Utility.print(board));
        String expected = System.lineSeparator() + "X-O-X- -O- -X- -O-" + System.lineSeparator();
        assertEquals(expected, out);
    }

    @Test
    void printIntBoard() {
        int[] board = {1,2,3,4,5,6,7,8,9};
        String out = captureOutput(() -> Utility.print(board));
        String expected = System.lineSeparator() + "1-2-3-4-5-6-7-8-9-" + System.lineSeparator();
        assertEquals(expected, out);
    }

    @Test
    void printArrayList() {
        ArrayList<Integer> moves = new ArrayList<>();
        moves.add(3);
        moves.add(6);
        moves.add(7);
        String out = captureOutput(() -> Utility.print(moves));
        String expected = System.lineSeparator() + "3-6-7-" + System.lineSeparator();
        assertEquals(expected, out);
    }

    @Test
    void utilityConstructorDoesNotThrow() {
        assertDoesNotThrow(Utility::new);
    }
}
