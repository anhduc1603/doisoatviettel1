package DoiSoatBenBank;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransViettel {
    public static void main(String[] args) throws IOException {


        String date = "2023-04-17";
        String dateCnnt = "4/17/2023";//mm/dd/yyyy
        Long countDate = countDay(date);

        final long startTime = System.currentTimeMillis();
//		chon thu muc
        StringBuilder link = new StringBuilder("D:\\DucHA\\Viettel\\");
        link.append(date).append("\\Bank");

        File baseDir = new File(String.valueOf(link));
        // thư mục

        //Mgay Viettel (Lấy ngày hiện tại - ngày cần đối soát)
        String dateVT2 = LocalDate.now().minusDays(countDate).toString();

        //Ngay LVT
        String dateLV2 = LocalDate.now().minusDays(countDate).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString(); // dd/MM/yyyy

        //Ngay CNTT
        String dateCNTT1 = LocalDate.now().minusDays((countDate-1)).format(DateTimeFormatter.ofPattern("M/d/yyyy")).toString();// mm/DD/yyyy
        String dateCNTT2 = LocalDate.now().minusDays(countDate).format(DateTimeFormatter.ofPattern("M/d/yyyy")).toString();
        String dateCNTT3 = LocalDate.now().minusDays((countDate + 1)).format(DateTimeFormatter.ofPattern("M/d/yyyy")).toString();

        File[] files = baseDir.listFiles();

        File fViettel = null;
        File fileBankCheck = null;
        File fCntt = null;

        for (File f : files) {
            if (f.getName().endsWith(".txt")) {
                fCntt = f;
            } else if (f.getName().endsWith(".csv")) {
                fViettel = f;
            } else if (f.getName().endsWith(".tsv")) {
                fileBankCheck = f;
            }
        }
//		System.out.println(fCntt.getPath());
//		System.out.println(fViettel.getPath());
//		System.out.println(fLv24h.getPath());

        if (fViettel == null || fileBankCheck == null || fCntt == null) {
            // throw exception
            System.out.println("null");
            throw new RuntimeException();
        }

        // Đọc dữ liệu từ File với Scanner

        Map<String, LvtTrans> lvtTransFull = new HashMap<>();
//        Set<String> vtlTransIds = new HashSet<String>();
        Map<String,String> mapViettel = new HashMap<>();
        Map<String, String> lvt_vtl = new HashMap<String, String>();
        Map<String, String> vtl_lvt = new HashMap<>();

        List<String> listStatus = new ArrayList<>(); //Status TimeOut Viettel

        List<String> lines1 = Files.readAllLines(Paths.get(fViettel.getPath()));

        // 1. đọc từng dòng, tách theo dấu phẩy, lấy trường ORDER_ID và lưu vào
        // vtlTransIds
        for (String line : lines1) {
            String[] sps = line.split(",");
//			String[] sps = line.split("\t");
            String requestDate = sps[2];
            if (requestDate.contains(dateVT2)) {
                String orderId = sps[1];
                String status = sps[7];
//                if("Thanh cong".equals(status)){
//                    mapViettel.put(orderId,status);
//                }
                mapViettel.put(orderId,status);
            }
        }
//		System.out.println("**************************************************************************************");
        List<String> lines2 = Files.readAllLines(Paths.get(fileBankCheck.getPath()));
        List<String> eror = new ArrayList<String>();
        Map<String,String> mapEror = new HashMap<>();
        // 2. đọc từng dòng, tách theo dấu tab, lấy trường PAYMENT_REF_NO và lưu vào
        // lvtTransIds
        for (String line : lines2) {
            String[] sps = line.split("\t");
            String transDate = sps[0];

            if (transDate.contains(dateLV2)) {
                String paymentRefNo = sps[5];
                if (paymentRefNo == null || paymentRefNo.trim().isEmpty()) {
//					System.out.println("[Thất Bại] Mã TransId lỗi (Không có mã PaymentRefNo) : " + sps[1]);
                    eror.add(sps[1]);
                    mapEror.put(sps[1],sps[24]);
                } else {
                    LvtTrans tt = new LvtTrans();
                    tt.setTransId(sps[1]);
                    tt.setTransStatus(sps[24]);
                    tt.setTime(sps[0]);
                    tt.setPaymentRefNo(sps[5]);
                    tt.setDetail(sps[29]);
                    lvtTransFull.put(paymentRefNo, tt);
                }
            }
        }

        BankEntity b = new BankEntity();


        //File của LVBIS
        List<String> lines3 = Files.readAllLines(Paths.get(fCntt.getPath()));
        // 3. đọc từng dòng, tách theo dấu tab, lấy trường TRANSACTION_ID làm key và
        // ORDER_ID_REQ làm value
        for (String line : lines3) {
            String[] sps = line.split("\t");
            String transDt = sps[0];
            if (transDt.contains(dateCNTT1) || transDt.contains(dateCnnt) || transDt.contains(dateCNTT3)) {
                String transactionId = sps[1];
                String orderIdReq = sps[3];

                if (!"ORDER_ID_REQ".equals(orderIdReq)) {
//					cntt_vtlTransIds.add(orderIdReq.substring(9, orderIdReq.length() - 1));
                    String xx = orderIdReq.substring(9, orderIdReq.length() - 1);
                    lvt_vtl.put(transactionId, xx);
                    vtl_lvt.put(xx, transactionId);
                }
            }
        }

        Map<String,BankEntity> mapBankCheck = new HashMap<>();
        //Đọc file của bank cần check
        List<String> lines4 = Files.readAllLines(Paths.get(fileBankCheck.getPath()));
        for (String line : lines4) {
            String[] sps = line.split("\t");
                String dateBank = sps[3];
                String formatDate =   LocalDate.now().minusDays((countDate)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
            if (dateBank.contains(formatDate)) {
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
        }

        System.out.println("TRANS_ID" + "\t" + "USER_NAME" + "\t" + "FULL_NAME" + "\t" + "TRANS_TIME"
                + "\t" + "TRANS_NAME" + "\t" + "INPUT_VALUE" + "\t" + "AMOUNT_TRANSFER" + "\t" + "INPUT_AMOUNT"
                + "\t" + "PAYMENT_REF_NO" + "\t" + "PARTNER_CODE" + "\t" + "FROM_SYSTEM_ID"  + "\t" + "STATUS");

        Set<String> lvtTransIds = mapBankCheck.keySet();
        for (String lvtPaymentRefNo : lvtTransIds) {
            //Check trong LVBIS
            String lvtTrans = lvt_vtl.get(lvtPaymentRefNo);
            if(lvtTrans==null){
                System.out.println("[XEM LẠI] Không tồn tại trong file LV24H " + "\t" + lvtPaymentRefNo);
            }else {
                //Check orderID với file Viettel
                Set<String> lvbisOderId = vtl_lvt.keySet();
                String idsViettel = mapViettel.get(lvtTrans);
                //Nếu không có bên file Viettel
                if(idsViettel==null){
                    BankEntity bankEntity = mapBankCheck.get(lvtPaymentRefNo);
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
                    BankEntity bankEntity = mapBankCheck.get(lvtPaymentRefNo);
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
                                    + idsViettel
                    );
                }
            }

        }

    }

    public static Long countDay(String date){
        // Định dạng thời gian
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        // Định nghĩa 2 mốc thời gian ban đầu
        java.sql.Date date1 = java.sql.Date.valueOf(date);

        c1.setTime(date1);
        c2.setTime(new Date());

        // Công thức tính số ngày giữa 2 mốc thời gian:
        long noDay = (c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000);

        return noDay;
    }
}
