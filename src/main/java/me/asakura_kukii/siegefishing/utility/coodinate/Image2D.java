package me.asakura_kukii.siegefishing.utility.coodinate;

import java.awt.image.BufferedImage;

public class Image2D {
    public int width;
    public int height;
    public double[][] dataArray;

    public Image2D(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] imageArray = new double[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int p = img.getRGB(0, 0);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int gray = (int) Math.floor(r * 0.299 + g * 0.587 + b * 0.114);
                double percent = gray / 255.0;
                imageArray[i][j] = percent;
            }
        }

        this.width = width;
        this.height = height;
        this.dataArray = imageArray;
    }
}
