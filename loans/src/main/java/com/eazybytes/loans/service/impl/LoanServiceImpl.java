package com.eazybytes.loans.service.impl;

import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.LoanDTO;
import com.eazybytes.loans.entity.Loan;
import com.eazybytes.loans.exception.LoanAlreadyExistsException;
import com.eazybytes.loans.exception.ResourceNotFoundException;
import com.eazybytes.loans.mapper.LoanMapper;
import com.eazybytes.loans.repository.LoanRepository;
import com.eazybytes.loans.service.ILoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private final LoanRepository loanRepository;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        // Check if loan already registered
        Optional<Loan> existingLoan = loanRepository.findByMobileNumber(mobileNumber);
        if (existingLoan.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already registered with mobile number " + mobileNumber);
        }
        Loan newLoan = createNewLoan(mobileNumber);
        try {
            loanRepository.save(newLoan);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    @Override
    public LoanDTO fetchLoan(String mobileNumber) {
        Loan loan = loanRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Loan associated with mobile number: " + mobileNumber + " not found")
                );
        return LoanMapper.mapToLoanDTO(loan, new LoanDTO());
    }

    /**
     * @param loanDTO - LoansDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateLoan(LoanDTO loanDTO) {
        // Retrieve loan
        Loan loan = loanRepository.findByMobileNumber(loanDTO.getMobileNumber())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Loan associated with mobile number: " + loanDTO.getMobileNumber() + " not found")
                );
        // Update
        LoanMapper.mapToLoan(loanDTO, loan);
        // Save updated loan
        try {
            loanRepository.save(loan);
            return true;
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of loan details is successful or not
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        // Retrieve Loan
        Loan loan = loanRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Loan associated with mobile number: " + mobileNumber + " not found")
                );
        try {
            loanRepository.deleteById(loan.getLoanId());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting loan: " + mobileNumber + "\nContact dev support");
        }
        return true;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new loan details
     */
    private Loan createNewLoan(String mobileNumber) {
        Loan newLoan = new Loan();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }
}
