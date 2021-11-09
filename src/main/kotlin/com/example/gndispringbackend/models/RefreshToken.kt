package com.example.gndispringbackend.models

import java.time.Instant
import javax.persistence.*

@Entity(name = "refreshtoken")
data class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User = User(),

    @Column(nullable = false, unique = true)
    val token: String = "",

    @Column(nullable = false)
    val expiryDate: Instant = Instant.now()
)





