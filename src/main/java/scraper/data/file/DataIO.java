package scraper.data.file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DataIO {

    public static void saveTissHistory(List<String> lines) {
        System.out.println("Salvando Histórico das versões dos Componentes do Padrão TISS");
        try (OutputStreamWriter wr = new OutputStreamWriter(
                new FileOutputStream("src/main/resources/downloads/tiss_history.csv"), StandardCharsets.UTF_8)
        ) {

            wr.write("\n");
            for (String line : lines) {
                wr.write(line);
                wr.write("\n");
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void saveFileTo(InputStream inputStream, String folderPath, String fileName) {
        createFolder(folderPath);
        File file = new File(folderPath, fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving the file", e);
        }
    }

    private static void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean ignored = folder.mkdirs();
        }
    }
}
