package ui;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChessGUI extends JFrame {
    private JButton[][] squares = new JButton[8][8];
    private int startX = -1, startY = -1;
    private boolean isWhiteTurn = true; // White starts the game

    public ChessGUI() {
        setTitle("Chess Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        initializeBoard();
        setVisible(true);
    }

    private Map<String, ImageIcon> pieceIcons;

private void loadPieceIcons() {
    pieceIcons = new HashMap<>();
    pieceIcons.put("P", new ImageIcon("resources/images/white_pawn.png"));
    pieceIcons.put("p", new ImageIcon("resources/images/black_pawn.png"));
    pieceIcons.put("R", new ImageIcon("resources/images/white_rook.png"));
    pieceIcons.put("r", new ImageIcon("resources/images/black_rook.png"));
    pieceIcons.put("N", new ImageIcon("resources/images/white_knight.png"));
    pieceIcons.put("n", new ImageIcon("resources/images/black_knight.png"));
    pieceIcons.put("B", new ImageIcon("resources/images/white_bishop.png"));
    pieceIcons.put("b", new ImageIcon("resources/images/black_bishop.png"));
    pieceIcons.put("Q", new ImageIcon("resources/images/white_queen.png"));
    pieceIcons.put("q", new ImageIcon("resources/images/black_queen.png"));
    pieceIcons.put("K", new ImageIcon("resources/images/white_king.png"));
    pieceIcons.put("k", new ImageIcon("resources/images/black_king.png"));
}


    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new JButton();
                squares[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                squares[i][j].setFocusPainted(false);
                squares[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                int x = i, y = j; // Capture coordinates for event handling
                squares[i][j].addActionListener(e -> handleSquareClick(x, y));
                add(squares[i][j]);
            }
        }

        setupInitialPositions();
    }

    private void setupInitialPositions() {
        loadPieceIcons(); // Load images
    
        // Setup pawns
        for (int j = 0; j < 8; j++) {
            squares[6][j].setIcon(pieceIcons.get("P")); // White pawns
            squares[1][j].setIcon(pieceIcons.get("p")); // Black pawns
        }
    
        // Setup rooks
        squares[7][0].setIcon(pieceIcons.get("R")); squares[7][7].setIcon(pieceIcons.get("R"));
        squares[0][0].setIcon(pieceIcons.get("r")); squares[0][7].setIcon(pieceIcons.get("r"));
    
        // Setup knights
        squares[7][1].setIcon(pieceIcons.get("N")); squares[7][6].setIcon(pieceIcons.get("N"));
        squares[0][1].setIcon(pieceIcons.get("n")); squares[0][6].setIcon(pieceIcons.get("n"));
    
        // Setup bishops
        squares[7][2].setIcon(pieceIcons.get("B")); squares[7][5].setIcon(pieceIcons.get("B"));
        squares[0][2].setIcon(pieceIcons.get("b")); squares[0][5].setIcon(pieceIcons.get("b"));
    
        // Setup queens
        squares[7][3].setIcon(pieceIcons.get("Q"));
        squares[0][3].setIcon(pieceIcons.get("q"));
    
        // Setup kings
        squares[7][4].setIcon(pieceIcons.get("K"));
        squares[0][4].setIcon(pieceIcons.get("k"));
    }
    

    private int[] findKing(boolean isWhite) {
        String king = isWhite ? "K" : "k";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j].getText().equals(king)) {
                    return new int[]{i, j};
                }
            }
        }
        return null; // King not found (should never happen in a valid game)
    }

    private boolean isKingInCheck(boolean isWhite) {
        int[] kingPos = findKing(isWhite);
        if (kingPos == null) return false; // Safety check
    
        int kingX = kingPos[0];
        int kingY = kingPos[1];
    
        // Check all opponent's pieces to see if they can attack the king
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String piece = squares[i][j].getText();
                if (!piece.isEmpty() && Character.isUpperCase(piece.charAt(0)) != isWhite) {
                    if (isValidMove(i, j, kingX, kingY)) {
                        return true; // King is in check
                    }
                }
            }
        }
    
        return false; // King is safe
    }
    
    private boolean isCheckmate(boolean isWhite) {
        if (!isKingInCheck(isWhite)) return false; // Not in check, so not checkmate
    
        // Try all possible moves for the current player
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String piece = squares[i][j].getText();
                if (!piece.isEmpty() && Character.isUpperCase(piece.charAt(0)) == isWhite) {
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            String backup = squares[x][y].getText();
                            if (isValidMove(i, j, x, y)) {
                                // Simulate the move
                                squares[x][y].setText(squares[i][j].getText());
                                squares[i][j].setText("");
                                boolean stillInCheck = isKingInCheck(isWhite);
                                // Undo the move
                                squares[i][j].setText(squares[x][y].getText());
                                squares[x][y].setText(backup);
    
                                if (!stillInCheck) return false; // King can escape check
                            }
                        }
                    }
                }
            }
        }
    
        return true; // No valid moves to escape check
    }
    
    
    private boolean gameEnded = false; // Track if the game has ended

    private boolean isLegalMove(int startX, int startY, int endX, int endY) {
        if (!isValidMove(startX, startY, endX, endY)) return false; // Ensure the move itself is valid
    
        // Simulate the move
        String backup = squares[endX][endY].getText();
        squares[endX][endY].setText(squares[startX][startY].getText());
        squares[startX][startY].setText("");
        boolean stillInCheck = isKingInCheck(isWhiteTurn); // Check if the king is still in check
        // Undo the move
        squares[startX][startY].setText(squares[endX][endY].getText());
        squares[endX][endY].setText(backup);
    
        return !stillInCheck; // Move is legal only if the king is not in check afterward
    }
    

    private void handleSquareClick(int x, int y) {
        if (gameEnded) return; // Prevent any moves if the game has ended
    
        if (startX == -1 && startY == -1) {
            // First click: Select a piece
            if (squares[x][y].getIcon() != null && isCorrectTurn(x, y)) {
                startX = x;
                startY = y;
                squares[x][y].setBackground(Color.YELLOW); // Highlight the selected square
            }
        } else {
            // Second click: Attempt to move
            if (isLegalMove(startX, startY, x, y)) {
                squares[x][y].setIcon(squares[startX][startY].getIcon());
                squares[startX][startY].setIcon(null);
    
                if (isCheckmate(!isWhiteTurn)) {
                    JOptionPane.showMessageDialog(this, (isWhiteTurn ? "White" : "Black") + " wins by checkmate!");
                    gameEnded = true; // End the game
                    return;
                } else if (isKingInCheck(!isWhiteTurn)) {
                    JOptionPane.showMessageDialog(this, (isWhiteTurn ? "Black" : "White") + " is in check!");
                }
    
                isWhiteTurn = !isWhiteTurn; // Switch turns
            } else {
                JOptionPane.showMessageDialog(this, "Invalid move: You must get out of check!");
            }
    
            resetBoardColors();
            startX = -1;
            startY = -1;
        }
    }
    
    
    
    

    private boolean isCorrectTurn(int x, int y) {
        String piece = squares[x][y].getText();
        return isWhiteTurn ? Character.isUpperCase(piece.charAt(0)) : Character.isLowerCase(piece.charAt(0));
    }

    private boolean isFriendlyPiece(int startX, int startY, int endX, int endY) {
        String startPiece = squares[startX][startY].getText();
        String endPiece = squares[endX][endY].getText();
        if (endPiece.isEmpty()) {
            return false; // Destination is empty
        }
        return Character.isUpperCase(startPiece.charAt(0)) == Character.isUpperCase(endPiece.charAt(0));
    }
    

    private boolean isValidMove(int startX, int startY, int endX, int endY) {
        if (isFriendlyPiece(startX, startY, endX, endY)) {
            return false; // Can't attack your own piece
        }
    
        String piece = squares[startX][startY].getText().toLowerCase();
        switch (piece) {
            case "p": return handlePawnMove(startX, startY, endX, endY);
            case "r": return handleRookMove(startX, startY, endX, endY);
            case "n": return handleKnightMove(startX, startY, endX, endY);
            case "b": return handleBishopMove(startX, startY, endX, endY);
            case "q": return handleQueenMove(startX, startY, endX, endY);
            case "k": return handleKingMove(startX, startY, endX, endY);
            default: return false;
        }
    }
    
    

    private boolean handlePawnMove(int startX, int startY, int endX, int endY) {
        int direction = squares[startX][startY].getText().equals("P") ? -1 : 1; // White moves up, Black moves down

        // Normal forward move
        if (startY == endY && squares[endX][endY].getText().isEmpty()) {
            if (endX - startX == direction) return true;
            if ((startX == 6 && direction == -1 || startX == 1 && direction == 1) && endX - startX == 2 * direction) {
                return squares[startX + direction][startY].getText().isEmpty();
            }
        }

        // Capture diagonally
        if (Math.abs(startY - endY) == 1 && endX - startX == direction && !squares[endX][endY].getText().isEmpty()) {
            return true;
        }

        return false;
    }

    private boolean handleRookMove(int startX, int startY, int endX, int endY) {
        if (startX == endX || startY == endY) {
            return isPathClear(startX, startY, endX, endY);
        }
        return false;
    }

    private boolean handleKnightMove(int startX, int startY, int endX, int endY) {
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    private boolean handleBishopMove(int startX, int startY, int endX, int endY) {
        if (Math.abs(startX - endX) == Math.abs(startY - endY)) {
            return isPathClear(startX, startY, endX, endY);
        }
        return false;
    }

    private boolean handleQueenMove(int startX, int startY, int endX, int endY) {
        return handleRookMove(startX, startY, endX, endY) || handleBishopMove(startX, startY, endX, endY);
    }

    private boolean handleKingMove(int startX, int startY, int endX, int endY) {
        return Math.abs(startX - endX) <= 1 && Math.abs(startY - endY) <= 1;
    }

    private boolean isPathClear(int startX, int startY, int endX, int endY) {
        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);
        int x = startX + dx, y = startY + dy;

        while (x != endX || y != endY) {
            if (!squares[x][y].getText().isEmpty()) {
                return false; // Path is blocked
            }
            x += dx;
            y += dy;
        }
        return true;
    }

    private void resetBoardColors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGUI());
    }
}
