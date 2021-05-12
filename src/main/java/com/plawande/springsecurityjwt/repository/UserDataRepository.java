package com.plawande.springsecurityjwt.repository;

import com.plawande.springsecurityjwt.models.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Integer> {
    Optional<UserData> findUserByUserName(String userName);
}
