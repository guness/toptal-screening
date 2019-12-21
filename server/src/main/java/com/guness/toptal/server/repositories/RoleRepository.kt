package com.guness.toptal.server.repositories

import com.guness.toptal.protocol.dto.UserRole
import com.guness.toptal.server.model.Authority
import org.springframework.data.repository.CrudRepository

interface RoleRepository : CrudRepository<Authority, Long> {
    fun findByRole(role: UserRole = UserRole.ROLE_USER): Authority?
}