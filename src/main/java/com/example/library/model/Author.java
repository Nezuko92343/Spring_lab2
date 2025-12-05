package com.example.library.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Author entity representing a book author")
public class Author {

    @Schema(description = "Unique identifier of the author", example = "1")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    @Schema(description = "Full name of the author", example = "Stephen King", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    @Schema(description = "Country of origin", example = "USA")
    private String country;

    @Schema(description = "Year of birth", example = "1947")
    private Integer birthYear;
}
