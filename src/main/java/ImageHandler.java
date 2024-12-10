import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler extends JPanel {
    // Propiedades principales
    private BufferedImage image = null;
    private double scale = 1.0;
    private int rotation = 0;

    // Controles de la interfaz
    private JLabel imageLabel;
    private JTextField zoomField;
    private JButton btnZoomIn;
    private JButton btnZoomOut;
    private JButton btnRotate;
    private JButton btnChangeImage;

    // Constructor
    public ImageHandler() {
        setLayout(new BorderLayout());

        // Área de visualización
        imageLabel = new JLabel("No Image Loaded", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 300));
        add(imageLabel, BorderLayout.CENTER);

        // Panel de controles inferiores
        JPanel controlPanel = new JPanel();
        btnZoomIn = new JButton("Zoom In");
        btnZoomOut = new JButton("Zoom Out");
        btnRotate = new JButton("Rotate");
        btnChangeImage = new JButton("Change Image");
        zoomField = new JTextField(String.format("%.2f", scale), 5); // Inicializar con el valor actual de escala

        controlPanel.add(btnZoomIn);
        controlPanel.add(btnZoomOut);
        controlPanel.add(new JLabel("Zoom:"));
        controlPanel.add(zoomField);
        controlPanel.add(btnRotate);
        controlPanel.add(btnChangeImage);
        add(controlPanel, BorderLayout.SOUTH);

        // Listeners
        btnZoomIn.addActionListener(e -> {
            zoomImage(1.1);
            updateZoomField();
        });
        btnZoomOut.addActionListener(e -> {
            zoomImage(0.9);
            updateZoomField();
        });
        btnRotate.addActionListener(e -> rotateImage(90));
        btnChangeImage.addActionListener(e -> changeImage());

        // Listener para el campo de zoom
        zoomField.addActionListener(e -> {
            try {
                double newScale = Double.parseDouble(zoomField.getText());
                if (newScale > 0) {
                    scale = newScale;
                    updateImageDisplay();
                } else {
                    JOptionPane.showMessageDialog(this, "El zoom debe ser un valor positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    updateZoomField(); // Restaurar el valor anterior
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
                updateZoomField(); // Restaurar el valor anterior
            }
        });
    }

    // Método: Cargar nueva imagen
    private void setImage(File file) {
        try {
            BufferedImage newImage = ImageIO.read(file);
            if (newImage != null) {
                image = newImage;
                scale = 1.0;
                rotation = 0;
                updateZoomField();
                updateImageDisplay();
            } else {
                JOptionPane.showMessageDialog(this, "El archivo seleccionado no es una imagen válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método: Cambiar imagen mediante diálogo
    private void changeImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Imagen", "jpg", "jpeg", "png", "bmp", "gif");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            setImage(fileChooser.getSelectedFile());
        }
    }

    // Método: Hacer zoom en la imagen
    private void zoomImage(double factor) {
        if (image != null) {
            scale *= factor;
            updateImageDisplay();
        }
    }

    // Método: Rotar la imagen
    private void rotateImage(int degrees) {
        if (image != null) {
            rotation = (rotation + degrees) % 360;
            if (rotation < 0) rotation += 360;
            updateImageDisplay();
        }
    }

    // Método: Actualizar la visualización de la imagen
    private void updateImageDisplay() {
        if (image == null) {
            imageLabel.setText("No Image Loaded");
            imageLabel.setIcon(null);
        } else {
            int w = image.getWidth();
            int h = image.getHeight();

            // Crear imagen escalada y rotada
            BufferedImage transformedImage = new BufferedImage(w, h, image.getType());
            Graphics2D g2d = transformedImage.createGraphics();

            // Aplicar transformaciones
            AffineTransform transform = new AffineTransform();
            transform.translate(w / 2.0, h / 2.0);
            transform.scale(scale, scale);
            transform.rotate(Math.toRadians(rotation));
            transform.translate(-w / 2.0, -h / 2.0);

            g2d.setTransform(transform);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            // Actualizar JLabel con la nueva imagen
            imageLabel.setText(null);
            imageLabel.setIcon(new ImageIcon(transformedImage));
        }
    }

    // Método: Actualizar el campo de zoom
    private void updateZoomField() {
        zoomField.setText(String.format("%.2f", scale));
    }

    // Método principal para probar el componente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Image Handler Component");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new ImageHandler());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
