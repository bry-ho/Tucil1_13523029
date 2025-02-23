import java.util.List;

public class Solver {
    private Board board;
    private List<Piece> pieces;
    private boolean isSolved;
    private long caseChecked;
    private long duration;

    public Solver(Board board, List<Piece> pieces) {
        this.board = board;
        this.pieces = pieces;
        this.isSolved = false;
        this.caseChecked = 0;
        this.duration = 0;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public long getCaseChecked() {
        return caseChecked;
    }

    public long getDuration() {
        return duration;
    }
    
    public void solve(int idx) {
        if (board.isComplete() && idx == pieces.size()) {
            isSolved = true;
            board.printBoard();
            return;
        }
        
        if (idx == pieces.size()) {
            return;
        }
        
        Piece piece = pieces.get(idx);
        List<Piece> variants = piece.getAllVariants();
        
        for (Piece variant : variants) {
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getCols(); j++) {
                    if (board.canPlacePiece(variant, i, j)) {
                        caseChecked++;
                        // System.out.println("case ke = " +  caseChecked);
                        // System.out.println("Placing piece " + idx + " at position (" + i + ", " + j + ")");
                        board.placePiece(variant, i, j);

                        // board.printBoard();
                        // System.out.println();
                        
                        solve(idx + 1);
                        if (isSolved) {
                            return;
                        }
                        
                        // System.out.println("Removing piece " + idx + " from position (" + i + ", " + j + ")");
                        board.removePiece(variant, i, j);
                    }
                }
            }
        }
    }

    public void solvePuzzle() {
        long startTime = System.currentTimeMillis();
        solve(0);
        long endTime = System.currentTimeMillis();
        duration = endTime - startTime;
    
        if (isSolved) {
            System.out.println("Solution found");
        } else {
            System.out.println("No solution found.");
        }
    
        System.out.println("Waktu pencarian: " + duration + " ms");
        System.out.println("Banyak kasus yang ditinjau: " + caseChecked);
    }
}
