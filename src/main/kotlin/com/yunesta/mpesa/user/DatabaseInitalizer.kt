package com.yunesta.mpesa.user

import com.yunesta.mpesa.roles.Role
import com.yunesta.mpesa.permissions.Permission
import com.yunesta.mpesa.permissions.PermissionsRepository
import com.yunesta.mpesa.roles.RolesRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DatabaseInitializer {

    @Bean
    fun initDatabase(
        userRepository: UserRepository,
        roleRepository: RolesRepository,
        permissionRepository: PermissionsRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner = CommandLineRunner {
        // Create system permissions
        val manageUsersPermission = permissionRepository.findByName("users.manage")
            .orElseGet {
                permissionRepository.save(
                    Permission(
                        id = null,
                        name = "users.manage"
                    )
                )
            }
        val manageRolesPermission = permissionRepository.findByName("roles.manage")
            .orElseGet {
                permissionRepository.save(
                    Permission(
                        id = null,
                        name = "roles.manage"
                    )
                )
            }
        val manageLoansPermission = permissionRepository.findByName("loans.manage")
            .orElseGet {
                permissionRepository.save(
                    Permission(
                        id = null,
                        name = "loans.manage"
                    )
                )
            }
        val viewReportsPermission = permissionRepository.findByName("reports.view")
            .orElseGet {
                permissionRepository.save(
                    Permission(
                        id = null,
                        name = "reports.view"
                    )
                )
            }

        // Create admin role with all permissions
        var adminRole = Role(
            id = null,
            name = "ROLE_ADMIN",
            description = "Administrator with full system access",
            permissions = mutableSetOf(
                manageUsersPermission,
                manageRolesPermission,
                manageLoansPermission,
                viewReportsPermission
            )
        )

        // Create loan officer role with loan management and report viewing permissions
        var loanOfficerRole = Role(
            id = null,
            name = "ROLE_LOAN_OFFICER",
            description = "Loan Officer with loan management access",
            permissions = mutableSetOf(
                manageLoansPermission,
                viewReportsPermission
            )
        )

        // Create basic user role with only report viewing permission
        var userRole = Role(
            id = null,
            name = "ROLE_USER",
            description = "Regular user with basic access",
            permissions = mutableSetOf(viewReportsPermission)
        )

        // Save roles if they don't exist
        adminRole = roleRepository.findByName(adminRole.name)
            .orElseGet { roleRepository.save(adminRole) }
        loanOfficerRole = roleRepository.findByName(loanOfficerRole.name)
            .orElseGet { roleRepository.save(loanOfficerRole) }
        userRole = roleRepository.findByName(userRole.name)
            .orElseGet { roleRepository.save(userRole) }

        println("Saved Roles: $adminRole, $loanOfficerRole, $userRole")

        // Create admin user
        val adminUser = User(
            id = null,
            name = "System Admin",
            email = "admin@mpesa.com",
            password = passwordEncoder.encode("admin123"),
            status = "ACTIVE",
            roles = mutableSetOf(adminRole)
        )

        // Create loan officer user
        val loanOfficerUser = User(
            id = null,
            name = "Loan Officer",
            email = "officer@mpesa.com",
            password = passwordEncoder.encode("officer123"),
            status = "ACTIVE",
            roles = mutableSetOf(loanOfficerRole)
        )

        // Create regular user
        val regularUser = User(
            id = null,
            name = "Regular User",
            email = "user@mpesa.com",
            password = passwordEncoder.encode("user123"),
            status = "ACTIVE",
            roles = mutableSetOf(userRole)
        )

        // Save users if they don't exist
        userRepository.findByEmail(adminUser.email)
            .orElseGet { userRepository.save(adminUser) }
        userRepository.findByEmail(loanOfficerUser.email)
            .orElseGet { userRepository.save(loanOfficerUser) }
        userRepository.findByEmail(regularUser.email)
            .orElseGet { userRepository.save(regularUser) }
    }
}