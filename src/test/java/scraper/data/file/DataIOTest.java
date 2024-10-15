package scraper.data.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DataIOTest {

    private static final String ROOT_PATH = "src/test/resources/";

    private static final String FILE_PATH = ROOT_PATH + "tiss_history.txt";
    private static final String FOLDER_PATH = ROOT_PATH + "downloads";
    private static final String FILE_NAME = "file.txt";

    @AfterEach
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void tearDown() {
        new File(FILE_PATH).delete();
        new File(FOLDER_PATH, FILE_NAME).delete();
        new File(FOLDER_PATH).delete();
    }

    @Test
    public void testSaveTISSHistory() {
        List<String> lines = List.of("line 1", "line 2", "line 3");
        DataIO.saveTissHistory(lines, FILE_PATH);

        Assertions.assertTrue(new File(FILE_PATH).exists());

        try (BufferedReader bf = new BufferedReader(new FileReader(FILE_PATH))) {
            Assertions.assertEquals("line 1", bf.readLine());
            Assertions.assertEquals("line 2", bf.readLine());
            Assertions.assertEquals("line 3", bf.readLine());
        } catch (Exception e) {
            Assertions.fail("Error reading file: " + e.getMessage());
        }
    }

    @Test
    public void testSaveFileTo() {
        String content = "This is a test file";
        byte[] response = content.getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(response);
        DataIO.saveFileTo(inputStream, FOLDER_PATH, FILE_NAME);

        Assertions.assertTrue(new File(FOLDER_PATH, FILE_NAME).exists());

        try (BufferedReader bf = new BufferedReader(new FileReader(FOLDER_PATH + "/" + FILE_NAME))) {
            Assertions.assertEquals(content, bf.readLine());
        } catch (Exception e) {
            Assertions.fail("Error reading file: " + e.getMessage());
        }
    }

    @Test
    public void testCreateFolder() {
        DataIO.saveFileTo(new ByteArrayInputStream(new byte[0]), FOLDER_PATH, FILE_NAME);

        File createdFolder = new File(FOLDER_PATH);

        Assertions.assertTrue(createdFolder.exists());
        Assertions.assertTrue(createdFolder.isDirectory());
    }
}
