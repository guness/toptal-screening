package com.guness.toptal.server.security.auth

import com.guness.toptal.server.security.TokenHelper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter(private val tokenHelper: TokenHelper, private val userDetailsService: UserDetailsService) : OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val username: String?
        val authToken = tokenHelper.getToken(request)
        if (authToken != null) { // get username from token
            username = tokenHelper.getUsernameFromToken(authToken)
            if (username != null) { // get user
                val userDetails = userDetailsService.loadUserByUsername(username)
                if (tokenHelper.validateToken(authToken, userDetails)) { // create authentication
                    val authentication = TokenBasedAuthentication(userDetails)
                    authentication.token = authToken
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
        chain.doFilter(request, response)
    }
}