package com.yunesta.mpesa.user

import com.yunesta.mpesa.roles.Role
import jakarta.persistence.*
import lombok.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long?,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    private val password: String,

    @Column(nullable = false)
    val status: String,

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableSetOf()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }
}