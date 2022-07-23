package com.study.mapstruct.mapper

import com.study.mapstruct.domain.User
import com.study.mapstruct.vo.UserVO
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

interface GenericMapper<E, V> {
    fun toEntity(vo: V): E
    fun toVO(entity: E): V
}

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper: GenericMapper<User, UserVO> {

}