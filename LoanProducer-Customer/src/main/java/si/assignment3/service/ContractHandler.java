package si.assignment3.service;

import com.itextpdf.text.*;
import com.google.gson.Gson;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ContractHandler {
    private static final Logger logger = LoggerFactory.getLogger(ContractHandler.class);

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
                System.out.println("Converting from byte Array to PDF...");
                Document document = new Document();
                document.open();
                try {
                    PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
                    document.add(new Chunk("Hejsa"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                document.close();
            };
            channel.basicConsume("contract-delivery", true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
