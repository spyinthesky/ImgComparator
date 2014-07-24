package com.sergey;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by alex on 19.07.2014.
 */
public class ImageForm extends JFrame {
    private JPanel rootPanel;
    private JLabel imgLable;
    private JButton saveButton;
    private JFileChooser fc;
    private BufferedImage image;

    public void updateImage(BufferedImage img){
        image=img;
        imgLable.setIcon(new ImageIcon(image));
        imgLable.updateUI();
    }
    public ImageForm(BufferedImage img){
        super("Image");
        image = img;
        imgLable.setIcon(new ImageIcon(img));
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setCurrentDirectory(new File("D://ImgData"));
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
        pack();
        setVisible(true);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int retrieve =fc.showSaveDialog(ImageForm.this);
                if(retrieve == JFileChooser.APPROVE_OPTION){
                    try {
                        ImageIO.write(image,"png", new File(ImgUtills.getExt(fc.getSelectedFile()).equals("") ? fc.getSelectedFile().getAbsolutePath() + ".png" : fc.getSelectedFile().getAbsolutePath()));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

}
