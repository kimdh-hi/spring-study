package hello.repository;

import hello.member.Member;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

  private final JdbcTemplate jdbcTemplate;

  public MemberRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void initTable() {
    jdbcTemplate.execute(
      "create table member(member_id varchar primary key, name varchar)"
    );
  }

  public void save(Member member) {
    jdbcTemplate.update("insert into member(member_id, name) values(?, ?)", member.getMemberId(), member.getName());
  }

  public Member find(String memberId) {
    return jdbcTemplate.queryForObject(
      "select member_id, name from member where member_id=?",
      BeanPropertyRowMapper.newInstance(Member.class),
      memberId
    );
  }

  public List<Member> findAll() {
    return jdbcTemplate.query(
      "select member_id, name from member",
      BeanPropertyRowMapper.newInstance(Member.class)
    );
  }
}
