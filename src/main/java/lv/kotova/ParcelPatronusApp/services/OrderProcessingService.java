package lv.kotova.ParcelPatronusApp.services;

import lv.kotova.ParcelPatronusApp.models.DeliveryDetails;
import lv.kotova.ParcelPatronusApp.models.Parcel;
import lv.kotova.ParcelPatronusApp.models.ParcelMachine;
import lv.kotova.ParcelPatronusApp.models.User;
import lv.kotova.ParcelPatronusApp.models.enums.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class OrderProcessingService {

    private final ParcelService parcelService;
    private final DeliveryDetailsService deliveryDetailsService;
    private final ParcelMachineService parcelMachineService;

    public OrderProcessingService(ParcelService parcelService, DeliveryDetailsService deliveryDetailsService, ParcelMachineService parcelMachineService) {
        this.parcelService = parcelService;
        this.deliveryDetailsService = deliveryDetailsService;
        this.parcelMachineService = parcelMachineService;
    }

    @Transactional
    public void create(User user, Parcel parcel, DeliveryDetails deliveryDetails, String machine1, String machine2) {

        parcel.setSender(user);
        parcel.setSize();
        parcel.setDeliveryDetails(deliveryDetails);
        parcel.setCreatedAt(LocalDateTime.now());

        deliveryDetails.setParcel(parcel); // will do smth about LAZY loading later
        deliveryDetails.setStatus(Status.AWAITING_PICKUP);
        deliveryDetails.setStatusTracking(Status.AWAITING_PICKUP);
        deliveryDetails.setDispatchParcelMachine(parcelMachineService.findByAddress(machine1));
        deliveryDetails.setDestinationParcelMachine(parcelMachineService.findByAddress(machine2));
        deliveryDetails.setCreatedAt(LocalDateTime.now());
        deliveryDetails.setTrackingNumber();
        deliveryDetails.setDoorCode();

        assignBox(parcelMachineService.findByAddress(machine1), parcel);

        parcelService.save(parcel);

        deliveryDetailsService.setCellIdDispatch(parcel);
        deliveryDetailsService.save(deliveryDetails);

    }



    public void assignBox(ParcelMachine parcelMachine, Parcel parcel) {
        changeOccupancy(parcelMachine, parcel, -1);
    }
    public void releaseBox(ParcelMachine parcelMachine, Parcel parcel) {
        changeOccupancy(parcelMachine, parcel, 1);
    }

    private void changeOccupancy(ParcelMachine parcelMachine, Parcel parcel, int adjustment) {
        switch(parcel.getSize()){
            case S -> parcelMachine.setAvailableBoxesSizeS((parcelMachine.getAvailableBoxesSizeS() + adjustment));
            case M -> parcelMachine.setAvailableBoxesSizeM((parcelMachine.getAvailableBoxesSizeM() + adjustment));
            case L -> parcelMachine.setAvailableBoxesSizeL((parcelMachine.getAvailableBoxesSizeL() + adjustment));
            case XL -> parcelMachine.setAvailableBoxesSizeXL((parcelMachine.getAvailableBoxesSizeXL() + adjustment));
        }
    }

}
