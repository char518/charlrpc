
package piggy.common;

public class StringUtils {

    public static String[] splitString(String str, String word) {
        return org.apache.commons.lang3.StringUtils.splitByWholeSeparator(str, word);
    }

    public static boolean isEmpty(String str) {
        return org.apache.commons.lang3.StringUtils.isEmpty(str.trim());
    }

}
