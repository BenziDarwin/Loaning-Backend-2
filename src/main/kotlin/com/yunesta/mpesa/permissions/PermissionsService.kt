package com.yunesta.mpesa.permissions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PermissionsService @Autowired constructor(
    private val permissionsRepository: PermissionsRepository
) {

    // Create or Update a Permission
    @Transactional
    fun savePermission(permission: Permission): Permission {
        return permissionsRepository.save(permission)
    }

    // Get all Permissions
    fun getAllPermissions(): List<Permission> {
        return permissionsRepository.findAll()
    }

    // Get Permission by ID
    fun getPermissionById(id: Long): Optional<Permission> {
        return permissionsRepository.findById(id)
    }

    // Delete Permission by ID
    @Transactional
    fun deletePermissionById(id: Long) {
        permissionsRepository.deleteById(id)
    }

    // Check if Permission exists by ID
    fun permissionExists(id: Long): Boolean {
        return permissionsRepository.existsById(id)
    }
}
