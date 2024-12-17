package com.dataAccess;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataAccess {
    private final String connectionString;
    private final String containerName;

    public DataAccess(String connectionString, String containerName) {
        this.connectionString = connectionString;
        this.containerName = containerName;
    }

    public DataAccess(){
        this.connectionString = "DefaultEndpointsProtocol=https;AccountName=almacenspdvi;AccountKey=JY23PNhp9akXxzqmMlhS0vzhqWwmO9MRZqJYT8XvjD46gSfgwoQF12aFj0Q+0G2i1PzIE6JeGOKM+AStsTiG9Q==;EndpointSuffix=core.windows.net";
        this.containerName = "contenedor";
    }

    public List<File> getImages() throws IOException {
        // Crear el cliente para el contenedor
        BlobContainerClient containerClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient()
                .getBlobContainerClient(containerName);

        List<byte[]> images = new ArrayList<>();

        // Iterar sobre los blobs del contenedor
        for (BlobItem blobItem : containerClient.listBlobs()) {
            // Obtener el cliente de blob para el blob actual
            BlobClient blobClient = containerClient.getBlobClient(blobItem.getName());

            // Descargar el contenido del blob en un ByteArrayOutputStream
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                blobClient.downloadStream(outputStream);
                images.add(outputStream.toByteArray());
            }
        }


        return writeFiles(images);
    }

    private static List<File> writeFiles(List<byte[]> imagenes) {
        String path = "imagenes/";
        java.io.File directory = new java.io.File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        List<File> files = new ArrayList<>();
        for (int i = 0; i < imagenes.size(); i++) {
            File file = new File(path + "imagen" + i + ".jpg");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(imagenes.get(i));
                files.add(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    
}