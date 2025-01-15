package lv.kotova.ParcelPatronusApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 3, max = 30, message = "Username should be between 3 and 30 characters")
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    @Pattern(regexp = "^(\\+|00)\\d{10,18}$|^2\\d{7}$",
            message = "Invalid phone number format. Must match either international format or local Latvian format")
    private String phoneNumber;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sender")
    private List<Parcel> registeredParcels;

    @OneToOne(mappedBy = "user")
    private Employee employee;

    public User() {}

    public User(String username, String password, String fullName, String phoneNumber, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public @NotEmpty(message = "Username should not be empty") @Size(min = 3, max = 30, message = "Username should be between 3 and 30 characters") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "Username should not be empty") @Size(min = 3, max = 30, message = "Username should be between 3 and 30 characters") String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Parcel> getRegisteredParcels() {
        return registeredParcels;
    }

    public void setRegisteredParcels(List<Parcel> registeredParcels) {
        this.registeredParcels = registeredParcels;
    }
    public void addRegisteredParcels(Parcel parcel){
        this.registeredParcels.add(parcel);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", registeredParcels=" + registeredParcels +
                ", employee=" + employee +
                '}';
    }
}
