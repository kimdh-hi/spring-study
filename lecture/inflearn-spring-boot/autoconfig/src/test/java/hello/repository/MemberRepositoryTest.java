package hello.repository;

import hello.member.Member;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Transactional
  @Test
  void memberTest() {
    Member member = new Member("m1", "member1");
    memberRepository.initTable();
    memberRepository.save(member);
    Member findMember = memberRepository.find(member.getMemberId());

    assertEquals(member.getMemberId(), findMember.getMemberId());
  }

}