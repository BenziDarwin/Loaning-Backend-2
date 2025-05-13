package com.yunesta.mpesa.roles

import com.yunesta.mpesa.permissions.Permission
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Optional

@RestController
@RequestMapping("api/v1/roles")  // Changed from "users" to "roles" to match the resource
class RolesController(
    private val rolesService: RolesService
) {
    @PostMapping
    fun createRole(@RequestBody role: Role): ResponseEntity<Role> {
        val newRole = rolesService.createRole(role)
        return ResponseEntity(newRole, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllRoles(): ResponseEntity<List<Role>> {
        val roles = rolesService.getAllRoles()
        return ResponseEntity.ok(roles)
    }

    @GetMapping("/{id}")
    fun getRoleById(@PathVariable id: Long): ResponseEntity<Role> {
        return try {
            val role = rolesService.getRoleById(id)
            ResponseEntity.ok(role)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/name/{name}")
    fun getRoleByName(@PathVariable name: String): ResponseEntity<Role> {
        return try {
            val role = rolesService.getRoleByName(name)
            ResponseEntity.ok(role)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateRole(
        @PathVariable id: Long,
        @RequestBody roleDetails: Role
    ): ResponseEntity<Role> {
        return try {
            val updatedRole = rolesService.updateRole(id, roleDetails)
            ResponseEntity.ok(updatedRole)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteRole(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            rolesService.deleteRole(id)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{roleId}/permissions")
    fun addPermissionsToRole(
        @PathVariable roleId: Long,
        @RequestBody permissions: Set<Permission>
    ): ResponseEntity<Role> {
        return try {
            val updatedRole = rolesService.addPermissionsToRole(roleId, permissions)
            ResponseEntity.ok(updatedRole)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{roleId}/permissions")
    fun removePermissionsFromRole(
        @PathVariable roleId: Long,
        @RequestBody permissions: Set<Permission>
    ): ResponseEntity<Role> {
        return try {
            val updatedRole = rolesService.removePermissionsFromRole(roleId, permissions)
            ResponseEntity.ok(updatedRole)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }
}