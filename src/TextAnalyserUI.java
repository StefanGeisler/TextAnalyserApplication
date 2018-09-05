import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Class defines the UI for Text Analyser Application
 */
public class TextAnalyserUI extends Application {

    private Stage       primaryStage;

    private MenuItem    itemOpen;
    private MenuItem    itemSave;
    private MenuItem    itemClear;

    private TextArea    textArea;
    private TableView   lettersTable;

    private Button      buttonAnalyse;

    private CheckBox    removeSpacesBox;
    private ComboBox    alphabetComboBox;


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public TableView getLettersTable() {
        return lettersTable;
    }

    public MenuItem getItemSave() {
        return itemSave;
    }

    public MenuItem getItemOpen() {
        return itemOpen;
    }

    public MenuItem getItemClear() {
        return itemClear;
    }

    public Button getButtonAnalyse() {
        return buttonAnalyse;
    }

    public CheckBox getRemoveSpacesBox() {
        return removeSpacesBox;
    }

    public ComboBox getAlphabetComboBox() {
        return alphabetComboBox;
    }


    /**
     * Method creates menu for application.
     *
     * @return MenuBar node, containing the menu-items.
     */
    private MenuBar createMenu() {
        MenuBar menuBar = new MenuBar();

        // super-menu
        Menu menuFile = new Menu("File");
        Menu menuOptions = new Menu("Options");

        // sub-menus
        itemOpen = new MenuItem("open");
        itemSave = new MenuItem("save");
        itemClear = new MenuItem("clear");
        menuFile.getItems().addAll(itemOpen, itemSave, itemClear);

        menuBar.getMenus().addAll(menuFile,menuOptions);
        return menuBar;
    }

    /**
     * Method creates a TabPane with two tabs for the UI.
     * Primary tab holds a text area.
     *
     * @return TabPane node
     */
    private TabPane createTabPane() {
        // create TabPane
        TabPane tabPane = new TabPane();
        Tab mainTab = new Tab("Original");
        Tab secondaryTab = new Tab("Modified");
        tabPane.getTabs().addAll(mainTab, secondaryTab);

        // create TextArea
        this.textArea = new TextArea();
        textArea.setPromptText("Enter text, open file or drag&drop...");
        textArea.setPrefHeight(400);
        textArea.setWrapText(true);
        mainTab.setContent(textArea);
        return tabPane;
    }

    /**
     * Method creates a FlowPane with the UI controls for encryption.
     *
     * @return FlowPane node
     */
    private FlowPane createEncryptPane() {
        // create FlowPane as container for the UI control elements
        FlowPane encryptPane = new FlowPane(5,5);
        encryptPane.setPadding(new Insets(5));
        //encryptPane.setPrefWrapLength(170);

        //TODO: add ui elements

        //this.removeSpacesBox = new CheckBox("remove spaces");
        //this.alphabetComboBox = createAlphabetBox();
        //HBox alphabetHBox = new HBox(5, new Label("Key:"), alphabetComboBox);

        //encryptPane.getChildren().addAll(removeSpacesBox, alphabetHBox);
        return encryptPane;
    }

    /**
     * Method creates a FlowPane with the UI controls for decryption.
     *
     * @return FlowPane node
     */
    private FlowPane createDecryptPane() {
        // create FlowPane as container for the UI control elements
        FlowPane decryptPane = new FlowPane(5,5);
        decryptPane.setPadding(new Insets(5));
        //decryptPane.setPrefWrapLength(170);

        //TODO: add ui elements

        //decryptPane.getChildren().addAll();
        return decryptPane;
    }

    /**
     * Method creates a ComboBox with options A to Z.
     *
     * @return ComboBox node
     */
    private ComboBox createAlphabetBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (char c : alphabet) {
            comboBox.getItems().add("A -> " + c);
        }
        return comboBox;
    }


    /**
     * Main method for starting and initializing the UI.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        /* define UI-elements */

        // root node
        BorderPane root = new BorderPane();


        /* TOP section */

        // add menu on top
        root.setTop(createMenu());


        /* LEFT section */

        VBox leftVBox = new VBox();
        leftVBox.setSpacing(10);
        leftVBox.setPadding(new Insets(10,5,10,5));
        leftVBox.setMaxWidth(160);
        root.setLeft(leftVBox);

        // add label
        final Label frequencyLabel = new Label("Frequency Analysis");
        leftVBox.getChildren().add(frequencyLabel);

        // add table
        lettersTable = new TableView();
        leftVBox.getChildren().add(lettersTable);

        // add button
        buttonAnalyse = new Button("Analyse Text");
        buttonAnalyse.setMaxWidth(Double.MAX_VALUE);
        leftVBox.getChildren().add(buttonAnalyse);


        /* CENTER section */

        GridPane centerGrid = new GridPane();
        centerGrid.setVgap(8);
        centerGrid.setHgap(8);
        //centerGrid.setPadding(new Insets(5));
        //centerGrid.setGridLinesVisible(true);
        root.setCenter(centerGrid);

        // add TabPane with two tabs and TextArea
        TabPane tabPane = createTabPane();
        centerGrid.add(tabPane,0,0,3,1);

        // add Separators
        Separator hSeparator = new Separator();
        centerGrid.add(hSeparator, 0,1,3,1);
        Separator vSeparator = new Separator(Orientation.VERTICAL);
        centerGrid.add(vSeparator,1,2,1,2);

        // add control areas for "Encryption" and "Decryption"
        final Label encryptLabel = new Label("Encrypt:");
        centerGrid.add(encryptLabel,0,2,1,1);
        centerGrid.add(createEncryptPane(),0,3,1,1);
        final Label decryptLabel = new Label("Decrypt:");
        centerGrid.add(decryptLabel,2,2,1,1);
        centerGrid.add(createDecryptPane(),2,3,1,1);


        // set main scene
        Scene mainScene = new Scene(root, 800, 600);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Text Analyse Tool");
        primaryStage.show();

        // initialize controller
        new TextAnalyserController(this);

    }

}