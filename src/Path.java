import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Path {
    private HashMap<Character, Integer> path;
    public ArrayList<Character> remainingBlocks;
    public ArrayList<Character> placedBlocks;

    // Instantiation
    public Path(ArrayList<Character> ids) {
        this.path = new HashMap<>();
        this.remainingBlocks = new ArrayList<>();
        this.placedBlocks = new ArrayList<>();
        for (char id : ids) {
            path.put(id, -1);
            remainingBlocks.add(id);
        }
    }

    public ArrayList<ArrayList<Character>> generateRemainingBlocksPermutations() {
        ArrayList<ArrayList<Character>> remainingBlocksPermutations = generatePermutations(this.remainingBlocks);
        return remainingBlocksPermutations;
    }

    public void recordPath(char id, int variation) {
        if (path.containsKey(id)) {
            path.put(id, variation);
        } else {
            System.out.println("Error: ID" + id + "not found in path.");
        }
    }

    public Integer getPath(char id) {
        return path.getOrDefault(id, -1);
    }

    public Integer resetPath(char id) {
        return path.put(id, -1);
    }

    public void printPaths() {
        for (char id : path.keySet()) {
            System.out.println("Path for " + id + ": " + path.get(id));
        }
    }

    // HELPER FUNCTIONS
    public static ArrayList<ArrayList<Character>> generatePermutations(ArrayList<Character> chars) {
        ArrayList<ArrayList<Character>> results = new ArrayList<>();
        permuteHelper(chars, 0, results);
        return results;
    }

    private static void permuteHelper(ArrayList<Character> chars, int start, ArrayList<ArrayList<Character>> results) {
        if (start == chars.size() - 1) {
            results.add(new ArrayList<>(chars));
        } else {
            for (int i = start; i < chars.size(); i++) {
                Collections.swap(chars, i, start);
                permuteHelper(chars, start+1, results);
                Collections.swap(chars, start, i);
            }
        }
    }
}
