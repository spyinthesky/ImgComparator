package com.sergey;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Серегй on 23.07.2014.
 */
public class MainWorker extends SwingWorker<Holder, Integer > {
    public static final int DO_SCALING =1;
    public static final int DO_FULL_COMPARSION=2;
    public static final int DO_CHANGE_ACC=3;
    private Callback ui;
    private BufferedImage first;
    private BufferedImage second;
    private int acc;
    private int whatToDo;
    private ArrayList<Pixel> diffs;
    BufferedImage little;
    private static Logger log = Logger.getLogger(MainWorker.class.getName());
    private static int number=0;
    public void setWhatToDo(int whatToDo) {
        this.whatToDo = whatToDo;
    }

    public MainWorker(Callback changer, BufferedImage first, BufferedImage second, int currentAccuracy, ArrayList<Pixel> diffs){
        ui=changer;
        this.first = first;
        this.second = second;
        acc = currentAccuracy;
        this.diffs = diffs;
        number++;
    }
    public MainWorker(){
        number++;
    }


    public void setAcc(int acc) {
        this.acc = acc;
    }

    public void setFirst(BufferedImage first) {
        this.first = first;
    }

    public void setSecond(BufferedImage second) {
        this.second = second;
    }

    public void setUi(Callback ui) {
        this.ui = ui;
    }

    public void setDiffs(ArrayList<Pixel> diffs) {
        this.diffs = diffs;
    }

    @Override
    protected Holder doInBackground() throws Exception {
        log.info("Worker" + this.toString()+ " start work " + number);
        if(!isCancelled()){
            if (whatToDo==DO_SCALING){
                little = ImgUtills.scaleImg(first, 300,200);
                return new Holder(first, null);
            }
            else if(whatToDo==DO_FULL_COMPARSION){
                ArrayList<Pixel> difData =ImgUtills.getDiffs(first, second);
                BufferedImage big = ImgUtills.compareImg(first, second, acc);
                little = ImgUtills.scaleImg(big, 300,200);
                return new Holder(big, difData);
            }
            else if(whatToDo==DO_CHANGE_ACC){
                BufferedImage big = ImgUtills.changeAcc(second,acc,diffs);
                little = ImgUtills.scaleImg(big, 300,200);
                log.info("Worker" + this.toString()+ " has produced new images" + big.toString() + " " + little.toString());
                return new Holder(big, diffs);
            }
            else{
                throw new RuntimeException("Need to set what to do");
            }

        }
        else {
            throw new RuntimeException("Background method was cancelled");
        }
    }

    @Override
    protected void done() {
        log.info("Worker" + this.toString()+ " done executed");
        ui.updateUI(little);

    }



}
