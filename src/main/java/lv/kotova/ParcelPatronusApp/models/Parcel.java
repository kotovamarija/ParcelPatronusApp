package lv.kotova.ParcelPatronusApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import lv.kotova.ParcelPatronusApp.models.enums.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "Parcels")
public class Parcel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "weight")
    @DecimalMax(value = "20.0", message = "Maximum accepted weight is 15 kilograms per 1 package")
    private Double weight;

    @Column(name = "length")
    @Max(value = 65, message = "Maximum accepted length is 65 centimeters")
    private int length;

    @Column(name = "width")
    @Max(value = 40, message = "Maximum accepted width is 40 centimeters")
    private int width;

    @Column(name = "height")
    @Max(value = 35, message = "Maximum accepted height is 35 centimeters")
    private int height;

    @Transient
    private double dimensionalWeight;

    @Column(name = "size")
    @Enumerated(EnumType.STRING)
    private Size size;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @OneToOne(mappedBy = "parcel", cascade = CascadeType.PERSIST)
    private DeliveryDetails deliveryDetails;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Parcel() {
        setSize();
    }

    public Parcel(Double weight, int length, int width, int height) {
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        setSize();
    }

    public void setSize(){
        this.dimensionalWeight = (double) (this.length * this.width * this.height) / 5000;
        // Take the dimensions of the package in centimeters, multiply them together, then divide by 5000.
        // (Length × Width × Height) / 5000

        if (dimensionalWeight <= 5) {
            this.size = Size.S;
        }
        else if (dimensionalWeight <= 10) {
            this.size = Size.M;
        }
        else if (dimensionalWeight <= 15) {
            this.size = Size.L;
        }
        else {
            this.size = Size.XL;
        }
    }

    public Size getSize() {
        return size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public double getDimensionalWeight() {
        return dimensionalWeight;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "id=" + id +
                ", weight=" + weight +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", dimensionalWeight=" + dimensionalWeight +
                ", size=" + size +
                ", createdAt=" + createdAt +
                '}';
    }
}