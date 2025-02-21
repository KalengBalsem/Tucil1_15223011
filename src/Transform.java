import java.util.ArrayList;

public class Transform {
    // GEOMETRY TRANSFORMATION FUNCTIONS
    // clockwise rotation function
    public static ArrayList<Pair> RotateCW(ArrayList<Pair> arrangement) {
        ArrayList<Pair> rotatedArr = new ArrayList<>();
        // finding max row as the rotation anchor/reference (because the position (0,0) -> (0, max_row_of_initial_state) after 90 degree rotation CW)
        int max_row = MaxRowColumn(arrangement).row();
        int new_row, new_column;
        for (Pair p : arrangement) {
            if (p.row() > max_row) {
                max_row = p.row();
            }
        }
        for (Pair p : arrangement) {
            new_row = p.column;
            new_column = max_row - p.row();
            rotatedArr.add(new Pair(new_row, new_column));
        }

        return rotatedArr;
    }
    // mirror functions
    public static ArrayList<Pair> MirrorV(ArrayList<Pair> arrangement) {
        ArrayList<Pair> mirroredArr = new ArrayList<>();
        // vertical mirror (cerminkan terhadap kolom -> posisi kolom berubah; baris tetap)
        int max_column = MaxRowColumn(arrangement).column();
        for (Pair p : arrangement) {
            mirroredArr.add(new Pair(p.row(), max_column - p.column()));
        }
        return mirroredArr;
    }

    public static ArrayList<Pair> MirrorH(ArrayList<Pair> arrangement) {
        ArrayList<Pair> mirroredArr = new ArrayList<>();
        // horizontal mirror (cerminkan terhadap baris -> posisi baris berubah; kolom tetap)
        int max_row = MaxRowColumn(arrangement).row();
        for (Pair p : arrangement) {
            mirroredArr.add(new Pair(max_row - p.row(), p.column()));
        }
        return mirroredArr;
    }
    // finding max row and max column in "list of pairs"
    public static Pair MaxRowColumn(ArrayList<Pair> arrangement) {
        int max_row = -1, max_column = -1;
        for (Pair p : arrangement) {
            if (p.row() > max_row) {
                max_row = p.row();
            }
            if (p.column() > max_column) {
                max_column = p.column();
            }
        }
        return new Pair(max_row, max_column);
    }

    // function for (row, column) pairs
    public record Pair(int row, int column) {
        @Override
        public String toString() {
            return "(" + row + "," + column + ")";  // for better formatting
        }
    }
}
