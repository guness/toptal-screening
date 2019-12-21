package com.guness.toptal.server.model

import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import com.guness.toptal.protocol.dto.User as DtoUser

@Entity
@Table(name = "User")
data class StoredUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val username: String,
    val password: String,
    val enabled: Boolean,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "User_Authority",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id", referencedColumnName = "id")]
    )
    val authorities: List<Authority>
)

class DetailedUser(val storedUser: StoredUser) : UserDetails {

    override fun getAuthorities() = storedUser.authorities

    override fun isEnabled() = storedUser.enabled

    override fun getUsername() = storedUser.username

    override fun isCredentialsNonExpired() = true

    override fun getPassword() = storedUser.password

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true
}

fun StoredUser.toDto() = DtoUser(id, username, authorities.first().role)
fun DetailedUser.toDto() = DtoUser(storedUser.id, username, authorities.first().role)