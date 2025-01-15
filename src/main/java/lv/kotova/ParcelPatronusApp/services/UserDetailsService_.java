package lv.kotova.ParcelPatronusApp.services;

import lv.kotova.ParcelPatronusApp.models.User;
import lv.kotova.ParcelPatronusApp.repositories.UserRepository;
import lv.kotova.ParcelPatronusApp.security.UserDetails_;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService_ implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsService_(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user = userRepository.findByUsername(username);
       if(user.isEmpty()){
           throw new UsernameNotFoundException("User not found...!");
       }
       return new UserDetails_(user.get());
    }
}
