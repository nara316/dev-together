package com.project.devtogether.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findFirstByEmailAndPassword(String email, String password);

    public Optional<Member> findFirstByEmail(String email);
}
