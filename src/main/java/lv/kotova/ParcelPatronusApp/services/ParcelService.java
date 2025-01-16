package lv.kotova.ParcelPatronusApp.services;

import lv.kotova.ParcelPatronusApp.models.Parcel;
import lv.kotova.ParcelPatronusApp.repositories.ParcelRepository;
import lv.kotova.ParcelPatronusApp.util.ParcelNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ParcelService {

    private final ParcelRepository parcelRepository;

    @Autowired
    public ParcelService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    public List<Parcel> findAll(){
        return parcelRepository.findAll();
    }

    public Parcel findById(int id){
        Optional<Parcel> foundParcel = parcelRepository.findById(id);
        return foundParcel.orElseThrow(ParcelNotFoundException::new);
    }

    @Transactional
    public void save(Parcel parcel) {
        parcelRepository.save(parcel);
    }

}
