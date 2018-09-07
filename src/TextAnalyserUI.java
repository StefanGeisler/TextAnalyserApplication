import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    private Button      buttonHelp;

    private ToggleGroup         whitespaceGroup;
    private ChoiceBox<String>   cipherSelectionBox;
    private ComboBox<String>    keyComboBox;
    private TextField           keyTextField;


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

    public Button getButtonHelp() {
        return buttonHelp;
    }

    public ToggleGroup getWhitespaceGroup() {
        return whitespaceGroup;
    }

    public ChoiceBox<String> getCipherSelectionBox() {
        return cipherSelectionBox;
    }

    public ComboBox<String> getKeyComboBox() {
        return keyComboBox;
    }

    public TextField getKeyTextField() {
        return keyTextField;
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
        FlowPane encryptPane = new FlowPane();

        // create mutually exclusive Radio Buttons for "whitespace handling"
        this.whitespaceGroup = new ToggleGroup();
        RadioButton rbIgnore = new RadioButton("ignore whitespace");
        rbIgnore.setToggleGroup(whitespaceGroup);
        rbIgnore.setSelected(true);
        RadioButton rbRemove = new RadioButton("remove whitespace");
        rbRemove.setToggleGroup(whitespaceGroup);
        VBox whitespaceVBox = new VBox(5, rbIgnore, rbRemove);
        whitespaceVBox.setPadding(new Insets(5));
        encryptPane.getChildren().add(whitespaceVBox);

        VBox cipherVBox = new VBox(5);
        cipherVBox.setPadding(new Insets(5));
        encryptPane.getChildren().add(cipherVBox);

        // create Choice Box for "cipher selection"
        this.cipherSelectionBox = new ChoiceBox<>(FXCollections.observableArrayList(
                "Shift Cipher", "Polyalphabetic Cipher")
        );
        cipherSelectionBox.setTooltip(new Tooltip("Select Cipher Method"));
        cipherVBox.getChildren().add(cipherSelectionBox);

        // shift cipher key settings (active, only after proper cipher selection)
        this.keyComboBox = createKeySelectionBox();
        HBox keyHBox = new HBox(5, new Label("Key:"), keyComboBox);
        keyHBox.setDisable(true);
        cipherVBox.getChildren().add(keyHBox);

        // polyalphabetic cipher key settings (active, only after proper cipher selection)
        this.keyTextField = new TextField();
        keyTextField.setPromptText("Enter keyword");
        keyTextField.setPrefColumnCount(10);
        HBox keyWordHBox = new HBox(5, new Label("Key:"), keyTextField);
        keyWordHBox.setDisable(true);
        cipherVBox.getChildren().add(keyWordHBox);

        return encryptPane;
    }

    /**
     * Method creates a FlowPane with the UI controls for decryption.
     *
     * @return FlowPane node
     */
    private FlowPane createDecryptPane() {
        // create FlowPane as container for the UI control elements
        FlowPane decryptPane = new FlowPane();

        //TODO: add ui elements

        return decryptPane;
    }

    /**
     * Method creates a ComboBox with key options A to Z.
     *
     * @return ComboBox node
     */
    private ComboBox<String> createKeySelectionBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (char c : alphabet) {
            comboBox.getItems().add("A -> " + c);
        }
        return comboBox;
    }

    /**
     * Method creates a Button with "Help" icon.
     *
     * @return Button node
     */
    private Button createHelpButton() {
        Image imageHelp = new Image(getClass().getResourceAsStream("Button-Help-icon.png"));
        Button button = new Button();
        ImageView imageView = new ImageView(imageHelp);
        imageView.setFitHeight(22);
        imageView.setFitWidth(22);
        button.setGraphic(imageView);
        return button;
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
        buttonHelp = createHelpButton();
        Region emptyRegion = new Region();
        HBox encryptHBox = new HBox(encryptLabel, emptyRegion, buttonHelp);
        HBox.setHgrow(emptyRegion, Priority.ALWAYS);
        centerGrid.add(encryptHBox,0,2,1,1);
        centerGrid.add(createEncryptPane(),0,3,1,1);

        final Label decryptLabel = new Label("Decrypt:");
        centerGrid.add(decryptLabel,2,2,1,1);
        centerGrid.add(createDecryptPane(),2,3,1,1);


        // set main scene
        Scene mainScene = new Scene(root, 800, 640);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Text Analyse Tool");
        primaryStage.show();

        // initialize controller
        new TextAnalyserController(this);

    }

}