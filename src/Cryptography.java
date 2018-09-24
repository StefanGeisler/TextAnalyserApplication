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
     * Static method for shift cipher (caesar's cipher) decryption of a given plane text.
     * The method works by adding an (cyclic) offset to the ascii code point. Non alphabetic characters
     * and non latin alphabet characters will be ignored.
     *
     * @param shift The amount, each letter will be shifted to the right in the alphabet.
     *              e.g. shift 3 will replace A -> D
     * @param planeText The text which will be encrypted.
     * @return The encrypted cipher text.
     */
    public static String shiftCipher(final int shift, final String planeText) {
        StringBuilder cipherText = new StringBuilder();
        planeText.chars()
                .map(i -> {
                    if (i >= 65 && i <= 90) { // A to Z
                        i += shift;
                        if (i > 90) {
                            return i % 91 + 65;  // cyclic shift
                        } else {
                            return i;
                        }
                    } else if (i >= 97 && i <= 122) { // a to z
                        i += shift;
                        if (i > 122) {
                            return i % 123 + 97;  // cyclic shift
                        } else {
                            return i;
                        }
                    } else {
                        return i;
                    }
                })
                .forEach(cipherText::appendCodePoint);

        return cipherText.toString();
    }

    /**
     * Static method for polyalphabetic substitution cipher (Vigenère cipher) encryption of
     * a given plane text. Non alphabetic characters and non latin alphabet characters
     * in the plane text will be ignored.
     *
     * @param keyword The keyword for the substitution. Has to be uppercase and it is only allowed
     *                to contain letters of the latin alphabet (ASCII A - Z).
     * @param planeText The text which will be encrypted.
     * @return The encrypted cipher text.
     */
    public static String polyalphabeticCipher(final String keyword, final String planeText) throws IllegalArgumentException {
        if (keyword == null || keyword.length() == 0) {
            throw new IllegalArgumentException("Non valid keyword");
        }
        // calculate offset of each keyword letter to ASCII code point A (65)
        int[]offsets = new int[keyword.length()];
        for(int i = 0; i < keyword.length(); i++) {
            offsets[i] = keyword.codePointAt(i) - 65;
        }

        StringBuilder cipherText = new StringBuilder();
        int i = 0;  // planeText index
        int j = 0;  // offsets index
        while (i < planeText.length()) {
            int codePoint = planeText.codePointAt(i);

            if (codePoint >= 65 && codePoint <= 90) { // A to Z
                codePoint += offsets[j];
                if (codePoint > 90) {
                    codePoint = codePoint % 91 + 65;  // cyclic shift
                }
                j++;
            } else if (codePoint >= 97 && codePoint <= 122) { // a to z
                codePoint += offsets[j];
                if (codePoint > 122) {
                    codePoint = codePoint % 123 + 97;  // cyclic shift
                }
                j++;
            }
            cipherText.appendCodePoint(codePoint);
            i++;
            if (j >= offsets.length) {
                j = 0;
            }
        }
        return cipherText.toString();
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
