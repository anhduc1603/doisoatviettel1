import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {


//        link.append("text1.txt");
//
//        StringBuilder link2 = new StringBuilder("D:\\DucHA\\LVBIT\\");
//        link2.append("text2.txt");

        String toDate = "2023-04-19";

//        File[] files = {new File(String.valueOf(link)), new File(String.valueOf(link2))};
        File[] fileArray = new File[3];
        try {

            for (int i = 0; i < 3; i++) {
                StringBuilder link = new StringBuilder("D:\\DucHA\\LVBIT\\");
                link.append(dateCompare(toDate,i)).append(".txt");
                fileArray[i] = new File(String.valueOf(link));
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Lỗi không thể mở list file Lvbis");
        }

        System.out.println();
        // Fetching all the files
        for (File file : fileArray) {
            if (file.isFile()) {
                BufferedReader inputStream = null;
                String line;
                try {
                    inputStream = new BufferedReader(new FileReader(file));
                    while ((line = inputStream.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            }
        }


    }

    public static String dateCompare(String toDate, int rangerDate) throws ParseException {
        String datetimeFormatPattern = "yyyy-MM-dd";
        Date date = new SimpleDateFormat(datetimeFormatPattern).parse(toDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, - rangerDate); //minus number would decrement the days
        cal.getTime();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return dateString;
    }


}

