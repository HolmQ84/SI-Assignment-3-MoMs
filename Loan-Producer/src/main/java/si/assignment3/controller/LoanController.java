package si.assignment3.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import si.assignment3.model.LoanOffer;
import si.assignment3.model.LoanRequest;
import si.assignment3.service.LoanService;

import java.util.*;

@RestController
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping(value = "/loanrequest")
    public String loanRequest(@RequestBody LoanRequest loanRequest) {
        loanService.sendLoanRequest(loanRequest);
        return "Loan Request Published: " + loanRequest.toString();
    }

    @GetMapping(value = "/loanOffers")
    public List<LoanOffer> loanOffers() {
        return loanService.getOffers();
    }
}
