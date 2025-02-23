import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PrettyOutput {
    private static final String RESET = "\u001B[0m"; // Reset ANSI color
    private static final int CELL_SIZE = 50; // Each puzzle block size
    private static final int PADDING = 5;    // Space between cells
    private static final int FONT_SIZE = 30; // Font size for letters

    // 26 distinct RGB colors (untuk A-Z)
    private static final int[][] DISTINCT_COLORS = {
        {255, 0, 0},      // Red
        {0, 255, 0},      // Green
        {0, 0, 255},      // Blue
        {255, 255, 0},    // Yellow
        {255, 0, 255},    // Magenta
        {0, 255, 255},    // Cyan
        {255, 165, 0},    // Orange
        {75, 0, 130},     // Indigo
        {255, 20, 147},   // Deep Pink
        {0, 128, 128},    // Teal
        {128, 0, 0},      // Maroon
        {0, 128, 0},      // Dark Green
        {0, 0, 128},      // Navy
        {128, 128, 0},    // Olive
        {128, 0, 128},    // Purple
        {0, 255, 127},    // Spring Green
        {139, 69, 19},    // Saddle Brown
        {46, 139, 87},    // Sea Green
        {70, 130, 180},   // Steel Blue
        {218, 112, 214},  // Orchid
        {255, 99, 71},    // Tomato
        {154, 205, 50},   // Yellow Green
        {147, 112, 219},  // Medium Purple
        {255, 140, 0},    // Dark Orange
        {0, 206, 209},    // Dark Turquoise
        {186, 85, 211}    // Medium Orchid
    };

    public static String getColoredChar(char c) {
        if (!Character.isLetter(c)) return String.valueOf(c); // Return as is if not a letter

        int index = Character.toUpperCase(c) - 'A'; // Map A-Z
        if (index < 0 || index > 25) return String.valueOf(c);

        int[] rgb = DISTINCT_COLORS[index];
        String ansiColor = String.format("\u001B[38;2;%d;%d;%dm", rgb[0], rgb[1], rgb[2]);

        return ansiColor + c + RESET; // Return colored character
    }

    public static void generatePuzzleImage(char[][] board, String filename) {
        int rows = board.length;
        int cols = board[0].length;

        int width = cols * (CELL_SIZE + PADDING) - PADDING;
        int height = rows * (CELL_SIZE + PADDING) - PADDING;

        // Create an empty image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable anti-aliasing for better graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set background color
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Draw each puzzle block
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char piece = board[row][col];
                if (Character.isLetter(piece)) {
                    // Get color
                    Color blockColor = getColorForChar(piece);
                    g2d.setColor(blockColor);
                    g2d.fillRect(col * (CELL_SIZE + PADDING), row * (CELL_SIZE + PADDING), CELL_SIZE, CELL_SIZE);

                    // Draw letter in the center of the cell
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Roboto", Font.PLAIN, FONT_SIZE));
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = col * (CELL_SIZE + PADDING) + (CELL_SIZE - fm.charWidth(piece)) / 2;
                    int y = row * (CELL_SIZE + PADDING) + ((CELL_SIZE - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(String.valueOf(piece), x, y);
                }
            }
        }

        // Dispose and save image
        g2d.dispose();
        try {
            ImageIO.write(image, "PNG", new File(filename));
            System.out.println("Puzzle image saved as " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Map char to RGB color based on PrettyOutput
    private static Color getColorForChar(char c) {
        int[][] DISTINCT_COLORS = {
            {255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0}, {255, 0, 255}, {0, 255, 255},
            {255, 165, 0}, {75, 0, 130}, {255, 20, 147}, {0, 128, 128}, {128, 0, 0}, {0, 128, 0},
            {0, 0, 128}, {128, 128, 0}, {128, 0, 128}, {0, 255, 127}, {139, 69, 19}, {46, 139, 87},
            {70, 130, 180}, {218, 112, 214}, {255, 99, 71}, {154, 205, 50}, {147, 112, 219}, {255, 140, 0},
            {0, 206, 209}, {186, 85, 211}
        };

        int index = Character.toUpperCase(c) - 'A';
        if (index < 0 || index >= DISTINCT_COLORS.length) {
            return Color.GRAY; // Default color for invalid characters
        }

        int[] rgb = DISTINCT_COLORS[index];
        return new Color(rgb[0], rgb[1], rgb[2]);
    }

}
