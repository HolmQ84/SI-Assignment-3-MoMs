package si.assignment3.service;

import org.springframework.stereotype.Service;
import si.assignment3.model.LoanOffer;
import si.assignment3.model.LoanRequest;

import java.util.Random;

@Service
public class LoanCalculater {
    Random random = new Random();

    public LoanOffer calculateLoan(LoanRequest loanRequest) {
        LoanOffer loanOffer = new LoanOffer();

        loanOffer.setLoanId(random.nextInt(89999998)+10000001);

        // Put in data from Request along with Bank Info.
        loanOffer.setCustomerId(loanRequest.getCustomerId());
        loanOffer.setCustomerName(loanRequest.getCustomerName());
        loanOffer.setCustomerTitle(loanRequest.getCustomerTitle());
        loanOffer.setBankName("Nordea");
        loanOffer.setBankId("9ke509LpUi%JjNmvjQEy");

        // Calculate Loan Amount
        int total = 0;
        total += (loanRequest.getYearlySalary()*5)-(loanRequest.getDebtAmount()*2);
        if (loanRequest.isCarOwner()) {
            total = (int) (total*0.9);
        }
        if (loanRequest.isHouseOwner()) {
            total = (int) (total*2.5);
        }
        loanOffer.setLoanAmount(total);

        // Calculate Loan Interest
        double interest = total / 900000.0;
        if (loanRequest.isCarOwner()) {
            interest += 1.2;
        }
        if (loanRequest.isHouseOwner()) {
            interest -= 0.7;
        }
        double roundOffInterest = Math.round(interest * 100.0) / 100.0;
        loanOffer.setLoanInterest(roundOffInterest);

        // Calculate payback months
        loanOffer.setPaybackMonths(
            (int) (total * interest) / 9500
        );

        return loanOffer;
    }
}
