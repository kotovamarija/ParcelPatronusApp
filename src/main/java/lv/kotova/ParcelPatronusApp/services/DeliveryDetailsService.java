package lv.kotova.ParcelPatronusApp.services;

import lv.kotova.ParcelPatronusApp.models.DeliveryDetails;
import lv.kotova.ParcelPatronusApp.models.Employee;
import lv.kotova.ParcelPatronusApp.models.Parcel;
import lv.kotova.ParcelPatronusApp.models.enums.Status;
import lv.kotova.ParcelPatronusApp.repositories.DeliveryDetailsRepository;
import lv.kotova.ParcelPatronusApp.util.DeliveryDetailsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class DeliveryDetailsService {

    private final DeliveryDetailsRepository deliveryDetailsRepository;
    private final EmployeeService employeeService;
    private final OrderProcessingService orderProcessingService;
    private final Random random = new Random();

    @Autowired
    public DeliveryDetailsService(DeliveryDetailsRepository deliveryDetailsRepository, EmployeeService employeeService, @Lazy OrderProcessingService orderProcessingService) {
        this.deliveryDetailsRepository = deliveryDetailsRepository;
        this.employeeService = employeeService;
        this.orderProcessingService = orderProcessingService;
    }

    public List<DeliveryDetails> findAll(){
        return deliveryDetailsRepository.findAll();
    }

    public List<DeliveryDetails> findByEmployee(Employee employee){
        return deliveryDetailsRepository.findByCourierIdOrWarehouseManagerId(employee.getId(), employee.getId());
    }

    public List<DeliveryDetails> findByEmployeeIdAndStatusOrderByDispatch(int employeeId, Status status){
        return deliveryDetailsRepository.findByStatusLikeAndCourierIdOrWarehouseManagerIdOrderByDispatchParcelMachineAsc(status, employeeId, employeeId);
    }

    public List<DeliveryDetails> findByEmployeeIdAndStatusOrderByDestination(int employeeId, Status status){
        return deliveryDetailsRepository.findByStatusLikeAndCourierIdOrWarehouseManagerIdOrderByDestinationParcelMachineAsc(status, employeeId, employeeId);
    }

    public DeliveryDetails findById(int id){
        Optional<DeliveryDetails> foundDeliveryDetails = deliveryDetailsRepository.findById(id);
        return foundDeliveryDetails.orElseThrow(DeliveryDetailsNotFoundException::new);
    }

    @Transactional
    public void save(DeliveryDetails deliveryDetails) {
        deliveryDetailsRepository.save(deliveryDetails);
    }

    public List<DeliveryDetails> findParcelsToPickup() {
        List<DeliveryDetails> details = new ArrayList<>();
        details.addAll(deliveryDetailsRepository.findByStatus(Status.AWAITING_PICKUP));
        details.addAll(deliveryDetailsRepository.findByStatus(Status.RETURNED_TO_DELIVERY_BOX));
        details.addAll(deliveryDetailsRepository.findByStatus(Status.UNCOLLECTED_PARCEL));
        return details;
    }

    public List<DeliveryDetails> findParcelsToDeliver() {
        return deliveryDetailsRepository.findByStatus(Status.ACCEPTED_AT_SORTING_FACILITY);
    }

    @Transactional
    public void assignCourierToPickUp(String machineAddress, Employee courier) { // FOR A GROUP OF DELIVERIES
        List<DeliveryDetails> toPickup = findParcelsToPickup();
        for(DeliveryDetails detail : toPickup) {
            if(detail.getDispatchParcelMachine().getAddress().equals(machineAddress)) {
                detail.setStatus(Status.ASSIGNED_TO_COURIER_FOR_PICKUP);
                employeeService.addTaskForEmployee(employeeService.findByFullName(courier.getFullName()), detail);
            }
        }
    }

    @Transactional
    public void assignCourierToDeliver(String machineAddress, Employee courier) { // FOR A GROUP OF DELIVERIES
        List<DeliveryDetails> toDeliver = findParcelsToDeliver();
        for(DeliveryDetails detail : toDeliver) {
            if(detail.getDestinationParcelMachine().getAddress().equals(machineAddress)) {
                detail.setStatus(Status.ASSIGNED_TO_COURIER_FOR_DELIVERY);
                employeeService.addTaskForEmployee(employeeService.findByFullName(courier.getFullName()), detail);
                setCellIdDestination(detail.getParcel());
                orderProcessingService.assignBox(detail.getDestinationParcelMachine(), detail.getParcel());
            }
        }
    }

    @Transactional
    public void moveToNextStatus(int id, Status currentStatus) { // FOR A SINGLE DELIVERY
        DeliveryDetails detail = findById(id);
        switch(currentStatus) {
            case ASSIGNED_TO_COURIER_FOR_PICKUP:
                 detail.setStatus(Status.IN_TRANSIT_TO_SORTING_FACILITY);
                 detail.setCellIdDispatch(0);
                 orderProcessingService.releaseBox(detail.getDispatchParcelMachine(), detail.getParcel());
                 break;
            case IN_TRANSIT_TO_SORTING_FACILITY:
                 detail.setStatus(Status.UNLOADED_AT_SORTING_FACILITY);
                 detail.setWarehouseManager(employeeService.findById(5));
                // SETTING RANDOM WAREHOUSE MANAGER:
//                if (random.nextInt(2) == 0) {
//                    detail.setWarehouseManager(employeeService.findById(4));
//                } else {
//                    detail.setWarehouseManager(employeeService.findById(5));
//                }
                 break;
            case UNLOADED_AT_SORTING_FACILITY:
                 detail.setStatus(Status.ACCEPTED_AT_SORTING_FACILITY);
                 detail.setCourier(null);
                 detail.setWarehouseManager(null);
                 break;
            case ACCEPTED_AT_SORTING_FACILITY:
                 detail.setStatus(Status.ASSIGNED_TO_COURIER_FOR_DELIVERY);
                 break;
            case ASSIGNED_TO_COURIER_FOR_DELIVERY:
                 detail.setStatus(Status.IN_TRANSIT_TO_DESTINATION);
                 break;
            case IN_TRANSIT_TO_DESTINATION:
                 detail.setStatus(Status.DELIVERED_TO_DESTINATION_MACHINE);
                 detail.setCourier(null);
                 break;
            case DELIVERED_TO_DESTINATION_MACHINE:
                 detail.setStatus(Status.COLLECTED_BY_RECIPIENT);
                 detail.setCellIdDestination(0);
                 orderProcessingService.releaseBox(detail.getDestinationParcelMachine(), detail.getParcel());
        }
    }

    public DeliveryDetails findByDoorCode(String doorCode){
        Optional <DeliveryDetails> delivery = deliveryDetailsRepository.findByDoorCode(doorCode);
        return delivery.orElseThrow(DeliveryDetailsNotFoundException::new);
    }

    public DeliveryDetails findByTrackingNumber(String trackingNumber){
        Optional <DeliveryDetails> delivery = deliveryDetailsRepository.findByTrackingNumber(trackingNumber);
        return delivery.orElseThrow(DeliveryDetailsNotFoundException::new);
    }

    @Transactional
    public void finalizeDelivery(String doorCode){
        moveToNextStatus(findByDoorCode(doorCode).getId(), Status.DELIVERED_TO_DESTINATION_MACHINE);
    }

    public int getRandomCellID(Parcel parcel){
        Random random = new Random();
        switch(parcel.getSize()){
            case S -> {
                return random.nextInt(1, 20);
            }
            case M -> {
                return random.nextInt(21, 55);
            }
            case L -> {
                return random.nextInt(56, 85);
            }
            case XL -> {
                return random.nextInt(86, 89);
            }
            default -> {
                return 0;
            }
        }
    }

    @Transactional
    public void setCellIdDispatch(Parcel parcel){
        int cellId = getRandomCellID(parcel);
        List<DeliveryDetails> allDeliveries = deliveryDetailsRepository.findAll();
        boolean checkIfCellIsBusy = true;

        while(checkIfCellIsBusy) {
            for (DeliveryDetails delivery : allDeliveries) {
                if (parcel.getDeliveryDetails().getDispatchParcelMachine() == delivery.getDispatchParcelMachine() ||
                        parcel.getDeliveryDetails().getDispatchParcelMachine() == delivery.getDestinationParcelMachine())
                {
                    if(cellId == delivery.getCellIdDispatch() || cellId == delivery.getCellIdDestination()) {
                        cellId = getRandomCellID(parcel);
                        break;
                    }
                }
                if (delivery.equals(allDeliveries.get(allDeliveries.size() - 1)))
                {
                    checkIfCellIsBusy = false;
                    parcel.getDeliveryDetails().setCellIdDispatch(cellId);
                }
            }
        }
    }

    @Transactional
    public void setCellIdDestination(Parcel parcel){
        int cellId = getRandomCellID(parcel);
        List<DeliveryDetails> allDeliveries = deliveryDetailsRepository.findAll();
        boolean checkIfCellIsBusy = true;

        while(checkIfCellIsBusy) {
            for (DeliveryDetails delivery : allDeliveries) {

                if (parcel.getDeliveryDetails().getDestinationParcelMachine() == delivery.getDestinationParcelMachine())
                {
                    if(cellId == delivery.getCellIdDestination()) {
                        cellId = getRandomCellID(parcel);
                        break;
                    }
                }
                if (delivery.equals(allDeliveries.get(allDeliveries.size() - 1)))
                {
                    checkIfCellIsBusy = false;
                    parcel.getDeliveryDetails().setCellIdDestination(cellId);
                }
            }
        }
    }


}
