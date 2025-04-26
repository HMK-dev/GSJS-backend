package com.gsjs.gsjs.domain.member.repository;

import com.gsjs.gsjs.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
