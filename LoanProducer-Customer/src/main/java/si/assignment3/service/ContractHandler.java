package si.assignment3.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;

@Service
public class ContractHandler {
    private final static Logger logger = LoggerFactory.getLogger(ContractHandler.class);

    public static void connectQueue()
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Register for a queue
            channel.queueDeclare("contract-delivery", false, false, false, null);
            logger.info("Ready for incoming contracts...");

            // Get notified, if a message for this receiver arrives
            DeliverCallback deliverCallback = (consumerTag, delivery) ->
            {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("Received new contract: " + message);
                Document document = new Gson().fromJson(message, Document.class);
                System.out.println(document);
            };
            channel.basicConsume("contract-delivery", true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
