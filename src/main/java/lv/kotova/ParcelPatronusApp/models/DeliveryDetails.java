package lv.kotova.ParcelPatronusApp.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lv.kotova.ParcelPatronusApp.models.enums.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Delivery_details")
public class DeliveryDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private Status status;

    @Column(name = "status_tracking")
    private String statusTracking;

    @OneToOne
    @JoinColumn(name = "package_id", referencedColumnName = "id")
    private Parcel parcel;

    @Column(name = "cell_ID_dispatch", columnDefinition = "int default 0")
    private Integer cellIdDispatch = 0;

    @Column(name = "cell_ID_destination", columnDefinition = "int default 0")
    private Integer cellIdDestination = 0;

    @ManyToOne
    @JoinColumn(name = "dispatch_machine_ID", referencedColumnName = "id")
    private ParcelMachine dispatchParcelMachine;

    @ManyToOne
    @JoinColumn(name = "courier_id", referencedColumnName = "id")
    private Employee courier;

    @ManyToOne
    @JoinColumn(name = "warehouse_manager_id", referencedColumnName = "id")
    private Employee warehouseManager;

    @ManyToOne
    @JoinColumn(name = "destination_machine_ID", referencedColumnName = "id")
    private ParcelMachine destinationParcelMachine;

    @Column(name = "recipient")
    @NotEmpty(message = "Enter recipient")
    private String recipient;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "recipients_phone")
    @Pattern(regexp = "^(\\+|00)\\d{10,18}$|^2\\d{7}$",
            message = "Invalid phone number format. Must match either international format or local Latvian format")
    @NotEmpty(message = "Enter recipient's phone number")
    private String recipientsPhoneNumber;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "door_code")
    private String doorCode;

    public DeliveryDetails() {}

    public DeliveryDetails(Parcel parcel, ParcelMachine parcelMachineFrom, ParcelMachine parcelMachineTo) {
        this.status = Status.AWAITING_PICKUP;
        this.parcel = parcel;
        this.dispatchParcelMachine = parcelMachineFrom;
        this.destinationParcelMachine = parcelMachineTo;
    }

    public void setTrackingNumber() {
        this.trackingNumber = "LV2025PP" + dispatchParcelMachine.getId() +
                destinationParcelMachine.getId() + System.currentTimeMillis();
    }

    public void setDoorCode() {
        this.doorCode = String.valueOf(destinationParcelMachine.getId()) +
                dispatchParcelMachine.getId() + (System.currentTimeMillis() % 10000);
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getDoorCode() {
        return doorCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        addStatusTracking(status);
    }

    public ParcelMachine getDispatchParcelMachine() {
        return dispatchParcelMachine;
    }

    public void setDispatchParcelMachine(ParcelMachine dispatchParcelMachine) {
        this.dispatchParcelMachine = dispatchParcelMachine;
    }

    public ParcelMachine getDestinationParcelMachine() {
        return destinationParcelMachine;
    }

    public void setDestinationParcelMachine(ParcelMachine destinationParcelMachine) {
        this.destinationParcelMachine = destinationParcelMachine;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcelToDeliver) {
        this.parcel = parcelToDeliver;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientsPhoneNumber() {
        return recipientsPhoneNumber;
    }

    public void setRecipientsPhoneNumber(String recipientsPhoneNumber) {
        this.recipientsPhoneNumber = recipientsPhoneNumber;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Employee getCourier() {
        return courier;
    }

    public void setCourier(Employee courier) {
        this.courier = courier;
    }

    public Employee getWarehouseManager() {
        return warehouseManager;
    }

    public void setWarehouseManager(Employee warehouseManager) {
        this.warehouseManager = warehouseManager;
    }

    public Integer getCellIdDispatch() {
        return cellIdDispatch;
    }

    public void setCellIdDispatch(Integer cellIdDispatch) {
        this.cellIdDispatch = cellIdDispatch;
    }

    public Integer getCellIdDestination() {
        return cellIdDestination;
    }

    public void setCellIdDestination(Integer cellIdDestination) {
        this.cellIdDestination = cellIdDestination;
    }

    public String getStatusTracking() {
        return statusTracking;
    }

    public void setStatusTracking(Status status) {
        this.statusTracking = "Status " + status + " - on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void addStatusTracking(Status status) {
        this.statusTracking = this.statusTracking + "; " + System.lineSeparator() + "Status changed to " + status + " on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getFormattedStatusTracking() {
        return this.statusTracking.replace(System.lineSeparator(), "<br/>");
    }

    @Override
    public String toString() {
        return "DeliveryDetails{" +
                "id=" + id +
                ", status=" + status +
                ", statusTracking='" + statusTracking + '\'' +
                ", parcel=" + parcel +
                ", cellIdDispatch=" + cellIdDispatch +
                ", cellIdDestination=" + cellIdDestination +
                ", dispatchParcelMachine=" + dispatchParcelMachine +
                ", courier=" + courier +
                ", warehouseManager=" + warehouseManager +
                ", destinationParcelMachine=" + destinationParcelMachine +
                ", recipient='" + recipient + '\'' +
                ", createdAt=" + createdAt +
                ", recipientsPhoneNumber='" + recipientsPhoneNumber + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", doorCode='" + doorCode + '\'' +
                '}';
    }
}
