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
}