import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestDoiSoatCNTT {
    public static void main(String[] args) throws IOException {
        double a = 3045154545254l;
        System.out.println(formatNumber(a));

    }

    public static String formatNumber(double value) {
        DecimalFormat df2 = new DecimalFormat("###,###,###,###,###.###############");
        String valueFormat = df2.format(value);
        return valueFormat;
    }
}
