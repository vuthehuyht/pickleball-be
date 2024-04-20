package com.macrace.pickleball.service.impl;

import com.macrace.pickleball.custom.CUserDetail;
import com.macrace.pickleball.exceptions.PhoneNumberNotFoundException;
import com.macrace.pickleball.model.User;
import com.macrace.pickleball.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByPhoneNumber(username);
        if (userOptional.isEmpty()) {
            throw new PhoneNumberNotFoundException();
        }

        return new CUserDetail(
                userOptional.get().getEmail(),
                userOptional.get().getPassword()
        );
    }
}
