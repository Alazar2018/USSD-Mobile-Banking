package com.example.gebeya.ussd.ussdbankingdemo.Services.Implementations;

import com.example.gebeya.ussd.ussdbankingdemo.Exceptions.*;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Account;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Customer;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.MobileBankingUser;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Entity.Transaction;
import com.example.gebeya.ussd.ussdbankingdemo.Model.Enum.Status;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.AccountRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.CustomerRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Repository.TransactionRepository;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.AccountService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.HistoryService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.MobileBankingUserService;
import com.example.gebeya.ussd.ussdbankingdemo.Services.Interfaces.TransactionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MobileBankingUserService mobileBankingUserService;

    @Autowired
    private HistoryService historyService;

    private final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public List<Account> getAllAccounts() {
        log.info("getting all account");
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountByNum(String accountNum) throws AccountNotFoundException {
        log.info("get account with account number: {}", accountNum);
        return accountRepository.findById(Integer.valueOf(accountNum)).orElseThrow(() -> new AccountNotFoundException());
    }


    @Override
    public List<Account> getAllAccountsOfCustomer(Customer customer) {
        log.info("getting all account by customer: {}", customer.getCif());
        return accountRepository.findAllByCustomerCif(customer.getCif());
    }

    @Override
    public Account saveAccount(Account account) {
        log.info("saving account: {}", account.getAccountNum());
        return accountRepository.save(account);
    }

    @Override
    public Account saveAccountForCustomer(int cif, Account account) throws CustomerNotFoundException {
        log.info("saving account({}) for customer ({})", account.getAccountNum(), cif);
        Customer customer = customerRepository.findById(cif)
                .orElseThrow(() -> new CustomerNotFoundException());
        account.setAccountNum(Integer.parseInt(customer.getAccount_number()));
        account.setCustomer(customer);
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public String depositMoney(String accountNumber, BigDecimal amount) throws AccountNotFoundException, MobileBankingUserNotFoundException, InsufficientBalanceException {
        log.info("deposit {} money to account {}", amount, accountNumber);
        Account account = accountRepository.findById(Integer.valueOf(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Account not found with account number: " + accountNumber));

        boolean isMerchant = checkMerchantUser(account);
        boolean isUser = checkCustomerUser(account);
        if (isUser && account.getAccountStatus() != Status.INACTIVE) {
            if (amount.compareTo(account.getBalance()) > 0) {
                throw new InsufficientBalanceException("Insufficient balance for withdrawal from account: " + account.getAccountNum());
            }

            // Generate OTP
            String otp = generateOTP();
            // Set expiration time to 30 min from now
            LocalDateTime expireDate = LocalDateTime.now().plusMinutes(30);
            transactionService.depositTransaction(account, amount, otp, expireDate);

            // Record the deposit transaction
            return "The Generated OTP is : " + otp + " This will also expire in : " + expireDate;
        }
        if (isMerchant && account.getAccountStatus() != Status.INACTIVE) {
            if (amount.compareTo(account.getBalance()) > 0) {
                throw new InsufficientBalanceException("Insufficient balance for withdrawal from account: " + account.getAccountNum());
            }

            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
            transactionService.merchantWithdrawTransaction(account, amount);
            return "Deposit was successful of this amount " + amount + " from this Merchant account :- " + account.getAccountNum() + " ";
        }
        return "The account user is not a mobile banking user so please sign up first Or In Active account number";
    }

    @Override
    @Transactional
    public String verifyDeposit(String accountNumber, String customerAccountNumber, String OTP) throws AccountNotFoundException, InsufficientBalanceException, TransactionNotFoundException, MobileBankingUserNotFoundException {
        log.info("verifying deposit for account {}", accountNumber);
        // Implementation for verifyDeposit method
        // Find the merchant account
        Account merchantAccount = accountRepository.findById(Integer.valueOf(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Merchant account not found with account number: " + accountNumber));

        // Find the customer account by account number
        Account customerAccount = accountRepository.findById(Integer.valueOf(customerAccountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Customer account not found with account number: " + customerAccountNumber));
        boolean isCustomer=checkCustomerUser(customerAccount);
        boolean isMerchant=checkMerchantUser(merchantAccount);
        if(isCustomer){
            if(isMerchant) {
                // Retrieve the transactions from the database based on the customer account
                List<Transaction> transactions = transactionRepository.findByAccount(customerAccount);

                // Check if there are any transactions for the given account
                if (transactions.isEmpty()) {
                    throw new TransactionNotFoundException("Transaction not found for account: " + customerAccountNumber);
                }

                // Check if any transaction has a matching OTP
                for (Transaction checkDeposit : transactions) {
                    Transaction currentTransaction = transactionRepository.findById(checkDeposit.getRrn())
                            .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + checkDeposit.getRrn()));
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    // Check if OTP is correct and not expired
                    if (OTP.equals(currentTransaction.getOTP())&&currentDateTime.isBefore(currentTransaction.getOtpexpireddate())&&currentTransaction.getSide()==Status.DEPOSIT) {
                        // Update customer account balance
                        if (currentTransaction.getAmount().compareTo(merchantAccount.getBalance()) > 0) {
                            throw new com.example.gebeya.ussd.ussdbankingdemo.Exceptions.InsufficientBalanceException("Insufficient balance for withdrawal from account: " + merchantAccount.getAccountNum());
                        }
                        BigDecimal newBalance = customerAccount.getBalance().add(currentTransaction.getAmount());
                        BigDecimal merchantNewBalance = merchantAccount.getBalance().subtract(currentTransaction.getAmount());
                        customerAccount.setBalance(newBalance);
                        merchantAccount.setBalance(merchantNewBalance);
                        Transaction merchantTransaction=new Transaction();
                        merchantTransaction.setAccount(merchantAccount);
                        merchantTransaction.setCustomerAccount(String.valueOf(customerAccount.getAccountNum()));
                        merchantTransaction.setAmount(currentTransaction.getAmount());
                        transactionService.saveMerchantDepositTransaction(merchantTransaction);
                        currentTransaction.setRespCode("Deposited succesfully by merchant");
                        currentTransaction.setTransactionStatus(Status.SUCCESSFUL);
                        currentTransaction.setOTP("");
                        currentTransaction.setOtpexpireddate(null);
                        historyService.saveHistoryForTransaction(currentTransaction);

                        transactionRepository.save(currentTransaction);
                        return "Successfully Deposited from : " + customerAccountNumber + "an amount of" +currentTransaction.getAmount()+" "+ "New balance of Merchant:+ " + merchantNewBalance;
                    }
                }



            }else{ return "This is not a merchant account please go to active merchant";}
        }else{
            return "Customer has not signed in mobile banking please signup first!";
        }
        return "Deposit to " + customerAccountNumber + " failed. Invalid OTP or expired.";
    }

    @Override
    @Transactional
    public String verifyWithdraw(String accountNumber, String customerAccountNumber, String OTP)
            throws AccountNotFoundException, TransactionNotFoundException, InsufficientBalanceException,
            MobileBankingUserNotFoundException {
        log.info("verifying withdraw for account {}", accountNumber);

        // Implementation for verifyWithdraw method
        // Find the merchant account
        Account merchantAccount = accountRepository.findById(Integer.valueOf(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Merchant account not found with account number: " + accountNumber));

        // Find the customer account by account number
        Account customerAccount = accountRepository.findById(Integer.valueOf(customerAccountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Customer account not found with account number: " + customerAccountNumber));

        boolean isCustomer = checkCustomerUser(customerAccount);
        boolean isMerchant = checkMerchantUser(merchantAccount);

        if (isCustomer) {
            if (isMerchant) {
                // Retrieve the transactions from the database based on the customer account
                List<Transaction> transactions = transactionRepository.findByAccount(customerAccount);

                // Check if there are any transactions for the given account
                if (transactions.isEmpty()) {
                    throw new TransactionNotFoundException("Transaction not found for account: " + customerAccountNumber);
                }
                // Check if any transaction has a matching OTP
                for (Transaction checkDeposit : transactions) {
                    Transaction currentTransaction = transactionRepository.findById(checkDeposit.getRrn())
                            .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + checkDeposit.getRrn()));
                    // Check if OTP is correct and not expired
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    if (OTP.equals(currentTransaction.getOTP()) && currentDateTime.isBefore(currentTransaction.getOtpexpireddate())
                            && currentTransaction.getSide() == Status.WITHDRAW) {
                        // Update customer account balance
                        if (currentTransaction.getAmount().compareTo(merchantAccount.getBalance()) > 0) {
                            throw new InsufficientBalanceException(
                                    "Insufficient balance for withdrawal from account: " + merchantAccount.getAccountNum());
                        } else {
                            BigDecimal newBalance = customerAccount.getBalance().subtract(currentTransaction.getAmount());
                            BigDecimal merchantNewBalance = merchantAccount.getBalance().add(currentTransaction.getAmount());
                            customerAccount.setBalance(newBalance);
                            merchantAccount.setBalance(merchantNewBalance);
                            Transaction merchantTransaction = new Transaction();
                            merchantTransaction.setAccount(merchantAccount);
                            merchantTransaction.setCustomerAccount(String.valueOf(customerAccount.getAccountNum()));
                            merchantTransaction.setAmount(currentTransaction.getAmount());
                            transactionService.saveMerchantWithdrawTransaction(merchantTransaction);
                            currentTransaction.setRespCode("Withdrawn successfully by merchant");
                            currentTransaction.setTransactionStatus(Status.SUCCESSFUL);
                            currentTransaction.setOTP("");
                            currentTransaction.setOtpexpireddate(null);
                            historyService.saveHistoryForTransaction(currentTransaction);
                            transactionRepository.save(currentTransaction);
                            return "Successfully withdrawn from : " + customerAccountNumber + " an amount of "
                                    + currentTransaction.getAmount() + " " + "New balance of Merchant: " + merchantNewBalance;
                        }
                    }
                }
            } else {
                return "This is not a merchant user";
            }
        } else {
            return "Customer needs to sign up for mobile banking user";
        }

        return "Withdraw from " + customerAccountNumber + " failed. Invalid OTP or expired.";
    }


    @Override
    @Transactional
    public String withdrawMoney(String accountNumber, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException {
        log.info("withdrawing money from account {}", accountNumber);
        // Implementation for withdrawMoney method
        Account account = accountRepository.findById(Integer.valueOf(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Account not found with account number: " + accountNumber));

        boolean isMerchant=checkMerchantUser(account);
        boolean isUSer=checkCustomerUser(account);
        if(isUSer&&account.getAccountStatus()!=Status.INACTIVE) {
            if (amount.compareTo(account.getBalance()) > 0) {
                throw new InsufficientBalanceException("Insufficient balance for withdrawal from account: " + account.getAccountNum());
            }

            // Generate OTP
            String otp = generateOTP();
            // Set expiration time to 30 min from now
            LocalDateTime expireDate = LocalDateTime.now().plusMinutes(30);
            transactionService.withdrawTransaction(account, amount, otp, expireDate);

            // Record the deposit transaction
            return "The Generated OTP is : " + otp + " This will also expire in : " + expireDate;
        }if(isMerchant&&account.getAccountStatus()!=Status.INACTIVE){
            if (amount.compareTo(account.getBalance()) > 0) {
                throw new InsufficientBalanceException("Insufficient balance for withdrawal from account: " + account);
            }else {
                BigDecimal newBalance = account.getBalance().subtract(amount);
                account.setBalance(newBalance);
                transactionService.merchantWithdrawTransaction(account, amount);
                return "Withdraw was successful of this amount " + amount + " from this Merchant account :- " + account.getAccountNum() + " ";
            }
        }
        return "The account user is not a mobile banking user so please signup first Or In Active account number";
    }

    @Override
    @Transactional
    public String transactionMoney(String senderAccountNumber, String recieverAccountNumber, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException, MobileBankingUserNotFoundException {
        log.info("transfer {} money from {} to {}", amount, senderAccountNumber, recieverAccountNumber);
        // Implementation for transactionMoney method
        Account senderaccount = accountRepository.findById(Integer.valueOf(senderAccountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Account not found with account number: " + senderAccountNumber));
        Account recieveraccount = accountRepository.findById(Integer.valueOf(recieverAccountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Account not found with account number: " + recieverAccountNumber));
        boolean isUser=checkCustomerUser(senderaccount);
        boolean isMerchant=checkMerchantUser(senderaccount);
        if(isUser||isMerchant) {
            if (senderaccount.getAccountStatus() != Status.INACTIVE && recieveraccount.getAccountStatus() != Status.INACTIVE) {
                // Update account balance
                if (amount.compareTo(senderaccount.getBalance()) > 0) {
                    throw new InsufficientBalanceException("Insufficient balance for withdrawal from account: " + senderAccountNumber);
                }
                BigDecimal newSenderBalance = senderaccount.getBalance().subtract(amount);
                BigDecimal newRecieverBalance = recieveraccount.getBalance().add(amount);
                senderaccount.setBalance(newSenderBalance);
                recieveraccount.setBalance(newRecieverBalance);

                // Save the updated account
                Account updatedSenderAccount = accountRepository.save(senderaccount);
                Account updatedRecieverAccount = accountRepository.save(recieveraccount);

                // Record the deposit transaction
                transactionService.TransferTransaction(updatedSenderAccount, amount);
                transactionService.TransferTransaction(updatedRecieverAccount, amount);
                return "Transfer have been made Successfully from your account : " + updatedSenderAccount.getAccountNum() + " " + "to: " + updatedRecieverAccount.getAccountNum() + " an amount of " + amount;
            }else{
                if(senderaccount.getAccountStatus()!=Status.INACTIVE) {
                    return "Transaction has failed because this account "+recieveraccount.getAccountNum()+" is INACTIVE";
                }else{
                    return "Transaction has failed because this account "+senderaccount.getAccountNum()+" is INACTIVE";
                }

            }
        }
        return "Transaction has failed register to moblie banking please :";
    }

    @Override
    @Transactional
    public String updateAccount(Account account) {
        log.info("updating account {}", account.getAccountNum());
        // Implementation for updateAccount method
        try {
            // Find the existing account
            Account existingAccount = accountRepository.findById(account.getAccountNum())
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with account number: " + account.getAccountNum()));

            // Update fields of the existing account with the values from the provided account
            if (existingAccount != null) {
                //existingAccount.setBalance(account.getBalance());
                existingAccount.setUpdatedAt(LocalDateTime.now());
                existingAccount.setAccountStatus(account.getAccountStatus());
                // ... other fields you want to update

                // Save the updated account
                accountRepository.save(existingAccount);

                return "Account updated successfully";
            }

        } catch (AccountNotFoundException e) {
            e.printStackTrace(); // Log the exception or use a logging framework
            return "Account update failed. " + e.getMessage();
        }
        return "Account update failed. ";
    }

    @Override
    @Transactional
    public boolean deleteAccount(int accountNum) {
        log.info("deleting account {}", accountNum);
        // Implementation for deleteAccount method
        Optional<Account> accountOptional = accountRepository.findById(accountNum);

        if (accountOptional.isPresent()) {
            accountRepository.deleteById(accountNum);
            return true; // Deletion successful
        } else {
            return false; // Account not found
        }
    }
    @Override
    public boolean checkCustomerUser(Account account) throws MobileBankingUserNotFoundException {
        log.info("check customer user {}", account.getAccountNum());
        MobileBankingUser mobileBankingUser = mobileBankingUserService.getMobileBankingUserDetailsForCustomer(account.getCustomer().getCif());
        boolean isUser = false;

        if (mobileBankingUser != null) {
            // Use equals to compare strings, and ignore case for better robustness
            if ("Default".equalsIgnoreCase(mobileBankingUser.getCustomerProfile())) {
                isUser = true;
            }
        }

        return isUser;
    }
    @Override
    public boolean checkMerchantUser(Account account) throws MobileBankingUserNotFoundException {
        log.info("check merchant user for account {}", account.getAccountNum());
        MobileBankingUser mobileBankingUser = mobileBankingUserService.getMobileBankingUserDetailsForCustomer(account.getCustomer().getCif());
        boolean isUser = false;

        if (mobileBankingUser != null) {
            // Use equals to compare strings, and ignore case for better robustness
            if ("Merchant".equalsIgnoreCase(mobileBankingUser.getCustomerProfile())) {
                isUser = true;
            }
        }

        return isUser;
    }

    // Generate auto-generated OTP
    private String generateOTP() {
        int length = 6;
        StringBuilder otp = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}
