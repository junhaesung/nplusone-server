package com.haeseong.nplusone.infrastructure.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.haeseong.nplusone.application.TokenService
import com.haeseong.nplusone.domain.ResultCode
import com.haeseong.nplusone.domain.member.MemberService
import com.haeseong.nplusone.ui.ApiResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    private val memberService: MemberService,
    private val tokenService: TokenService<Long>,
) : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .mvcMatchers(
                "/error",
                "/favicon.ico",
                "/swagger-ui/**",
                "/webjars/springfox-swagger-ui/**",
                "/swagger-resources/**",
                "/v1/api-docs",
                "/h2-console/**",
                "/hello"
            )
    }

    override fun configure(http: HttpSecurity) {
        http.antMatcher("/api/v1/**")
            .authorizeRequests()
            // FIXME: client authentication 개발되면 items api 도 인증필요
            .antMatchers("/api/v1/items/**").permitAll()
            .antMatchers("/api/v1/members/login").permitAll()
            .anyRequest().hasAuthority(MEMBER_ROLE_NAME)
        http.cors().configurationSource(corsConfigurationSource())
        http.csrf().disable()
        http.logout().disable()
        http.formLogin().disable()
        http.httpBasic().disable()
        http.requestCache().disable()
        http.addFilterAt(tokenPreAuthFilter(), AbstractPreAuthenticatedProcessingFilter::class.java)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.exceptionHandling()
            .authenticationEntryPoint { request: HttpServletRequest?, response: HttpServletResponse, authException: AuthenticationException? ->
                response.status = HttpStatus.UNAUTHORIZED.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                objectMapper.writeValue(
                    response.outputStream,
                    ApiResponse.failure(ResultCode.UNAUTHORIZED)
                )
            }
            .accessDeniedHandler { request: HttpServletRequest?, response: HttpServletResponse, accessDeniedException: AccessDeniedException? ->
                response.status = HttpStatus.FORBIDDEN.value()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                objectMapper.writeValue(
                    response.outputStream,
                    ApiResponse.failure(ResultCode.FORBIDDEN)
                )
            }
    }

    @Bean
    fun tokenPreAuthFilter(): TokenPreAuthFilter {
        val tokenPreAuthFilter = TokenPreAuthFilter()
        tokenPreAuthFilter.setAuthenticationManager(ProviderManager(preAuthTokenProvider()))
        return tokenPreAuthFilter
    }

    @Bean
    fun preAuthTokenProvider(): PreAuthTokenProvider {
        return PreAuthTokenProvider(
            memberService = memberService,
            tokenService = tokenService,
        )
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.addAllowedOriginPattern("*")
        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    companion object {
        const val MEMBER_ROLE_NAME = "MEMBER"
    }
}
