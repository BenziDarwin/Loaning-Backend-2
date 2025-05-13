package com.yunesta.mpesa.roles

import com.yunesta.mpesa.permissions.Permission
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_permissions",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    var permissions: MutableSet<Permission>,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Role) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}