package DoiSoatBenBank;

public class LvbEntity {
    private String TRAN_DT;
    private String TRANSACTION_ID;
    private String ORDER_ID_REQ;

    public LvbEntity(String TRAN_DT, String TRANSACTION_ID, String ORDER_ID_REQ) {
        this.TRAN_DT = TRAN_DT;
        this.TRANSACTION_ID = TRANSACTION_ID;
        this.ORDER_ID_REQ = ORDER_ID_REQ;
    }

    public String getTRAN_DT() {
        return TRAN_DT;
    }

    public void setTRAN_DT(String TRAN_DT) {
        this.TRAN_DT = TRAN_DT;
    }

    public String getTRANSACTION_ID() {
        return TRANSACTION_ID;
    }

    public void setTRANSACTION_ID(String TRANSACTION_ID) {
        this.TRANSACTION_ID = TRANSACTION_ID;
    }

    public String getORDER_ID_REQ() {
        return ORDER_ID_REQ;
    }

    public void setORDER_ID_REQ(String ORDER_ID_REQ) {
        this.ORDER_ID_REQ = ORDER_ID_REQ;
    }
}
