package com.guness.toptal.server.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class TokenHelper {
    @Value("\${app.name}")
    private val APP_NAME: String? = null

    @Value("\${jwt.secret}")
    var SECRET: String? = null

    @Value("\${jwt.expires_in}")
    val EXPIRES_IN = 0L

    @Value("\${jwt.header}")
    private val AUTH_HEADER: String? = null

    private val SIGNATURE_ALGORITHM: SignatureAlgorithm =
        SignatureAlgorithm.HS512

    fun getUsernameFromToken(token: String): String? {
        val username: String?
        username = try {
            val claims: Claims? = getAllClaimsFromToken(token)
            claims?.subject
        } catch (e: Exception) {
            null
        }
        return username
    }

    fun getIssuedAtDateFromToken(token: String): Date? {
        val issueAt: Date?
        issueAt = try {
            val claims: Claims? = getAllClaimsFromToken(token)
            claims?.getIssuedAt()
        } catch (e: Exception) {
            null
        }
        return issueAt
    }

    fun getAudienceFromToken(token: String): String? {
        val audience: String?
        audience = try {
            val claims: Claims? = getAllClaimsFromToken(token)
            claims?.getAudience()
        } catch (e: Exception) {
            null
        }
        return audience
    }

    fun refreshToken(token: String): String? {
        val refreshedToken: String?
        refreshedToken = try {
            val claims: Claims? = getAllClaimsFromToken(token)
            claims?.setIssuedAt(Date())
            Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact()
        } catch (e: Exception) {
            null
        }
        return refreshedToken
    }

    fun generateToken(username: String?): String {
        return Jwts.builder()
            .setIssuer(APP_NAME)
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(generateExpirationDate())
            .signWith(SIGNATURE_ALGORITHM, SECRET)
            .compact()
    }


    private fun getAllClaimsFromToken(token: String): Claims? {
        val claims: Claims?
        claims = try {
            Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null
        }
        return claims
    }

    private fun generateExpirationDate(): Date {
        return Date(Date().getTime() + EXPIRES_IN * 1000)
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username != null && username == userDetails.username
    }

    fun getToken(request: HttpServletRequest): String? {
        /**
         * Getting the token from Authentication header
         * e.g Bearer your_token
         */
        val authHeader = getAuthHeaderFromHeader(request)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else null
    }

    fun getAuthHeaderFromHeader(request: HttpServletRequest): String {
        return request.getHeader(AUTH_HEADER)
    }
}