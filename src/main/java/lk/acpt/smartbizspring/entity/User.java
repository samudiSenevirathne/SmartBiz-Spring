package lk.acpt.smartbizspring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String role;
    private String address;
    private String email;

    @OneToOne(mappedBy = "user")
    private Register register;

    public User(String name, String role, String address, String email) {
        this.name = name;
        this.role = role;
        this.address = address;
        this.email = email;
    }
}