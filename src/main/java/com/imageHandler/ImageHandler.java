package com.imageHandler;

import com.Pages.Sercher;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ImageHandler extends JPanel {
    private BufferedImage image = null;

    // Tabla para gestionar las imágenes
    private JTable imageTable;
    private DefaultTableModel tableModel;

    // Botones
    private JButton btnAddImage;
    private JButton botonEditar;

    // Panel para mostrar la imagen ampliada
    private JPanel imagePanel;
    private JLabel imageLabel;
    private JButton btnBack;

    // Mapa para almacenar las ubicaciones originales de las imágenes
    private HashMap<Point, String> imageMap;

    // Constructor
    public ImageHandler() {
        setLayout(new BorderLayout());

        imageMap = new HashMap<>(); // Inicializar el mapa

        // Modelo de tabla con 5 columnas fijas
        tableModel = new DefaultTableModel(0, 5) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer todas las celdas no editables
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return ImageIcon.class; // Todas las columnas contienen imágenes
            }
        };

        imageTable = new JTable(tableModel);
        imageTable.setRowHeight(64); // Ajustar la altura de las filas para las miniaturas
        imageTable.setTableHeader(null); // Ocultar encabezado de la tabla

        JScrollPane scrollPane = new JScrollPane(imageTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de controles
        JPanel controlPanel = new JPanel(new BorderLayout());
        btnAddImage = new JButton("Load Image");
        botonEditar = new JButton("Edit Image"); // Botón adicional
        controlPanel.add(btnAddImage, BorderLayout.EAST);
        controlPanel.add(botonEditar, BorderLayout.WEST); // Añadir botón a la izquierda
        add(controlPanel, BorderLayout.SOUTH);

        // Crear panel para mostrar la imagen ampliada
        imagePanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        btnBack = new JButton("Back to Table");
        btnBack.addActionListener(e -> showTable());
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(btnBack, BorderLayout.SOUTH);

        // Listeners
        btnAddImage.addActionListener(e -> addImageToTable(null));
        botonEditar.addActionListener(e -> editImageButton()); // Listener del botón adicional

        // Añadir listener para doble clic
        imageTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Doble clic
                    int row = imageTable.rowAtPoint(e.getPoint());
                    int column = imageTable.columnAtPoint(e.getPoint());
                    Object value = tableModel.getValueAt(row, column);
                    if (value instanceof ImageIcon) {
                        Point cell = new Point(row, column);
                        String filePath = imageMap.get(cell);
                        if (filePath != null) {
                            showOriginalImage(filePath);
                        }
                    }
                }
            }
        });

        // Configuración inicial de la tabla
        initializeTableWithEmptyRows(5); // Añadir 5 filas vacías por defecto
    }

    private void initializeTableWithEmptyRows(int rowCount) {
        for (int i = 0; i < rowCount; i++) {
            Object[] emptyRow = new Object[tableModel.getColumnCount()];
            tableModel.addRow(emptyRow);
        }
    }

    private void addImageToTable(File file) {

        if (file == null)
            file = Sercher.showDialog((Frame) SwingUtilities.getWindowAncestor(this));

        try {
            BufferedImage img = ImageIO.read(file);
            if (img != null) {
                ImageIcon thumbnail = new ImageIcon(img.getScaledInstance(64, 64, Image.SCALE_SMOOTH));

                // Buscar fila disponible o crear una nueva
                int rowCount = tableModel.getRowCount();
                int columnCount = tableModel.getColumnCount();
                boolean added = false;

                for (int i = 0; i < rowCount; i++) {
                    for (int j = 0; j < columnCount; j++) {
                        if (tableModel.getValueAt(i, j) == null) {
                            tableModel.setValueAt(thumbnail, i, j);
                            imageMap.put(new Point(i, j), file.getAbsolutePath()); // Mapear celda a archivo
                            added = true;
                            break;
                        }
                    }
                    if (added) break;
                }

                // Si no hay espacio, añadir una nueva fila
                if (!added) {
                    Object[] newRow = new Object[columnCount];
                    newRow[0] = thumbnail;
                    tableModel.addRow(newRow);
                    imageMap.put(new Point(rowCount, 0), file.getAbsolutePath()); // Mapear celda a archivo
                }

            } else {
                JOptionPane.showMessageDialog(this, "El archivo seleccionado no es una imagen válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showOriginalImage(String filePath) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(filePath));
            if (originalImage != null) {
                int maxWidth = 800;
                int maxHeight = 600;

                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();

                // Calcular nuevas dimensiones manteniendo la relación de aspecto
                double widthRatio = (double) maxWidth / originalWidth;
                double heightRatio = (double) maxHeight / originalHeight;
                double ratio = Math.min(widthRatio, heightRatio);

                int newWidth = (int) (originalWidth * ratio);
                int newHeight = (int) (originalHeight * ratio);

                Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
                removeAll();
                add(imagePanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen original: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTable() {
        removeAll();
        JScrollPane scrollPane = new JScrollPane(imageTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(btnAddImage, BorderLayout.EAST);
        controlPanel.add(botonEditar, BorderLayout.WEST);
        add(controlPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void editImageButton() {
        ImageEditor imageEditor = new ImageEditor();

        imageEditor.editImage(getSelectedImageFile(), (editedImage) -> {
            if (editedImage == null) {
                return;
            }
            File file = new File("imagenes/edited_image.png");
            int counter = 1;
            while (file.exists()) {
                file = new File("imagenes/edited_image_" + counter + ".png");
                counter++;
            }
            try {
                ImageIO.write(editedImage, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addImageToTable(file);

        });
    }
    private BufferedImage getSelectedImageFile() {
        int selectedRow = imageTable.getSelectedRow();
        int selectedColumn = imageTable.getSelectedColumn();
        if (selectedRow != -1 && selectedColumn != -1) {
            Point cell = new Point(selectedRow, selectedColumn);
            String filePath = imageMap.get(cell);
            if (filePath != null) {
                try {
                    return ImageIO.read(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Image Handler with Table");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new ImageHandler());
            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}