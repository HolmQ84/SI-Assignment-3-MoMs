package si.assignment3.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ContractHandler {
    private static final Logger logger = LoggerFactory.getLogger(ContractHandler.class);
    static byte[] aByte = new byte[1];
    static int bytesRead;

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
                byte[] message = delivery.getBody();
                logger.info("Received new contract!");
                System.out.println("Converting from byte Array to PDF...");

                InputStream is = new ByteArrayInputStream(message);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileOutputStream fos;
                BufferedOutputStream bos;

                try {
                    LoanService ls = new LoanService();
                    String dir = System.getProperty("user.dir");
                    fos = new FileOutputStream(dir+"/LoanProducer-Customer/src/main/resources/static/contracts/Contract-"+ls.getLoanId()+".pdf");
                    bos = new BufferedOutputStream(fos);
                    bytesRead = is.read(aByte, 0, aByte.length);

                    do {
                        baos.write(aByte);
                        bytesRead = is.read(aByte);
                    } while (bytesRead != -1);

                    bos.write(baos.toByteArray());
                    bos.flush();
                    bos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.println("Succesfully converted byte array to PDF!");
            };
            channel.basicConsume("contract-delivery", true, deliverCallback, consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
