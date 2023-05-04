package DoiSoatBenBank;

public class ViettelEntity {
    private String REQUEST_ID;
    private String ORDER_ID;
    private String REQUEST_DATE;

    private String CP_CODE;
    private String BILLING_CODE;
    private String AMOUNT;
    private String QUANTITY;

    private String TRANG_THAI;

    public ViettelEntity(String REQUEST_ID, String ORDER_ID, String REQUEST_DATE, String CP_CODE, String BILLING_CODE, String AMOUNT, String QUANTITY, String TRANG_THAI) {
        this.REQUEST_ID = REQUEST_ID;
        this.ORDER_ID = ORDER_ID;
        this.REQUEST_DATE = REQUEST_DATE;
        this.CP_CODE = CP_CODE;
        this.BILLING_CODE = BILLING_CODE;
        this.AMOUNT = AMOUNT;
        this.QUANTITY = QUANTITY;
        this.TRANG_THAI = TRANG_THAI;
    }


    public ViettelEntity(String REQUEST_ID, String ORDER_ID, String TRANG_THAI) {
        this.REQUEST_ID = REQUEST_ID;
        this.ORDER_ID = ORDER_ID;
        this.TRANG_THAI = TRANG_THAI;
    }

    public String getREQUEST_ID() {
        return REQUEST_ID;
    }

    public void setREQUEST_ID(String REQUEST_ID) {
        this.REQUEST_ID = REQUEST_ID;
    }

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public String getTRANG_THAI() {
        return TRANG_THAI;
    }

    public void setTRANG_THAI(String TRANG_THAI) {
        this.TRANG_THAI = TRANG_THAI;
    }

    public String getREQUEST_DATE() {
        return REQUEST_DATE;
    }

    public void setREQUEST_DATE(String REQUEST_DATE) {
        this.REQUEST_DATE = REQUEST_DATE;
    }

    public String getCP_CODE() {
        return CP_CODE;
    }

    public void setCP_CODE(String CP_CODE) {
        this.CP_CODE = CP_CODE;
    }

    public String getBILLING_CODE() {
        return BILLING_CODE;
    }

    public void setBILLING_CODE(String BILLING_CODE) {
        this.BILLING_CODE = BILLING_CODE;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getQUANTITY() {
        return QUANTITY;
    }

    public void setQUANTITY(String QUANTITY) {
        this.QUANTITY = QUANTITY;
    }

    @Override
    public String toString() {
        return "ViettelEntity{" +
                "REQUEST_ID='" + REQUEST_ID + '\'' +
                ", ORDER_ID='" + ORDER_ID + '\'' +
                ", REQUEST_DATE='" + REQUEST_DATE + '\'' +
                ", CP_CODE='" + CP_CODE + '\'' +
                ", BILLING_CODE='" + BILLING_CODE + '\'' +
                ", AMOUNT='" + AMOUNT + '\'' +
                ", QUANTITY='" + QUANTITY + '\'' +
                ", TRANG_THAI='" + TRANG_THAI + '\'' +
                '}';
    }
}
