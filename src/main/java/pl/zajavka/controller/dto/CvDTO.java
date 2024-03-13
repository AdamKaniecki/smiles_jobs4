package pl.zajavka.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.database.entity.ProgrammingLanguage;

import java.util.Set;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvDTO {
    private Integer id;
    private String name;
    private String surname;
    private String dateOfBirth;
    private String sex;
    private String maritalStatus;
    @Email
    private String contactEmail;
    @Size(min = 7,max = 15)
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String phoneNumber;
    private String education;
    private String workExperience;
    private String skills;
    private Set<ProgrammingLanguage> programmingLanguages;
//    private Set<IT_Specializations> it_specializations;
    private String tools;
    private Integer yearsOfExperience;
    private String language;
    private String languageLevel;
    private String hobby;
    private User user;
    private Address address;
}