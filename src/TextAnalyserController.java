import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Class contains controller with functionality for the Text Analyser Application.
 */
public class TextAnalyserController {

    private final TextAnalyserUI ui;

    private StringProperty originalText;            // the text written in the text area or imported by the user
    private StringProperty cipherText;              // the text after applying encryption method
    private StringProperty planeText;               // the text after applying decryption method

    private ObservableList<FrequencyModel> data;    // data model for the TableView binding


    /**
     * Constructor.
	 *
     * @param ui UI to be controlled and filled with this controller.
	 */
    public TextAnalyserController(TextAnalyserUI ui) {
        this.ui = ui;
        this.originalText = new SimpleStringProperty();
        this.cipherText = new SimpleStringProperty();
        this.planeText = new SimpleStringProperty();
        this.data = FXCollections.observableArrayList();

        /* set EventHandlers */

        ui.getItemOpen().setOnAction(new OpenHandler());
        ui.getItemSave().setOnAction(new SaveHandler());
        ui.getItemClear().setOnAction((ActionEvent event) -> {
                ui.getOriginalTextArea().clear();
                ui.getCipherTextArea().clear();
                ui.getPlaneTextArea().clear();
                ui.getLogTextArea().clear();
                data.clear();
        });

        ui.getItemResize().setOnAction((ActionEvent event) -> {
                ui.getPrimaryStage().setWidth(800);
                ui.getPrimaryStage().setHeight(680);
        });

        ui.getPrimaryStage().setOnCloseRequest((WindowEvent event) -> {
                // close all other application windows before exit
                ui.getSettingsStage().close();
                ui.getHelpStage().close();
        });

        ui.getButtonAnalyse().setOnAction(new AnalyseHandler());
        ui.getButtonHelp().setOnAction((ActionEvent event) ->
                ui.getHelpStage().show()
        );
        ui.getButtonSettings().setOnAction((ActionEvent event) ->
                ui.getSettingsStage().show()
        );
        ui.getButtonEncrypt().setOnAction(new EncryptHandler());

        ui.getOriginalTextArea().setOnDragEntered(new DragEnteredHandler());
        ui.getOriginalTextArea().setOnDragExited((DragEvent event) ->
                ui.getOriginalTextArea().setStyle("-fx-border-color:transparent;")
        );
        ui.getOriginalTextArea().setOnDragOver(new DragOverHandler());
        ui.getOriginalTextArea().setOnDragDropped(new DragDroppedHandler());

        /* add BooleanBindings between StringProperties and UI elements */

        // enables the "encrypt/decrypt buttons" only if text has been entered or imported
        ui.getButtonEncrypt().disableProperty().bind(originalText.isEmpty());
        ui.getButtonDecrypt().disableProperty().bind(originalText.isEmpty());

        // enables safe option and "cipher text tab" only if text has been encrypted
        ui.getItemSave().disableProperty().bind(cipherText.isEmpty());
        ui.getCipherTab().disableProperty().bind(cipherText.isEmpty());

        // enables "plane text tab" only if text has been decrypted
        ui.getPlaneTab().disableProperty().bind(planeText.isEmpty());


        // add Listener for cipher selection
        ui.getCipherSelectionBox().getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
                    switch (newVal.intValue()) {
                        case 0: // "Shift Cipher" option selected
                                ui.getKeyComboBox().getParent().setDisable(false);
                                ui.getKeyTextField().getParent().setDisable(true);
                                break;
                        case 1: // "Polyalphabetic Cipher" option selected
                                ui.getKeyTextField().getParent().setDisable(false);
                                ui.getKeyComboBox().getParent().setDisable(true);
                                break;
                    }
                }
        );

        // bind text areas to StringProperty
        originalText.bindBidirectional(ui.getOriginalTextArea().textProperty());
        cipherText.bindBidirectional(ui.getCipherTextArea().textProperty());
        planeText.bindBidirectional(ui.getPlaneTextArea().textProperty());

        // initialize keyboard shortcuts
        initializeShortcuts(ui.getPrimaryStage().getScene());

        // initialize TableView
        initializeLettersTable();
    }


    /**
     * Method initializes the shortcuts for the mainScene.
     *
     * @param scene The scene where the shortcuts are applied.
     */
    private void initializeShortcuts(Scene scene) {
        KeyCombination kcOpen = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
        KeyCombination kcSave = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        KeyCombination kcClear = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
        scene.getAccelerators().put(kcOpen, () -> this.ui.getItemOpen().fire());
        scene.getAccelerators().put(kcSave, () -> this.ui.getItemSave().fire());
        scene.getAccelerators().put(kcClear, () -> this.ui.getItemClear().fire());
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
        colLetter.setCellValueFactory(new PropertyValueFactory<>("letter"));
        colFrequency.setCellValueFactory(new PropertyValueFactory<>("frequency"));
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
                originalText.setValue(TextAnalyserIO.openFile(inputFile));
                showLog("Imported text from " + inputFile.toString());
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
                TextAnalyserIO.saveFile(cipherText.get(), outputFile);
                showLog("Saved text to " + outputFile.toString());
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
            TextArea target = ui.getOriginalTextArea();
            Dragboard db = event.getDragboard();
            String color = "-fx-border-color:RED";

            // don't allow multiple files to be dragged
            if (db.hasFiles() && db.getFiles().size() == 1) {
                try {
                    // check if text can be read from the dragged file
                    if (TextAnalyserIO.fileContainsText(db.getFiles().get(0))) {
                        color = "-fx-border-color:LIGHTGREEN";
                    }
                } catch (IOException e) {
                    // no handling needed
                }
            // also allow direct text dragging
            } else if (db.hasString() && !db.hasFiles()) {
                color = "-fx-border-color:LIGHTGREEN";
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
            TextArea target = ui.getOriginalTextArea();
            Dragboard db = event.getDragboard();
            boolean success = false;

            // don't allow multiple files to be dragged
            if (db.hasFiles() && db.getFiles().size() == 1) {
                try {
                    // check if text can be read from the dragged file
                    File inputFile = db.getFiles().get(0);
                    if (TextAnalyserIO.fileContainsText(inputFile)) {
                        target.setText(TextAnalyserIO.openFile(inputFile));
                        showLog("Imported text from " + inputFile.toString());
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
            if (originalText.getValueSafe().isEmpty()) {
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
            showLog(originalText.getValue().length() + " characters analysed");
        }
    }

    /**
     * Inner Class for ActionEvent "encrypt-button-pressed"
     */
    private class EncryptHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // check if all selection options have been made and give visual highlight to missing options
            SelectionModel<String> selectionModel = ui.getCipherSelectionBox().getSelectionModel();
            if (selectionModel.isEmpty()) {
                ui.getCipherSelectionBox().setStyle("-fx-border-color:RED");
                return;
            } else {
                ui.getCipherSelectionBox().setStyle("-fx-border-color:transparent");
            }
            if (ui.getKeyComboBox().isDisabled()) {
                if (ui.getKeyTextField().getText().isEmpty()) {
                    ui.getKeyTextField().setStyle("-fx-border-color:RED");
                    return;
                }
                ui.getKeyTextField().setStyle("-fx-border-color:transparent");

            } else {
                if (ui.getKeyComboBox().getSelectionModel().isEmpty()) {
                    ui.getKeyComboBox().setStyle("-fx-border-color:RED");
                    return;
                }
                ui.getKeyComboBox().setStyle("-fx-border-color:transparent");
            }

            if (!originalText.getValueSafe().isEmpty()) {
                // convert to upper case, remove whitespace, punctuation and non-alphanumeric characters if options are enabled
                // originalText -> modifiedText -> cipherText
                String modifiedText = originalText.get();
                boolean removeWhitespace = ui.getWhitespaceToggleGroup().getSelectedToggle().getUserData().equals("remove");
                if (removeWhitespace) {
                    modifiedText = modifiedText.replaceAll("[\\s]", "");
                }
                boolean removePunctuation = ui.getPunctuationToggleGroup().getSelectedToggle().getUserData().equals("remove");
                if (removePunctuation) {
                    modifiedText = modifiedText.replaceAll("[\\p{Punct}]", "");
                }
                boolean onlyAlphaNumeric = ui.getAlphaNumericCheckBox().isSelected();
                if (onlyAlphaNumeric) {
                    modifiedText = modifiedText.replaceAll("[^\\p{Alnum}]", "");
                }
                boolean toUpperCase = ui.getCaseToggleGroup().getSelectedToggle().getUserData().equals("convert");
                if (toUpperCase) {
                    modifiedText = modifiedText.toUpperCase();
                }

                // do encryption
                String cipher = selectionModel.getSelectedItem();
                switch (cipher) {
                    case "Shift Cipher":
                        int shift = ui.getKeyComboBox().getSelectionModel().getSelectedIndex();
                        showLog("Caesar (shift: " + shift + ")");
                        cipherText.setValue(Cryptography.shiftCipher(shift, modifiedText));
                        break;
                    case "Polyalphabetic Cipher":
                        String keyword = ui.getKeyTextField().getText().toUpperCase();
                        keyword = keyword.replaceAll("[^A-Z]" , ""); // allow only latin letters in keyword
                        showLog("Vigenère (keyword: " + keyword + ")");
                        try {
                            cipherText.setValue(Cryptography.polyalphabeticCipher(keyword, modifiedText));
                        } catch (IllegalArgumentException e) {
                            showAlert(e);
                        }
                        break;
                }
            }
        }
    }

    /**
     * Method shows a logging message with an added time stamp in a specified TextArea of the GUI.
     *
     * @param message The logging message.
     */
    private void showLog(final String message) {
        String timeStamp = new SimpleDateFormat("[HH:mm:ss] ").format(new Date());
        ui.getLogTextArea().appendText(timeStamp + message + "\n");
    }

    /**
     * Static method for generating a simple error message
     * to inform the user about an occurred exception (especially IO).
     *
     * @param exception Exception to generate an alert message from.
     */
    protected static void showAlert(final Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception occurred");
        alert.setHeaderText(exception.getClass().getName());
        alert.setContentText(exception.getMessage());
        alert.show();
    }
}