package com.mike.lunchvoter.security;

import com.mike.lunchvoter.exception.ObjectNotFoundException;
import com.mike.lunchvoter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new SecurityUserDetails(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new ObjectNotFoundException("There is no user with email = " + email))
        );
    }
}
