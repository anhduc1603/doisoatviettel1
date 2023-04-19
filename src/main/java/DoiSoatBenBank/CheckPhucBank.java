package DoiSoatBenBank;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CheckPhucBank {

    private static int rangerDate = 3;
    private static String datetimeFormatPattern = "yyyy-MM-dd";

    private static String toDate = "2023-04-17";

    private static String linkFileLvbis = "C:\\Users\\saotr\\Downloads\\Viettel\\LVBIT\\";

    private static String linkFileViettel = "C:\\Users\\saotr\\Downloads\\Viettel\\Viettel\\";

    private static String linkFileLvt = "";

    private static  void checkLvt() throws IOException {
        //read file Phúc gửi, xử lý từng dòng
        //Lấy PAYMENT_REF_NO(TRANSACTION_ID), check trong Map{TRANSACTION_ID,TrạngThái} có không, có thì trả về trạng thái, không thì trả về Empty
        //Ghi file kết quả



        List<LvbEntity> listLvbis = getDataLvBis();
        List<ViettelEntity> listViettel = getDataViettels();

        Map<String,BankEntity> mapLVT = getDataLvt();

        String tranId = "230413030007000156";
//        String tranId = "230415030007000025";

        String status = compareLvbWithVietetl(listLvbis,listViettel,tranId);
        if(status!=null){
            mapLVT.get(tranId)
        }
    }

    private static List<ViettelEntity> getDataViettels() {// Map{orderId, TrạngThái}
        //load tất cả file viettel theo rangerDate

        String formatFix = ".csv";
        List<ViettelEntity> listViettel = new ArrayList<>();
        try {
            File[] fileArray = getListFile(toDate, rangerDate, linkFileViettel, formatFix);
            for (File fCntt : fileArray) {
                List<String> lines3 = Files.readAllLines(Paths.get(fCntt.getPath()));
                for (String line : lines3) {
                    String[] sps = line.split(",");
                    String requestId = sps[0];
                    if(!"REQUEST_ID".equals(requestId)){
                        String orderId = sps[1];
                        String status = sps[7];
                        ViettelEntity viettelEntity = new ViettelEntity(requestId,orderId,status);
                        listViettel.add(viettelEntity);
                    }
                }
            }
        }catch (Exception e){
        }
        return listViettel;

    }

    public static Map<String,BankEntity> getDataLvt() throws IOException {
        Map<String,BankEntity> mapBankCheck = new HashMap<>();
        //Đọc file của bank cần check
        List<String> lines4 = Files.readAllLines(Paths.get(linkFileLvt));
        for (String line : lines4) {
            String[] sps = line.split("\t");
                String transactionId = sps[8];
                BankEntity bankEntity = new BankEntity();
                bankEntity.setTRANS_ID(sps[0]);
                bankEntity.setUSER_NAME(sps[1]);
                bankEntity.setFULL_NAME(sps[2]);
                bankEntity.setTRANS_TIME(sps[3]);
                bankEntity.setTRANS_NAME(sps[4]);
                bankEntity.setINPUT_VALUE(sps[5]);
                bankEntity.setAMOUNT_TRANSFER(sps[6]);
                bankEntity.setINPUT_AMOUNT(sps[7]);
                bankEntity.setPAYMENT_REF_NO(sps[8]);
                bankEntity.setPARTNER_CODE(sps[9]);
                bankEntity.setFROM_SYSTEM_ID(sps[10]);
                mapBankCheck.put(transactionId, bankEntity);
            }

        return mapBankCheck;

    }

    private static List<LvbEntity> getDataLvBis() {// Map{TRANSACTION_ID, ORDER_ID_REQ}
        //load tất cả file lvbis theo rangerDate
        //File của LVBIS
        String formatFix = ".txt";
        List<LvbEntity> listMapLvbis = new ArrayList<>();
        try {
            File[] fileArray = getListFile(toDate, rangerDate, linkFileLvbis, formatFix);
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

    private static String compareLvbWithVietetl( List<LvbEntity> listLvbis, List<ViettelEntity> listViettel ,String trandstion_id) {
        String status = null;
        try{

            String orderId = listLvbis.stream()
                    .filter(item -> item.getTRANSACTION_ID() != null&&item.getTRANSACTION_ID().equals(trandstion_id))
                    .findAny().get().getORDER_ID_REQ();

                        if(orderId!=null){
                            try {
                                 for (ViettelEntity item:listViettel){
                                     if(item.getORDER_ID()!=null&&orderId.equals(item.getORDER_ID())){
                                          status = item.getTRANG_THAI();
                                          break;
                                     }
                                 }
                                 if(status==null){
                                     status = "Thất bại";
                                 }
                            }catch (Exception e){
                                System.out.println("Lỗi phát sinh khi tìm trong file viettel");
                            }
                        }

        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println("Lỗi chưa xác định");
        }
        return status;
    }

    private static File[] getListFile(String toDate, int rangerDate, String linkFile, String formatFile) {
        File[] fileArray = new File[rangerDate];
        try {

            for (int i = 0; i < rangerDate; i++) {
                StringBuilder link = new StringBuilder(linkFile);
                link.append(dateCompare(toDate, i)).append(formatFile);
                fileArray[i] = new File(String.valueOf(link));
            }
            return fileArray;
        } catch (Exception e) {
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
        String tranId = "230413030007000156";
//        String tranId = "230415030007000025";
        String status = checkLvt(tranId);

        }
    }

