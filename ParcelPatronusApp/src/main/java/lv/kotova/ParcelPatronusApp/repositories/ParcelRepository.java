package lv.kotova.ParcelPatronusApp.repositories;

import lv.kotova.ParcelPatronusApp.models.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Integer> {
}
