import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestDoiSoatViettel {
    public static void main(String[] args) throws IOException {
        File baseDir = new File("D:\\DucHa\\Viettel");

        String date = "2022-09-28";
        Long countDate = countDay(date);

        Map<String, String> lvt_vtl = new HashMap<String, String>();
        File dirByDate = new File(baseDir, LocalDate.now().minusDays(countDate).toString());

//        String dateVT2 = LocalDate.now().minusDays(countDate).toString();

        String dateVT2 = "28-09-2022";

        Set<String> vtlTransIds = new HashSet<String>();

        File fViettel = null;
        File[] files = dirByDate.listFiles();
        for (File f : files) {
            if (f.getName().endsWith(".csv")) {
                fViettel = f;
            }
        }
        List<String> lines1 = Files.readAllLines(Paths.get(fViettel.getPath()));
		System.out.println(fViettel.getPath());

        for (String line : lines1) {
            String[] sps = line.split(",");
//			String[] sps = line.split("\t");
            String requestDate = sps[3];
            if (requestDate.contains(dateVT2)) {
                String orderId = sps[2];
                vtlTransIds.add(orderId);
            }
        }
        System.out.println(vtlTransIds);
    }
    public static Long countDay(String date){
        // Định dạng thời gian
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        // Định nghĩa 2 mốc thời gian ban đầu
        java.sql.Date date1 = java.sql.Date.valueOf(date);

        c1.setTime(date1);
        c2.setTime(new java.util.Date());

        // Công thức tính số ngày giữa 2 mốc thời gian:
        long noDay = (c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000);

        return noDay;
    }
}
