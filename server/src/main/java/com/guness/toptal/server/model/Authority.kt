package com.guness.toptal.server.model

import com.guness.toptal.protocol.dto.UserRole
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name = "Authority")
data class Authority(

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    val id: Long,

    @Enumerated(EnumType.STRING)
    val role: UserRole
) : GrantedAuthority {

    override fun getAuthority(): String {
        return role.name
    }

    companion object {
        val user = Authority(1, UserRole.ROLE_USER)
        val manager = Authority(2, UserRole.ROLE_MANAGER)
        val admin = Authority(3, UserRole.ROLE_ADMIN)
    }
}