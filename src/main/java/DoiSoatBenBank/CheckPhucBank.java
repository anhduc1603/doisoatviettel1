package DoiSoatBenBank;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CheckPhucBank {

    private static int rangerDate = 3;
    private static String datetimeFormatPattern = "yyyy-MM-dd";

    private static String toDate = "2023-04-17";

    private static String linkFileLvbis = "D:\\DucHA\\LVBIT\\";

    private void checkLvt() {
        //read file Phúc gửi, xử lý từng dòng
        //Lấy PAYMENT_REF_NO(TRANSACTION_ID), check trong Map{TRANSACTION_ID,TrạngThái} có không, có thì trả về trạng thái, không thì trả về Empty
        //Ghi file kết quả
    }

    private Map<String, String> getDataViettels() {// Map{orderId, TrạngThái}
        //load tất cả file viettel theo rangerDate
        return null;
    }

    private static List<LvbEntity> getDataLvBis() {// Map{TRANSACTION_ID, ORDER_ID_REQ}
        //load tất cả file lvbis theo rangerDate
        //File của LVBIS
        List<LvbEntity> listMapLvbis = new ArrayList<>();
        try {
            File[] fileArray = getListFile(toDate,rangerDate,linkFileLvbis);
            for (File fCntt : fileArray) {
                List<String> lines3 = Files.readAllLines(Paths.get(fCntt.getPath()));
                for (String line : lines3) {
                    String[] sps = line.split("\t");
                    String transDt = sps[0];

                    String transactionId = sps[1];
                        String orderIdReq = sps[3];
                        if (!"ORDER_ID_REQ".equals(orderIdReq)) {
                            String xx = orderIdReq.substring(9, orderIdReq.length() - 1);
                            LvbEntity lvbEntity = new LvbEntity(transDt,transactionId,xx);
                            listMapLvbis.add(lvbEntity);
                        }
                }
            }
        }catch (Exception e){

        }
        return listMapLvbis;
    }

    /*
     * // Map{TRANSACTION_ID,TrạngThái}
     * */
    private Map<String, String> joinDataMaps(Map<String, String> mapLvbis, Map<String, String> mapViettels) {
        return null;
    }

    private static File[] getListFile(String toDate, int rangerDate,String linkFile){
        File[] fileArray = new File[rangerDate];
        try {

            for (int i = 0; i < rangerDate; i++) {
                StringBuilder link = new StringBuilder(linkFile);
                link.append(dateCompare(toDate,i)).append(".txt");
                fileArray[i] = new File(String.valueOf(link));
            }
            return fileArray;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Lỗi không thể mở list file");
        }
        return null;
    }

    public static String dateCompare(String toDate, int rangerDate) throws ParseException {
        Date date = new SimpleDateFormat(datetimeFormatPattern).parse(toDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, - rangerDate); //minus number would decrement the days
        cal.getTime();
        String dateString = new SimpleDateFormat(datetimeFormatPattern).format(cal.getTime());
        return dateString;
    }


    public static void main(String[] args) {
        List<LvbEntity> listLvbis = getDataLvBis();
        String trandstion_id = "230418030007000106";

        String orderId = listLvbis.stream()
                .filter(item -> item.getTRANSACTION_ID() != null&&item.getTRANSACTION_ID().equals(trandstion_id))
                .findAny().get().getORDER_ID_REQ();
        System.out.println(orderId);

        }
    }

