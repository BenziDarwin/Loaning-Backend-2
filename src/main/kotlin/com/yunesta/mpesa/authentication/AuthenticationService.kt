package com.yunesta.mpesa.authentication

import com.yunesta.mpesa.config.JwtService
import com.yunesta.mpesa.roles.Role
import com.yunesta.mpesa.roles.RolesRepository
import com.yunesta.mpesa.user.User
import com.yunesta.mpesa.user.UserRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import javax.naming.AuthenticationException

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val roleRepository: RolesRepository,
    private val  authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    /**
     * Registers a new user with the specified role.
     *
     * @param request The registration request containing user details.
     * @param roleID The ID of the role to assign to the user.
     * @return An authentication response containing the JWT token and user details.
     */
    fun register(request: RegisterRequest, roleID: Long): AuthenticationResponse {
        // Retrieve the role by ID
        val role = roleRepository.findById(roleID)
            .orElseThrow { IllegalArgumentException("Role not found") }
        if (userRepository.findByEmail(request.email).isPresent) {
            throw  Exception("Email already exists!")
        }


        // Create a set of roles for the user
        val roles = hashSetOf(role)

        // Save the new user to the database
        val user = User(
            email = request.email,
            name = request.name,
            password = passwordEncoder.encode(request.password),
            roles = roles,
            status = request.status,
            id = null
        )
        userRepository.save(user)

        // Generate and return the authentication response
        return generateToken(user, roles)
    }

    /**
     * Authenticates a user based on their credentials.
     *
     * @param request The authentication request containing email and password.
     * @return An authentication response containing the JWT token and user details.
     * @throws NotFoundException If the user is not found in the database.
     */
    @Throws(NotFoundException::class, AuthenticationException::class)
    fun authenticate(request: AuthenticationRequest): AuthenticationResponse? {
        println("Trying to authenticate with email: ${request.email}")

        // Retrieve the user from the database
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { NotFoundException() }

        // Validate the password manually
        if (!passwordEncoder.matches(request.password, user.password)) {
            println("Authentication failed: Invalid credentials")
            throw AuthenticationException("Invalid credentials for email: ${request.email}")
        }

        // ✅ Fetch user details (including roles/authorities)
        val userDetails = org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            user.authorities // Ensure roles are passed correctly
        )

        // ✅ Create a fully authenticated token
        val authenticationToken = UsernamePasswordAuthenticationToken(
            userDetails,
            null, // No credentials needed now
            userDetails.authorities // Set authorities here
        )

        // ✅ Manually create a SecurityContext and set authentication
        val securityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = authenticationToken
        SecurityContextHolder.setContext(securityContext) // ✅ This properly sets the context

        println("Authentication successful for email: ${request.email}")

        // ✅ Generate JWT token and return response
        return generateToken(user, user.roles)
    }




    /**
     * Generates an authentication response containing the JWT token and user details.
     *
     * @param user The authenticated user.
     * @param roles The roles assigned to the user.
     * @return An authentication response object.
     */
    private fun generateToken(user: User, roles: Set<Role>): AuthenticationResponse {
        // Generate the JWT token for the user
        val jwtToken = jwtService.generateToken(user)

        // Return the authentication response
        return AuthenticationResponse(
            token = jwtToken,
            name = user.name,
            email = user.email,
            roles = roles, // Convert roles to their names for simplicity
            id = user.id
        )
    }
}