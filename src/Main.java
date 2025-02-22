import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // MEMBACA FILE "*.TXT"
        // Scanner object untuk user input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter filename (including .txt): ");      // e.g.: ./test/test1.txt
        String filename = reader.readLine();

        String fileContent = ReadFile.readFile(filename);
        String[] parsedFile = parseFile(fileContent);

        // MEMPROSES PARAMETER UTAMA
        // menyortir parameter utama N M P dan S
        // Split baris pertama parsedFile berdasarkan whitespace
        String[] tokens = parsedFile[0].trim().split("\\s+");

        // membaca parameter: N, M, P
        int N = Integer.parseInt(tokens[0]);
        int M = Integer.parseInt(tokens[1]);
        int P = Integer.parseInt(tokens[2]);

        // membaca kategori puzzle (DEFAULT, CUSTOM, or PYRAMID)
        String S = parsedFile[1];
        int startParsingBlocks = 0;
        if (S.equalsIgnoreCase("DEFAULT")) {
            startParsingBlocks = 2;
        }
        else if (S.equalsIgnoreCase("CUSTOM")) {
            startParsingBlocks = 2 + N;                              // block dalam .txt berada di bawah konfigurasi papan CUSTOM yang setinggi N.
        } 
        else {
            throw new java.lang.Error("Board category invalid. Board category seharusnya DEFAULT atau CUSTOM.");
        }
 
        // MEMBACA BLOK PUZZLE
        // menyortir blok dan menyetor data arrangement pertama tiap blok
        int row = 0;
        int column = 0;
        ArrayList<Character> block_ids = new ArrayList<>();
        HashMap<Character, ArrayList<ArrayList<Pair>>> blocks = new HashMap<>();
        char id = ' ';  // initialize id

        for (int i = startParsingBlocks; i < parsedFile.length; i++) {
            for (char ch: parsedFile[i].toCharArray()){
                if (Character.isAlphabetic(ch)) {
                    id = ch;
                    break;
                }
            }
            if (!block_ids.contains(id)) {
                row = 0;
                column = 0;
                block_ids.add(id);
                blocks.putIfAbsent(id, new ArrayList<>());
                blocks.get(id).add(new ArrayList<>());
            }
            for (char ch: parsedFile[i].toCharArray()){
                if (!Character.isWhitespace(ch)) {
                    blocks.get(id).get(0).add(new Pair(row, column)); // menyetor data arrangement pertama untuk block dengan id
                }
                column++;
            }
            column = 0;
            row++;
        }

        // mengecek validitas parameter dan input (CONSTRAINTS)
        /*
         * LIST:
         * - cek apakah block_unique_ids.size() = block_ids.size() untuk cek apakah ada ID duplikat
         * - cek apakah block_ids.size() == P?
         */

        // MEMPROSES BLOK PUZZLE
        // menciptakan semua kemungkinan variasi tiap blok (rotasi dan cermin)
        for (Map.Entry<Character, ArrayList<ArrayList<Pair>>> entry : blocks.entrySet()) {
            // take id and entry
            id = entry.getKey();
            ArrayList<ArrayList<Pair>> arrangements = entry.getValue();
            ArrayList<ArrayList<Pair>> newVariations = new ArrayList<>();
            // ROTATION
            int num_of_rotation = 3; // variasi rotasi <= 3
            ArrayList<Pair> arrangement = arrangements.get(0);     // starting arrangement
            for (int i = 0; i < num_of_rotation; i ++) {
                arrangement = Transform.rotateCW(arrangement);
                newVariations.add(arrangement);
            }
            // // END ROTATION
            blocks.get(id).addAll(newVariations);                       // add rotation variations to the arrangements (before they are mirrored.)
            newVariations = new ArrayList<>();
            // MIRROR
            for (ArrayList<Pair> arr : arrangements) {
                arrangement = Transform.mirrorV(arr);
                newVariations.add(arrangement);
                arrangement = Transform.mirrorH(arr);
                newVariations.add(arrangement);
            }
            // // END MIRROR
            blocks.get(id).addAll(newVariations);                       // add mirror variations to the arrangements.
        }

        // membersihkan variasi tiap blok (agar tidak ada variasi duplikat)
        blocks = Transform.cleanBlockVariations(blocks);

        // MENYELESAIKAN PUZZLE
        PuzzleBoard board = new PuzzleBoard(N, M, S);
        if (S.equalsIgnoreCase("CUSTOM")) {
            board.insertCustomConfiguration(Arrays.copyOfRange(parsedFile, 2, 2 + N));
        }
        board.buildBoard();
        Path path = new Path(block_ids);
        boolean answerFound = false;
        ArrayList<Character> singleBlockPermutation = block_ids;
        Collections.sort(singleBlockPermutation);

        long startTime = System.currentTimeMillis();                    // mengukur waktu awal algoritma
        // Permutasi pertama dari block_ids
        if (board.solvePuzzle(0, singleBlockPermutation, path.placedBlocks, blocks, path)) {
            answerFound = true;
        }
        // Menghasilkan permutasi baru di tiap loop sembari mencoba apakah permutasi tersebut dapat menyelesaikan IQ Puzzler Pro
        while (!answerFound && nextPermutation(singleBlockPermutation)) {
            if (board.solvePuzzle(0, singleBlockPermutation, path.placedBlocks, blocks, path)) {
                answerFound = true;
            }
        }
        long endTime = System.currentTimeMillis();;                     // mengukur waktu akhir algoritma
        long executionTime = endTime - startTime;                       // Waktu eksekusi dalam milisecond

        // OUTPUT PRINT
        if (answerFound == true) {
            board.printBoard();
        }
        else {
            System.out.println("\nSolusi tidak ditemukan.");
        }
        System.out.println("\n" + "Waktu pencarian: " + executionTime + " ms");
        System.out.println("\n" + "Banyak kasus yang ditinjau: " + path.exploredCases);
        System.out.println("\n" + "Apakah anda ingin menyimpan solusi? (ya/tidak) ");
        String menyimpanSolusi = reader.readLine();
        
        // MENYIMPAN SOLUSI
        String inputFilename = Paths.get(filename).getFileName().toString().replaceAll("\\.txt$", ""); // Get file name
        if (menyimpanSolusi.equalsIgnoreCase("ya")){
            try (FileWriter writer = new FileWriter(String.format("test_output/%s_output.txt", inputFilename))) {
                    writer.write(board.boardToText());
                    System.out.println("File saved successfully as 'output.txt'.");
            }
            PrettyOutput.generatePuzzleImage(board.board, String.format("test_output/%s.png", inputFilename));
        }
    }


    // HELPER FUNCTIONS
    // function to parse input file
    public static String[] parseFile(String args) {
        String[] texts = args.split("\n");                  // Split by new line
        return texts;
    }

    // function for (row, column) pairs
    public record Pair(int row, int column) {
        @Override
        public String toString() {
            return "(" + row + "," + column + ")";                // for better formatting
        }
    }

    // permutation function
    private static boolean nextPermutation(ArrayList<Character> arr) {
        int n = arr.size();
        // Find largest index k where arr[k] < arr[k+1].
        int k = n - 2;
        while (k >= 0 && arr.get(k) >= arr.get(k + 1)) {
            k--;
        }
        if (k < 0) {
            return false; // no more permutations
        }
        // Find largest index l > k such that arr[k] < arr[l].
        int l = n - 1;
        while (arr.get(k) >= arr.get(l)) {
            l--;
        }
        // Swap arr[k] and arr[l].
        Collections.swap(arr, k, l);
        // Reverse the sub-list from k+1 to the end.
        reverse(arr, k + 1, n - 1);
        return true;
    }

    private static void reverse(List<Character> arr, int start, int end) {
        while (start < end) {
            Collections.swap(arr, start, end);
            start++;
            end--;
        }
    }
}

