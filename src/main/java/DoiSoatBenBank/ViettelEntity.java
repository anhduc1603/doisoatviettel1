package DoiSoatBenBank;

public class ViettelEntity {
    private String REQUEST_ID;
    private String ORDER_ID;
    private String TRANG_THAI;

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

    public ViettelEntity(String REQUEST_ID, String ORDER_ID, String TRANG_THAI) {
        this.REQUEST_ID = REQUEST_ID;
        this.ORDER_ID = ORDER_ID;
        this.TRANG_THAI = TRANG_THAI;
    }
}
