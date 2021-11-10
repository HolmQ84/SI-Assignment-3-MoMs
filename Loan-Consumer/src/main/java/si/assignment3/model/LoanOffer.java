package si.assignment3.model;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LoanOffer {
    @Id
    private int id;
    private String customerId;
    private String bankId;
    private String bankName;
    private String customerName;
    private String customerTitle;
    private int loanAmount;
    private double loanInterest;
    private int paybackMonths;

    public LoanOffer(String customerId, String bankId, String bankName, String customerName, String customerTitle, int loanAmount, double loanInterest, int paybackMonths) {
        this.customerId = customerId;
        this.bankId = bankId;
        this.bankName = bankName;
        this.customerName = customerName;
        this.customerTitle = customerTitle;
        this.loanAmount = loanAmount;
        this.loanInterest = loanInterest;
        this.paybackMonths = paybackMonths;
    }

    public LoanOffer() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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
        return "LoanOffer{" +
                "customerId='" + customerId + '\'' +
                ", bankId='" + bankId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerTitle='" + customerTitle + '\'' +
                ", loanAmount=" + loanAmount +
                ", loanInterest=" + loanInterest +
                ", paybackMonths=" + paybackMonths +
                '}';
    }
}
