package DoiSoatBenBank;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CheckPhucBank {

    private static int rangerDate = 10;
    private static String datetimeFormatPattern = "yyyy-MM-dd";

    private static String toDate = "2023-05-04";

    private static String linkFileLvbis = "F:\\New folder\\LVBIT\\";

    private static String linkFileViettel = "F:\\New folder\\ViettelFile\\";

    private static String linkFileLvt = "F:\\New folder\\LV24H\\bank.tsv";

    private static  void checkLvt() throws IOException {

        List<LvbEntity> listLvbis = getDataLvBis(); //Get file Lvbis
        List<ViettelEntity> listViettel = getDataViettels(); //Get file viettel
        Map<String,BankEntity> mapLVT = getDataLvt();  //Get File LV24h;
        Map<Integer,BankEntity> result = new HashMap<>();

        System.out.println("TRANS_ID" + "\t" + "USER_NAME" + "\t" + "FULL_NAME" + "\t" + "TRANS_TIME"
                + "\t" + "TRANS_NAME" + "\t" + "INPUT_VALUE" + "\t" + "AMOUNT_TRANSFER" + "\t" + "INPUT_AMOUNT"
                + "\t" + "PAYMENT_REF_NO" + "\t" + "PARTNER_CODE" + "\t" + "FROM_SYSTEM_ID"  + "\t" + "STATUS");

        int count = 1;

        Set<String> lvtTransIds = mapLVT.keySet();
        for (String item:lvtTransIds) {
            String status = compareLvbWithVietetl(listLvbis,listViettel,item);
            if(status==null){
                BankEntity bankEntity = mapLVT.get(item);
                bankEntity.setSTATUS("Thất bại");
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
                result.put(count,bankEntity);
                count++;
            }else {
                //Có bên file viettel
                BankEntity bankEntity = mapLVT.get(item);
                bankEntity.setSTATUS(status);
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
                result.put(count,bankEntity);
                count++;
            }
        }
        result.get(1);
        try {
            writerFileExcel(result);
        }catch (Exception e){
            System.out.println("Lỗi writer file excel");
        }

    }

    private static List<ViettelEntity> getDataViettels() {// Map{orderId, TrạngThái}
        //load tất cả file viettel theo rangerDate

        String formatFix = ".csv";
        List<ViettelEntity> listViettel = new ArrayList<>();
        try {
//            File[] fileArray = getListFile(toDate, rangerDate, linkFileViettel, formatFix);
            File[] fileArray = getNameFileInFolder(linkFileViettel);
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

    private static List<LvbEntity> getDataLvBis() {
        // Map{TRANSACTION_ID, ORDER_ID_REQ}
        //load tất cả file lvbis theo rangerDate
        //File của LVBIS
        String formatFix = ".txt";
        List<LvbEntity> listMapLvbis = new ArrayList<>();
        try {
//            File[] fileArray = getListFile(toDate, rangerDate, linkFileLvbis, formatFix);
            File[] fileArray = getNameFileInFolder(linkFileLvbis);
            for (File fCntt : fileArray) {
                List<String> lines3 = Files.readAllLines(Paths.get(fCntt.getPath()));
                if(lines3.size()>0){
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
                }else {
                    continue;
                }
            }
        } catch (Exception e){
            System.out.println("Đọc file LVBIS lỗi");
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

    public static void writerFileExcel(Map<Integer,BankEntity> result) throws IOException {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();

        // spreadsheet object
        XSSFSheet spreadsheet = workbook.createSheet("Data");

        // creating a row object
        XSSFRow row;

        // This data needs to be written (Object[])
//        Map<String, Object[]> studentData = new TreeMap<String, Object[]>();

        result.put(1,new BankEntity("TRANS_ID","USER_NAME","FULL_NAME","TRANS_TIME","TRANS_NAME","INPUT_VALUE","AMOUNT_TRANSFER","INPUT_AMOUNT","PAYMENT_REF_NO","PARTNER_CODE","PARTNER_CODE","FROM_SYSTEM_ID","STATUS"));

//
//        studentData.put("2", new Object[]{"128", "Aditya",
//                "2nd year"});

        Set<Integer> keyid = result.keySet();

        int rowid = 0;

        // writing the data into the sheets...

        for (Integer key : keyid) {
            row = spreadsheet.createRow(rowid++);
            BankEntity bankEntity = result.get(key);

            for (int i = 0; i < 11; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(bankEntity.getINPUT_VALUE());
                cell.setCellValue(bankEntity.getSTATUS());
            }

//            for ( obj : objectArr) {
//                Cell cell = row.createCell(cellid++);
//                cell.setCellValue((String) obj);
//            }
        }

        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(
                new File("D:\\DucHA\\kq\\GFGsheet.xlsx"));

        workbook.write(out);
        out.close();
        System.out.println("Writesheet.xlsx written successfully");
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

    public static File[] getNameFileInFolder(String linkFile){
        File folder = new File(linkFile);
        File[] listOfFiles = folder.listFiles();
//        for (int i = 0; i < listOfFiles.length; i++) {
//            if (listOfFiles[i].isFile()) {
//                System.out.println("File " + listOfFiles[i].getName());
//            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("Directory " + listOfFiles[i].getName());
//            }
//        }
        return listOfFiles;
    }


    public static void main(String[] args) throws IOException {
          checkLvt();
        }

    }

