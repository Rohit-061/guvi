package model;

public class Board {
    private Piece[][] board;

    public Board() {
        board = new Piece[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("white");
            board[6][i] = new Pawn("black");
        }
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public boolean movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board[startX][startY];
        if (piece != null && piece.isValidMove(startX, startY, endX, endY)) {
            board[endX][endY] = piece;
            board[startX][startY] = null;
            return true;
        }
        return false;
    }
}
