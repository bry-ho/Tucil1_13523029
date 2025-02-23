public class Board {
    private char[][] board;
    private int rows;
    private int cols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '#';
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean canPlacePiece(Piece piece, int x, int y) {
        char[][] shape = piece.getPiece();
        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();

        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (shape[i][j] != '#' && (x + i >= rows || y + j >= cols || board[x + i][y + j] != '#')) {
                    return false;
                }
            }
        }

        return true; 
    }

    public void placePiece(Piece piece, int x, int y) {
        if (canPlacePiece(piece, x, y)) {
            char[][] shape = piece.getPiece();
            int pieceWidth = piece.getWidth();
            int pieceHeight = piece.getHeight();

            for (int i = 0; i < pieceHeight; i++) {
                for (int j = 0; j < pieceWidth; j++) {
                    if (shape[i][j] != '#') {
                        board[x + i][y + j] = shape[i][j];
                    }
                }
            }
        }
    }

    public void removePiece(Piece piece, int x, int y) {
        char[][] shape = piece.getPiece();
        int pieceHeight = piece.getHeight();
        int pieceWidth = piece.getWidth();

        for (int i = 0; i < pieceHeight; i++) {
            for (int j = 0; j < pieceWidth; j++) {
                if (shape[i][j] != '#') {
                    board[x + i][y + j] = '#';
                }
            }
        }
    }

    public boolean isComplete() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == '#') {
                    return false;
                }
            }
        }

        return true;
    }
    
    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(getColor(board[i][j]) + board[i][j] + "\u001B[0m");
            }
            System.out.println();
        }
    }
    
    public String getColor(char c) {
        if (c == '#') return "\u001B[37m"; // White for empty spaces
        if (c < 'A' || c > 'Z') return "\u001B[0m"; // Ignore non-A-Z chars
        
        switch(c) {
            case 'A': return "\u001B[31m";  // Red
            case 'B': return "\u001B[32m";  // Green
            case 'C': return "\u001B[33m";  // Yellow
            case 'D': return "\u001B[34m";  // Blue
            case 'E': return "\u001B[35m";  // Magenta
            case 'F': return "\u001B[36m";  // Cyan
            case 'G': return "\u001B[91m";  // Bright Red
            case 'H': return "\u001B[92m";  // Bright Green
            case 'I': return "\u001B[93m";  // Bright Yellow
            case 'J': return "\u001B[94m";  // Bright Blue
            case 'K': return "\u001B[95m";  // Bright Magenta
            case 'L': return "\u001B[96m";  // Bright Cyan
            case 'M': return "\u001B[35m";  // Dark Orchid (Purple)
            case 'N': return "\u001B[90m";  // Gray
            case 'O': return "\u001B[32m";  // Dark Green
            case 'P': return "\u001B[41m\u001B[30m";  // Red Background
            case 'Q': return "\u001B[42m\u001B[30m";  // Green Background
            case 'R': return "\u001B[43m\u001B[30m";  // Yellow Background
            case 'S': return "\u001B[44m\u001B[30m";  // Blue Background
            case 'T': return "\u001B[45m\u001B[30m";  // Magenta Background
            case 'U': return "\u001B[46m\u001B[30m";  // Cyan Background
            case 'V': return "\u001B[101m\u001B[30m"; // Bright Red Background
            case 'W': return "\u001B[102m\u001B[30m"; // Bright Green Background
            case 'X': return "\u001B[103m\u001B[30m"; // Bright Yellow Background
            case 'Y': return "\u001B[104m\u001B[30m"; // Bright Blue Background
            case 'Z': return "\u001B[105m\u001B[30m"; // Bright Magenta Background
            default: return "\u001B[0m";
        }
    }

    // public static void main(String[] args) {
    //     Board board = new Board(5, 5);

    //     Piece piece = new Piece(new char[][]{{'A', 'A'}, {'A', '.'}});

    //     board.placePiece(piece, 0, 0);
    //     board.printBoard();
    //     System.out.println(board.canPlacePiece(piece, 0, 0));
    //     System.out.println();

    //     Piece piece2 = new Piece(new char[][]{{'B', 'B'}, {'B', '.'}});
    //     board.placePiece(piece2, 0, 2);
    //     board.printBoard();
    //     System.out.println(board.canPlacePiece(piece2, 0, 2));
    //     System.out.println();

    //     board.removePiece(piece, 0, 0);
    //     board.printBoard();
    // }
}

    
