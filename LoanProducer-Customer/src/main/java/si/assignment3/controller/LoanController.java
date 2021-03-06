package si.assignment3.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.*;
import si.assignment3.model.LoanOffer;
import si.assignment3.model.LoanRequest;
import si.assignment3.service.ContractHandler;
import si.assignment3.service.LoanService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.List;

@RestController
public class LoanController {
    private int loanId;

    @Autowired
    private LoanService loanService;

    @Autowired
    ContractHandler contractHandler;

    @PostMapping(value = "/loanrequest")
    public String loanRequest(@RequestBody LoanRequest loanRequest) {
        loanService.sendLoanRequest(loanRequest);
        return "Loan Request Published: " + loanRequest.toString();
    }

    @GetMapping(value = "/loanOffers")
    public List<LoanOffer> loanOffers() {
        return loanService.getOffers();
    }

    @PostMapping(value = "/acceptLoan/{loanId}")
    public String acceptLoan(@PathVariable int loanId) {
        loanService.sendLoanAcceptance(loanId);
        this.loanId = loanId;
        contractHandler.connectQueue(this.loanId);
        return "Loan acceptance sent for loan offer with ID: " + loanId;
    }

    @GetMapping(value = "/download/{loanId}")
    public InputStreamResource downloadContract(HttpServletResponse response, @PathVariable int loanId) {
        String dir = System.getProperty("user.dir");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Contract-"+this.loanId+".pdf\"");
        try {
            return new InputStreamResource(new FileInputStream(dir+"/LoanProducer-Customer/src/main/resources/static/contracts/Contract-"+this.loanId+".pdf"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
