package si.assignment3.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import si.assignment3.model.LoanOffer;
import si.assignment3.model.LoanRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(LoanRequestHandler.class);
    private final List<LoanOffer> offers = new ArrayList<>();

    @Autowired
    LoanCalculater loanCalculater = new LoanCalculater();
    @Autowired
    SendLoanHandler sendLoanHandler = new SendLoanHandler();

    @KafkaListener(topics = "loan-request", groupId = "my-group2")
    public void listen(String message) {
        try {
            // Convert message to LoanRequest object.
            LoanRequest loanRequest = new Gson().fromJson(message, LoanRequest.class);
            // Calculate Loan and convert LoanRequest object to LoanOffer object.
            LoanOffer loanOffer = loanCalculater.calculateLoan(loanRequest);
            // Store the LoanOffer for later usage.
            offers.add(loanOffer);
            // Send the Loan Offer.
            sendLoanHandler.sendLoanOffer(loanOffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LoanOffer> getOffers() {
        return offers;
    }
}
