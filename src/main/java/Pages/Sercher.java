package Pages;

import DataAccess.DataAccess;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Sercher extends JDialog {
    private File selectedFile = null;
    private boolean selected = false;

    public Sercher(Frame owner) {
        super(owner, "Seleccionar Imagen, doble click para seleccionar", true);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel thumbnailPanel = new JPanel();
        thumbnailPanel.setLayout(new GridLayout(0, 3, 10, 10)); // Grid de 3 columnas

        // Obtener rutas de imágenes desde DataAccess
        DataAccess dataAccess = new DataAccess();
        List<File> imagePaths = null;
        try {
            imagePaths = dataAccess.getImages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (imagePaths.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron imágenes.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (File imag : imagePaths) {
                thumbnailPanel.add(createThumbnail(imag));
            }
        }

        JScrollPane scrollPane = new JScrollPane(thumbnailPanel);
        add(scrollPane, BorderLayout.CENTER);

        setSize(700, 400);
        setLocationRelativeTo(getOwner());
    }

    private JPanel createThumbnail(File file) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Miniatura de imagen
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        Image image = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        panel.add(imageLabel, BorderLayout.CENTER);

        // Nombre del archivo
        JLabel nameLabel = new JLabel(file.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        panel.add(nameLabel, BorderLayout.SOUTH);

        // Click para seleccionar
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (selectedFile != null && !selectedFile.equals(file)) {
                    selected = false; // Reiniciar la selección si cambias de archivo
                }

                selectedFile = file; // Guardar archivo seleccionado
                highlightSelection(panel, (JPanel) panel.getParent()); // Resaltar panel seleccionado

                if(selected) {
                    dispose(); // Cerrar el diálogo
                }
                selected = true;
            }
        });

        return panel;
    }

    private void highlightSelection(JPanel selectedPanel, JPanel thumbnailPanel) {
        // Restaurar fondo de todos los paneles
        for (Component comp : thumbnailPanel.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(null); // Restaurar fondo a su color original
            }
        }
        // Resaltar el fondo del panel seleccionado
        selectedPanel.setBackground(Color.CYAN); // Cambiar el fondo al color deseado
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    // Método estático para mostrar el diálogo
    public static File showDialog(Frame owner) {
        Sercher sercher = new Sercher(owner);
        sercher.setVisible(true);
        return sercher.getSelectedFile();
    }
}
