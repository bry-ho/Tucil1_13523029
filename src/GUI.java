import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame {
    private JButton openButton, solveButton, saveTxtButton, saveImgButton;
    private JPanel boardPanel;
    private JLabel statusLabel;
    private IO IO;
    private Solver solver;
    private String currentFilePath;

    public GUI() {
        setTitle("Puzzle Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel controlPanel = new JPanel();
        openButton = new JButton("Open File");
        solveButton = new JButton("Solve");
        saveTxtButton = new JButton("Save as TXT");
        saveImgButton = new JButton("Save as PNG");
        
        controlPanel.add(openButton);
        controlPanel.add(solveButton);
        controlPanel.add(saveTxtButton);
        controlPanel.add(saveImgButton);
        
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(700, 700));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);

        add(controlPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        solveButton.setEnabled(false);
        saveTxtButton.setEnabled(false);
        saveImgButton.setEnabled(false);
        
        openButton.addActionListener(e -> openFile());
        solveButton.addActionListener(e -> solvePuzzle());
        saveTxtButton.addActionListener(e -> saveTxtFile());
        saveImgButton.addActionListener(e -> saveImgFile());
        
        pack();
        setLocationRelativeTo(null);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Puzzle File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
    
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                currentFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                IO = new IO();
                IO.readInputFile(currentFilePath);
                displayBoard(IO.getBoard());
                solveButton.setEnabled(true);
                
                JOptionPane.showMessageDialog(this,
                    "File loaded successfully:\n" + currentFilePath,
                    "File Loaded",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                statusLabel.setText("File loaded successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading file: " + ex.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void solvePuzzle() {
        if (IO != null) {
            solver = new Solver(IO.getBoard(), IO.getPieces());
            solver.solvePuzzle();
    
            if (solver.isSolved()) {
                displayBoard(IO.getBoard());
                String message = String.format("Puzzle solved!\nTime taken: %d ms\nCases checked: %d", 
                    solver.getDuration(), solver.getCaseChecked());
                JOptionPane.showMessageDialog(this,
                    message,
                    "Solution Found",
                    JOptionPane.INFORMATION_MESSAGE);
                statusLabel.setText("Solved in " + solver.getDuration() + "ms. Cases checked: " + solver.getCaseChecked());
                saveTxtButton.setEnabled(true);
                saveImgButton.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No solution exists for this puzzle configuration.",
                    "No Solution",
                    JOptionPane.WARNING_MESSAGE);
                statusLabel.setText("No solution found");
            }
        }
    }

   private void saveTxtFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Solution as Text File");
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        try {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            if (!filePath.endsWith(".txt")) {
                file = new File(filePath + ".txt");
            }
            
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            IO.setSolver(solver); 
            IO.saveBoardToFile(file.getAbsolutePath());
            
            if (file.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "Solution saved successfully to:\n" + file.getAbsolutePath(),
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new IOException("File was not created successfully");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error saving file: " + ex.getMessage() + "\nPlease check if you have write permissions.",
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

private void saveImgFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save Solution as Image");
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        try {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            if (!filePath.endsWith(".png")) {
                file = new File(filePath + ".png");
            }
            
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            IO.setSolver(solver); 
            IO.saveBoardAsImage(file.getAbsolutePath());
            
            if (file.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "Image saved successfully to:\n" + file.getAbsolutePath(),
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new IOException("Image was not created successfully");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error saving image: " + ex.getMessage() + "\nPlease check if you have write permissions.",
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

    private void displayBoard(Board board) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(board.getRows(), board.getCols()));
        
        int cellSize = Math.min(500 / board.getCols(), 500 / board.getRows());
        
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                JPanel cell = new JPanel();
                cell.setLayout(new BorderLayout());
                cell.setPreferredSize(new Dimension(cellSize, cellSize));
                cell.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                
                char piece = board.getBoard()[i][j];
                if (piece != '.' && piece != '#') {
                    cell.setBackground(IO.getColorFromChar(piece));

                    JLabel label = new JLabel(String.valueOf(piece));
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, cellSize/2)); 
                    label.setForeground(Color.WHITE); 
                    cell.add(label, BorderLayout.CENTER);
                } else {
                    cell.setBackground(Color.BLACK);
                }
                
                boardPanel.add(cell);
            }
        }
        
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI().setVisible(true);
        });
    }
}