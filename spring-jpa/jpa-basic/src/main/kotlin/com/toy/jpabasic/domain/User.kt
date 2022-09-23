package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.envers.Audited
import org.hibernate.envers.RelationTargetAuditMode
import java.io.Serial
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "tb_user")
@Audited
class User (

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var username: String,

  /*
  단방향 연관관계에서 delete cascading 을 사용하려면 @OnDelete 를 사용한다.
  단, hibernate envers 로 삭제 이력 관리가 불가능하다. jpa 가 아닌 db 의 속성을 직접 사용하기 때문이다. (on delete cascade)

  양방향 연관관계가 주는 불편함? 과 이력관리를 하지 못하는 등등의 트레이드 오프가 있는 듯 하다.

  엔티티 삭제 이력은 중요한 정보이므로 CascadeType.REMOVE 를 최대한 사용하고 그렇지 않은 경우 @OnDelete 를 살짝 고려하고
  양방향도 싫고 이력도 관리하고 싶다면 직접 관계에 있는 엔티티를 delete 해주자.
   */
  @ManyToOne(optional = false)
//  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "company_id")
  var company: Company,

  @ManyToOne(optional = false)
  @JoinColumn(name = "role_id")
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  var role: Role
): Serializable {

  companion object {
    @Serial
    private const val serialVersionUID: Long = 425994759014562522L
  }

  override fun toString(): String {
    return "User(id=$id, username='$username', company=$company, role=$role)"
  }
}