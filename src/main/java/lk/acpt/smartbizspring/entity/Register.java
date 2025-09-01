package lk.acpt.smartbizspring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    @Column(updatable = false)
    private LocalDate date = LocalDate.now();
    @Column(updatable = false)
    private LocalTime time = LocalTime.now();


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Register(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Register(String username, String password,User user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }
}

