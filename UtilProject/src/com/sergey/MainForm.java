package com.sergey;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by alex on 17.07.2014.
 */
public class MainForm extends JFrame{
    private JPanel rootPanel;
    private JFormattedTextField secondImageInput;
    private JFormattedTextField firstImageInput;
    private JButton firstImageView;
    private JButton secondImageView;
    private JSlider slider1;
    private JButton firstImageOk;
    private JButton secondImageOk;
    private JLabel lbl1;
    private JLabel lbl2;
    private JLabel titleLable;
    private JLabel lableAcc;
    private JScrollBar scrollBar1;
    private JFileChooser fc;
    private File firstFile;
    private File secondFile;
    private static Logger log = Logger.getLogger(MainForm.class.getName());
    private int currentAccuracy;
    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage img3;
    private BufferedImage scaledImg1;
    private BufferedImage scaledImg3;
    private ImageForm form1;
    private ImageForm form2;


    public MainForm(){
        super("Image comarator");
        rootPanel.setSize(50,50);
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        titleLable.setText("Input the first image file");
        currentAccuracy=slider1.getValue();
        lableAcc.setText("Accuracy: " + currentAccuracy);
        lableAcc.setForeground(Color.RED);
        pack();
        setVisible(true);
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {

                return ImgUtills.getExt(f).equals("png");
            }

            @Override
            public String getDescription() {
                return ".png";
            }
        });

        lbl1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                log.info("Mouse click on lbl1");
                if(form1==null){
                    form1=new ImageForm(img1);
                }
                else{
                    form1.updateImage(img1);
                    form1.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        lbl2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                log.info("Mouse click on lbl2");
                if(form2==null){
                    form2=new ImageForm(img3);
                }
                else{
                    form2.updateImage(img3);
                    form2.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                currentAccuracy=slider1.getValue();
                lableAcc.setText("Accuracy: " + currentAccuracy);
                if(img2!=null && img1!=null){
                    img3 = ImgUtills.compareImg(img1, img2, currentAccuracy);
                    scaledImg3 = ImgUtills.scaleImg(img3, 300,200);
                    lbl2.setIcon(new ImageIcon(scaledImg3));
                    lbl2.updateUI();
                }
            }
        });
        
        firstImageView.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("Chooser1 opened");
                fc.setCurrentDirectory(new File("D://ImgData"));

                int returnVal = fc.showOpenDialog(MainForm.this);

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    firstImageInput.setText(fc.getSelectedFile().getAbsolutePath());
                }
                log.info("Chooser1 closed");
            }
        });
        firstImageOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstFile = new File(firstImageInput.getText());
                if(firstFile.exists()){
                    if(ImgUtills.getExt(firstFile).equals("png")){
                        try {
                            img1 = ImageIO.read(firstFile);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        scaledImg1 = ImgUtills.scaleImg(img1, 300,200);
                        lbl1.setIcon(new ImageIcon(scaledImg1));
                        lbl1.setPreferredSize(new Dimension(scaledImg1.getWidth()+2, scaledImg1.getHeight()+2));
                        lbl1.updateUI();

                    }
                    else{
                        JOptionPane.showMessageDialog(MainForm.this, "The file has a wrong extension. Need to be .png");
                    }

                }
                else{
                    JOptionPane.showMessageDialog(MainForm.this, "The file is not exists");
                }
            }
        });

        secondImageView.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setCurrentDirectory(new File("D://ImgData"));
                log.info("Chooser2 opened");
                int returnVal = fc.showOpenDialog(MainForm.this);

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    secondImageInput.setText(fc.getSelectedFile().getAbsolutePath());
                }
                log.info("Chooser2 closed");
            }
        });

        secondImageOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondFile = new File(secondImageInput.getText());
                if(firstFile.exists()){
                    if(ImgUtills.getExt(secondFile).equals("jpg")||ImgUtills.getExt(secondFile).equals("png")||ImgUtills.getExt(secondFile).equals("gif")) {
                        try {
                            img2 = ImageIO.read(secondFile);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if (img1.getHeight() == img2.getHeight() && img1.getWidth() == img2.getWidth()) {
                            img3 = ImgUtills.compareImg(img1, img2, currentAccuracy);
                            scaledImg3 = ImgUtills.scaleImg(img3, 300,200);
                            lbl2.setIcon(new ImageIcon(scaledImg3));
                            lbl2.setPreferredSize(new Dimension(scaledImg3.getWidth(), scaledImg3.getHeight()));
                        } else {
                            JOptionPane.showMessageDialog(MainForm.this, "The file has a wrong resolution");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(MainForm.this, "The file has a wrong extension. Need to be .gif, .jpg or .png");
                    }

                }
                else{
                    JOptionPane.showMessageDialog(MainForm.this, "The file is not exists");
                }
            }
        });
    }
}
