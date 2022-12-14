package com.svj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.svj.annotation.OnlyCharacters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequestDTO{
    @NotBlank(message= "First name shouldn't be NULL OR EMPTY")
    @OnlyCharacters(message= "First name should contain only alphabets")
    private String firstName;
    @NotBlank
    private String lastName;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
    @FutureOrPresent(message = "We do not support backtracked purchase entries from app. Please contact admin")
    private LocalDateTime dateTimeOfPurchase;
}
