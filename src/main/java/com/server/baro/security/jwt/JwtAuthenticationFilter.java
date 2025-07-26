package com.server.baro.security.jwt;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.server.baro.common.ErrorCode;
import com.server.baro.common.exception.CustomAuthenticationException;
import com.server.baro.security.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JWT Filter")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String[] whiteList = {
		"/signup",
		"/login",
		"/h2-console/**",
		"/swagger-ui/**",
		"/swagger-ui.html",
		"/v3/api-docs/**"
	};
	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;
	private final AuthenticationEntryPoint authenticationEntryPoint;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String requestURI = request.getRequestURI();

		if (isMatchedWhiteList(requestURI)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			String jwtToken = jwtUtil.getJwtFromHeader(request);

			if (!StringUtils.hasText(jwtToken)) {
				log.error("JWT 토큰이 요청 헤더에 존재하지 않습니다.");
				throw new CustomAuthenticationException(ErrorCode.TOKEN_NOT_FOUND);
			}

			if (jwtUtil.isTokenExpired(jwtToken)) {
				log.error("JWT 토큰이 만료되었습니다.");
				throw new CustomAuthenticationException(ErrorCode.TOKEN_EXPIRED);
			}

			if (!jwtUtil.validateToken(jwtToken)) {
				log.error("JWT 토큰 유효성 검증을 실패했습니다.");
				throw new CustomAuthenticationException(ErrorCode.INVALID_TOKEN);
			}

			Claims userInfo = jwtUtil.getUserInfoFromToken(jwtToken);
			log.info("JWT 토큰에서 추출한 사용자 username: {}", userInfo.getSubject());
			setAuthentication(userInfo.getSubject());

			filterChain.doFilter(request, response);
		} catch (CustomAuthenticationException e) {
			authenticationEntryPoint.commence(request, response, e);
		}
	}

	private boolean isMatchedWhiteList(String requestURI) {
		return PatternMatchUtils.simpleMatch(whiteList, requestURI);
	}

	private void setAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		log.info("UserDetails 생성 완료 - username: {}", userDetails.getUsername());
		log.info("사용자 권한 목록: {}", userDetails.getAuthorities());

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(username);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	private Authentication createAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
