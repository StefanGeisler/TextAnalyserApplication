import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * Class contains controller with functionality for the Text Analyser Application.
 */
public class TextAnalyserController {

    private final TextAnalyserUI ui;

    private StringProperty originalText;            // the text written or loaded by the user
    private StringProperty modifiedText;            // the text after modification (e.g. encrypting/decrypting)

    private ObservableList<FrequencyModel> data;    // data model for the TableView binding


    /**
     * Constructor.
	 *
     * @param ui UI to be controlled and filled with this controller.
	 */
    public TextAnalyserController(TextAnalyserUI ui) {
        this.ui = ui;
        this.originalText = new SimpleStringProperty();
        this.modifiedText = new SimpleStringProperty();
        this.data = FXCollections.observableArrayList();

        // set EventHandlers
        ui.getItemOpen().setOnAction(new OpenHandler());
        ui.getItemSave().setOnAction(new SaveHandler());
        ui.getItemClear().setOnAction((ActionEvent event) -> {
            ui.getTextArea().clear();
            data.clear();
        });

        ui.getButtonAnalyse().setOnAction(new AnalyseHandler());

        ui.getTextArea().setOnDragEntered(new DragEnteredHandler());
        ui.getTextArea().setOnDragExited((DragEvent event) -> {
            ui.getTextArea().setStyle("-fx-background-color:LIGHTGREY;");
        });
        ui.getTextArea().setOnDragOver(new DragOverHandler());
        ui.getTextArea().setOnDragDropped(new DragDroppedHandler());

        // bind text area to StringProperty
        originalText.bindBidirectional(ui.getTextArea().textProperty());

        // initialize TableView
        initializeLettersTable();

    }

    /**
     * Method creates two columns for the TableView of the UI
     * and binds the data to the TableView.
     */
    private void initializeLettersTable() {
        // create Columns
        TableColumn<FrequencyModel, String> colLetter = new TableColumn<>("Letter");
        TableColumn<FrequencyModel, String> colFrequency = new TableColumn<>("Frequency");
        colLetter.setPrefWidth(60);
        colFrequency.setPrefWidth(90);
        colLetter.setResizable(false);
        colFrequency.setResizable(false);
        ui.getLettersTable().getColumns().addAll(colLetter, colFrequency);

        // bind model to table
        colLetter.setCellValueFactory(new PropertyValueFactory<FrequencyModel, String>("letter"));
        colFrequency.setCellValueFactory(new PropertyValueFactory<FrequencyModel, String>("frequency"));
        ui.getLettersTable().setItems(this.data);
    }

    /**
     * Inner class for ActionEvent "open file".
     */
    private class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // file chooser configuration
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All files", "*")

            );
            // show open file dialog
            File inputFile = fileChooser.showOpenDialog(ui.getPrimaryStage());

            // read text from specified file
            if (inputFile != null) {
                originalText.setValue(TextAnalyserController.openFile(inputFile));
                data.clear();
            }
        }
    }

    /**
     * Inner Class for ActionEvent "save file".
     */
    private class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // open file chooser and retrieve chosen file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Modified Text");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All files", "*")
            );
            // show save file dialog
            File outputFile = fileChooser.showSaveDialog(ui.getPrimaryStage());
            // save "modified text" to specified file
            if (outputFile != null) {
                TextAnalyserController.saveFile(modifiedText.get(), outputFile);
            }
        }
    }

    /**
     * Inner Class for DRAG-ENTERED Event of the text area.
     * Gives visual feedback for the user.
     */
    private class DragEnteredHandler implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            TextArea target = ui.getTextArea();
            Dragboard db = event.getDragboard();
            String color = "-fx-background-color:RED";

            // don't allow multiple files to be dragged
            if (db.hasFiles() && db.getFiles().size() == 1) {
                try {
                    // check if text can be read from the dragged file
                    if (fileContainsText(db.getFiles().get(0))) {
                        color = "-fx-background-color:LIGHTGREEN";
                    }
                } catch (IOException e) {
                    // no handling needed
                }
            // also allow direct text dragging
            } else if (db.hasString() && !db.hasFiles()) {
                color = "-fx-background-color:LIGHTGREEN";
            }
            target.setStyle(color);
        }
    }

    /**
     * Inner Class for DRAG-OVER Event of the text area.
     */
    private class DragOverHandler implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            /* data is dragged over the target */
            if (event.getDragboard().hasString() || event.getDragboard().hasFiles()) {
                // allow for both copying and moving
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        }
    }

    /**
     * Inner Class for DRAG-DROPPED Event of the text area.
     * Allows drag and drop feature for strings and plane text files.
     */
    private class DragDroppedHandler implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            /* data dropped */
            TextArea target = ui.getTextArea();
            Dragboard db = event.getDragboard();
            boolean success = false;

            // don't allow multiple files to be dragged
            if (db.hasFiles() && db.getFiles().size() == 1) {
                try {
                    // check if text can be read from the dragged file
                    File inputFile = db.getFiles().get(0);
                    if (fileContainsText(inputFile)) {
                        target.setText(TextAnalyserController.openFile(inputFile));
                        success = true;
                    }
                } catch (IOException e) {
                    // no handling needed
                }
            // also allow direct text dragging
            } else if (db.hasString() && !db.hasFiles()) {
                target.setText(db.getString());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        }
    }

    /**
     * Inner Class for ActionEvent "analyse text".
     */
    private class AnalyseHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (originalText == null) {
                return;
            }
            // do frequency analysis
            Map<Character, Integer> frequencyMap = Cryptography.frequencyAnalysis(originalText.get());

            // generate data model from EntrySet
            List<FrequencyModel> frequencyList = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry: frequencyMap.entrySet()) {
                FrequencyModel current = new FrequencyModel(entry.getKey(), entry.getValue());
                frequencyList.add(current);
            }
            // update TableView
            data.setAll(frequencyList);
        }
    }


    /**
     * Static method for reading from an txt file.
     * Returns content as string-object.
     *
     * @param inputFile Input file (txt) to read from.
     *
     * @return Content of the file represented as string-object or null, if an exception occurred.
     */
    private static String openFile(final File inputFile) {
        StringBuilder content = new StringBuilder();

        try (Stream<String> stream = Files.lines(inputFile.toPath())) {
            // check, if file contains plane text
            if (fileContainsText(inputFile)) {
                stream.forEach(content::append);
            } else {
                throw new IllegalArgumentException("No valid file was selected!");
            }
        } catch (Exception e) {
            showAlert(e);
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
    private static void saveFile(final String content, final File outputFile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write(content);
        } catch (IOException e) {
            showAlert(e);
            e.printStackTrace();
        }
    }

    /**
     * Method checks, if a selected file is valid for reading text from it.
     *
     * @return True if the file contains plane text and data can be read from it.
     *          otherwise false.
     */
    private static boolean fileContainsText(final File file) throws IOException {
        if (file == null || !file.isFile()) {
            return false;
        }
        // check if file-type is text*
        String fileType = Files.probeContentType(file.toPath());
        return (fileType != null && fileType.matches("(text).*"));
    }

    /**
     * Static method for generating a simple error message
     * to inform the user about an occurred exception (especially IO).
     *
     * @param exception Exception to generate an alert message from.
     */
    private static void showAlert(final Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception occurred");
        alert.setHeaderText(exception.getClass().getName());
        alert.setContentText(exception.getMessage());
        alert.show();
    }
}