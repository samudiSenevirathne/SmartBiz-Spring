package lk.acpt.smartbizspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String username;
    private String password;
    private LocalDate date;
    private LocalTime time;

}
