package com.DogFoot.adpotAnimal.users.repository;

import com.DogFoot.adpotAnimal.users.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUserId(String userId);
    Optional<Users> findById(Long id);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    void deleteById(Long id);
}
