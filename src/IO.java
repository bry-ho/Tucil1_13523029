import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class IO {
    private int N, M, P;
    private Board board;
    private List<Piece> pieces;
    private Solver solver;
    private char[][] shape;
    private String caseType;

    public IO() {
        this.N = 0;
        this.M = 0;
        this.P = 0;
        this.board = null;
        this.pieces = null;
        this.solver = null;
        this.shape = null;
        this.caseType = null;
    }

    public Board getBoard() {
        return board;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public void readInputFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String firstLine = br.readLine();

        if (firstLine == null || firstLine.trim().isEmpty()) {
            throw new IOException("File kosong");
        }

        StringTokenizer st = new StringTokenizer(firstLine);

        if (st.countTokens() != 3) {
            throw new IOException("Format file salah");
        }

        try {
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            P = Integer.parseInt(st.nextToken());
    
            if (N <= 0 || M <= 0 || P <= 0) {
                throw new NumberFormatException("Error: N, M, and P must be positive integers.");
            }
        } catch (NumberFormatException e) {
            throw new IOException("Error: N, M, and P must be valid positive integers.");
        }

        caseType = br.readLine();
        if (caseType == null || caseType.trim().isEmpty()) {
            throw new IOException("Error: Missing case type.");
        }

        pieces = new ArrayList<>();
        List<String> shape = new ArrayList<>();
        
        caseType = caseType.trim().toUpperCase();

        this.board = new Board(N, M);

        if (!caseType.equals("DEFAULT") && !caseType.equals("CUSTOM")) {
            throw new IOException("Error: Invalid case type.");
        }

        if (caseType.equals("CUSTOM")) {
            List<String> customLines = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                String line = br.readLine();
                customLines.add(line);
            }
            char[][] customBoard = readCustomMatrix(customLines);
            for (int i = 0; i < customBoard.length; i++) {
                for (int j = 0; j < customBoard[i].length; j++) {
                    if (customBoard[i][j] == '.') {
                        board.getBoard()[i][j] = '.';
                    }
                }
            }
        }
        
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) { 
            line = br.readLine();
        }
    
        if (line == null) return;
    
        char currentPiece = firstCharPiece(line);
    
        while (line != null) {
            if (line.trim().isEmpty()) {
                line = br.readLine();
                continue;
            }
    
            char firstLetter = firstCharPiece(line);
            if (firstLetter != currentPiece) { 
                if (!shape.isEmpty()) {
                    pieces.add(new Piece(stringListToCharArray(shape)));
                }
    
                shape = new ArrayList<>();
                currentPiece = firstLetter;
            }
    
            shape.add(line);
            line = br.readLine();
        }
    
        if (!shape.isEmpty()) {
            pieces.add(new Piece(stringListToCharArray(shape)));
        }
    
        // Validasi jumlah pieces
        if (pieces.size() != P) {
            throw new IOException("Format file salah");
        }
            

        // System.out.println("pieces size = " + pieces.size());
        // System.out.println("P = " + P);
        
        // for (Piece piece : pieces) {
        //     piece.printPiece();
        //     System.out.println();
        // }
    }


    public void saveBoardToFile(String filename) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getCols(); j++) {
                    writer.write(board.getBoard()[i][j]);
                }
                writer.newLine();
            }
            System.out.println("Board disimpan ke " + filename);
            writer.newLine();  
            writer.write("Waktu pencarian: " + (solver.getDuration()) + " ms");
            writer.newLine();
            writer.write("Banyak kasus yang ditinjau: " + solver.getCaseChecked());
        } catch (IOException e) {
            System.out.println("Error saving board: " + e.getMessage());
        }
    }

    public void saveBoardAsImage(String filename) {
        int cellSize = 50;

        int width = board.getCols() * cellSize;
        int height = board.getRows() * cellSize;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
    
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                char pieceChar = board.getBoard()[i][j];
                Color color = getColorFromChar(pieceChar);
                g.setColor(color);
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
    
                g.setColor(Color.BLACK);
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    
        g.dispose();
    
        try {
            ImageIO.write(image, "png", new File(filename));
            System.out.println("Board disimpan ke " + filename);
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }
    
    public char[][] readCustomMatrix(List<String> customLine) {
        int maxChars = 0;
        for (String line : customLine) {
            maxChars = Math.max(maxChars, line.length());
        }
    
        char[][] customMatrix = new char[customLine.size()][maxChars];
    
        for (int i = 0; i < customLine.size(); i++) {
            String line = customLine.get(i);
            for (int j = 0; j < maxChars; j++) {
                if (j < line.length() && line.charAt(j) == 'X') {
                    customMatrix[i][j] = 'X';
                } else {
                    customMatrix[i][j] = '.';
                }
            }
        }
    
        return customMatrix;        
    }

    private char firstCharPiece(String line) {
        for (char c : line.toCharArray()) {
            if (Character.isLetter(c)) {
                return c;
            }
        }
        return '#';
    }

    private char[][] stringListToCharArray(List<String> shapeLines) {
        int maxChars = 0;
        for (String line : shapeLines) {
            maxChars = Math.max(maxChars, line.length());
        }
    
        this.shape = new char[shapeLines.size()][maxChars];
    
        for (int i = 0;i<shapeLines.size();i++) {
            String line = shapeLines.get(i);
            for (int j = 0; j < maxChars; j++) {
                if (j < line.length()) {
                    char currentChar = line.charAt(j);
                    this.shape[i][j] = (currentChar == ' ')?'#' : currentChar;
                } else {
                    this.shape[i][j] = '#';
                }
            }
        }

        return this.shape;
    }

    public Color getColorFromChar(char piece) {
        switch (piece) {
            case 'A': return new Color(255, 0, 0);     // Red
            case 'B': return new Color(0, 255, 0);     // Green
            case 'C': return new Color(255, 255, 0);   // Yellow
            case 'D': return new Color(0, 0, 255);     // Blue
            case 'E': return new Color(255, 0, 255);   // Magenta
            case 'F': return new Color(0, 255, 255);   // Cyan
            case 'G': return new Color(255, 100, 100); // Bright Red
            case 'H': return new Color(100, 255, 100); // Bright Green
            case 'I': return new Color(255, 255, 150); // Bright Yellow
            case 'J': return new Color(100, 100, 255); // Bright Blue
            case 'K': return new Color(255, 100, 255); // Bright Magenta
            case 'L': return new Color(100, 255, 255); // Bright Cyan
            case 'M': return new Color(153, 50, 204);  // Dark Orchid (Purple) 
            case 'N': return new Color(128, 128, 128); // Gray
            case 'O': return new Color(0, 100, 0);     // Dark Green
            case 'P': return new Color(255, 0, 0);     // Red Background
            case 'Q': return new Color(0, 255, 0);     // Green Background
            case 'R': return new Color(255, 255, 0);   // Yellow Background
            case 'S': return new Color(0, 0, 255);     // Blue Background
            case 'T': return new Color(255, 0, 255);   // Magenta Background
            case 'U': return new Color(0, 255, 255);   // Cyan Background
            case 'V': return new Color(255, 100, 100); // Bright Red Background
            case 'W': return new Color(100, 255, 100); // Bright Green Background
            case 'X': return new Color(255, 255, 150); // Bright Yellow Background
            case 'Y': return new Color(100, 100, 255); // Bright Blue Background
            case 'Z': return new Color(255, 100, 255); // Bright Magenta Background
            default: return Color.BLACK;              // Unknown characters
        }
    }
}