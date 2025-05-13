package com.yunesta.mpesa.permissions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/permissions")
class PermissionsController @Autowired constructor(
    private val permissionsService: PermissionsService
) {

    // Create or Update a Permission
    @PostMapping
    fun createOrUpdatePermission(@RequestBody permission: Permission): ResponseEntity<Permission> {
        val savedPermission = permissionsService.savePermission(permission)
        return ResponseEntity(savedPermission, HttpStatus.CREATED)
    }

    // Get all Permissions
    @GetMapping
    fun getAllPermissions(): ResponseEntity<List<Permission>> {
        val permissions = permissionsService.getAllPermissions()
        return ResponseEntity(permissions, HttpStatus.OK)
    }

    // Get Permission by ID
    @GetMapping("/{id}")
    fun getPermissionById(@PathVariable id: Long): ResponseEntity<Permission> {
        val permission = permissionsService.getPermissionById(id)
        return if (permission.isPresent) {
            ResponseEntity(permission.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Delete Permission by ID
    @DeleteMapping("/{id}")
    fun deletePermissionById(@PathVariable id: Long): ResponseEntity<Void> {
        return if (permissionsService.permissionExists(id)) {
            permissionsService.deletePermissionById(id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
