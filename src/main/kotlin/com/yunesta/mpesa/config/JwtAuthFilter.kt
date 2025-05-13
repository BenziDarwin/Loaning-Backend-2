package com.yunesta.mpesa.config

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.lang.NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            println("No valid Authorization header found.")
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)
        val userEmail = jwtService.extractUserEmail(jwt)

        println("Extracted userEmail: $userEmail")  // Debug log

        println(SecurityContextHolder.getContext())


        if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
            try {
                val userDetails = userDetailsService.loadUserByUsername(userEmail)
                println("Loaded userDetails: $userDetails")  // Debug log

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                    println("Authentication set in security context.")
                } else {
                    println("Invalid JWT token.")
                }
            } catch (e: Exception) {
                println("Error loading user details: ${e.message}")
            }
        } else {
            println("User is already authenticated or email is null.")
        }

        filterChain.doFilter(request, response)
    }
}
