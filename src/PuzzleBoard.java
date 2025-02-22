import java.util.*;
public class PuzzleBoard {
    private int rows;
    private int columns;
    private String category;
    private String[] customConfiguration;
    public char[][] board;

    // object instantiation
    public PuzzleBoard(int rows, int columns, String category) {
        this.rows = rows;
        this.columns = columns;
        this.category = category;
        this.board = new char[rows][columns];
    }

    public void buildBoard() {
        // initialize board with '0' (empty space)
        // DEFAULT (RECTANGULAR)
        if (this.category.equalsIgnoreCase("DEFAULT")) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    board[r][c] = '0';
                }
            }
        }
        // CUSTOM
        else if (this.category.equalsIgnoreCase("CUSTOM")) {
            for (int r = 0; r < rows; r++) {
                String customRow = customConfiguration[r];
                for (int c = 0; c < columns; c++) {
                    char customColumn = customRow.charAt(c);
                    if (customColumn == 'X' || customColumn == 'x') {
                        board[r][c] = '0';
                    }
                    else if (customColumn == '.') {
                        board[r][c] = '.';
                    } else {
                        throw new java.lang.Error("custom board configuration format tidak valid.");
                    }
                }
            }
        }
    }
    
    public void insertCustomConfiguration(String[] customConfiguration) {
        this.customConfiguration = customConfiguration;
    }

    public boolean placeBlock(char id, ArrayList<Main.Pair> arrangement, Main.Pair blockAnchor){
        // memperbaiki/normalisasi block yang tidak start dari (0,0) (agar tiap arrangement dipastikan memiliki 1 subblock dengan Pair(0,0) yang menempati posisi blockAnchor)
        int first_row = arrangement.get(0).row();
        int first_column = arrangement.get(0).column();
        if (first_row != 0 || first_column != 0) {
            for (int i = 0; i < arrangement.size(); i++) {
                arrangement.set(i, new Main.Pair(arrangement.get(i).row() - first_row, arrangement.get(i).column() - first_column));
            }
        }
        // meletakkan block dalam bidang jika memenuhi syarat.
        boolean validity = true;
        for (Main.Pair p : arrangement) {
            int row_position = blockAnchor.row() + p.row();
            int column_position = blockAnchor.column() + p.column();
            if (!isValidPosition(row_position, column_position)) {
                validity = false;
            }
        }
        if (validity) {
            for (Main.Pair p : arrangement) {
                int row_position = blockAnchor.row() + p.row();
                int column_position = blockAnchor.column() + p.column();
                board[row_position][column_position] = id;
            }
        }
        return validity;
    }

    public boolean solvePuzzle(Integer blockIndex, ArrayList<Character> block_ids, ArrayList<Character> placedBlocks, HashMap<Character, ArrayList<ArrayList<Main.Pair>>> blocks, Path path) {
        path.exploredCases++; // menghitung jumlah iterasi ditinjau dari algorima rekursif

        if (this.gameFinishStatus() && placedBlocks.size() == block_ids.size()) {
            return true;
        } else {
            char id = block_ids.get(blockIndex);

            // note: default path.getPath(id) = -1
            for (int i = 0; i < blocks.get(id).size(); i++) {
                ArrayList<Main.Pair> arrangement = blocks.get(id).get(i);
                boolean blockPlaced = this.placeBlock(id, arrangement, this.findEmptyBlockAnchor());
                if (blockPlaced) {
                    path.recordPath(id, i);
                    placedBlocks.add(id);
                    // recursion
                    if (solvePuzzle(blockIndex + 1, block_ids, placedBlocks, blocks, path)) {
                        return true;
                    }
                    // backtrack
                    path.exploredCases++; // menghitung jumlah iterasi ditinjau dari algorima backtrack

                    // reset blok yang telah diletakkan menjadi "0" kembali
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < columns; c++) {
                            if (board[r][c] == id) {
                                board[r][c] = '0';
                            }
                        }
                    }
                    placedBlocks.remove(placedBlocks.size() - 1); // unplace/undo block
                }
            }
        }
        return false;
    }

    public boolean gameFinishStatus() {
        // check if there are no empty spaces left
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (board[r][c] == '0') {
                    return false;
                }
            }
        }
        return true;
    }

    public Main.Pair findEmptyBlockAnchor() {
        // mencari blockAnchor (urut dari kiri ke kanan lalu dari atas ke bawah, untuk mencari posisi yang masih kosong)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (board[r][c] == '0') {
                    return new Main.Pair(r, c);
                }
            }
        }
        return new Main.Pair(-1, -1);
    }

    public void printBoard() {
        for (char[] row : board) {
            StringBuilder coloredRow = new StringBuilder();
            for (char column : row) {
                coloredRow.append(PrettyOutput.getColoredChar(column)).append(" "); // Space for better readability
            }
            System.out.println(coloredRow);
        }
    }

    public String boardToText() {
        List<String> boardText = new ArrayList<>();
        for (char[] row : board) {
            StringBuilder coloredRow = new StringBuilder();
            for (char column : row) {
                char plainChar = column;
                coloredRow.append(plainChar);
            }
            boardText.add(coloredRow.toString());
        }
        return String.join("\n", boardText); // Join rows with newline characters
    }

    private boolean isValidPosition(int row, int column) {
        try {
            return board[row][column] == '0';
        } catch (Exception e) {
            return false;
        }
    }
}
