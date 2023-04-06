package com.w2n.challenge.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewHeroDTO {

    @NotBlank(message = "'name' field cannot be null or empty.")
    @Size(max = 50, message = "The maximum length of the 'name' field must not be greater than 50.")
    String name;

    @NotBlank(message = "'universe' field cannot be null or empty.")
    @Size(max = 50, message = "The maximum length of the 'universe' field must not be greater than 50.")
    String universe;

    @Min(value = 1800, message = "The year of appearance must be greater than 1800.")
    @Max(value = 3000, message = "The year of appearance must be less than 3000.")
    Integer firstApparition;

}
