package si.assignment3.service;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import si.assignment3.model.LoanOffer;
import si.assignment3.model.LoanRequest;

import java.util.*;

@Service
public class LoanService {

    private final String requestTopic = "loan-request";
    private final String acceptTopic = "loan-acceptance";
    private final Logger logger = LoggerFactory.getLogger(LoanService.class);
    private final List<LoanOffer> offers = new ArrayList<>();
    private int loanId;

    @Autowired
    KafkaTemplate<String, String> template;

    public void sendLoanRequest(LoanRequest loanRequest) {
        try {
            IdGenerator idGen = new IdGenerator();

            JSONObject request = new JSONObject();
            request.put("customerId", idGen.getId());
            request.put("customerName", loanRequest.getCustomerName());
            request.put("customerTitle", loanRequest.getCustomerTitle());
            request.put("yearlySalary", loanRequest.getYearlySalary());
            request.put("debtAmount", loanRequest.getDebtAmount());
            request.put("carOwner", loanRequest.isCarOwner());
            request.put("houseOwner", loanRequest.isHouseOwner());
            template.send(requestTopic, request.toString());
            logger.info("Sent Loan Request to Kafka - " + request.toString());
            template.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "loan-offer", groupId = "my-group")
    public LoanOffer listen(String message) {
        LoanOffer loanOffer = new Gson().fromJson(message, LoanOffer.class);
        logger.info("Received loan offer from bank.");
        offers.add(loanOffer);
        return loanOffer;
    }

    public List<LoanOffer> getOffers() {
        return offers;
    }

    public void sendLoanAcceptance(int loanId) {
        this.loanId = loanId;
        try {
            JSONObject request = new JSONObject();
            request.put("loanId", loanId);
            request.put("status", "accept");
            template.send(acceptTopic, request.toString());
            logger.info("Sent acceptance for loan offer with ID: " +loanId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
