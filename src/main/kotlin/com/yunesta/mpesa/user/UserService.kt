package com.yunesta.mpesa.user

import com.yunesta.mpesa.roles.RolesRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val rolesRepository: RolesRepository, private val passwordEncoder: PasswordEncoder) {

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: Long): User? = userRepository.findById(id).orElse(null)

    fun createUser(user: User): User {
        val updatedUser = user.copy(id = null, status = user.status.uppercase(), password = passwordEncoder.encode(user.password), roles = user.roles)
        println("Creating User $updatedUser")
        return userRepository.save(updatedUser)
    }


    fun updateUser(id: Long, updatedUser: User): User? {
        return userRepository.findById(id).map { existingUser ->
            // Clear existing roles first
            existingUser.roles.clear()

            // Add new roles
            val managedRoles = updatedUser.roles.map { role ->
                rolesRepository.findById(role.id!!).orElse(role)
            }.toSet()

            val updatedEntity = existingUser.copy(
                id = updatedUser.id,
                name = updatedUser.name,
                status = updatedUser.status,
                email = updatedUser.email,
                roles = managedRoles.toMutableSet()
            )

            userRepository.save(updatedEntity)
        }.orElse(null)
    }


    fun deleteUser(id: Long): Boolean {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}
