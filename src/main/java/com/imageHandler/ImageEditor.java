package com.imageHandler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class ImageEditor {
    private JFrame frame; // Ventana del editor
    private JLabel imageLabel; // Para mostrar la imagen
    private JSlider brightnessSlider; // Slider de brillo
    private JSlider contrastSlider; // Slider de contraste
    private JButton saveButton; // Botón para guardar
    private BufferedImage originalImage; // Imagen original
    private BufferedImage editedImage; // Imagen editada
    private SaveCallback saveCallback; // Callback para guardar

    // Interfaz para el callback
    public interface SaveCallback {
        void onSave(BufferedImage editedImage);
    }

    // Constructor
    public ImageEditor() {
        frame = new JFrame("Image Editor");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        imageLabel = new JLabel("No Image Loaded", SwingConstants.CENTER);
        frame.add(imageLabel, BorderLayout.CENTER);

        // Panel de controles
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // Slider de brillo
        JLabel brightnessLabel = new JLabel("Brightness:");
        brightnessSlider = new JSlider(-100, 100, 0);
        brightnessSlider.addChangeListener(e -> updateImage());

        // Slider de contraste
        JLabel contrastLabel = new JLabel("Contrast:");
        contrastSlider = new JSlider(0, 200, 100);
        contrastSlider.addChangeListener(e -> updateImage());

        // Botón para guardar
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (saveCallback != null && editedImage != null) {
                saveCallback.onSave(editedImage); // Llamar al callback
                frame.dispose(); // Cerrar la ventana del editor
            }
        });

        controlsPanel.add(brightnessLabel);
        controlsPanel.add(brightnessSlider);
        controlsPanel.add(contrastLabel);
        controlsPanel.add(contrastSlider);
        controlsPanel.add(new JLabel()); // Espaciador
        controlsPanel.add(saveButton);

        frame.add(controlsPanel, BorderLayout.SOUTH);
    }

    // Método para abrir el editor con la imagen seleccionada
    public void editImage(BufferedImage image, SaveCallback callback) {
        if (image == null) {
            JOptionPane.showMessageDialog(null, "No hay imagen cargada para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        originalImage = image;
        editedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        saveCallback = callback;

        // Mostrar la imagen original
        imageLabel.setIcon(new ImageIcon(originalImage));
        frame.setVisible(true);
    }

    // Método para actualizar la imagen en función del brillo y contraste
    private void updateImage() {
        if (originalImage == null) return;

        float brightness = brightnessSlider.getValue() / 100f;
        float contrast = contrastSlider.getValue() / 100f;

        // Escala de brillo y contraste
        RescaleOp rescaleOp = new RescaleOp(contrast, brightness * 255, null);
        rescaleOp.filter(originalImage, editedImage);

        // Actualizar el JLabel con la imagen modificada
        imageLabel.setIcon(new ImageIcon(editedImage));
    }
}
