package DoiSoatBenBank;

import java.io.File;

public class BankEntity {
    private String TRANS_ID;
    private String USER_NAME;
    private String FULL_NAME;
    private String TRANS_TIME;
    private String TRANS_NAME;
    private String INPUT_VALUE;
    private String AMOUNT_TRANSFER;
    private String INPUT_AMOUNT;
    private String PAYMENT_REF_NO;
    private String PARTNER_CODE;
    private String FROM_SYSTEM_ID;

    private File file;

    public File getFile() {
        return file;
    }


    public void setFile(File file) {
        this.file = file;
    }

    public String getTRANS_ID() {
        return TRANS_ID;
    }

    public void setTRANS_ID(String TRANS_ID) {
        this.TRANS_ID = TRANS_ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getFULL_NAME() {
        return FULL_NAME;
    }

    public void setFULL_NAME(String FULL_NAME) {
        this.FULL_NAME = FULL_NAME;
    }

    public String getTRANS_TIME() {
        return TRANS_TIME;
    }

    public void setTRANS_TIME(String TRANS_TIME) {
        this.TRANS_TIME = TRANS_TIME;
    }

    public String getTRANS_NAME() {
        return TRANS_NAME;
    }

    public void setTRANS_NAME(String TRANS_NAME) {
        this.TRANS_NAME = TRANS_NAME;
    }

    public String getINPUT_VALUE() {
        return INPUT_VALUE;
    }

    public void setINPUT_VALUE(String INPUT_VALUE) {
        this.INPUT_VALUE = INPUT_VALUE;
    }

    public String getAMOUNT_TRANSFER() {
        return AMOUNT_TRANSFER;
    }

    public void setAMOUNT_TRANSFER(String AMOUNT_TRANSFER) {
        this.AMOUNT_TRANSFER = AMOUNT_TRANSFER;
    }

    public String getINPUT_AMOUNT() {
        return INPUT_AMOUNT;
    }

    public void setINPUT_AMOUNT(String INPUT_AMOUNT) {
        this.INPUT_AMOUNT = INPUT_AMOUNT;
    }

    public String getPAYMENT_REF_NO() {
        return PAYMENT_REF_NO;
    }

    public void setPAYMENT_REF_NO(String PAYMENT_REF_NO) {
        this.PAYMENT_REF_NO = PAYMENT_REF_NO;
    }

    public String getPARTNER_CODE() {
        return PARTNER_CODE;
    }

    public void setPARTNER_CODE(String PARTNER_CODE) {
        this.PARTNER_CODE = PARTNER_CODE;
    }

    public String getFROM_SYSTEM_ID() {
        return FROM_SYSTEM_ID;
    }

    public void setFROM_SYSTEM_ID(String FROM_SYSTEM_ID) {
        this.FROM_SYSTEM_ID = FROM_SYSTEM_ID;
    }
}
