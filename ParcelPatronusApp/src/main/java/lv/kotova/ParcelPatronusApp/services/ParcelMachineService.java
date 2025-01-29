package lv.kotova.ParcelPatronusApp.services;

import lv.kotova.ParcelPatronusApp.models.ParcelMachine;
import lv.kotova.ParcelPatronusApp.repositories.ParcelMachineRepository;
import lv.kotova.ParcelPatronusApp.util.ParcelMachineNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ParcelMachineService {

    private final ParcelMachineRepository parcelMachineRepository;

    @Autowired
    public ParcelMachineService(ParcelMachineRepository parcelMachineRepository) {
        this.parcelMachineRepository = parcelMachineRepository;
    }

    public List<ParcelMachine> findAll() {
        return parcelMachineRepository.findAll();
    }

    public ParcelMachine findById(int id) {
        Optional<ParcelMachine> foundParcelMachine = parcelMachineRepository.findById(id);
        return foundParcelMachine.orElseThrow(ParcelMachineNotFoundException::new);
    }

    public ParcelMachine findByAddress(String address){
        Optional<ParcelMachine> foundParcelMachine = parcelMachineRepository.findByAddress(address);
        return foundParcelMachine.orElseThrow(ParcelMachineNotFoundException::new);
    }

    @Transactional
    public void save(ParcelMachine parcelMachine) {
        parcelMachineRepository.save(parcelMachine);
    }

    @Transactional
    public void saveFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filename))));
            String line;
            while((line = reader.readLine()) != null) {
            if(!line.trim().isEmpty()) {
                ParcelMachine parcelMachine = new ParcelMachine(line);
                save(parcelMachine);
            }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }

        List<ParcelMachine> thelist = parcelMachineRepository.findAll();
        for(ParcelMachine parcelMachine : thelist) {
            if(parcelMachine.getAddress().equals("Mūkusalas iela 73, Rīga")
                    || parcelMachine.getAddress().equals("Kurzemes prospekts 141, Rīga")
                    || parcelMachine.getAddress().equals("Biķernieku iela 143, Rīga")
                    || parcelMachine.getAddress().equals("Klaipēdas iela 62, Liepāja")
                    || parcelMachine.getAddress().equals("Cietokšņa iela 60, Daugavpils")
            ){
            parcelMachine.setColdProtected(true);
            }
        }
    }

}
