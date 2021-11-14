package si.assignment3.model;

public class LoanRequest {
    private String customerId;
    private String customerName;
    private String customerTitle;
    private int yearlySalary;
    private int debtAmount;
    private boolean carOwner;
    private boolean houseOwner;

    public LoanRequest(String customerId, String customerName, String customerTitle,int yearlySalary, int debtAmount, boolean carOwner, boolean houseOwner) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerTitle = customerTitle;
        this.yearlySalary = yearlySalary;
        this.debtAmount = debtAmount;
        this.carOwner = carOwner;
        this.houseOwner = houseOwner;
    }

    public LoanRequest() {
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

    public int getYearlySalary() {
        return yearlySalary;
    }

    public void setYearlySalary(int yearlySalary) {
        this.yearlySalary = yearlySalary;
    }

    public int getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(int debtAmount) {
        this.debtAmount = debtAmount;
    }

    public boolean isCarOwner() {
        return carOwner;
    }

    public void setCarOwner(boolean carOwner) {
        this.carOwner = carOwner;
    }

    public boolean isHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(boolean houseOwner) {
        this.houseOwner = houseOwner;
    }
}

