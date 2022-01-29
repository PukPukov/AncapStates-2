package states.API;

public class SMassiveAPI {
    public static String add(String string, String string1) {
        if (string == null || string.equals("")) {
            return string1;
        }
        return string+", "+string1;
    }
    public static String remove(String string, String string1) {
        if (string == null || string.equals("")) {
            return "";
        }
        if (string1 == null || string1.equals("")) {
            return string;
        }
        string = string.replace(", "+string1, "");
        string = string.replace(string1+", ", "");
        string = string.replace(string1, "");
        return string;
    }
    public static boolean contain(String string, String containable) {
        if (string == null || string.equals("")) {
            return false;
        }
        if (string.contains(containable)) {
            return true;
        }
        return false;
    }
    public static String[] toMassive(String string) {
        if (string == null || string.equals("")) {
            String[] massive = new String[0];
            return massive;
        }
        if (string.startsWith(", ")) {
            string = string.substring(2);
        }
        return string.split(", ");
    }
    public static String toString(String[] StringMassive) {
        String string = "";
        for (String added : StringMassive) {
            string = string+", "+added;
        }
        return string;
    }

    public static String toString(String[] StringMassive, String separator) {
        String string = "";
        for (String added : StringMassive) {
            string = string+separator+added;
        }
        return string;
    }
}
