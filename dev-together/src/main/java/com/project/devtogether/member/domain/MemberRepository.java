package com.project.devtogether.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Member findFirstByEmailAndPassword(String email, String password);
}
