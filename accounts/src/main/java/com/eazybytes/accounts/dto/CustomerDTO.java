package com.eazybytes.accounts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(
        name = "Customer",
        description = "Schema to hold customer and account information"
)
@Getter
@Setter
@ToString
public class CustomerDTO {

    @Schema(
            description = "Name of the customer",
            example = "Emmanuel"
    )
    @NotEmpty(message = "Name cannot be null or empty")
    private String name;

    @Schema(
            description = "Email address of customer"
    )
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Provide a valid email")
    private String email;

    @Schema(
            description = "Mobile number of customer"
    )
    private String mobileNumber;

    @JsonProperty("account")
    private AccountDTO accountDTO;
}
