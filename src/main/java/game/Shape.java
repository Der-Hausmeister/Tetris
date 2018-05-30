package graphics.java2d.tetris;

import java.util.Random;

public class Shape {

    protected enum Tetrominoes {
        NoShape, ZShape, SShape, LineShape,
        TSHape, SquareShape, LShape, MirroredLShape;
    }

    private Tetrominoes pieceShape;

    private int coords[][];
    private int[][][] coordsTable;
    public Shape() {
        coords = new int[4][2];
        setShape(Tetrominoes.NoShape);
    }

    public void setShape(Tetrominoes shape) {
        coordsTable = new int[][][]{
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}}, // NoShape
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}}, // ZShape
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}}, // SShape
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},  //LineShape
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}}, // TSHape
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}}, // SquareShape
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}}, // LShape
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}} //  MirroredLShape
        };

        for (int i = 0; i < 4; i++) {
            System.arraycopy(coordsTable[shape.ordinal()][i], 0, coords[i], 0, 2);
        }

        pieceShape = shape;
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int x(int index) {
        return coords[index][0];
    }

    public int y(int index) {
        return coords[index][1];
    }

    public Tetrominoes getShape() {
        return pieceShape;
    }

    public void setRandomShape() {
        Random r = new Random();
        int x = r.nextInt(7) % 7 + 1;
        Tetrominoes[] values = Tetrominoes.values();
        setShape(values[x]);
    }

    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    public int minX() {
        int m = coords[0][0];

        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }

    public Shape rotateLeft() {
        if (pieceShape == Tetrominoes.SquareShape)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    /**
     * Rotation means switching the x- and y-axis. In the case of the right rotation, the
     * the x-axis as the new y-axis is just numbered in the opposite direction than the previous
     * y-axis.
     *
     * @return
     */
    public Shape rotateRight() {
        if (pieceShape == Tetrominoes.SquareShape)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}
