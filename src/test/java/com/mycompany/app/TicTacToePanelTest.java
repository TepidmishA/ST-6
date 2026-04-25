package com.mycompany.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToePanelModified extends TicTacToePanel {
    boolean messageShown = false;
    boolean windowClosed = false;
    String lastMessage;

    TicTacToePanelModified(GridLayout layout) {
        super(layout);
    }

    @Override
    protected void showEndGame(String message) {
        messageShown = true;
        lastMessage = message;
    }

    @Override
    protected void closeWindow() {
        windowClosed = true;
    }
}

class TicTacToePanelTest {

    private TicTacToePanelModified panel;
    private Game game;

    @BeforeEach
    void setUp() throws Exception {
        panel = new TicTacToePanelModified(new GridLayout(3, 3));
        Field gameField = TicTacToePanel.class.getDeclaredField("game");
        gameField.setAccessible(true);
        game = (Game) gameField.get(panel);
    }

    @Test
    void panelCreatesNineCells() {
        Component[] components = panel.getComponents();
        assertEquals(9, components.length);
        for (Component c : components) {
            assertTrue(c instanceof TicTacToeCell);
        }
    }

    @Test
    void gameInitializedWithPlayer1AsCurrent() {
        assertSame(game.player1, game.cplayer);
    }

    @Test
    void humanClickPlacesSymbolOnBoard() {
        TicTacToeCell cell = (TicTacToeCell) panel.getComponent(0);
        cell.doClick();
        assertEquals('X', game.board[0]);
        assertEquals('X', cell.getMarker());
    }

    @Test
    void clickOnAlreadyFilledCellDoesNothing() {
        TicTacToeCell cell = (TicTacToeCell) panel.getComponent(0);
        cell.doClick();
        char marker = cell.getMarker();
        cell.doClick();
        assertEquals(marker, cell.getMarker());
    }

    @Test
    void boardStateMatchesCellsAfterClick() {
        TicTacToeCell cell4 = (TicTacToeCell) panel.getComponent(4);
        cell4.doClick();
        for (int i = 0; i < 9; i++) {
            TicTacToeCell c = (TicTacToeCell) panel.getComponent(i);
            assertEquals(game.board[i], c.getMarker());
        }
    }

    @Test
    void gameStateBecomesXWinAfterThreeInRow() {
        game.board = new char[]{'X','X','X', 'O','O',' ', ' ',' ',' '};
        game.symbol = 'X';
        
        for (int i = 0; i < 9; i++) {
            if (game.board[i] != ' ') {
                ((TicTacToeCell) panel.getComponent(i)).setMarker(String.valueOf(game.board[i]));
                ((TicTacToeCell) panel.getComponent(i)).setEnabled(false);
            }
        }
        
        game.cplayer = game.player1;
        game.state = game.checkState(game.board);
        
        assertEquals(State.XWIN, game.state);
    }

    @Test
    void gameStateRemainsPlayingAfterNonWinningMove() {
        TicTacToeCell cell4 = (TicTacToeCell) panel.getComponent(4);
        cell4.doClick();
        assertEquals(State.PLAYING, game.state);
    }

    @Test
    void aiMoveCanBeSetManually() {
        game.player2.symbol = 'O';
        game.player2.move = 4;
        int move = game.player2.move;
        assertTrue(move >= 1 && move <= 9);
        assertEquals(4, move);
    }

    @Test
    void gameEndShowsMessageAndClosesWindowOnXWin() {
        game.state = State.XWIN;
        panel.endGame(game.state);
        assertTrue(panel.messageShown);
        assertEquals("Выиграли крестики", panel.lastMessage);
        assertTrue(panel.windowClosed);
    }

    @Test
    void gameEndShowsMessageAndClosesWindowOnOWin() {
        game.state = State.OWIN;
        panel.endGame(game.state);
        assertTrue(panel.messageShown);
        assertEquals("Выиграли нолики", panel.lastMessage);
        assertTrue(panel.windowClosed);
    }

    @Test
    void gameEndShowsMessageAndClosesWindowOnDraw() {
        game.state = State.DRAW;
        panel.endGame(game.state);
        assertTrue(panel.messageShown);
        assertEquals("Ничья", panel.lastMessage);
        assertTrue(panel.windowClosed);
    }

    @Test
    void gameEndDoesNothingWhenPlaying() {
        game.state = State.PLAYING;
        panel.endGame(game.state);
        assertFalse(panel.messageShown);
        assertFalse(panel.windowClosed);
    }
}