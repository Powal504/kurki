package org.example.kurki.security.repository;

import org.example.kurki.security.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByVerificationCode(String verificationCode);
}
