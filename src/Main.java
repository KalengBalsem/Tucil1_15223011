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
        long N = Long.parseLong(String.valueOf(parsedFile[0].charAt(0)));
        long M = Long.parseLong(String.valueOf(parsedFile[0].charAt(2)));
        long P = Long.parseLong(String.valueOf(parsedFile[0].charAt(4)));

        String S = parsedFile[1];

        // MEMPROSES BLOK PUZZLE
        /* 
         * TODO:
         * - dont repeat yourself (DRY), make functions for repititive lines of code -> make functions for rotating and mirror
         * - bagaimana jika input hanya " A" (hanya satu subblok A) tapi terbaca (0,1) alih-alih (0,0)
         * DONE:
         * - array and dictionary format in Java to process and store block arrangement data
        */

        // menyortir blok dan setor data blok
        int row = 0;
        int column = 0;
        ArrayList<Character> block_id = new ArrayList<>();
        HashMap<Character, ArrayList<ArrayList<Pair>>> blocks = new HashMap<>();

        int variation_num = 0;
        char id = 'a';  // initialize id

        for (int i = 2; i < parsedFile.length; i++) {
            for (char ch: parsedFile[i].toCharArray()){
                if (Character.isAlphabetic(ch)) {
                    id = ch;
                    break;
                }
            }

            if (!block_id.contains(id)) {
                row = 0;
                column = 0;
                block_id.add(id);
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

            int max_row = 0, max_column = 0;
            for (Pair p : arrangements.get(0)) {
                if (p.row() > max_row) {
                    max_row = p.row();
                }
                if (p.column() > max_column) {
                    max_column = p.column();
                }
            }

            // ROTATION (WORKING ON MAKING 1 GENERAL FUNCTION)
            // cek jumlah rotasi berdasarkan susunan blok dari nilai unik pojok2 posisi.
            Pair corner1 = new Pair(0, 0);
            Pair corner2 = new Pair(0, max_row); // rotated 90 (column -> row, & vice versa)
            Pair corner3;
            if (max_row != 0) {
                corner3 = new Pair(max_row, max_column);
            } else {
                corner3 = corner1;
            } // rotated 180  (max_row and max_column != 0)
            Pair corner4 = new Pair(max_column, 0); // rotated 270 (row -> column, & vice versa)
            
            ArrayList<Pair> raw_corners = new ArrayList<>(Arrays.asList(corner1, corner2, corner3, corner4));

            ArrayList<Pair> corners = new ArrayList<>();
            for (Pair corner: raw_corners) {
                if (!corners.contains(corner)){
                    corners.add(corner);
                }
            }

            for (int i = 0; i < corners.size() - 1; i++) {
                blocks.get(id).add(new ArrayList<>());
            }

            // operasi "rotasi" (loop berdasarkan jumlah rotasi)
            for (Pair position : arrangements.get(0)) {
                int a = position.row(), b = position.column();
                for (Pair corner : corners) {
                    int new_row, new_column;
                    int x = corner.row(), y = corner.column();
                    if (x == 0 && y == 0) {
                        continue;
                    }
                    else if (x == 0) {
                        new_row = x + b;
                        new_column = y - a;
                    }
                    else if (y == 0) {
                        new_row = x - b;
                        new_column = y + a;
                    }
                    else {
                        new_row = x - a;
                        new_column = y - b;
                    }

                    variation_num++;
                    blocks.get(id).get
                    (variation_num).add(new Pair(new_row, new_column));
                }
                variation_num = 0;
            }
            // // END ROTATION

            // MIRROR (WORKING ON MAKING 1 GENERAL FUNCTION)
            // operasi "mirror" untuk tiap variasi rotasi termasuk rotasi 0, 90, 180, 270.
            arrangements = entry.getValue();
            ArrayList<ArrayList<Pair>> newVariations = new ArrayList<>();

            for (ArrayList<Pair> arrangement : arrangements) {
                max_row = 0;
                max_column = 0;
                for (Pair position : arrangement) {
                    if (position.row() > max_row) {
                        max_row = position.row();
                    }
                    if (position.column() > max_column) {
                        max_column = position.column();
                    }
                }
                // vertical mirror (cerminkan kolom)
                ArrayList<Pair> newVariation = new ArrayList<>();
                for (Pair position : arrangement) {
                    int new_column = max_column - position.column();
                    newVariation.add(new Pair(position.row(), new_column));
                }
                newVariations.add(newVariation);

                // horizontal mirror (cerminkan baris)
                newVariation = new ArrayList<>();
                for (Pair position : arrangement) {
                    int new_row = max_row - position.row();
                    newVariation.add(new Pair(new_row, position.column()));
                }
                newVariations.add(newVariation);
            }
            blocks.get(id).addAll(newVariations);
            variation_num = 0;
        }
        // // END MIRROR

        // membersihkan kemungkinan variasi tiap blok (agar tidak ada variasi duplikat)
        for (Map.Entry<Character, ArrayList<ArrayList<Pair>>> entry : blocks.entrySet()) {
            // take id and entry
            id = entry.getKey();
            ArrayList<ArrayList<Pair>> arrangements = entry.getValue();

            System.out.println(id);
            System.out.println(arrangements);
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

