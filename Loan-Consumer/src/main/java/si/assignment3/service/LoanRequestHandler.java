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

    @KafkaListener(topics = "loan-request", groupId = "request")
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

    @KafkaListener(topics = "loan-acceptance", groupId = "accept")
    public void listen2(String message) {
        logger.info("Loan acceptance received! " + message);
        LoanOffer loanOffer = getOfferById(Integer.parseInt(message.substring(10,18)));

        // TODO - Use found Loan Offer (var loanOffer line 45) to send data via
        //  RabbitMQ to external project to create Contract.

    }

    public LoanOffer getOfferById(int loanId) {
        for (LoanOffer current: offers) {
            if (current.getLoanId() == loanId) {
                return current;
            }
        }
        return null;
    }
}
