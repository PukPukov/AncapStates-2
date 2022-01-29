package states.Nation;

public class NationColor {

    private String color;

    public static final NationColor WHITE = new NationColor("fafafa");

    public static final NationColor LIGHT_RED = new NationColor("ff3838");

    public static final NationColor RED = new NationColor("d41515");

    public static final NationColor DARK_RED = new NationColor("8a0404");

    public static final NationColor LIGHT_BLUE = new NationColor("1047b5");

    public static final NationColor BLUE = new NationColor("003eba");

    public static final NationColor DARK_BLUE = new NationColor("002369");

    public static final NationColor LIGHT_ACID_YELLOW = new NationColor("eaff00");

    public static final NationColor ACID_YELLOW = new NationColor("c4d41e");

    public static final NationColor LIGHT_YELLOW = new NationColor("ebd407");

    public static final NationColor YELLOW = new NationColor("dbc500");

    public static final NationColor LIGHT_ORANGE = new NationColor("f2a50a");

    public static final NationColor ORANGE = new NationColor("db8400");

    public static final NationColor KHAKI = new NationColor("ad6800");

    public static final NationColor GOLUBOI = new NationColor("00decf");

    public static final NationColor LIGHT_ACID_PURPLE = new NationColor("ea00ff");

    public static final NationColor ACID_PURPLE = new NationColor("d500e8");

    public static final NationColor PURPLE = new NationColor("a52eb0");

    public static final NationColor DARK_PURPLE = new NationColor("56005e");

    public static final NationColor EBAT_KAKOI_KRASIVI = new NationColor("4e00f7");

    public static final NationColor LIGHT_ACID_GREEN = new NationColor("00ff09");

    public static final NationColor ACID_GREEN = new NationColor("00ed08");

    public static final NationColor GREEN = new NationColor("00b306");

    public static final NationColor PASTEL_GREEN = new NationColor("43ab46");

    public static final NationColor DARK_GREEN = new NationColor("006303");

    public static final NationColor GOVNO = new NationColor("69390d");

    public static final NationColor PONOS = new NationColor("66220d");

    public static final NationColor HUI_ZNAET_KAK_NAZIVAETSYA_NO_KRASIVYI = new NationColor("ff7c54");

    public NationColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }
}
