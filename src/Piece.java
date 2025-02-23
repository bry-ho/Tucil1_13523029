import java.util.List;
import java.util.ArrayList;

public class Piece {
    private char[][] piece;
    private int width;
    private int height;

    public Piece(char[][] piece) {
        this.width = piece[0].length;
        this.height = piece.length;
        this.piece = new char[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.piece[i][j] = piece[i][j];
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char[][] getPiece() {
        char [][] copy = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                copy[i][j] = piece[i][j];
            }
        }

        return copy;
    }
    
    public Piece rotate() {
        char[][] rotated = new char[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rotated[i][j] = piece[height - j - 1][i];
            }
        }
        
        return new Piece(rotated);
    }

    public Piece flipHorizontal() {
        char[][] flipped = new char[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                flipped[i][j] = piece[i][width - j - 1];
            }
        }

        return new Piece(flipped);
    }   
    
    public List<Piece> getAllVariants() {
        List<Piece> variants = new ArrayList<>();
        Piece current = this;

        for (int i = 0; i < 4; i++) {
            variants.add(current);
            variants.add(current.flipHorizontal());
            current = current.rotate();
        }

        return variants;
    }
        
    void printPiece() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(piece[i][j]);
            }
            System.out.println();
        }
    }

    // public static void main(String[] args) {
    //     char[][] piece = {
    //         {'A', 'B', 'C'},
    //         {'D', 'E', 'F'}
    //     };
    //     Piece p = new Piece(piece);
    //     p.printPiece();
    //     System.out.println();
    //     System.out.println("char piece:");
    //     for (int i = 0; i < p.getHeight(); i++) {
    //         for (int j = 0; j < p.getWidth(); j++) {
    //             System.out.print(p.getPiece()[i][j]);
    //             System.out.println();
    //         }
    //     }
    //     System.out.println();
    //     System.out.println("all piece variants:");
    //     List<Piece> variants = p.getAllVariants();
    //     for (Piece variant : variants) {
    //         variant.printPiece();
    //         System.out.println();
    //     }
    // }
}
