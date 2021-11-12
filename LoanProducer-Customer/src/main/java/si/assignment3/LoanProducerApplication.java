package si.assignment3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import si.assignment3.service.ContractHandler;


@SpringBootApplication
public class LoanProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanProducerApplication.class, args);
        ContractHandler.connectQueue();
    }
}
