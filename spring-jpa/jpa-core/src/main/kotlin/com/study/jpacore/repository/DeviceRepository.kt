package com.study.jpacore.repository

import com.study.jpacore.entity.Device
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<Device, String> {
}
