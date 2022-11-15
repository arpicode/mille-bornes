import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConfigurationTest {
    private static final String cardRegexp = Configuration.cardRegexp;

    @BeforeAll
    static void setupTestFile() {
        deleteConfigFile("tmpconfig.txt");
    }

    @AfterAll
    static void tearDown() {
        deleteConfigFile("tmpconfig.txt");
    }

    @Test
    @DisplayName("it should create the default config file.")
    void testCreateDefaultConfigFile() {
        Configuration.parse("tmpconfig.txt");
        assertTrue(Configuration.exists("tmpconfig.txt"));
    }

    @Test
    @DisplayName("it should report correctly the default file having 22 relevant lines.")
    void testNumLinesDefaultConfig() {
        ArrayList<String> lines = Configuration.parse("tmpconfig.txt");
        assertEquals(22, lines.size());
    }

    @Test
    @DisplayName("it should report correctly the default file having 7 relevant lines.")
    void testNumLinesTestFile2() {
        ArrayList<String> lines = Configuration.parse("testfiles/config_test2.txt");
        assertEquals(7, lines.size());
    }

    @Test
    @DisplayName("it should match valid config line pattern.")
    void testLineRegex() {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("10;Accident");
        lines.add("   3;Nom de Carte   ");
        lines.add("  5  ;  100   ");
        lines.add("2 ;essence");
        lines.add("8; citerne");

        for (String line : lines) {
            assertTrue(line.trim().matches(cardRegexp));
        }
    }

    private static boolean deleteConfigFile(String fullFileName) {
        boolean result = false;
        try {
            result = Files.deleteIfExists(Paths.get(fullFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
