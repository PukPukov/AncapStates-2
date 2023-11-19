package ru.ancap.library;

public class StringAPI {

    public static boolean isHex(String arg) {
        if (arg == null || arg.isEmpty()) {
            return false;
        }

        // Проверяем, что каждый символ является шестнадцатеричной цифрой
        for (int i = 0; i < arg.length(); i++) {
            char c = arg.charAt(i);
            if (!isHexDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isHexDigit(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F');
    }
    
}
