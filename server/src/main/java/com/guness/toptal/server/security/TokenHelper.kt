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
    private val appName: String? = null

    @Value("\${jwt.secret}")
    var secret: String? = null

    @Value("\${jwt.expires_in}")
    val expiresIn = 0L

    @Value("\${jwt.header}")
    private val authHeader: String? = null

    private val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS512

    fun getUsernameFromToken(token: String): String? {
        return try {
            getAllClaimsFromToken(token)?.subject
        } catch (e: Exception) {
            null
        }
    }

    fun generateToken(username: String?): String {
        return Jwts.builder()
            .setIssuer(appName)
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(generateExpirationDate())
            .signWith(signatureAlgorithm, secret)
            .compact()
    }


    private fun getAllClaimsFromToken(token: String): Claims? {
        return try {
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            null
        }
    }

    private fun generateExpirationDate(): Date {
        return Date(Date().time + expiresIn * 1000)
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
        return if (authHeader?.startsWith("Bearer ") == true) {
            authHeader.substring(7)
        } else null
    }

    fun getAuthHeaderFromHeader(request: HttpServletRequest): String? {
        return request.getHeader(authHeader)
    }
}