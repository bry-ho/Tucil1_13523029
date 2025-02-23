import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String inputFilename = "test/input/1" ;
        String outputFilename = "test/output/1" ;
        try {
            IO IO = new IO();
            IO.readInputFile(inputFilename + ".txt");

            Solver solver = new Solver(IO.getBoard(), IO.getPieces());
            IO.setSolver(solver); 

            solver.solvePuzzle();
            
            Scanner scanner = new Scanner(System.in);
            System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak): ");
            String response = scanner.nextLine().trim().toLowerCase();
            scanner.close();

            if (response.equals("ya")) {
                IO.saveBoardToFile(outputFilename + "_solution.txt");
                IO.saveBoardAsImage(outputFilename + "_solution.png");
                System.out.println("Solusi telah disimpan.");
            } else {
                System.out.println("Solusi tidak disimpan.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
