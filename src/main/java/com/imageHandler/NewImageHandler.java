/*
 * Created by JFormDesigner on Tue Dec 10 12:34:02 CET 2024
 */

package com.imageHandler;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author Oscar
 */
public class NewImageHandler extends JPanel {
    private BufferedImage image = null;
    private double scale = 1.0;
    private int rotation = 0;
    public NewImageHandler() {
        initComponents();
    }

    private void btnZoomIn(ActionEvent e) {
        // TODO add your code here
    }

    private void zoom_in(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Óscar Mezquita
        imageLabel = new JLabel();
        btnZoomIn = new JButton();
        btnZoomOut = new JButton();
        zoomLabel = new JLabel();
        zoomField = new JTextField();
        btnRotate = new JButton();
        btnChangeImage = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border.
        EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing
        . border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ),
        java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( )
        { @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () ))
        throw new RuntimeException( ); }} );
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- imageLabel ----
        imageLabel.setText("No Image Loaded");
        add(imageLabel, "cell 0 0 6 11");

        //---- btnZoomIn ----
        btnZoomIn.setText("Zoom In");
        btnZoomIn.addActionListener(e -> zoom_in(e));
        add(btnZoomIn, "cell 0 11");

        //---- btnZoomOut ----
        btnZoomOut.setText("Zoom Out");
        add(btnZoomOut, "cell 1 11");

        //---- zoomLabel ----
        zoomLabel.setText("Zoom:");
        add(zoomLabel, "cell 2 11");
        add(zoomField, "cell 2 11");

        //---- btnRotate ----
        btnRotate.setText("Rotate");
        add(btnRotate, "cell 3 11");

        //---- btnChangeImage ----
        btnChangeImage.setText("Change Image");
        add(btnChangeImage, "cell 4 11");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Óscar Mezquita
    private JLabel imageLabel;
    private JButton btnZoomIn;
    private JButton btnZoomOut;
    private JLabel zoomLabel;
    private JTextField zoomField;
    private JButton btnRotate;
    private JButton btnChangeImage;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
