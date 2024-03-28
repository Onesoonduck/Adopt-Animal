package com.DogFoot.adpotAnimal.member.repository;

import com.DogFoot.adpotAnimal.member.entity.Member;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
