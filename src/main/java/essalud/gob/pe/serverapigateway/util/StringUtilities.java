package essalud.gob.pe.serverapigateway.util;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtilities {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static String getRandomNumber(Integer digits) {
        Random rand = new Random();
        return String.format("%0" + digits + "d", rand.nextInt(10000));
    }

    public static String capitalize(final String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static boolean isNullOrEmpty(String text) {
        return (text == null || text.trim().isEmpty());
    }

    public static boolean hasLength(String text, int length) {
        if (isNullOrEmpty(text)) return false;
        return text.length() == length;
    }

    public static boolean hasLengthBetween(String text, int minLength, int maxLength) {
        if (isNullOrEmpty(text)) return false;
        return text.length() >= minLength && text.length() <= maxLength;
    }

    public static boolean hasLengthGreaterOrEqualThan(String text, int minLength) {
        if (isNullOrEmpty(text)) return false;
        return text.length() >= minLength;
    }

    public static boolean hasLengthLessOrEqualThan(String text, int maxLength) {
        if (isNullOrEmpty(text)) return false;
        return text.length() <= maxLength;
    }

    public static boolean equalsIgnoreSpecial(String c1, String c2) {
        boolean result = false;
        if (c1 != null && c2 != null) {
            c1 = replaceSpecialCharacters(c1.toUpperCase());
            c2 = replaceSpecialCharacters(c2.toUpperCase());
            result = c1.equals(c2);
        }
        return result;
    }

    public static boolean containsIgnoreSpecial(String c1, String c2) {
        boolean result = false;
        if (c1 != null && c2 != null) {
            c1 = replaceSpecialCharacters(c1.toUpperCase());
            c2 = replaceSpecialCharacters(c2.toUpperCase());
            result = c1.contains(c2);
        }
        return result;
    }

    private static String replaceSpecialCharacters(String original) {
        String cadenaNormalize = Normalizer.normalize(original, Normalizer.Form.NFD);
        return cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
    }

    public static String leftPad(String padString, int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += padString;
        }
        return result;
    }

    public static String leftPadDiff(String text, String padString, int maxLength) {
        int textLength = text.length();
        int diffLength = (maxLength - textLength);

        StringBuilder textToConcat = new StringBuilder();
        for (int i = 0; i < diffLength; i++) {
            textToConcat.append(padString);
        }

        return textToConcat.toString().concat(text);
    }

    public static String reverse(String str) {
        if (isNullOrEmpty(str)) return null;
        return new StringBuffer(str).reverse().toString();
    }

    public static boolean isStringInMatcher(String strFind, String strDataPattern) {
        Pattern p = Pattern.compile(strDataPattern);
        Matcher m = p.matcher(strFind);
        boolean isFound = m.find();
        return isFound;
    }

    public static String getStringWithMaxLength(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        if (maxLength < 0) {
            throw new IllegalArgumentException("maxLength debe ser un valor no negativo");
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}