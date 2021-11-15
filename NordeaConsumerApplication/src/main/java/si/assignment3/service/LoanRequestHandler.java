package si.assignment3.service;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import si.assignment3.model.LoanOffer;
import si.assignment3.model.LoanRequest;

import java.nio.charset.StandardCharsets;
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

    @KafkaListener(topics = "loan-request", groupId = "request-nordea")
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

    @KafkaListener(topics = "loan-acceptance", groupId = "accept-nordea")
    public void listen2(String message) {
        logger.info("Loan acceptance received! " + message);
        LoanOffer loanOffer = getOfferById(Integer.parseInt(message.substring(10,18)));
        if (loanOffer != null) {
            logger.info("Loan found in DB!");
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            String queueName = "contract";
            try {
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(queueName, false, false, false, null);
                channel.basicPublish("", queueName, null, loanOffer.toString().getBytes(StandardCharsets.UTF_8));
                logger.info("Sent data to contract handler.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("Loan not found in DB.");
        }
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
