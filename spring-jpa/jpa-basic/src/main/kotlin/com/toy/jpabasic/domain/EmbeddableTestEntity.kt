package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 * jpa 는 Embedded 타입의 경우 db 조회후 객체화할 때 embedded 타입 내부 모든 필드가 null 이라면 객체화하지 못한다.
 * embeddableSample.isNotNull() 접근시 NPE 발생
 */

@Entity
@Table(name = "tb_embeddable_test")
class EmbeddableTestEntity(
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: String? = null,
) {

    @Embedded
    val embeddableSample: EmbeddableSample = EmbeddableSample()
}

@Embeddable
data class EmbeddableSample(
    @Column(name = "data1")
    val data1: String? = null,

    @Column(name = "data2")
    val data2: String? = null
) {

    fun isNotNull(): Boolean {
        return data1 != null && data2 != null
    }
}
