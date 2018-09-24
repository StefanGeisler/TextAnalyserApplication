import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Class defines the UI for Text Analyser Application
 */
public class TextAnalyserUI extends Application {

    private Stage       primaryStage;
    private Stage       helpStage;
    private Stage       settingsStage;

    private MenuItem    itemOpen;
    private MenuItem    itemSave;
    private MenuItem    itemClear;
    private MenuItem    itemResize;
    private MenuItem    itemCompare;
    private MenuItem    itemAbout;

    private TextArea    originalTextArea;
    private TextArea    cipherTextArea;
    private TextArea    planeTextArea;
    private Tab         cipherTab;
    private Tab         planeTab;
    private TableView   lettersTable;

    private Button      buttonAnalyse;
    private Button      buttonHelp;
    private Button      buttonEncrypt;
    private Button      buttonSettings;
    private Button      buttonDecrypt;

    private ToggleGroup         whitespaceToggleGroup;
    private ToggleGroup         punctuationToggleGroup;
    private ToggleGroup         caseToggleGroup;
    private CheckBox            alphaNumericCheckBox;
    private ChoiceBox<String>   cipherSelectionBox;
    private ComboBox<String>    keyComboBox;
    private TextField           keyTextField;


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Stage getHelpStage() {
        return helpStage;
    }

    public Stage getSettingsStage() {
        return settingsStage;
    }

    public TextArea getOriginalTextArea() {
        return originalTextArea;
    }

    public TextArea getCipherTextArea() {
        return cipherTextArea;
    }

    public TextArea getPlaneTextArea() {
        return planeTextArea;
    }

    public Tab getCipherTab() {
        return cipherTab;
    }

    public Tab getPlaneTab() {
        return planeTab;
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

    public MenuItem getItemResize() {
        return itemResize;
    }

    public MenuItem getItemCompare() {
        return itemCompare;
    }

    public MenuItem getItemAbout() {
        return itemAbout;
    }

    public Button getButtonAnalyse() {
        return buttonAnalyse;
    }

    public Button getButtonHelp() {
        return buttonHelp;
    }

    public Button getButtonEncrypt() {
        return buttonEncrypt;
    }

    public Button getButtonSettings() {
        return buttonSettings;
    }

    public Button getButtonDecrypt() {
        return buttonDecrypt;
    }

    public ToggleGroup getWhitespaceToggleGroup() {
        return whitespaceToggleGroup;
    }

    public ToggleGroup getPunctuationToggleGroup() {
        return punctuationToggleGroup;
    }

    public ToggleGroup getCaseToggleGroup() {
        return caseToggleGroup;
    }

    public CheckBox getAlphaNumericCheckBox() {
        return alphaNumericCheckBox;
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
        Menu menuFile = new Menu("_File");
        Menu menuOptions = new Menu("_Options");
        Menu menuView = new Menu("_View");
        Menu menuHelp = new Menu("_Help");

        // sub-menus
        itemOpen = new MenuItem("Open");
        itemSave = new MenuItem("Save");
        itemClear = new MenuItem("Clear");
        menuFile.getItems().addAll(itemOpen, itemSave, itemClear);
        itemResize = new MenuItem("Resize");
        itemCompare = new MenuItem("Compare");
        menuView.getItems().addAll(itemResize, itemCompare);
        itemAbout = new MenuItem("About");
        menuHelp.getItems().addAll(itemAbout);

        // add shortcuts
        itemOpen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        itemSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        itemClear.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));

        menuBar.getMenus().addAll(menuFile, menuView, menuOptions, menuHelp);
        return menuBar;
    }

    /**
     * Method creates a TabPane including three tabs, each with a text area, for the UI.
     *
     * @return TabPane node
     */
    private TabPane createTabPane() {
        // create TabPane
        TabPane tabPane = new TabPane();
        Tab mainTab = new Tab("Original");
        mainTab.setClosable(false);
        this.cipherTab = new Tab("Cipher Text");
        cipherTab.setClosable(false);
        this.planeTab = new Tab("Plane Text");
        planeTab.setClosable(false);
        tabPane.getTabs().addAll(mainTab, cipherTab, planeTab);

        // create TextArea for original tab
        this.originalTextArea = new TextArea();
        originalTextArea.setPromptText("Enter text, open file or drag&drop...");
        originalTextArea.setPrefHeight(400);
        originalTextArea.setWrapText(true);
        mainTab.setContent(originalTextArea);

        // create TextArea for cipher tab
        this.cipherTextArea = new TextArea();
        cipherTextArea.setWrapText(true);
        cipherTab.setContent(cipherTextArea);

        // create TextArea for plane tab
        this.planeTextArea = new TextArea();
        planeTextArea.setWrapText(true);
        planeTab.setContent(planeTextArea);

        return tabPane;
    }

    /**
     * Method creates a HBox for the encryption Label and Buttons.
     *
     * @return HBox node
     */
    private HBox createEncryptHBox() {
        final Label encryptLabel = new Label("Encrypt:");
        buttonEncrypt = createIconButton("Media/Button-Lock-Icon.png");
        buttonEncrypt.setTooltip(new Tooltip("Encrypt plane text to cipher text"));
        buttonSettings = createIconButton("Media/Button-Settings-Icon.png");
        buttonSettings.setTooltip(new Tooltip("Settings"));
        buttonHelp = createIconButton("Media/Button-Info-Icon.png");
        buttonHelp.setTooltip(new Tooltip("Info"));
        Region emptyRegion = new Region();
        HBox encryptHBox = new HBox(encryptLabel, emptyRegion, buttonEncrypt, buttonSettings, buttonHelp);
        HBox.setHgrow(emptyRegion, Priority.ALWAYS);
        return encryptHBox;
    }

    /**
     * Method creates a HBox for the decryption Label and Buttons.
     *
     * @return HBox node
     */
    private HBox createDecryptHBox() {
        final Label decryptLabel = new Label("Decrypt:");
        buttonDecrypt = createIconButton("Media/Button-Key-Icon.png");
        buttonDecrypt.setTooltip(new Tooltip("Decrypt cipher text"));
        Region emptyRegion = new Region();
        HBox decryptHBox = new HBox(decryptLabel, emptyRegion, buttonDecrypt);
        HBox.setHgrow(emptyRegion, Priority.ALWAYS);
        return decryptHBox;
    }

    /**
     * Method creates a FlowPane with the UI controls for encryption.
     *
     * @return FlowPane node
     */
    private FlowPane createEncryptPane() {
        // create FlowPane as container for the UI control elements
        FlowPane encryptPane = new FlowPane();

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
        keyTextField.setTooltip(new Tooltip("Only latin letters allowed"));
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
     * Method creates a Button with an icon from a png file.
     *
     * @param resourceName name of the desired resource (See java.io.InputStream.getResourceAsStream)
     *
     * @return Button node
     */
    private Button createIconButton(String resourceName) {
        Image image = new Image(getClass().getResourceAsStream(resourceName));
        Button button = new Button();
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(24);
        imageView.setFitWidth(24);
        button.setGraphic(imageView);
        return button;
    }

    /**
     * Method initializes a stage with information about cipher methods.
     * The stage will be displayed when the user presses the "Help Button".
     */
    private void initHelpStage() {
        // provide basic information text
        Text helpText = new Text(Cryptography.getInformationText());
        helpText.setWrappingWidth(300);

        VBox helpVBox = new VBox(new Label("Cipher Methods") ,helpText);
        helpVBox.setSpacing(15);
        helpVBox.setPadding(new Insets(10));

        // provide hyperlinks to wikipedia
        final Hyperlink hyperlinkCaesar = new Hyperlink("Wikipedia: Caesar Cipher");
        hyperlinkCaesar.setOnAction(t ->
                this.getHostServices().showDocument("https://en.wikipedia.org/wiki/Caesar_cipher")
        );
        final Hyperlink hyperlinkVigenere = new Hyperlink("Wikipedia: Vigenère Cipher");
        hyperlinkCaesar.setOnAction(t ->
                this.getHostServices().showDocument("https://en.wikipedia.org/wiki/Vigenère_cipher")
        );

        VBox linkVBox = new VBox(new Label("See"), hyperlinkCaesar, hyperlinkVigenere);
        linkVBox.setSpacing(10);
        linkVBox.setPadding(new Insets(10));
        linkVBox.setStyle("-fx-background-color:LIGHTGREY;");

        BorderPane root = new BorderPane();
        root.setCenter(helpVBox);
        root.setRight(linkVBox);

        // show in an new window
        this.helpStage = new Stage();
        helpStage.setTitle("Help");
        helpStage.setScene(new Scene(root, 550, 400));
        helpStage.setResizable(false);
    }

    /**
     * Method initializes a stage with options for encryption.
     * The stage will be displayed when the user presses the "Settings Button".
     */
    private void initSettingsStage() {
        GridPane gridPane = new GridPane();

        // create a tree view for character handling settings
        TreeItem characterHandlingRoot = new TreeItem("Character Handling");
        ImageView charIcon = new ImageView(
                new Image(getClass().getResourceAsStream("Media/Option-Character-Icon.png"))
        );
        charIcon.setFitWidth(22);
        charIcon.setFitHeight(22);
        characterHandlingRoot.setGraphic(charIcon);
        characterHandlingRoot.setExpanded(true);
        TreeView<String> characterTree = new TreeView(characterHandlingRoot);
        gridPane.add(characterTree, 0,0);

        // create mutually exclusive radio buttons for "whitespace handling"
        this.whitespaceToggleGroup = new ToggleGroup();
        RadioButton rbIgnoreWhitespace = new RadioButton("ignore whitespace");
        rbIgnoreWhitespace.setUserData("ignore");
        rbIgnoreWhitespace.setToggleGroup(whitespaceToggleGroup);
        rbIgnoreWhitespace.setSelected(true);
        RadioButton rbRemoveWhitespace = new RadioButton("remove whitespace");
        rbRemoveWhitespace.setUserData("remove");
        rbRemoveWhitespace.setToggleGroup(whitespaceToggleGroup);
        TreeItem whitespaceItem = new TreeItem("Whitespace");
        whitespaceItem.getChildren().addAll(new TreeItem<>(rbIgnoreWhitespace), new TreeItem<>(rbRemoveWhitespace));
        characterHandlingRoot.getChildren().add(whitespaceItem);

        // create mutually exclusive radio buttons for "punctuation character handling"
        this.punctuationToggleGroup = new ToggleGroup();
        RadioButton rbIgnorePunctuation = new RadioButton("ignore punctuation");
        rbIgnorePunctuation.setUserData("ignore");
        rbIgnorePunctuation.setToggleGroup(punctuationToggleGroup);
        rbIgnorePunctuation.setSelected(true);
        RadioButton rbRemovePunctuation = new RadioButton("remove punctuation");
        rbRemovePunctuation.setUserData("remove");
        rbRemovePunctuation.setToggleGroup(punctuationToggleGroup);
        TreeItem punctuationItem = new TreeItem("Punctuation");
        punctuationItem.getChildren().addAll(new TreeItem<>(rbIgnorePunctuation), new TreeItem<>(rbRemovePunctuation));
        characterHandlingRoot.getChildren().add(punctuationItem);

        // create mutually exclusive radio buttons for "case sensitivity"
        this.caseToggleGroup = new ToggleGroup();
        RadioButton rbKeepCase = new RadioButton("keep case");
        rbKeepCase.setUserData("keep");
        rbKeepCase.setToggleGroup(caseToggleGroup);
        RadioButton rbConvertUpperCase = new RadioButton("convert to uppercase");
        rbConvertUpperCase.setUserData("convert");
        rbConvertUpperCase.setToggleGroup(caseToggleGroup);
        rbConvertUpperCase.setSelected(true);
        TreeItem CaseItem = new TreeItem("Case shift");
        CaseItem.getChildren().addAll(new TreeItem<>(rbKeepCase), new TreeItem<>(rbConvertUpperCase));
        characterHandlingRoot.getChildren().add(CaseItem);

        this.alphaNumericCheckBox = new CheckBox("only alpha-numeric chars");
        alphaNumericCheckBox.setTooltip(new Tooltip("removes all non alpha-numeric characters before encryption"));
        characterHandlingRoot.getChildren().add(new TreeItem<>(alphaNumericCheckBox));

        // show in a new window
        this.settingsStage = new Stage();
        settingsStage.setTitle("Settings");
        settingsStage.setScene(new Scene(gridPane));
        settingsStage.setResizable(false);
    }


    /**
     * Main method for starting and initializing the UI.
     */
    @Override
    public void start(Stage primaryStage) {
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
        buttonAnalyse = new Button("_Analyse Text");
        buttonAnalyse.setMaxWidth(Double.MAX_VALUE);
        leftVBox.getChildren().add(buttonAnalyse);


        /* CENTER section */

        GridPane centerGrid = new GridPane();
        centerGrid.setVgap(8);
        centerGrid.setHgap(8);
        root.setCenter(centerGrid);

        // add TabPane with three tabs and TextArea
        TabPane tabPane = createTabPane();
        centerGrid.add(tabPane,0,0,3,1);

        // add Separators
        Separator hSeparator = new Separator();
        centerGrid.add(hSeparator, 0,1,3,1);
        Separator vSeparator = new Separator(Orientation.VERTICAL);
        centerGrid.add(vSeparator,1,2,1,2);

        // add control areas for "Encryption" and "Decryption"
        centerGrid.add(createEncryptHBox(),0,2,1,1);
        centerGrid.add(createEncryptPane(),0,3,1,1);

        centerGrid.add(createDecryptHBox(),2,2,1,1);
        centerGrid.add(createDecryptPane(),2,3,1,1);

        // initialize other stages (will be shown, when the user interacts with the corresponding UI element)
        initHelpStage();
        initSettingsStage();

        // set main scene
        Scene mainScene = new Scene(root, 800, 650);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Text Analyse Tool");
        primaryStage.show();

        // initialize controller
        new TextAnalyserController(this);
    }
}