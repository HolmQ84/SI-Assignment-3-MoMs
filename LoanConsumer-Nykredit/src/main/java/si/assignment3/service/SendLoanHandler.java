package si.assignment3.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import si.assignment3.model.LoanOffer;

@Service
public class SendLoanHandler {
    private final String topic = "loan-offer";
    private final Logger logger = LoggerFactory.getLogger(SendLoanHandler.class);

    @Autowired
    KafkaTemplate<String, String> template;

    public void sendLoanOffer(LoanOffer loanOffer) {
        String offer = convertOfferToString(loanOffer);
        template.send(topic, offer);
        logger.info("Sent Loan Offer to Kafka.");
        template.flush();
    }

    public String convertOfferToString(LoanOffer loanOffer) {
        try {
            JSONObject offer = new JSONObject();
            offer.put("loanId", loanOffer.getLoanId());
            offer.put("customerId", loanOffer.getCustomerId());
            offer.put("customerName", loanOffer.getCustomerName());
            offer.put("customerTitle", loanOffer.getCustomerTitle());
            offer.put("bankId", loanOffer.getBankId());
            offer.put("bankName", loanOffer.getBankName());
            offer.put("loanAmount", loanOffer.getLoanAmount());
            offer.put("loanInterest", loanOffer.getLoanInterest());
            offer.put("paybackMonths", loanOffer.getPaybackMonths());
            return offer.toString();
        } catch (Exception e) {
            System.out.println("Fejl");
            e.printStackTrace();
        }
        return null;
    }
}
