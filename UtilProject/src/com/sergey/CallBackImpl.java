package com.sergey;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Серегй on 23.07.2014.
 */
public class CallBackImpl implements Callback{
    JLabel lable;
    public CallBackImpl(JLabel lable){
        this.lable = lable;

    }

    @Override
    public void updateUI(BufferedImage img) {
        Dimension d = new Dimension(img.getWidth(), img.getHeight());
        lable.setSize(d);
        lable.setPreferredSize(d);
        lable.invalidate();
        lable.getParent().validate();
        lable.setIcon(new ImageIcon(img));
        lable.updateUI();
    }
}
