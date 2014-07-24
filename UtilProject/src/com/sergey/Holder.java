package com.sergey;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Серегй on 23.07.2014.
 */
public class Holder{
    private BufferedImage img;
    private ArrayList<Pixel> difData;

    public BufferedImage getImg() {
        return img;
    }

    public ArrayList<Pixel> getDifData() {
        return difData;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public void setDifData(ArrayList<Pixel> difData) {
        this.difData = difData;
    }

    public Holder(BufferedImage img, ArrayList<Pixel> difData) {
        this.img = img;
        this.difData = difData;
    }
}
