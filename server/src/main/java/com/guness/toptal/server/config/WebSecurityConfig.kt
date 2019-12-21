package com.guness.toptal.server.config

import com.guness.toptal.server.security.TokenHelper
import com.guness.toptal.server.security.auth.RestAuthenticationEntryPoint
import com.guness.toptal.server.security.auth.TokenAuthenticationFilter
import com.guness.toptal.server.service.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    val detailsService: CustomUserDetailsService,
    val entryPoint: RestAuthenticationEntryPoint,
    val tokenHelper: TokenHelper
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(detailsService)
            .passwordEncoder(passwordEncoder())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling().authenticationEntryPoint(entryPoint).and()
            .authorizeRequests()
            .antMatchers("/auth/**", "/error").permitAll()
            .anyRequest().authenticated().and()
            .addFilterBefore(TokenAuthenticationFilter(tokenHelper, detailsService), BasicAuthenticationFilter::class.java)
        http.csrf().disable()
    }

    override fun configure(web: WebSecurity) { // TokenAuthenticationFilter will ignore the below paths
        web.ignoring().antMatchers(HttpMethod.POST, "/auth/**", "/error").and()
            .ignoring().antMatchers(HttpMethod.GET, "/", "/error")
    }
}