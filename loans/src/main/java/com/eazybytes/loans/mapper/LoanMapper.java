package com.eazybytes.loans.mapper;

import com.eazybytes.loans.dto.LoanDTO;
import com.eazybytes.loans.entity.Loan;

public final class LoanMapper {
    private LoanMapper() {

    }

    public static LoanDTO mapToLoanDTO(Loan loan, LoanDTO loanDto) {
        loanDto.setLoanNumber(loan.getLoanNumber());
        loanDto.setLoanType(loan.getLoanType());
        loanDto.setMobileNumber(loan.getMobileNumber());
        loanDto.setTotalLoan(loan.getTotalLoan());
        loanDto.setAmountPaid(loan.getAmountPaid());
        loanDto.setOutstandingAmount(loan.getOutstandingAmount());
        return loanDto;
    }

    public static Loan mapToLoan(LoanDTO loanDTO, Loan loan) {
        loan.setLoanNumber(loanDTO.getLoanNumber());
        loan.setLoanType(loanDTO.getLoanType());
        loan.setMobileNumber(loanDTO.getMobileNumber());
        loan.setTotalLoan(loanDTO.getTotalLoan());
        loan.setAmountPaid(loanDTO.getAmountPaid());
        loan.setOutstandingAmount(loanDTO.getOutstandingAmount());
        return loan;
    }
}
