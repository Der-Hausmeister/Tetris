package graphics.java2d.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel implements ActionListener {

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGTH = 22;
    private static final int LINES_FOR_NEXT_LEVEL = 10;
    private static final int MAX_LEVEL = 10 * LINES_FOR_NEXT_LEVEL;
    private static final int SPEED_UP = 25;

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private int nextLevel = 10;
    private JLabel statusBar;
    private Shape curPiece;
    private Shape.Tetrominoes[] board;

    public Board(Tetris parent) {
        initBoard(parent);
    }

    public static int getBoardWidth() {
        return BOARD_WIDTH;
    }

    public static int getBoardHeigth() {
        return BOARD_HEIGTH;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public boolean isFallingFinished() {
        return isFallingFinished;
    }

    public void setFallingFinished(boolean fallingFinished) {
        isFallingFinished = fallingFinished;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int getNumLinesRemoved() {
        return numLinesRemoved;
    }

    public void setNumLinesRemoved(int numLinesRemoved) {
        this.numLinesRemoved = numLinesRemoved;
    }

    public int getCurX() {
        return curX;
    }

    public void setCurX(int curX) {
        this.curX = curX;
    }

    public int getCurY() {
        return curY;
    }

    public void setCurY(int curY) {
        this.curY = curY;
    }

    public JLabel getStatusBar() {
        return statusBar;
    }

    public void setStatusBar(JLabel statusBar) {
        this.statusBar = statusBar;
    }

    public Shape getCurPiece() {
        return curPiece;
    }

    public void setCurPiece(Shape curPiece) {
        this.curPiece = curPiece;
    }

    public Shape.Tetrominoes[] getBoard() {
        return board;
    }

    public void setBoard(Shape.Tetrominoes[] board) {
        this.board = board;
    }

    private void initBoard(Tetris parent) {
        setFocusable(true);
        curPiece = new Shape();
        timer = new Timer(400, this);
        timer.start();

        statusBar = parent.getStatusbar();
        board = new Shape.Tetrominoes[BOARD_WIDTH * BOARD_HEIGTH];
        addKeyListener(new TAdapter(this));
        clearBoard();
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGTH * BOARD_WIDTH; i++) {
            board[i] = Shape.Tetrominoes.NoShape;
        }
    }


    public void start() {
        if (isPaused) {
            return;
        }

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
    }

    public void pause() {
        if (!isStarted) {
            return;
        }
        isPaused = !isPaused;

        if (isPaused) {
            timer.stop();
            statusBar.setText("paused");
        } else {
            timer.start();
            statusBar.setText(String.valueOf(numLinesRemoved));
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGTH * squareHeight();

        // iterate over the board and draw it
        for (int i = 0; i < BOARD_HEIGTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                // get shape at board point
                Shape.Tetrominoes shape = shapeAt(j, BOARD_HEIGTH - i - 1);
                // if a tetrominoes lise on the given point, a square is drawn in the color
                // of the tetrominoe
                if (shape != Shape.Tetrominoes.NoShape) {
                    drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
                }
            }
        }

        // draw the falling tetrominoe
        if(curPiece.getShape() != Shape.Tetrominoes.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, x * squareWidth(), boardTop + (BOARD_HEIGTH - y -1) * squareHeight(), curPiece.getShape());
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y, Shape.Tetrominoes shape) {
        // the different Tetrominoes have different colors
        Color colors[] = {
                new Color(0, 0, 0),
                new Color(204, 102, 102),
                new Color(102, 204, 102),
                new Color(102, 102, 204),
                new Color(204, 204, 102),
                new Color(204, 102, 204),
                new Color(102, 204, 204),
                new Color(218, 170, 0)
        };

        // paint the a single square. A square owns a border, therefore the
        // drawn rectangle fills the full square with a margin of 1 pixel.
        Color color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        // two lines for the border at the top and at the bottom of the square
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        // two lines for the border on the right and left side of the square
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() -1, x + squareWidth() -1, y +1);
    }

    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGTH;
    }

    private Shape.Tetrominoes shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    private void newPiece() {
        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGTH - 1 + curPiece.minY();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    /**
     * Move the tetrominoe one line down
     */
    public void oneLineDown() {
        if (!tryMove(curPiece, curX, curY -1)) {
            pieceDropped();
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }

        removeFullLines();

        if (!isFallingFinished) {
            newPiece();
        }
    }

    private void removeFullLines() {
        int numFullLines = 0;

        // go down from the top to the bottom line by line
        for (int i = BOARD_HEIGTH - 1; i >= 0; i--) {
            boolean lineIsFull = true;

            // if a no-shape field was found, the line is not full
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapeAt(j, i) == Shape.Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            // else the line is full
            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BOARD_HEIGTH - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        updateBoard(numFullLines);
    }

    private void updateBoard(int numFullLines) {
        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusBar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            curPiece.setShape(Shape.Tetrominoes.NoShape);
            repaint();
        }

        if(numLinesRemoved == nextLevel) {
            timer.setDelay(timer.getDelay() - SPEED_UP);
        }
    }

    public boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGTH) {
                return false;
            }

            if (shapeAt(x, y) != Shape.Tetrominoes.NoShape) {
                return false;
            }

        }
        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();

        return true;
    }

    public void dropDown() {
        int newY = curY;
        while (newY > 0) {
            // try moving the current tetrominoe lines down till the trying fails
            if(!tryMove(curPiece, curX, newY -1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

}
