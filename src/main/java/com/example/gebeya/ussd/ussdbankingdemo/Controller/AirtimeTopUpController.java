package com.example.gebeya.ussd.ussdbankingdemo.Controller;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Enum.AirtimeResponseDTO;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.gebeya.ussd.ussdbankingdemo.Model.DTO.AirtimeTopUpDTO;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.AccountService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.HistoryService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1.2/users/")
public class AirtimeTopUpController {

    @Autowired
    private AccountService accountService;
    // Setter method for HistoryService
    @Autowired
    private HistoryService historyService;
    @PostMapping("/airtime-top-up")
    public ResponseEntity<?> airtimeTopUp(@RequestBody AirtimeTopUpDTO airtimeTopUpDTO) {
        if (airtimeTopUpDTO.getAmount() == null || StringUtils.isBlank(airtimeTopUpDTO.getAccount())) {
            return new ResponseEntity<>("Invalid Inputs: amount is null or account is empty", HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.getAccountByNum(Integer.parseInt(airtimeTopUpDTO.getAccount()));
        BigDecimal amount = airtimeTopUpDTO.getAmount();

        if (account == null) {
            return new ResponseEntity<>("Invalid Inputs: Account doesn't exist", HttpStatus.NOT_FOUND);
        }

        WebClient webClient = WebClient.create();
        String url = "http://192.168.1.43:9090/mtelecom/topup/" + amount;

        AirtimeResponseDTO dto = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(AirtimeResponseDTO.class)
                .block();

        if (dto == null) {
            return new ResponseEntity<>("Error occurred during airtime top-up", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountService.updateAccount(account);
        historyService.saveHistoryForAccountUpdate(account);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

}
