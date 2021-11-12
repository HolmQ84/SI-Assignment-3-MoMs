package si.contract.model;

public class ContractInfo {
    private int loanId;
    private String customerId;
    private String customerName;
    private String customerTitle;
    private String bankName;
    private int loanAmount;
    private double loanInterest;
    private int paybackMonths;

    public ContractInfo() {
    }

    public ContractInfo(int loanId, String customerId, String customerName, String customerTitle, String bankName, int loanAmount, double loanInterest, int paybackMonths) {
        this.loanId = loanId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerTitle = customerTitle;
        this.bankName = bankName;
        this.loanAmount = loanAmount;
        this.loanInterest = loanInterest;
        this.paybackMonths = paybackMonths;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTitle() {
        return customerTitle;
    }

    public void setCustomerTitle(String customerTitle) {
        this.customerTitle = customerTitle;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(double loanInterest) {
        this.loanInterest = loanInterest;
    }

    public int getPaybackMonths() {
        return paybackMonths;
    }

    public void setPaybackMonths(int paybackMonths) {
        this.paybackMonths = paybackMonths;
    }

    @Override
    public String toString() {
        return "ContractInfo{" +
                "loanId=" + loanId +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerTitle='" + customerTitle + '\'' +
                ", bankName='" + bankName + '\'' +
                ", loanAmount=" + loanAmount +
                ", loanInterest=" + loanInterest +
                ", paybackMonths=" + paybackMonths +
                '}';
    }
}
