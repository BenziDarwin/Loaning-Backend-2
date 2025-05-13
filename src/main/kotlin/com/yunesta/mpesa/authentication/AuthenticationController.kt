package com.yunesta.mpesa.authentication

import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = ["*"])
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/register/{roleID}")
    fun register(
        @PathVariable roleID: Long,
        @RequestBody request: RegisterRequest
    ): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.register(request, roleID))
    }

    @PostMapping("/authenticate")
    @Throws(ChangeSetPersister.NotFoundException::class)
    fun authenticate(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }
}
