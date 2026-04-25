package com.mycompany.app;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static char[] emptyBoard() {
        char[] board = new char[9];
        Arrays.fill(board, ' ');
        return board;
    }

    private static char[] board(String layout) {
        if (layout.length() != 9) throw new IllegalArgumentException();
        return layout.toCharArray();
    }

    @Test
    void constructorInitializesEmptyGame() {
        Game g = new Game();
        assertEquals(State.PLAYING, g.state);
        assertEquals('X', g.player1.symbol);
        assertEquals('O', g.player2.symbol);
        for (char c : g.board) assertEquals(' ', c);
    }

    @Test
    void playerDefaultValues() {
        Player p = new Player();
        assertEquals('\0', p.symbol);
        assertEquals(0, p.move);
        assertFalse(p.selected);
        assertFalse(p.win);
    }

    @Test
    void checkStateXWinTopRow() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.XWIN, g.checkState(board("XXX      ")));
    }

    @Test
    void checkStateXWinMiddleRow() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.XWIN, g.checkState(board("   XXX   ")));
    }

    @Test
    void checkStateXWinBottomRow() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.XWIN, g.checkState(board("      XXX")));
    }

    @Test
    void checkStateXWinLeftCol() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.XWIN, g.checkState(board("X  X  X  ")));
    }

    @Test
    void checkStateXWinCenterCol() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.XWIN, g.checkState(board(" X  X  X ")));
    }

    @Test
    void checkStateXWinRightCol() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.XWIN, g.checkState(board("  X  X  X")));
    }

    @Test
    void checkStateXWinMainDiag() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.XWIN, g.checkState(board("X   X   X")));
    }

    @Test
    void checkStateXWinAntiDiag() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.XWIN, g.checkState(board("  X X X  ")));
    }

    @Test
    void checkStateOWin() {
        Game g = new Game();
        g.symbol = 'O';
        assertEquals(State.OWIN, g.checkState(board("OOO      ")));
    }

    @Test
    void checkStateDraw() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.DRAW, g.checkState(board("XOXOOXXXO")));
    }

    @Test
    void checkStatePlayingWhenMovesLeft() {
        Game g = new Game();
        g.symbol = 'X';
        assertEquals(State.PLAYING, g.checkState(board("XOX O    ")));
    }

    @Test
    void generateMovesEmptyBoard() {
        Game g = new Game();
        ArrayList<Integer> moves = new ArrayList<>();
        g.generateMoves(emptyBoard(), moves);
        assertEquals(9, moves.size());
        for (int i = 0; i < 9; i++) assertTrue(moves.contains(i));
    }

    @Test
    void generateMovesPartial() {
        Game g = new Game();
        ArrayList<Integer> moves = new ArrayList<>();
        g.generateMoves(board("XO XO    "), moves);
        assertEquals(Arrays.asList(2, 5, 6, 7, 8), moves);
    }

    @Test
    void generateMovesFullBoard() {
        Game g = new Game();
        ArrayList<Integer> moves = new ArrayList<>();
        g.generateMoves(board("XOXOOXXXO"), moves);
        assertEquals(0, moves.size());
    }

    @Test
    void evaluatePositionXWinForPlayerX() {
        Game g = new Game();
        g.symbol = 'X';
        Player p = new Player();
        p.symbol = 'X';
        assertEquals(Game.INF, g.evaluatePosition(board("XXX      "), p));
    }

    @Test
    void evaluatePositionXWinForPlayerO() {
        Game g = new Game();
        g.symbol = 'X';
        Player p = new Player();
        p.symbol = 'O';
        assertEquals(-Game.INF, g.evaluatePosition(board("XXX      "), p));
    }

    @Test
    void evaluatePositionOWinForPlayerO() {
        Game g = new Game();
        g.symbol = 'O';
        Player p = new Player();
        p.symbol = 'O';
        assertEquals(Game.INF, g.evaluatePosition(board("OOO      "), p));
    }

    @Test
    void evaluatePositionDraw() {
        Game g = new Game();
        g.symbol = 'X';
        Player p = new Player();
        p.symbol = 'X';
        assertEquals(0, g.evaluatePosition(board("XOXOOXXXO"), p));
    }

    @Test
    void evaluatePositionNonTerminal() {
        Game g = new Game();
        g.symbol = 'X';
        Player p = new Player();
        p.symbol = 'X';
        assertEquals(-1, g.evaluatePosition(board("XO XO    "), p));
    }

    @Test
    void minimaxImmediateWinForX() {
        Game g = new Game();
        Player p = new Player();
        p.symbol = 'X';
        int move = g.MiniMax(board("XX OO    "), p);
        assertEquals(3, move);
    }

    @Test
    void minimaxImmediateWinForO() {
        Game g = new Game();
        Player p = new Player();
        p.symbol = 'O';
        int move = g.MiniMax(board("OO XX    "), p);
        assertEquals(3, move);
    }

    @Test
    void minimaxDoesNotModifyBoard() {
        Game g = new Game();
        Player p = new Player();
        p.symbol = 'X';
        char[] original = board("XX OO    ");
        char[] copy = original.clone();
        g.MiniMax(copy, p);
        assertArrayEquals(original, copy);
    }

    @Test
    void minimaxReturnsValidMoveOnEmptyBoard() {
        Game g = new Game();
        Player p = new Player();
        p.symbol = 'X';
        int move = g.MiniMax(emptyBoard(), p);
        assertTrue(move >= 1 && move <= 9);
    }

    @Test
    void maxMoveReturnsInfOnWin() {
        Game g = new Game();
        Player p = new Player();
        p.symbol = 'X';
        int val = g.MaxMove(board("XX       "), p);
        assertEquals(Game.INF, val);
    }

    @Test
    void minMoveReturnsNegInfOnLoss() {
        Game g = new Game();
        Player p = new Player();
        p.symbol = 'O';
        int val = g.MinMove(board("XX       "), p);
        assertEquals(-Game.INF, val);
    }

    @Test
    void minMoveDrawOnFullBoard() {
        Game g = new Game();
        Player p = new Player();
        p.symbol = 'X';
        int val = g.MinMove(board("XOXOOXXXO"), p);
        assertEquals(0, val);
    }

    @Test
    void maxMoveDrawOnFullBoard() {
        Game g = new Game();
        Player p = new Player();
        p.symbol = 'X';
        int val = g.MaxMove(board("XOXOOXXXO"), p);
        assertEquals(0, val);
    }
}
