package DoiSoatBenBank;

public class LvtTrans {
    private String transId;
    private String date;
    private String time;
    private String paymentRefNo;
    private String transStatus;
    private String detail;
    private String eror;

    public String getTransId() {
        return transId;
    }
    public void setTransId(String transId) {
        this.transId = transId;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getEror() {
        return eror;
    }
    public void setEror(String eror) {
        this.eror = eror;
    }
    public String getPaymentRefNo() {
        return paymentRefNo;
    }
    public void setPaymentRefNo(String paymentRefNo) {
        this.paymentRefNo = paymentRefNo;
    }
    public String getTransStatus() {
        return transStatus;
    }
    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
