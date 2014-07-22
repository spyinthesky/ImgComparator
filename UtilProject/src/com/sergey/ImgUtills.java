package com.sergey;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

class Pixel implements Comparable<Pixel>{
    private int x;
    private int y;
    private int rgb;
    private int red;
    private int blue;
    private int green;
    private int comparelimit = 130;


    public Pixel(int X, int Y, int RGB){
        x=X;
        y=Y;
        rgb=RGB;
        red = (RGB >>16) & 0xFF;
        green = (RGB>>8) & 0xFF;
        blue = RGB &0xFF;
    }

    public Pixel(Pixel p){
        this.x = p.getX();
        this.y=p.getY();
        this.rgb=p.getRgb();
        this.red=p.getRed();
        this.green=p.getGreen();
        this.blue=p.getBlue();
        this.comparelimit=p.getComparelimit();
    }

    public void setCompareLimit(int lim){
        if(lim<=100 && lim>=0){
            comparelimit = (255*3)*(lim/100);
        }
        else
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
    public int compareTo(Pixel another){
        if(Math.abs((this.red+this.green+this.blue)-(another.getRed()+another.getGreen() + another.getBlue()))>=comparelimit)
            return -1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object o){
        if((Pixel)o.)
    }

}

public class ImgUtills {

    public static String getExt(File f){
        String name = f.getName();
        return name.contains(".") ? name.substring(name.lastIndexOf('.') +1, name.length()) : "";
    }
    public static ArrayList<int[]> getCorners(ArrayList<ArrayList<int[]>> inData) {
        ArrayList<int[]> result = new ArrayList<>();
        Iterator<ArrayList<int[]>> iter = inData.iterator();
        while (iter.hasNext()) {
            ArrayList<int[]> current = iter.next();
            int[] leftup = new int[]{current.get(0)[0],current.get(0)[1]};
            int[] rightdown = new int[]{current.get(0)[0],current.get(0)[1]};
            for (int i = 0; i < current.size(); i++) {
                if((current.get(i)[0]<leftup[0])){
                    leftup[0] = current.get(i)[0];
                }
                if((current.get(i)[1]<leftup[1])){
                    leftup[1] = current.get(i)[1];
                }
                if((current.get(i)[0]>rightdown[0])){
                    rightdown[0] =current.get(i)[0];
                }
                if((current.get(i)[1]>rightdown[1])){
                    rightdown[0] = current.get(i)[1];
                }
            }
            result.add(new int[]{leftup[0], leftup[1], rightdown[0], rightdown[1]});
        }
        return result;
    }

    public static int compareRGB(int RGB1, int RGB2, int compareLimit){
        Pixel px1= new Pixel(0,0,RGB1);
        Pixel px2 = new Pixel(0,0,RGB2);
        px1.setCompareLimit(compareLimit);
        return px1.compareTo(px2);
    }

    public static int compareRGB(int RGB1, int RGB2){
        Pixel px1= new Pixel(0,0,RGB1);
        Pixel px2 = new Pixel(0,0,RGB2);
        return px1.compareTo(px2);
    }

    public static void checkBelongs(ArrayList<int[]> worklist, ArrayList<int[]> result, int[] current, int sparseness){
        for(int i=1; i<=sparseness; i++){
            for (int j = 1; j <=sparseness ; j++) {
                int[] arr1=new int[]{current[0]+i,current[1]};
                int[] arr2=new int[]{current[0]+i,current[1]+j};
                int[] arr3=new int[]{current[0],current[1]+j};
                int[] arr4=new int[]{current[0]-i,current[1]+j};
                int[] arr5=new int[]{current[0]-i,current[1]};
                int[] arr6=new int[]{current[0]-i,current[1]-j};
                int[] arr7=new int[]{current[0],current[1]-j};
                int[] arr8=new int[]{current[0]+i,current[1]-j};
                if(worklist.contains(arr1)){
                    result.add(arr1);
                    worklist.remove(arr1);
                    checkBelongs(worklist, result, arr1, sparseness);
                }
                if(worklist.contains(arr2)){
                    result.add(arr2);
                    worklist.remove(arr2);
                    checkBelongs(worklist, result, arr2, sparseness);
                }
                if(worklist.contains(arr3)){
                    result.add(arr3);
                    worklist.remove(arr3);
                    checkBelongs(worklist, result, arr3, sparseness);
                }
                if(worklist.contains(arr4)){
                    result.add(arr4);
                    worklist.remove(arr4);
                    checkBelongs(worklist, result, arr4, sparseness);
                }
                if(worklist.contains(arr5)){
                    result.add(arr5);
                    worklist.remove(arr5);
                    checkBelongs(worklist, result, arr5, sparseness);
                }
                if(worklist.contains(arr6)){
                    result.add(arr6);
                    worklist.remove(arr6);
                    checkBelongs(worklist, result, arr6, sparseness);
                }
                if(worklist.contains(arr7)){
                    result.add(arr7);
                    worklist.remove(arr7);
                    checkBelongs(worklist, result, arr7, sparseness);
                }
                if(worklist.contains(arr8)){
                    result.add(arr8);
                    worklist.remove(arr8);
                    checkBelongs(worklist, result, arr8, sparseness);
                }
            }
        }
    }
    public static BufferedImage scaleImg(BufferedImage img, int newW, int newH){
        float scaleFactor;
        int scaledH=0, scaledW=0;
        if(img.getHeight()>newH){
            scaleFactor = (float)img.getHeight()/newH;
            scaledH=(int)((float)img.getHeight()/scaleFactor);
            scaledW=(int)((float)img.getWidth()/scaleFactor);
            if(scaledW>newW){
                scaleFactor = (float)scaledW/newW;
                scaledH=(int)((float)scaledH/scaleFactor);
                scaledW=(int)((float)scaledW/scaleFactor);
            }
        }
        BufferedImage result = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(img, 0, 0, scaledW, scaledH, null);
        g.dispose();
        return result;
    }

    public static BufferedImage compareImg(BufferedImage img1, BufferedImage img2, int sparseness){
        ArrayList<int[]> corners =getCorners(sort(getDiffs(img1, img2), sparseness));
        ColorModel img2cm = img2.getColorModel();
        boolean isAlphaPremultiplied = img2cm.isAlphaPremultiplied();
        WritableRaster img2raster = img2.copyData(null);
        BufferedImage img3 = new BufferedImage(img2cm, img2raster, isAlphaPremultiplied, null);
        for(int[] a : corners) {
            drawSquare(a, img3, 2);
        }
        return img3;
    }

    public static void drawSquare(int[] coords, BufferedImage img, int correction){
        if(coords.length<2)
            throw new RuntimeException("Wrong coords. There must be 2 packs of coords");
        else{
            Graphics2D gr = img.createGraphics();
            gr.setColor(Color.RED);
            gr.drawLine(coords[0]-correction,coords[1]-correction, coords[2]+correction, coords[2]-correction);
            gr.drawLine(coords[2]+correction,coords[1]-correction, coords[2]+correction, coords[3]+correction);
            gr.drawLine(coords[2]+correction,coords[3]+correction, coords[0]-correction, coords[3]+correction);
            gr.drawLine(coords[0]-correction,coords[3]+correction, coords[0]-correction, coords[1]-correction);
            gr.dispose();
        }
    }


    public static ArrayList<ArrayList<int[]>> sort(ArrayList<int[]> pixels, int sparseness){
        ArrayList<ArrayList<int[]>> result = new ArrayList<>();
        ArrayList<int[]> singleResult;
        while(pixels.size()>0){
            singleResult = new ArrayList<int[]>();
            int[] current= pixels.get(0);
            singleResult.add(current);
            pixels.remove(current);
            checkBelongs(pixels, singleResult, current, sparseness);
            result.add(singleResult);
        }

        return result;
    }


    public static ArrayList<int[]> getDiffs(BufferedImage first, BufferedImage second){
        ArrayList<int[]> result = new ArrayList<>();
        for(int i=0; i<first.getWidth(); i++){
            for(int j=0; j<first.getHeight(); j++){
                if(compareRGB(first.getRGB(i,j), second.getRGB(i,j))<0) {
                    int[] arr = {i,j};
                    result.add(arr);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        new Thread(null, new Runnable() {
            public void run() {

                try {
                    BufferedImage one = ImageIO.read(new File("D://ImgData/dino.png"));
                    BufferedImage two = ImageIO.read(new File("D://ImgData/dino2.png"));
                    BufferedImage three = compareImg(one, two, 5);
                    BufferedImage four = scaleImg(three, 300,200);
                    ImageIO.write(three, "png", new File("D://ImgData/RESULT.png"));
                    ImageIO.write(four, "png", new File("D://ImgData/SCALED.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "1", 1 << 23).start();




/*
	    ArrayList<int[]> data = new ArrayList<>();
        BufferedImage image = new BufferedImage(30,30, BufferedImage.TYPE_INT_ARGB);
        for(int i=0; i<30; i++){
            if(i%10==0)
               continue;
            for(int j=0; j<30; j++){
                if(i%10==0||j%10==0){
                    image.setRGB(i,j, Color.WHITE.getRGB() );
                    continue;
                }
                else
                    image.setRGB(i, j, Color.BLACK.getRGB());
                data.add(new int[]{i, j});

            }
        }
        ArrayList<int[]> data2 = new ArrayList<>();
        int[] square = new int[]{};
        System.out.println(Arrays.deepToString(data.toArray()));
        ImageIO.write(image, "png", new File("D://1.png"));
        ArrayList<ArrayList<int[]>> values = sort(data);
        ArrayList<int[]> corners = getCorners(values);
        System.out.println(Arrays.deepToString(corners.toArray()));
        System.out.println(values.size());
        System.out.println(Arrays.deepToString(values.toArray()));
        */
    }
}
