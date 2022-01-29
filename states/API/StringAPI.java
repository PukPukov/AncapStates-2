package states.API;

public class StringAPI {

    public static boolean isHex(String str) {
        try {
            Long.parseLong(str, 16);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
