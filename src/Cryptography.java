import java.util.HashMap;
import java.util.Map;

/**
 * Provides cryptographically functions, like frequency-analysis and shift cipher
 * for the Text Analyser Application.
 */
public class Cryptography {


    /**
     * Static method for frequency-analysis of an given text.
     * To ignore cases, use the toLowerCase() or toUpperCase() methods before calling this method.
     *
     * @param text String to be analysed
     *
     * @return HashMap<Character, Integer> (key->letter, value->frequency)
     */
    public static Map<Character, Integer> frequencyAnalysis(final String text) {
        Map<Character, Integer> result = new HashMap<>();
        char[] charArray = text.toCharArray();
        for (char c : charArray) {
            if (result.containsKey(c)) {
                int frequency = result.get(c);
                result.replace(c, ++frequency);
            } else {
                result.put(c, 1);
            }
        }
        return result;
    }
}
