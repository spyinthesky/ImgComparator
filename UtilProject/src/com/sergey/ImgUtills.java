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


public class ImgUtills  {


    //Методы сравнивают пиксели по цвету в стандартном режиме и с возможностью изменения пределов сравнения.
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

    //Метод находит все отличные пиксели на двух изображениях.
    public static ArrayList<Pixel> getDiffs(BufferedImage first, BufferedImage second){
        ArrayList<Pixel> result = new ArrayList<>();
        for(int i=0; i<first.getWidth(); i++){
            for(int j=0; j<first.getHeight(); j++){
                if(compareRGB(first.getRGB(i,j), second.getRGB(i,j))<0) {
                    Pixel p = new Pixel(i,j,second.getRGB(i,j));
                    result.add(p);
                }
            }
        }
        return result;
    }

    // Метод сортирует массив найденных отличий по группам
    public static ArrayList<ArrayList<Pixel>> sort(ArrayList<Pixel> pixels, int sparseness){
        ArrayList<ArrayList<Pixel>> result = new ArrayList<>();
        ArrayList<Pixel> singleResult;
        while(pixels.size()>0){
            singleResult = new ArrayList<Pixel>();
            Pixel current= pixels.get(0);
            singleResult.add(current);
            pixels.remove(current);
            checkBelongs(pixels, singleResult, current, sparseness);
            result.add(singleResult);
        }
        return result;
    }

    // Дополнительный метод, который рекурсивно ищет соседние точки для пикселя во входных данных
    public static void checkBelongs(ArrayList<Pixel> worklist, ArrayList<Pixel> result, Pixel current, int sparseness){
        for(int i=1; i<=sparseness; i++){
            for (int j = 1; j <=sparseness ; j++) {
                Pixel px1 = new Pixel(current.getX()+i, current.getY(),0);
                Pixel px2 = new Pixel(current.getX()+i, current.getY()+j,0);
                Pixel px3 = new Pixel(current.getX(), current.getY()+j,0);
                Pixel px4 = new Pixel(current.getX()-i, current.getY()+j,0);
                Pixel px5 = new Pixel(current.getX()-i, current.getY(),0);
                Pixel px6 = new Pixel(current.getX()-i, current.getY()-j,0);
                Pixel px7 = new Pixel(current.getX(), current.getY()-j,0);
                Pixel px8 = new Pixel(current.getX()+i, current.getY()-j,0);
                if(worklist.contains(px1)){
                    result.add(px1);
                    worklist.remove(px1);
                    checkBelongs(worklist, result, px1, sparseness);
                }
                if(worklist.contains(px2)){
                    result.add(px2);
                    worklist.remove(px2);
                    checkBelongs(worklist, result, px2, sparseness);
                }
                if(worklist.contains(px3)){
                    result.add(px3);
                    worklist.remove(px3);
                    checkBelongs(worklist, result, px3, sparseness);
                }
                if(worklist.contains(px4)){
                    result.add(px4);
                    worklist.remove(px4);
                    checkBelongs(worklist, result, px4, sparseness);
                }
                if(worklist.contains(px5)){
                    result.add(px5);
                    worklist.remove(px5);
                    checkBelongs(worklist, result, px5, sparseness);
                }
                if(worklist.contains(px6)){
                    result.add(px6);
                    worklist.remove(px6);
                    checkBelongs(worklist, result, px6, sparseness);
                }
                if(worklist.contains(px7)){
                    result.add(px7);
                    worklist.remove(px7);
                    checkBelongs(worklist, result, px7, sparseness);
                }
                if(worklist.contains(px8)){
                    result.add(px8);
                    worklist.remove(px8);
                    checkBelongs(worklist, result, px8, sparseness);
                }
            }
        }
    }

    //Метод возвращает крайние точки прямоугольника, который содержит внутри себя все точки представленные в массиве входных данных
    public static ArrayList<int[]> getCorners(ArrayList<ArrayList<Pixel>> inData) {
        ArrayList<int[]> result = new ArrayList<>();
        Iterator<ArrayList<Pixel>> iter = inData.iterator();
        while (iter.hasNext()) {
            ArrayList<Pixel> current = iter.next();
            Pixel leftup = new Pixel(current.get(0));
            Pixel rightdown = new Pixel(current.get(0));
            for (int i = 0; i < current.size(); i++) {
                if((current.get(i).getX()<leftup.getX())){
                    leftup.setX(current.get(i).getX());
                }
                if((current.get(i).getY()<leftup.getY())){
                    leftup.setY(current.get(i).getY());
                }
                if((current.get(i).getX()>rightdown.getX())){
                    rightdown.setX(current.get(i).getX());
                }
                if((current.get(i).getY()>rightdown.getY())){
                    rightdown.setY(current.get(i).getY());
                }
            }
            result.add(new int[]{leftup.getX(), leftup.getY(), rightdown.getX(), rightdown.getY()});
        }
        return result;
    }

    // Метод рисует на заданном изображении квадрат в соответствии с заданными координатами верхней левой и нижней правой точки.
    public static void drawSquare(int[] coords, BufferedImage img, int correction){
        if(coords.length<2)
            throw new RuntimeException("Wrong coords. There must be 2 packs of coords");
        else{
            Graphics2D gr = img.createGraphics();
            gr.setColor(Color.RED);
            gr.drawLine(coords[0]-correction,coords[1]-correction, coords[2]+correction, coords[1]-correction);
            gr.drawLine(coords[2]+correction,coords[1]-correction, coords[2]+correction, coords[3]+correction);
            gr.drawLine(coords[2]+correction,coords[3]+correction, coords[0]-correction, coords[3]+correction);
            gr.drawLine(coords[0]-correction,coords[3]+correction, coords[0]-correction, coords[1]-correction);
            gr.dispose();
        }
    }

    //Метод получает расширение файла или возвращает пустую строку в случае его отсутствия
    public static String getExt(File f){
        String name = f.getName();
        return name.contains(".") ? name.substring(name.lastIndexOf('.') +1, name.length()) : "";
    }

    //Общий метод для сравнения двух изображений. Параметр sparseness определяет на сколько пикселей могут отстоять пиксели с отличием при группировке. Возвращает новое изображение, с обведенными красным цветом найденными отличиями.
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

    //Общий метод для изменения точности
    public static BufferedImage changeAcc(BufferedImage img, int sparseness,  ArrayList<Pixel> diffs){
        ArrayList<Pixel> newdiffs = new ArrayList<>();
        for(Pixel p : diffs){
            newdiffs.add(new Pixel(p));
        }
        ArrayList<int[]> corners =getCorners(sort(newdiffs, sparseness));
        ColorModel img2cm = img.getColorModel();
        boolean isAlphaPremultiplied = img2cm.isAlphaPremultiplied();
        WritableRaster img2raster = img.copyData(null);
        BufferedImage img3 = new BufferedImage(img2cm, img2raster, isAlphaPremultiplied, null);
        for(int[] a : corners) {
            drawSquare(a, img3, 2);
        }
        return img3;
    }

    //Метод пропорционально сжимает изображения до размеров соответствующим входной высоте и ширине
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

    public static void main(String[] args) throws IOException {
        new Thread(null, new Runnable() {
            public void run() {

                try {
                    BufferedImage one = ImageIO.read(new File("E://Java/Images/Bob.png"));
                    BufferedImage two = ImageIO.read(new File("E://Java/Images/Bob2.png"));
                    BufferedImage three = compareImg(one, two, 15);
                    BufferedImage four = scaleImg(three, 300,200);
                    BufferedImage five = new BufferedImage(one.getWidth(), one.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    ArrayList<Pixel> diffs = getDiffs(one,two);
                    for(Pixel p: diffs){
                        five.setRGB(p.getX(),p.getY(),p.getRgb());
                    }

                    ImageIO.write(three, "png", new File("E://Java/Images/RESULT.png"));
                    ImageIO.write(four, "png", new File("E://Java/Images/SCALED.png"));
                    ImageIO.write(five, "png", new File("E://Java/Images/DIFFS.png"));
                    File f = new File("load");
                    f.createNewFile();
                    System.out.println(f.exists());
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
