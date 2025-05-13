package com.yunesta.mpesa.roles

import com.yunesta.mpesa.permissions.Permission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class RolesService(
    private val rolesRepository: RolesRepository
) {

    // Create
    @Transactional
    fun createRole(role: Role): Role {
        if (rolesRepository.existsByName(role.name)) {
            throw IllegalStateException("Role with name ${role.name} already exists")
        }
        return rolesRepository.save(role)
    }

    // Read all
    fun getAllRoles(): List<Role> = rolesRepository.findAll()

    // Read by id
    fun getRoleById(id: Long): Role = rolesRepository.findById(id)
        .orElseThrow { NoSuchElementException("Role not found with id: $id") }

    // Read by name
    fun getRoleByName(name: String): Role = rolesRepository.findByName(name)
        .orElseThrow { NoSuchElementException("Role not found with name: $name") }

    // Update
    @Transactional
    fun updateRole(id: Long, roleDetails: Role): Role {
        val existingRole = getRoleById(id)

        // Check if new name conflicts with another role
        if (roleDetails.name != existingRole.name && rolesRepository.existsByName(roleDetails.name)) {
            throw IllegalStateException("Role with name ${roleDetails.name} already exists")
        }

        // Create new role instance with updated fields but same ID
        val updatedRole = Role(
            id = existingRole.id,
            name = roleDetails.name,
            description = roleDetails.description,
            permissions = roleDetails.permissions
        )

        return rolesRepository.save(updatedRole)
    }

    // Delete
    @Transactional
    fun deleteRole(id: Long) {
        if (!rolesRepository.existsById(id)) {
            throw NoSuchElementException("Role not found with id: $id")
        }
        rolesRepository.deleteById(id)
    }

    // Add permissions to role
    @Transactional
    fun addPermissionsToRole(roleId: Long, permissions: Set<Permission>): Role {
        val role = getRoleById(roleId)
        role.permissions.addAll(permissions)
        return rolesRepository.save(role)
    }

    // Remove permissions from role
    @Transactional
    fun removePermissionsFromRole(roleId: Long, permissions: Set<Permission>): Role {
        val role = getRoleById(roleId)
        role.permissions.removeAll(permissions)
        return rolesRepository.save(role)
    }
}