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

    /**
     * Static method provides an information text about Cipher Methods for the user.
     *
     * @return Information text.
     */
    public static String getInformationText() {
        String info =
                "Shift Cipher:\n" +
                        "(Also known as Caesar's cipher) The shift cipher is one of the simplest and most widely known encryption techniques. It is a type of substitution cipher in which each letter in the plaintext is replaced by a letter some fixed number of positions down the alphabet.\n\n" +
                "Polialphabetic Cipher:\n" +
                        "(Also known as Vigenère cipher) A method of encrypting alphabetic text by using a series of interwoven Caesar ciphers, based on the letters of a keyword. It is a form of polyalphabetic substitution.";

        return info;
    }
}
