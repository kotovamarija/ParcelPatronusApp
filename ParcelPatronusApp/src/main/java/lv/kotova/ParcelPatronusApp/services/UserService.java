package lv.kotova.ParcelPatronusApp.services;

import lv.kotova.ParcelPatronusApp.models.Parcel;
import lv.kotova.ParcelPatronusApp.models.User;
import lv.kotova.ParcelPatronusApp.models.enums.Status;
import lv.kotova.ParcelPatronusApp.repositories.UserRepository;
import lv.kotova.ParcelPatronusApp.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(int id){
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElseThrow(UserNotFoundException::new);
    }

    public User findByUsername(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        return foundUser.orElseThrow(UserNotFoundException::new);
    }

    public User findByEmail(String email) {
        Optional<User> foundUser = userRepository.findByEmail(email);
        return foundUser.orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    public List<Parcel> findActiveParcelsByUserId(int id) {
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()){
            Optional<List<Parcel>> foundParcels = Optional.ofNullable(foundUser.get().getRegisteredParcels());
            List<Parcel> activeParcels = new ArrayList<>();
            for(Parcel parcel : foundParcels.get()){
                if(parcel.getDeliveryDetails().getStatus() != Status.DELIVERED_TO_DESTINATION_MACHINE
                        && parcel.getDeliveryDetails().getStatus() != Status.RETURNED_TO_SENDER){
                    activeParcels.add(parcel);
                }
            }
            return activeParcels;
         }
       throw new UserNotFoundException();
    }

}
