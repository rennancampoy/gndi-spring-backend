package com.example.gndispringbackend.repositories

import com.example.gndispringbackend.models.ERole
import com.example.gndispringbackend.models.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RoleRepository : JpaRepository<Role, String> {
     fun findByName(name: ERole): Optional<Role>
}