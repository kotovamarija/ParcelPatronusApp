package lv.kotova.ParcelPatronusApp.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "position")
public class Employee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "position")
    private String position;

    @OneToMany(mappedBy = "courier")
    private List<DeliveryDetails> assignedDeliveries;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<DeliveryDetails> getAssignedDeliveries() {
        return assignedDeliveries;
    }

    public void setAssignedDeliveries(List<DeliveryDetails> assigned_deliveries) {
        this.assignedDeliveries = assigned_deliveries;
    }

    public void addNewTask(DeliveryDetails deliveryDetails) {
        this.assignedDeliveries.add(deliveryDetails);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", position='" + position + '\'' +
                ", assignedDeliveries=" + assignedDeliveries +
                ", user=" + user +
                '}';
    }
}
