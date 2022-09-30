package com.study.mapstruct.mapper

import com.study.mapstruct.domain.User
import com.study.mapstruct.vo.UserVO
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface UserMapper {
//    @Mapping(source = "username", target = "username")
    @Mappings(
        Mapping(source = "username", target = "username"),
        Mapping(source = "name", target = "name")
    )
    fun toVO(user: User): UserVO

    @InheritInverseConfiguration
    fun toEntity(userVO: UserVO): User
}