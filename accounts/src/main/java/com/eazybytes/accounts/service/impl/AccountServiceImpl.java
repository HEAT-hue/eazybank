package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountConstants;
import com.eazybytes.accounts.dto.AccountDTO;
import com.eazybytes.accounts.dto.CustomerDTO;
import com.eazybytes.accounts.entity.Account;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    /**
     * @param customerDTO - CustomerDTO Object
     */
    @Override
    public void createAccount(CustomerDTO customerDTO) {
        // Get mobile number
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDTO.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobile number: " + customerDTO.getMobileNumber());
        }
        Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer customerSaved = customerRepository.save(customer);
        Account newAccount = createNewAccount(customerSaved);
        accountRepository.save(newAccount);
    }

    @Override
    public CustomerDTO fetchAccount(String mobileNumber) {
        // Get mobile number
        Customer customer = customerRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User does not exist with given mobile number: " + mobileNumber)
                );

        Account account = accountRepository
                .findByCustomerId(customer.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found! CustomerId: " + customer.getCustomerId())
                );

        AccountDTO accountDTO = AccountMapper.mapToAccountDTO(account, new AccountDTO());
        return CustomerMapper.mapToCustomerDto(customer, new CustomerDTO(), accountDTO);
    }

    @Override
    public boolean updateAccount(CustomerDTO customerDTO) {
        AccountDTO accountDTO = customerDTO.getAccountDTO();

        if (null == accountDTO) {
            return false;
        }

        // Retrieve stale account
        Account account = accountRepository
                .findById(Long.valueOf(accountDTO.getAccountNumber()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found! " + accountDTO.getAccountNumber())
                );

        // Update stale account
        AccountMapper.mapToAccount(accountDTO, account, false);
        accountRepository.save(account);

        // Retrieve stale customer data
        Long customerId = account.getCustomerId();
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found! " + customerId)
                );

        // Update stale customer data
        CustomerMapper.mapToCustomer(customerDTO, customer);
        customerRepository.save(customer);
        return true;
    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        // Get mobile number
        Customer customer = customerRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User does not exist with given mobile number: " + mobileNumber)
                );

        try {
            customerRepository.deleteById(customer.getCustomerId());
            accountRepository.deleteByCustomerId(customer.getCustomerId());
        } catch (Exception ex) {
            throw new RuntimeException("Error deleting account");
        }
        return true;
    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     * @description Create new customer account
     */
    private Account createNewAccount(Customer customer) {
        Account newAccount = new Account();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = (long) (new Random().nextDouble() * 1_000_000_0000L) + 1_000_000_0000L;
        newAccount.setAccountNumber("" + randomAccNumber);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Anonymous");
        return newAccount;
    }
}
