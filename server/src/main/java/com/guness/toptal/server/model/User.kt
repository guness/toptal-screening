package com.guness.toptal.server.model

import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "User")
data class User(
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

class DetailedUser(val user: User) : UserDetails {

    override fun getAuthorities() = user.authorities

    override fun isEnabled() = user.enabled

    override fun getUsername() = user.username

    override fun isCredentialsNonExpired() = true

    override fun getPassword() = user.password

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true
}

fun DetailedUser.toDto() = com.guness.toptal.protocol.dto.User(user.id, username, authorities.first().role)