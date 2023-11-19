package ru.ancap.states.message;

public class PrecisionFormatter {

    private static final int POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000};

    public static String format(double val, int precision) {
        StringBuilder sb = new StringBuilder();
        if (val < 0) {
            sb.append('-');
            val = -val;
        }
        int exp = POW10[precision];
        long lval = (long)(val * exp + 0.5);
        sb.append(lval / exp).append('.');
        long fval = lval % exp;
        for (int p = precision - 1; p > 0 && fval < POW10[p]; p--) {
            sb.append('0');
        }
        sb.append(fval);
        return sb.toString();
    }

}
