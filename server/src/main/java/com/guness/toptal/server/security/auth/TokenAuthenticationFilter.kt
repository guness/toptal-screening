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

class TokenAuthenticationFilter(
    private val tokenHelper: TokenHelper,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    @Throws(IOException::class, ServletException::class)
    public override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        tokenHelper.getToken(request)?.let { authToken ->
            tokenHelper.getUsernameFromToken(authToken)
                ?.let(userDetailsService::loadUserByUsername)
                ?.let { userDetails ->
                    if (tokenHelper.validateToken(authToken, userDetails)) {
                        val authentication = TokenBasedAuthentication(userDetails)
                        authentication.token = authToken
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
        }
        chain.doFilter(request, response)
    }
}