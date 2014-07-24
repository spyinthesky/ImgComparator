package com.sergey;

/**
 * Created by Серегй on 22.07.2014.
 */
public class Pixel implements Comparable<Pixel> {
    private int x;
    private int y;
    private int rgb;
    private int red;
    private int blue;
    private int green;
    private int comparelimit = 50;


    public Pixel(int X, int Y, int RGB) {
        x = X;
        y = Y;
        rgb = RGB;
        red = (RGB >> 16) & 0xFF;
        green = (RGB >> 8) & 0xFF;
        blue = RGB & 0xFF;
    }

    public Pixel(Pixel p) {
        this.x = new Integer(p.getX());
        this.y = new Integer(p.getY());
        this.rgb = new Integer(p.getRgb());
        this.red = new Integer(p.getRed());
        this.green = new Integer(p.getGreen());
        this.blue = new Integer(p.getBlue());
        this.comparelimit = p.getComparelimit();
    }

    public void setCompareLimit(int lim) {
        if (lim <= 100 && lim >= 0) {
            comparelimit = (255 * 3) * (lim / 100);
        } else
            throw new RuntimeException("Wrong limit. Limit must be from 0 to 100");
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRgb() {
        return rgb;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getGreen() {
        return green;
    }

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public int getComparelimit() {
        return comparelimit;
    }

    @Override
    public int compareTo(Pixel another) {
        if (Math.abs((this.red + this.green + this.blue) - (another.getRed() + another.getGreen() + another.getBlue())) >= comparelimit)
            return -1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pixel other = (Pixel) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

}
