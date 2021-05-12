package com.plawande.springsecurityjwt.service;

import com.plawande.springsecurityjwt.models.UserData;
import com.plawande.springsecurityjwt.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDataRepository userDataRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserData> userDataOptional = userDataRepository.findUserByUserName(userName);
        UserData userData = userDataOptional.orElseThrow(() -> new UsernameNotFoundException(userName + " not found!"));

        /*List<GrantedAuthority> authorities = Stream.of(userData.getRoles().split(","))
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());*/

        return User.builder()
                .username(userName)
                .password(userData.getPassword())
                .authorities(userData.getRoles().split(","))
                .accountExpired(!userData.isActive())
                .build();

        /*return new User(userName, "bar", new ArrayList<>());*/
    }
}
