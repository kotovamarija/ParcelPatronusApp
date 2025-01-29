package lv.kotova.ParcelPatronusApp.repositories;

import lv.kotova.ParcelPatronusApp.models.ParcelMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParcelMachineRepository extends JpaRepository<ParcelMachine, Integer> {
    Optional<ParcelMachine> findByAddress(String address);
}
