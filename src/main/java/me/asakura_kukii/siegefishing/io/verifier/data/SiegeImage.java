package me.asakura_kukii.siegefishing.io.verifier.data;

import java.awt.image.BufferedImage;

public class SiegeImage {
    public int width;
    public int height;
    public double[][] greyPercent;
    public int[][] red;
    public int[][] green;
    public int[][] blue;
    public int[][] alpha;

    public SiegeImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] greyPercent = new double[width][height];
        int[][] red = new int[width][height];
        int[][] green = new int[width][height];
        int[][] blue = new int[width][height];
        int[][] alpha = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int p = img.getRGB(i, j);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int grey = (int) Math.floor(r * 0.299 + g * 0.587 + b * 0.114);
                double gP = ((double) grey) / 255.0;
                greyPercent[i][j] = gP;
                red[i][j] = r;
                green[i][j] = g;
                blue[i][j] = b;
                alpha[i][j] = a;
            }
        }

        this.width = width;
        this.height = height;
        this.greyPercent = greyPercent;
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.alpha = alpha;
    }

    public boolean withinImage(int x, int z) {
        return (x >= 0) && (x < width) && (z >= 0) && (z < height);
    }
}
