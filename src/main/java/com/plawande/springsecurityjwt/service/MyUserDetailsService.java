package com.plawande.springsecurityjwt.service;

import com.plawande.springsecurityjwt.models.UserData;
import com.plawande.springsecurityjwt.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDataRepository userDataRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserData> userDataOptional = userDataRepository.findUserByUserName(userName);
        UserData userData = userDataOptional.orElseThrow(() -> new UsernameNotFoundException(userName + " not found!"));

        return User.builder()
                .username(userName)
                .password(userData.getPassword())
                .authorities(userData.getRoles().split(","))
                .accountExpired(!userData.isActive())
                .build();
    }
}
