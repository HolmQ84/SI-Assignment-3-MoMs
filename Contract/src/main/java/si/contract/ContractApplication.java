package si.contract;

import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Paragraph paragraph = new Paragraph(sdf.format(cal.getTime()), font);
            document.add(paragraph);
            document.add(new Paragraph("\n\nDear " + contractInfo.getCustomerTitle() + " " + contractInfo.getCustomerName(), font));
            document.add(new Paragraph("\nWe hereby send confirmation of your accepted loan.\n\nYou can see the details of the loan here:\n ", font));
            PdfPTable table = new PdfPTable(2);

            PdfPCell cellOne = new PdfPCell(new Phrase("Loan Amount", font));
            PdfPCell cellTwo = new PdfPCell(new Phrase(String.valueOf(contractInfo.getLoanAmount()) + " kr.", font));
            PdfPCell cellThree = new PdfPCell(new Phrase("Loan Interest", font));
            PdfPCell cellFour = new PdfPCell(new Phrase(String.valueOf(contractInfo.getLoanInterest()) + " %", font));
            PdfPCell cellFive = new PdfPCell(new Phrase("Payback Months", font));
            PdfPCell cellSix = new PdfPCell(new Phrase(String.valueOf(contractInfo.getPaybackMonths()) + " kr.", font));
            PdfPCell cellSeven = new PdfPCell(new Phrase("Monthly payment", font));
            PdfPCell cellEight = new PdfPCell(new Phrase(String.valueOf(Math.round(contractInfo.getLoanAmount() / contractInfo.getPaybackMonths())) + " kr.", font));

            cellOne.setBorder(Rectangle.NO_BORDER);
            cellTwo.setBorder(Rectangle.NO_BORDER);
            cellThree.setBorder(Rectangle.NO_BORDER);
            cellFour.setBorder(Rectangle.NO_BORDER);
            cellFive.setBorder(Rectangle.NO_BORDER);
            cellSix.setBorder(Rectangle.NO_BORDER);
            cellSeven.setBorder(Rectangle.NO_BORDER);
            cellEight.setBorder(Rectangle.NO_BORDER);

            table.addCell(cellOne);
            table.addCell(cellTwo);
            table.addCell(cellThree);
            table.addCell(cellFour);
            table.addCell(cellFive);
            table.addCell(cellSix);
            table.addCell(cellSeven);
            table.addCell(cellEight);

            document.add(table);
            document.add(new Paragraph("\nWe look forward to earning money on you - while doing nothing! :)", font));
            document.add(new Paragraph("\nKind Regards", font));
            document.add(new Paragraph(contractInfo.getBankName(), font));
            if (contractInfo.getBankName().equals("Danske Bank")) {
                Image image = Image.getInstance("https://vidensby.dk/wp-content/uploads/2018/01/danske-bank-logo-photos.png");
                image.scaleAbsolute(100, 20);
                document.add(image);
            }
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
