import java.util.ArrayList;
import java.util.HashMap;

public class Path {
    private HashMap<Character, Integer> path;
    public ArrayList<Character> placedBlocks;
    public long exploredCases = 0;

    // Instantiation
    public Path(ArrayList<Character> ids) {
        this.path = new HashMap<>();
        this.placedBlocks = new ArrayList<>();
        for (char id : ids) {
            path.put(id, -1);
        }
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

    public void printPaths() {
        for (char id : path.keySet()) {
            System.out.println("Path for " + id + ": " + path.get(id));
        }
    }
}
