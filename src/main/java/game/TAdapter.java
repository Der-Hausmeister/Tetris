package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TAdapter extends KeyAdapter {

    private Board board;

    public TAdapter(Board board) {
        this.board = board;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!board.isStarted() || board.getCurPiece().getShape() == Shape.Tetrominoes.NoShape) {
            return;
        }

        int keycode = e.getKeyCode();

        if (keycode == 'p' || keycode == 'P') {
            board.pause();
            return;
        }

        if (board.isPaused()) {
            return;
        }

        switch (keycode) {
            case KeyEvent.VK_LEFT:
                board.tryMove(board.getCurPiece(), board.getCurX() - 1, board.getCurY());
                break;
            case KeyEvent.VK_RIGHT:
                board.tryMove(board.getCurPiece(), board.getCurX() + 1, board.getCurY());
                break;
            case KeyEvent.VK_DOWN:
                board.tryMove(board.getCurPiece().rotateRight(), board.getCurX(), board.getCurY());
                break;
            case KeyEvent.VK_UP:
                board.tryMove(board.getCurPiece().rotateLeft(), board.getCurX(), board.getCurY());
                break;
            case KeyEvent.VK_SPACE:
                board.dropDown();
                break;
            case 'd':
                // Accelerates the droping of a Tetromino by calling the drop-down-command by hand.
                board.oneLineDown();
                break;
            case 'D':
                board.oneLineDown();
                break;
        }
    }

}
