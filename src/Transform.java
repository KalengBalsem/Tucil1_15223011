import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Transform {
    // 2D GEOMETRY TRANSFORMATION FUNCTIONS
    // clockwise rotation function
    public static ArrayList<Main.Pair> rotateCW(ArrayList<Main.Pair> arrangement) {
        ArrayList<Main.Pair> rotatedArr = new ArrayList<>();
        // finding max row as the rotation anchor/reference (because the position (0,0) -> (0, max_row_of_initial_state) after 90 degree rotation CW)
        int max_row = MaxRowColumn(arrangement).row();
        int new_row, new_column;
        for (Main.Pair p : arrangement) {
            if (p.row() > max_row) {
                max_row = p.row();
            }
        }
        for (Main.Pair p : arrangement) {
            new_row = p.column();
            new_column = max_row - p.row();
            rotatedArr.add(new Main.Pair(new_row, new_column));
        }

        return rotatedArr;
    }
    // mirror functions
    public static ArrayList<Main.Pair> mirrorV(ArrayList<Main.Pair> arrangement) {
        ArrayList<Main.Pair> mirroredArr = new ArrayList<>();
        // vertical mirror (cerminkan terhadap kolom -> posisi kolom berubah; baris tetap)
        int max_column = MaxRowColumn(arrangement).column();
        for (Main.Pair p : arrangement) {
            mirroredArr.add(new Main.Pair(p.row(), max_column - p.column()));
        }
        return mirroredArr;
    }

    public static ArrayList<Main.Pair> mirrorH(ArrayList<Main.Pair> arrangement) {
        ArrayList<Main.Pair> mirroredArr = new ArrayList<>();
        // horizontal mirror (cerminkan terhadap baris -> posisi baris berubah; kolom tetap)
        int max_row = MaxRowColumn(arrangement).row();
        for (Main.Pair p : arrangement) {
            mirroredArr.add(new Main.Pair(max_row - p.row(), p.column()));
        }
        return mirroredArr;
    }
    // finding max row and max column in "list of Main.Pairs"
    public static Main.Pair MaxRowColumn(ArrayList<Main.Pair> arrangement) {
        int max_row = -1, max_column = -1;
        for (Main.Pair p : arrangement) {
            if (p.row() > max_row) {
                max_row = p.row();
            }
            if (p.column() > max_column) {
                max_column = p.column();
            }
        }
        return new Main.Pair(max_row, max_column);
    }

    // ADDITIONAL FUNCTION
    public static HashMap<Character, ArrayList<ArrayList<Main.Pair>>> cleanBlockVariations(HashMap<Character, ArrayList<ArrayList<Main.Pair>>> blocks) {
        for (Map.Entry<Character, ArrayList<ArrayList<Main.Pair>>> entry : blocks.entrySet()) {
            // take id and entry
            char id = entry.getKey();
            ArrayList<ArrayList<Main.Pair>> arrangements = entry.getValue();
            
            ArrayList<ArrayList<Main.Pair>> uniqueArrangements = new ArrayList<>();
            for (ArrayList<Main.Pair> arr : arrangements) {
                arr.sort(Comparator.comparingInt(Main.Pair::row).thenComparingInt(Main.Pair::column));
                if (!uniqueArrangements.contains(arr)) {
                    uniqueArrangements.add(arr);
                }
            }
            blocks.put(id, uniqueArrangements); // overwrite arrangements dengan uniqueArrangements
        }
        return blocks;
    }
}
