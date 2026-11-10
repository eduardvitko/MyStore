package com.mystore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDto {

    @NotBlank(message = "Назва категорії обов'язкова")
    @Size(min = 2, max = 100, message = "Назва повинна бути від 2 до 100 символів")
    private String name;
}
