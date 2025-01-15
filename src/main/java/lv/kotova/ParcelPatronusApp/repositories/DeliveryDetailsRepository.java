package lv.kotova.ParcelPatronusApp.repositories;

import lv.kotova.ParcelPatronusApp.models.DeliveryDetails;
import lv.kotova.ParcelPatronusApp.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryDetailsRepository extends JpaRepository<DeliveryDetails, Integer> {
    List<DeliveryDetails> findByCourierIdOrWarehouseManagerId(int idCourier, int idWarehouseManager);
    List<DeliveryDetails> findByStatusLikeAndCourierIdOrWarehouseManagerIdOrderByDispatchParcelMachineAsc(Status status, int idCourier, int idWarehouseManager);
    List<DeliveryDetails> findByStatusLikeAndCourierIdOrWarehouseManagerIdOrderByDestinationParcelMachineAsc(Status status, int idCourier, int idWarehouseManager);
    List<DeliveryDetails> findByStatus(Status status);
    Optional<DeliveryDetails> findByDoorCode(String code);
    Optional<DeliveryDetails> findByTrackingNumber(String trackingNumber);
}
