package com.yunesta.mpesa.config

import com.yunesta.mpesa.user.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService {

    fun extractUserEmail(token: String?): String {
        return extractClaim(token) { claims -> claims.subject }
    }

    fun <T> extractClaim(token: String?, claimsResolver: (Claims) -> T): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }

    fun generateToken(userDetails: User): String {
        return generateToken(HashMap(), userDetails)
    }

    fun generateToken(
        extraClaims: Map<String?, Any?>?,
        userDetails: User
    ): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.email)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .signWith(signInKey, io.jsonwebtoken.SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String?, userDetails: UserDetails): Boolean {
        val userEmail = extractUserEmail(token)
        return (userEmail == userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String?): Boolean {
        return Date().after(extractExpiration(token))
    }

    private fun extractExpiration(token: String?): Date? {
        return extractClaim(token) { claims -> claims.expiration }
    }

    fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(signInKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private val signInKey: Key
        get() {
            val keyBytes: ByteArray = Decoders.BASE64.decode(SECRET_KEY)
            return Keys.hmacShaKeyFor(keyBytes)
        }

    companion object {
        private const val SECRET_KEY = "22cf33e41f2c8d2daa16881a4197a0c051070724a2d6df2944c67896ba9a697d"
    }
}
