package model;

public class Pawn extends Piece {
    public Pawn(String color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        if (color.equals("white")) {
            return startY == endY && endX == startX - 1;
        } else {
            return startY == endY && endX == startX + 1;
        }
    }
}
