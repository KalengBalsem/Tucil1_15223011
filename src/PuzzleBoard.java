import java.util.*;

import javax.management.RuntimeErrorException;

public class PuzzleBoard {
    private int rows;
    private int columns;
    private String category;
    private char[][] board;
    private String[] customConfiguration;

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
        // memperbaiki block yang tidak start dari (0,0) (agar selalu mulai dari blockAnchor)
        // (probably need to fix a.k.a simplify this part)
        int first_row = arrangement.get(0).row();
        int first_column = arrangement.get(0).column();
        if (first_row != 0 || first_column != 0) {
            for (int i = 0; i < arrangement.size(); i++) {
                arrangement.set(i, new Main.Pair(arrangement.get(i).row() - first_row, arrangement.get(i).column() - first_column));
            }
        }

        // meletakkan block dalam bidang jika memenuhi syarat
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

    // solvePuzzle(0, remainingBlocks, placedBlocks, blocks)
    public boolean solvePuzzle(Integer remainingBlockIndex, ArrayList<Character> remainingBlocks, ArrayList<Character> placedBlocks, HashMap<Character, ArrayList<ArrayList<Main.Pair>>> blocks, Path path) {
        if (this.gameFinishStatus() && remainingBlocks.size() == 0) {
            return true;
        } else {
            char id = remainingBlocks.get(remainingBlockIndex);

            // note: default path.getPath(id) = -1
            for (int i = (path.getPath(id) + 1); i < blocks.get(id).size(); i++) {
                ArrayList<Main.Pair> arrangement = blocks.get(id).get(i);
                boolean blockPlaced = this.placeBlock(id, arrangement, this.findEmptyBlockAnchor());
                if (blockPlaced) {
                    remainingBlocks.remove(Character.valueOf(id));
                    placedBlocks.add(id);
                    path.recordPath(id, i);
                    // recursion
                    if (solvePuzzle(0, remainingBlocks, placedBlocks, blocks, path)) {
                        return true;
                    }
                    // backtrack
                    path.resetPath(id);
                    char lastPlacedBlock = placedBlocks.get(placedBlocks.size() - 1);
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < columns; c++) {
                            if (board[r][c] == lastPlacedBlock) {
                                board[r][c] = '0';
                            }
                        }
                    }
                    placedBlocks.remove(placedBlocks.size() - 1);
                    remainingBlocks.add(0, lastPlacedBlock);
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
        // check if there are no empty spaces left
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
            System.out.println(new String(row));
        }
    }

    private boolean isValidPosition(int row, int column) {
        try {
            return board[row][column] == '0';
        } catch (Exception e) {
            return false;
        }
    }
}
