import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Scanner object untuk user input
        Scanner scanner = new Scanner(System.in);
        // Prompt user to enter filename
        System.out.print("Enter filename (including .txt): "); // e.g.: ./test/test1.txt
        String filename = scanner.nextLine();
        scanner.close();

        String fileContent = ReadFile.readFile(filename);
        String[] parsedFile = parseFile(fileContent);

        // MEMPROSES PARAMETER UTAMA
        // menyortir parameter utama N M P dan S
        long N = Long.parseLong(String.valueOf(parsedFile[0].charAt(0)));
        long M = Long.parseLong(String.valueOf(parsedFile[0].charAt(2)));
        long P = Long.parseLong(String.valueOf(parsedFile[0].charAt(4)));

        String S = parsedFile[1];

        // MEMPROSES BLOK PUZZLE
        /* 
         * TODO:
         * - array and dictionary format in Java to process and store block arrangement data
        */

        // menyortir blok dan setor data blok
        int row = 0;
        int column = 0;
        ArrayList<Integer> block_id = new ArrayList<>();
        HashMap<String, ArrayList<ArrayList<AbstractMap.SimpleEntry<Integer, Integer>>>> blocks = new HashMap<>();
        int variation_num = 0;

        System.out.println(row + " " + blocks);
    }


    // function to parse input file
    public static String[] parseFile(String args) {
        String[] texts = args.split("\n"); // Split by comma, semicolon, or pipe
        return texts;
    }
}