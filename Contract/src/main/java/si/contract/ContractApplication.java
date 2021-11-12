package si.contract;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import si.contract.model.ContractInfo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class ContractApplication {
    private final static Logger logger = LoggerFactory.getLogger(ContractApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ContractApplication.class, args);
        try {
            connectQueue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void connectQueue()
    {
        // Same as the producer: tries to create a queue, if it wasn't already created
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Register for a queue
            channel.queueDeclare("contract", false, false, false, null);
            logger.info("Ready for incoming messages...");

            // Get notified, if a message for this receiver arrives
            DeliverCallback deliverCallback = (consumerTag, delivery) ->
            {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.info("Received new contract data: " + message);
                ContractInfo contractInfo = new Gson().fromJson(message, ContractInfo.class);
                try {
                    Document document = createContract(contractInfo);
                    sendContract(document);
                    logger.info("Contract sent back to RabbitMQ with topic 'contract-delivery'");
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            };
            channel.basicConsume("contract", true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Document createContract(ContractInfo contractInfo) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        try {
            String dir = System.getProperty("user.dir");
            PdfWriter.getInstance(document, new FileOutputStream(dir+"/Contract/src/main/resources/static/contracts/Contract-"+contractInfo.getLoanId()+".pdf"));
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Chunk chunk = new Chunk(sdf.format(cal.getTime()), font);
            document.add(chunk);
            chunk = new Chunk("Dear " + contractInfo.getCustomerTitle() + " " + contractInfo.getCustomerName(), font);
            document.add(chunk);
            chunk = new Chunk("We hereby send confirmation of your accepted loan.\nYou can see the details of the loans here:", font);
            document.add(chunk);
            chunk = new Chunk("Loan Amount: " + contractInfo.getLoanAmount(), font);
            document.add(chunk);
            chunk = new Chunk("Loan Interest: " + contractInfo.getLoanInterest(), font);
            document.add(chunk);
            chunk = new Chunk("Payback Months: " + contractInfo.getPaybackMonths(), font);
            document.add(chunk);
            chunk = new Chunk("Amount to pay each month: " + (contractInfo.getLoanAmount() / contractInfo.getPaybackMonths()), font);
            document.add(chunk);
            chunk = new Chunk("We look forward to earning money on you! :)", font);
            document.add(chunk);
            chunk = new Chunk("Kind Regards", font);
            document.add(chunk);
            chunk = new Chunk(contractInfo.getBankName(), font);
            document.add(chunk);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    public static void sendContract(Document document) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        String queueName = "contract-delivery";
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("", queueName, null, document.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
