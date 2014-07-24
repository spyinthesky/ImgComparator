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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by alex on 17.07.2014.
 */

public class MainForm extends JFrame {
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
    private ImageForm form1;
    private ImageForm form2;
    private MainWorker worker1;
    private MainWorker worker2;
    private ArrayList<Pixel> diffs;
    private CallBackImpl lbl1changer;
    private CallBackImpl lbl2changer;
    private BufferedImage load;


    public MainForm() {

        super("Image comarator");
        try {
            load = ImageIO.read(new File("load.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lbl1changer = new CallBackImpl(lbl1);
        lbl2changer = new CallBackImpl(lbl2);
        rootPanel.setSize(50, 50);
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        titleLable.setText("Input the first image file");
        currentAccuracy = slider1.getValue();
        lableAcc.setText("Accuracy: " + currentAccuracy);
        lableAcc.setForeground(Color.RED);
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {

                return ImgUtills.getExt(f).equals("png")||f.isDirectory();
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
                if (form1 == null) {
                    form1 = new ImageForm(img1);
                } else {
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
                if (form2 == null) {
                    form2 = new ImageForm(img3);
                } else {
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
                currentAccuracy = slider1.getValue();
                lableAcc.setText("Accuracy: " + currentAccuracy);
            }
        });
        slider1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentAccuracy = slider1.getValue();
                if (worker2 == null || worker2.isDone()) {
                    worker2 = new MainWorker();
                } else {
                    worker2.cancel(true);
                    worker2 = new MainWorker();
                }
                worker2.setUi(lbl2changer);
                worker2.setSecond(img2);
                worker2.setAcc(currentAccuracy);
                worker2.setDiffs(diffs);
                worker2.setWhatToDo(MainWorker.DO_CHANGE_ACC);
                worker2.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getNewValue().toString().equals("DONE")) {
                            try {
                                img3 = worker2.get().getImg();
                                diffs = worker2.get().getDifData();
                                ImageIO.write(img3, "png", new File("E://Java/Images/RESULT.png"));
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
                lbl2changer.updateUI(load);
                worker2.execute();


            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        firstImageView.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setCurrentDirectory(new File("D://ImgData"));

                int returnVal = fc.showOpenDialog(MainForm.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    firstImageInput.setText(fc.getSelectedFile().getAbsolutePath());
                }
        }
        });
        firstImageOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstFile = new File(firstImageInput.getText());
                if (firstFile.exists()) if (ImgUtills.getExt(firstFile).equals("png")) {
                    try {
                        img1 = ImageIO.read(firstFile);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (worker1 == null || worker1.isDone()) {
                        worker1 = new MainWorker();
                    } else {
                        worker1.cancel(true);
                        worker1 = new MainWorker();
                    }
                    worker1.setUi(lbl1changer);
                    worker1.setFirst(img1);
                    worker1.setWhatToDo(MainWorker.DO_SCALING);
                    worker1.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if (evt.getNewValue().toString().equals("DONE")) {
                                try {
                                    img1 = worker1.get().getImg();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                    log.info("Before worker1 execute");
                    lbl1changer.updateUI(load);
                    worker1.execute();


                } else {
                    JOptionPane.showMessageDialog(MainForm.this, "The file has a wrong extension. Need to be .png");
                }
                else {
                    JOptionPane.showMessageDialog(MainForm.this, "The file is not exists");
                }
            }
        });

        secondImageView.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setCurrentDirectory(new File("D://ImgData"));
                int returnVal = fc.showOpenDialog(MainForm.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    secondImageInput.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });

        secondImageOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondFile = new File(secondImageInput.getText());
                if (firstFile.exists()) {
                    if (ImgUtills.getExt(secondFile).equals("jpg") || ImgUtills.getExt(secondFile).equals("png") || ImgUtills.getExt(secondFile).equals("gif")) {
                        try {
                            img2 = ImageIO.read(secondFile);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if (img1.getHeight() == img2.getHeight() && img1.getWidth() == img2.getWidth()) {
                            if (worker2 == null || worker2.isDone()) {
                                worker2 = new MainWorker();
                            } else {
                                worker2.cancel(true);
                                worker2 = new MainWorker();
                            }
                            worker2.setUi(lbl2changer);
                            worker2.setFirst(img1);
                            worker2.setSecond(img2);
                            worker2.setAcc(currentAccuracy);
                            worker2.setWhatToDo(MainWorker.DO_FULL_COMPARSION);
                            worker2.addPropertyChangeListener(new PropertyChangeListener() {
                                @Override
                                public void propertyChange(PropertyChangeEvent evt) {
                                    if (evt.getNewValue().toString().equals("DONE")) {
                                        try {
                                            img3 = worker2.get().getImg();
                                            diffs = worker2.get().getDifData();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            });
                            lbl2changer.updateUI(load);
                            worker2.execute();

                    } else {
                        JOptionPane.showMessageDialog(MainForm.this, "The file has a wrong resolution");
                    }
                } else {
                    JOptionPane.showMessageDialog(MainForm.this, "The file has a wrong extension. Need to be .gif, .jpg or .png");
                }

            }

            else

            {
                JOptionPane.showMessageDialog(MainForm.this, "The file is not exists");
            }
        }
    }

    );

    pack();

    setVisible(true);
}
}
