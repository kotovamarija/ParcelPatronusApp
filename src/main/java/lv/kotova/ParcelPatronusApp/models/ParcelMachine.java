package lv.kotova.ParcelPatronusApp.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Parcel_machines")
public class ParcelMachine {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "protected")
    private boolean coldProtected; // protected from the cold, as it is located inside the shopping center

    @Column(name = "out_of_order")
    private boolean outOfOrder;

    @Column(name = "available_size_S")
    private int availableBoxesSizeS;

    @Column(name = "available_size_M")
    private int availableBoxesSizeM;

    @Column(name = "available_size_L")
    private int availableBoxesSizeL;

    @Column(name = "available_size_XL")
    private int availableBoxesSizeXL;

    @OneToMany(mappedBy = "dispatchParcelMachine", cascade = CascadeType.ALL)
    private List<DeliveryDetails> outcomingDeliveries;

    @OneToMany(mappedBy = "destinationParcelMachine", cascade = CascadeType.ALL)
    private List<DeliveryDetails> incomingDeliveries;

    public ParcelMachine(String address) {
        this.address = address;
        initialize();
    }

    public ParcelMachine() {
        initialize();
    }

    public void initialize(){
        this.availableBoxesSizeS = 20;
        this.availableBoxesSizeM = 35;
        this.availableBoxesSizeL = 30;
        this.availableBoxesSizeXL = 4;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isColdProtected() {
        return coldProtected;
    }

    public void setColdProtected(boolean coldProtected) {
        this.coldProtected = coldProtected;
    }

    public boolean isOutOfOrder() {
        return outOfOrder;
    }

    public void setOutOfOrder(boolean outOfOrder) {
        this.outOfOrder = outOfOrder;
    }

    public int getAvailableBoxesSizeS() {
        return availableBoxesSizeS;
    }

    public void setAvailableBoxesSizeS(int availableBoxesSizeS) {
        this.availableBoxesSizeS = availableBoxesSizeS;
    }

    public int getAvailableBoxesSizeM() {
        return availableBoxesSizeM;
    }

    public void setAvailableBoxesSizeM(int availableBoxesSizeM) {
        this.availableBoxesSizeM = availableBoxesSizeM;
    }

    public int getAvailableBoxesSizeL() {
        return availableBoxesSizeL;
    }

    public void setAvailableBoxesSizeL(int availableBoxesSizeL) {
        this.availableBoxesSizeL = availableBoxesSizeL;
    }

    public int getAvailableBoxesSizeXL() {
        return availableBoxesSizeXL;
    }

    public void setAvailableBoxesSizeXL(int availableBoxesSizeXL) {
        this.availableBoxesSizeXL = availableBoxesSizeXL;
    }

    public List<DeliveryDetails> getOutcomingDeliveries() {
        return outcomingDeliveries;
    }

    public void setOutcomingDeliveries(List<DeliveryDetails> outcomingDeliveries) {
        this.outcomingDeliveries = outcomingDeliveries;
    }

    public List<DeliveryDetails> getIncomingDeliveries() {
        return incomingDeliveries;
    }

    public void setIncomingDeliveries(List<DeliveryDetails> incomingDeliveries) {
        this.incomingDeliveries = incomingDeliveries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParcelMachine that = (ParcelMachine) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address);
    }

    @Override
    public String toString() {
        return "ParcelMachine{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", coldProtected=" + coldProtected +
                ", outOfOrder=" + outOfOrder +
                ", availableBoxesSizeS=" + availableBoxesSizeS +
                ", availableBoxesSizeM=" + availableBoxesSizeM +
                ", availableBoxesSizeL=" + availableBoxesSizeL +
                ", availableBoxesSizeXL=" + availableBoxesSizeXL +
                '}';
    }
}
