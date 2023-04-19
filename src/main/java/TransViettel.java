import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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

        File baseDir = new File("D:\\DucHa\\Viettel");
//		String dateVT2 = LocalDate.now().minusDays(6).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
        // thư mục
        File dirByDate = new File(baseDir, LocalDate.now().minusDays(countDate).toString());
//		File dirByDate = new File(baseDir, "2021-12-27");


        //Mgay Viettel (Lấy ngày hiện tại - ngày cần đối soát)
        String dateVT2 = LocalDate.now().minusDays(countDate).toString();

        //Ngay LVT
        String dateLV2 = LocalDate.now().minusDays(countDate).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString(); // dd/MM/yyyy

        //Ngay CNTT
        String dateCNTT1 = LocalDate.now().minusDays((countDate-1)).format(DateTimeFormatter.ofPattern("M/d/yyyy")).toString();// mm/DD/yyyy
        String dateCNTT2 = LocalDate.now().minusDays(countDate).format(DateTimeFormatter.ofPattern("M/d/yyyy")).toString();
        String dateCNTT3 = LocalDate.now().minusDays((countDate + 1)).format(DateTimeFormatter.ofPattern("M/d/yyyy")).toString();

//        String dateCNTT1 = LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("M/d/yyyy")).toString();// mm/DD/yyyy
//        String dateCNTT2 = LocalDate.now().minusDays(3).format(DateTimeFormatter.ofPattern("M/d/yyyy")).toString();
//        String dateCNTT3 = LocalDate.now().minusDays(4).format(DateTimeFormatter.ofPattern("M/d/yyyy")).toString();

        File[] files = dirByDate.listFiles();

        File fViettel = null;
        File fLv24h = null;
        File fCntt = null;

        for (File f : files) {
            if (f.getName().endsWith(".txt")) {
                fCntt = f;
            } else if (f.getName().endsWith(".csv")) {
                fViettel = f;
            } else if (f.getName().endsWith(".tsv")) {
                fLv24h = f;
            }
        }
//		System.out.println(fCntt.getPath());
//		System.out.println(fViettel.getPath());
//		System.out.println(fLv24h.getPath());

        if (fViettel == null || fLv24h == null || fCntt == null) {
            // throw exception
            System.out.println("null");
            throw new RuntimeException();
        }

        // Đọc dữ liệu từ File với Scanner

//		Set<String> lvtTransIds = new HashSet<String>();
        Map<String, LvtTrans> lvtTransFull = new HashMap<>();
        Set<String> vtlTransIds = new HashSet<String>();
//		Set<String> cntt_lvtTransIds = new HashSet<String>();
//		Set<String> cntt_vtlTransIds = new HashSet<String>();
        Map<String, String> lvt_vtl = new HashMap<String, String>();
        Map<String, String> vtl_lvt = new HashMap<String, String>();

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
                if("Thanh cong".equals(status)){
                    vtlTransIds.add(orderId);
                }
                listStatus.add(orderId);
            }
        }
//		System.out.println("**************************************************************************************");
        List<String> lines2 = Files.readAllLines(Paths.get(fLv24h.getPath()));
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
//					tt.setDate(sps[1]);
                    tt.setPaymentRefNo(sps[5]);
                    tt.setDetail(sps[29]);
                    lvtTransFull.put(paymentRefNo, tt);
                }
            }
//			System.out.println(paymentRefNo);
        }
//		System.out.println("**************************************************************************************");


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

        System.out.println("Ngày :"+ "\t" + dateLV2);
        System.out.println("Số giao dịch của LienViet : " + "\t" + lvtTransFull.size());
        System.out.println("Số giao dịch của Viettel : " + "\t" + vtlTransIds.size());
        int a = lvtTransFull.size() - vtlTransIds.size();
        System.out.println("Số giao dịch chênh lệch : " + "\t" + a);
        System.out.println("Trạng thái sau khi đối soát" + "\t" + "Lí do" + "\t" + "Mã đối soát Viettel" + "\t" + "Mã đối soát LienViet"
                + "\t" + "Mã giao dịch Ví" + "\t" + "Trạng thái trả về trong dữ liệu LienViet" + "\t" + "Thời gian giao dịch" + "\t" + "Loại GD");

        // Đôi chiếu Lvt với Vtl
        Set<String> lvtTransIds = lvtTransFull.keySet();
        for (String lvtPaymentRefNo : lvtTransIds) {
            // lấy ra mã payment_ref_no trong file của cntt
            String orderNoVietel = lvt_vtl.get(lvtPaymentRefNo);
            if (orderNoVietel == null) {
                // lvt có nhưng cntt ko có => hiếm khi nào xảy ra
//				lvtTransFull.get(lvtPaymentRefNo);
                System.out.println("[Kiểm tra lại] lvt có nhưng cntt không có : " + lvtPaymentRefNo + "\t"
                        + lvtTransFull.get(lvtPaymentRefNo).getTransId() + "\t"
                        + lvtTransFull.get(lvtPaymentRefNo).getTransStatus() + "\t"
//						+ lvtTransFull.get(lvtPaymentRefNo).getDate() + "\t"
                        + lvtTransFull.get(lvtPaymentRefNo).getTime());
            } else {
                if (vtlTransIds.contains(orderNoVietel)) {
                    // khớp thông tin
                    // lấy ra object trans từ map
                    LvtTrans lvtt = lvtTransFull.get(lvtPaymentRefNo);
                    String transStatus = lvtt.getTransStatus();
                    if (!"Thành công".equals(transStatus)) {
                        // in ra thông tin
                        System.out.println("[Thành Công]"+"\t"+"Giao dịch thành công mà trạng thái trả về khác thành công : "
                                + "\t" + orderNoVietel
                                + "\t" + lvtt.getPaymentRefNo()
                                + "\t" + lvtt.getTransId()
                                + "\t" + lvtt.getTransStatus()
                                + "\t" + lvtt.getTime()
                                + "\t" + lvtt.getDetail());
                    }
//					count++;
                } else {
                    if(listStatus.contains(orderNoVietel)){
                        //Viettel trả về trạng thái TimeOut
                        System.out.println("[Viettel Timeout]"+"\t"+" Viettel trả về trạng thái Timeout" + "\t" + orderNoVietel + "\t"
                                + lvtPaymentRefNo + "\t" + lvtTransFull.get(lvtPaymentRefNo).getTransId() + "\t"
                                + lvtTransFull.get(lvtPaymentRefNo).getTransStatus().toString() + "\t"
//							+ lvtTransFull.get(lvtPaymentRefNo).getDate() + "\t"
                                + lvtTransFull.get(lvtPaymentRefNo).getTime() + "\t"
                                + lvtTransFull.get(lvtPaymentRefNo).getDetail().toString());
                    }else {
                        // VT không có GD của LVT
                        System.out.println("[Thất Bại]"+"\t"+" Mã LienViet có mà Viettel không có :" + "\t" + orderNoVietel + "\t"
                                + lvtPaymentRefNo + "\t" + lvtTransFull.get(lvtPaymentRefNo).getTransId() + "\t"
                                + lvtTransFull.get(lvtPaymentRefNo).getTransStatus().toString() + "\t"
//							+ lvtTransFull.get(lvtPaymentRefNo).getDate() + "\t"
                                + lvtTransFull.get(lvtPaymentRefNo).getTime() + "\t"
                                + lvtTransFull.get(lvtPaymentRefNo).getDetail().toString());
                    }
                }
            }
        }

        // Đối chiếu Vtl với Lvt
        for (String vtlOrderId : vtlTransIds) {
            String cnttTransId = vtl_lvt.get(vtlOrderId);
            if (cnttTransId == null) {
                // ghi ra vt có cntt ko có
                System.out.println("[YÊU CẦU KIỂM TRA] Mã giao dịch Viettel có mà CNTT không có : " + vtlOrderId);
            } else {
                boolean contains = lvtTransIds.contains(cnttTransId);
                if (contains) {
                    // ok ko làm gì cả
                } else {
                    // ghi ra
                    System.out.println("[YÊU CẦU KIỂM TRA]" + "\tMã Viettel có mà LienViet không có : " + "\t" + vtlOrderId +  "\t" + cnttTransId + "\t" + "\t" + "\t" + "\t" + "\t" + "Kiểm tra lại giao dịch của ngày: " + dateCNTT1);
                }
            }
        }
//		System.out.println("**************************************************************************************");
        for (String e : eror) {
            String status = "";
//            if(mapEror.containsKey(e)){
//
//            }
            status = mapEror.get(e);
            System.out.println("[Lỗi]" + "\t" + " Mã TransId lỗi (Không có mã PaymentRefNo) : " + "\t" + "\t" + "\t" + e + "\t"+ status);
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
        c2.setTime(new java.util.Date());

        // Công thức tính số ngày giữa 2 mốc thời gian:
        long noDay = (c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000);

        return noDay;
    }
}
