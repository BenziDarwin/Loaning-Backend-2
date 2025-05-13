package com.yunesta.mpesa.config

import com.yunesta.mpesa.user.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@RequiredArgsConstructor
class ApplicationConfig(  private val userRepository: UserRepository) {


    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username: String? ->
            userRepository.findByEmail(username)
                .orElseThrow { UsernameNotFoundException("User not found") }
        }
    }


    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .authenticationProvider(authenticationProvider())
            .build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
