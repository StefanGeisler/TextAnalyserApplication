import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class provides the data model for the TableView of the Text Analyser Application.
 */
public class FrequencyModel {
    private final SimpleStringProperty letter;
    private final SimpleIntegerProperty frequency;


    public FrequencyModel(Character c, Integer frequency) {
        this.letter = new SimpleStringProperty(c.toString());
        this.frequency = new SimpleIntegerProperty(frequency);
    }

    public String getLetter() {
        return letter.get();
    }

    public SimpleStringProperty letterProperty() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter.set(letter);
    }

    public int getFrequency() {
        return frequency.get();
    }

    public SimpleIntegerProperty frequencyProperty() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency.set(frequency);
    }
}
