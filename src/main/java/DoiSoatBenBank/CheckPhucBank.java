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

    private static String linkFileLvbis = "D:\\DucHA\\LVBIT\\";

    private static String linkFileViettel = "D:\\DucHA\\ViettelFile\\";

    private static String linkFileLvt = "D:\\DucHA\\LV24H\\new 2.tsv";

    private static  void checkLvt() throws IOException {

        List<LvbEntity> listLvbis = getDataLvBis(); //Get file Lvbis
        List<ViettelEntity> listViettel = getDataViettels(); //Get file viettel
        Map<String,BankEntity> mapLVT = getDataLvt();  //Get File LV24h;

        System.out.println("TRANS_ID" + "\t" + "USER_NAME" + "\t" + "FULL_NAME" + "\t" + "TRANS_TIME"
                + "\t" + "TRANS_NAME" + "\t" + "INPUT_VALUE" + "\t" + "AMOUNT_TRANSFER" + "\t" + "INPUT_AMOUNT"
                + "\t" + "PAYMENT_REF_NO" + "\t" + "PARTNER_CODE" + "\t" + "FROM_SYSTEM_ID"  + "\t" + "STATUS");

        Set<String> lvtTransIds = mapLVT.keySet();
        for (String item:lvtTransIds) {
            String status = compareLvbWithVietetl(listLvbis,listViettel,item);
            if(status==null){
                BankEntity bankEntity = mapLVT.get(item);
                System.out.println(
                        bankEntity.getTRANS_ID() + "\t"
                                + bankEntity.getUSER_NAME() + "\t"
                                + bankEntity.getFULL_NAME() + "\t"
                                + bankEntity.getTRANS_TIME() + "\t"
                                + bankEntity.getTRANS_NAME() + "\t"
                                + bankEntity.getINPUT_VALUE() + "\t"
                                + bankEntity.getAMOUNT_TRANSFER()+ "\t"
                                + bankEntity.getINPUT_AMOUNT() + "\t"
                                + bankEntity.getPAYMENT_REF_NO() + "\t"
                                + bankEntity.getPARTNER_CODE() + "\t"
                                + bankEntity.getFROM_SYSTEM_ID()+ "\t"
                                + "Thất bại"
                );
            }else {
                //Có bên file viettel
                BankEntity bankEntity = mapLVT.get(item);
                System.out.println(
                        bankEntity.getTRANS_ID() + "\t"
                                + bankEntity.getUSER_NAME() + "\t"
                                + bankEntity.getFULL_NAME() + "\t"
                                + bankEntity.getTRANS_TIME() + "\t"
                                + bankEntity.getTRANS_NAME() + "\t"
                                + bankEntity.getINPUT_VALUE() + "\t"
                                + bankEntity.getAMOUNT_TRANSFER()+ "\t"
                                + bankEntity.getINPUT_AMOUNT() + "\t"
                                + bankEntity.getPAYMENT_REF_NO() + "\t"
                                + bankEntity.getPARTNER_CODE() + "\t"
                                + bankEntity.getFROM_SYSTEM_ID()+ "\t"
                                + status
                );
            }
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

    public static HashMap<String,BankEntity> getDataLvt() throws IOException {
      try {
          HashMap<String,BankEntity> mapBankCheck = new HashMap<>();
          //Đọc file của bank cần check
          List<String> lines4 = Files.readAllLines(Paths.get(linkFileLvt));
          for (String line : lines4) {
              String[] sps = line.split("\t");
              String transactionId = sps[8];
              if(!"PAYMENT_REF_NO".equals(transactionId)){
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

          }
          return mapBankCheck;
      }catch (NullPointerException e){

      }catch (Exception e){
          System.out.println("Lỗi đọc file LV24H");
      }
      return null;

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
        } catch (Exception e){

        }
        return listMapLvbis;
    }

    private static String compareLvbWithVietetl( List<LvbEntity> listLvbis, List<ViettelEntity> listViettel ,String trandstion_id) {
        String status = null;
        try{

//            String orderId = listLvbis.stream()
//                    .filter(item -> item.getTRANSACTION_ID() != null&&item.getTRANSACTION_ID().equals(trandstion_id))
//                    .findAny().get().getORDER_ID_REQ();

            for (LvbEntity item:listLvbis) {
                if(item.getTRANSACTION_ID()!=null&&item.getTRANSACTION_ID().equals(trandstion_id)){
                    String orderId = item.getORDER_ID_REQ();
                    if(orderId!=null){
                        try {
                            for (ViettelEntity item1:listViettel){
                                if(item1.getORDER_ID()!=null&&orderId.equals(item1.getORDER_ID())){
                                    status = item1.getTRANG_THAI();
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


    public static void main(String[] args) throws IOException {
        String tranId = "230413030007000156";
//        String tranId = "230415030007000025";
//        String status = checkLvt(tranId);
          checkLvt();
        }
    }

