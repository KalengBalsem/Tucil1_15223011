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
        /* 
         * TODO:
         * - cek apakah tiap parameter valid?
        */

        // menyortir parameter utama N M P dan S
        // Split the first line on whitespace
        String[] tokens = parsedFile[0].trim().split("\\s+");

        // Parse N, M, P
        int N = Integer.parseInt(tokens[0]);
        int M = Integer.parseInt(tokens[1]);
        int P = Integer.parseInt(tokens[2]);

        // Read the puzzle type (DEFAULT, CUSTOM, or PYRAMID)
        String S = parsedFile[1];

        int startParsingBlocks = 0;
        if (S.equalsIgnoreCase("DEFAULT")) {
            startParsingBlocks = 2;
        }
        else if (S.equalsIgnoreCase("CUSTOM")) {
            startParsingBlocks = 2 + N; // block dalam .txt berada di bawah konfigurasi papan CUSTOM yang setinggi N.
            
        } 
        else {
            throw new java.lang.Error("Board category invalid. Board category seharusnya DEFAULT atau CUSTOM.");
        }

        // MEMPROSES BLOK PUZZLE
        /* 
         * TODO:
         * - bagaimana jika input hanya " A" (hanya satu subblok A) tapi terbaca (0,1) alih-alih (0,0)
        */

        // menyortir blok dan menyetor data arrangement pertama tiap blok
        int row = 0;
        int column = 0;
        ArrayList<Character> block_ids = new ArrayList<>();
        HashMap<Character, ArrayList<ArrayList<Pair>>> blocks = new HashMap<>();

        int variation_num = 0;
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
                    blocks.get(id).get(variation_num).add(new Pair(row, column));
                }
                column++;
            }

            column = 0;
            row++;
        }

        // menciptakan semua kemungkinan variasi tiap blok
        for (Map.Entry<Character, ArrayList<ArrayList<Pair>>> entry : blocks.entrySet()) {
            // take id and entry
            id = entry.getKey();
            ArrayList<ArrayList<Pair>> arrangements = entry.getValue();
            ArrayList<ArrayList<Pair>> newVariations = new ArrayList<>();
            // ROTATION
            int num_of_rotation = 3; // variasi rotasi <= 3
            ArrayList<Pair> arrangement = arrangements.get(0); // starting arrangement
            for (int i = 0; i < num_of_rotation; i ++) {
                arrangement = Transform.rotateCW(arrangement);
                newVariations.add(arrangement);
            }
            // // END ROTATION
            blocks.get(id).addAll(newVariations); // add rotation variations to the arrangements (before they are mirrored.)
            newVariations = new ArrayList<>();
            // MIRROR
            for (ArrayList<Pair> arr : arrangements) {
                arrangement = Transform.mirrorV(arr);
                newVariations.add(arrangement);
                arrangement = Transform.mirrorH(arr);
                newVariations.add(arrangement);
            }
            // // END MIRROR
            blocks.get(id).addAll(newVariations); // add mirror variations to the arrangements.
        }

        // membersihkan variasi tiap blok (agar tidak ada variasi duplikat)
        blocks = Transform.cleanBlockVariations(blocks);

        // PENYUSUNAN BLOK DALAM PAPAN
        /*
         * parameter yang harus diperhatikan:
         * - blok2 yang sudah diletakkan di dalam papan (sehingga mesin tidak meletakkan blok duplikat)
         * - keep track of variations that we have tried. (A_1 B_2 ...) if fail we try (A_2 B_1) etc but we're not going to use A_1 as the first placement again.
         /*
          STEPS:
            - build block (DONE)
            - search posisi yang masi kosong urut kiri ke kanan dan atas ke bawah.
            - loop untuk memposisikan blok
            - cek apakah posisi valid (tidak ada bagian blok yang menempati posisi yang sudah terisi).
                jika valid -> isi; else -> loop variasi hingga memenuhi. (jika tidak ada variasi yang memenuhi -> pindah ke id berikutnya dan lakukan cek validitas yang sama)
            - rekam path setelah berhasil meletakkan block
            - lakukan backtracking jika terjebak di jalan buntu
          */
        // GOAL: seluruh papan terisi & semua blok telah digunakan.

        PuzzleBoard board = new PuzzleBoard(N, M, S);

        if (S.equalsIgnoreCase("CUSTOM")) {
            board.insertCustomConfiguration(Arrays.copyOfRange(parsedFile, 2, 2 + N));
        }

        Path path = new Path(block_ids);
        board.buildBoard();
        board.printBoard();
        
        boolean answerFound = false;
        ArrayList<ArrayList<Character>> remainingBlocksPermutations = path.generateRemainingBlocksPermutations();
        for (int i = 0; i < remainingBlocksPermutations.size(); i++) {
            if (board.solvePuzzle(0, remainingBlocksPermutations.get(i), path.placedBlocks, blocks, path)) {
                answerFound = true;
                break;
            }
        }
        if (answerFound == true) {
            System.out.println("\nanswer was found");
            board.printBoard();
        }
        else {
            System.out.println("\nanswer was not found");
            board.printBoard();
        }
    }

    // MAIN FUNCTIONS
    // function to parse input file
    public static String[] parseFile(String args) {
        String[] texts = args.split("\n"); // Split by comma, semicolon, or pipe
        return texts;
    }

    // function for (row, column) pairs
    public record Pair(int row, int column) {
        @Override
        public String toString() {
            return "(" + row + "," + column + ")";  // for better formatting
        }
    }
}

