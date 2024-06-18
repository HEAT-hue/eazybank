package com.eazybytes.accounts.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(
        name = "Account",
        description = "Account details format"
)
@Getter
@Setter
@ToString
public class AccountDTO {

    @Schema(
            description = "Account number",
            example = "0201206624"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "AccountNumber must be 10 digits")
    private String accountNumber;

    @JsonIgnore
    private Long customerId;

    @NotEmpty(message = "AccountType can not be a null or empty")
    private String accountType;

    @NotEmpty(message = "BranchAddress can not be a null or empty")
    private String branchAddress;
}
