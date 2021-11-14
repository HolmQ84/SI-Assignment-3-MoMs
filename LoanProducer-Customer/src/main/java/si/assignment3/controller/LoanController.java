package si.assignment3.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import si.assignment3.model.LoanOffer;
import si.assignment3.model.LoanRequest;
import si.assignment3.service.ContractHandler;
import si.assignment3.service.LoanService;

import java.util.List;

@RestController
public class LoanController {

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
        return "Loan acceptance sent for loan offer with ID: " + loanId;
    }

    @GetMapping("/getContract")
    public ModelAndView download() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("download");
        return modelAndView;
    }

    // TODO - Implement endpoint for downloading file.

}
