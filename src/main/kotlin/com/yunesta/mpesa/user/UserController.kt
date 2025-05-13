package com.yunesta.mpesa.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> = ResponseEntity.ok(userService.getAllUsers())

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.getUserById(id)
        return if (user != null) ResponseEntity.ok(user) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val savedUser = userService.createUser(user)
        return ResponseEntity.ok(savedUser)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody updatedUser: User): ResponseEntity<User> {
        val user = userService.updateUser(id, updatedUser)
        return if (user != null) ResponseEntity.ok(user) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        return if (userService.deleteUser(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }
}
