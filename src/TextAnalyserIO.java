import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;


/**
 * Class provides IO functionality for the Text Analyser Application,
 * especially IO operations for reading and writing to an text file.
 */
public class TextAnalyserIO {

    /**
     * Static method for reading from an txt file.
     * Returns content as string-object.
     *
     * @param inputFile Input file (txt) to read from.
     *
     * @return Content of the file represented as string-object or null, if an exception occurred.
     */
    public static String openFile(final File inputFile) {
        StringBuilder content = new StringBuilder();

        try (Stream<String> stream = Files.lines(inputFile.toPath())) {
            // check, if file contains plane text
            if (fileContainsText(inputFile)) {
                stream.forEach(content::append);
            } else {
                throw new IllegalArgumentException("No valid file was selected!");
            }
        } catch (Exception e) {
            TextAnalyserController.showAlert(e);
            e.printStackTrace();
            return null;
        }
        return content.toString();
    }

    /**
     * Static method for saving to a txt file.
     *
     * @param content Content to be saved, represented as string.
     *
     * @param outputFile File to save content.
     */
    public static void saveFile(final String content, final File outputFile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write(content);
        } catch (IOException e) {
            TextAnalyserController.showAlert(e);
            e.printStackTrace();
        }
    }

    /**
     * Method checks, if a selected file is valid for reading text from it.
     *
     * @return True if the file contains plane text and data can be read from it.
     *          otherwise false.
     */
    public static boolean fileContainsText(final File file) throws IOException {
        if (file == null || !file.isFile()) {
            return false;
        }
        // check if file-type is text*
        String fileType = Files.probeContentType(file.toPath());
        return (fileType != null && fileType.matches("(text).*"));
    }
}
