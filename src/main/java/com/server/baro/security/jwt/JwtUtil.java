package com.server.baro.security.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.server.baro.user.entity.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
	public static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
	public static final String BEARER_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_KEY = "auth";

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private Key key;

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("${jwt.access-expires-in}")
	private long accessExpiresIn;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	// JWT 생성
	public String createToken(String username, UserRole role) {
		Date now = new Date();

		return Jwts.builder()
			.setSubject(username)
			.claim(AUTHORIZATION_KEY, role)
			.setExpiration(new Date(now.getTime() + accessExpiresIn))
			.setIssuedAt(now)
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	// JWT 추출
	public String getJwtFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		return null;
	}

	// 토큰 만료 시간 검증
	public boolean isTokenExpired(String token) {
		try {
			Date expiration = parseJwt(token)
				.getBody()
				.getExpiration();

			return expiration.before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		} catch (Exception e) {
			log.error("토큰 만료 시간 검증 실패: {}", e.getMessage());
			return false;
		}
	}

	// JWT 검증
	public boolean validateToken(String token) {
		try {
			parseJwt(token);
			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다. : {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT token 입니다. : {} ", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다. : {} ", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다. : {}", e.getMessage());
		}

		return false;
	}

	// JWT에서 사용자 정보 가져오기
	public Claims getUserInfoFromToken(String token) {
		return parseJwt(token).getBody();
	}

	private Jws<Claims> parseJwt(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
	}
}
