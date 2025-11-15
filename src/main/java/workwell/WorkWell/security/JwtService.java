package workwell.WorkWell.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import workwell.WorkWell.entity.Usuario;

@Component
public class JwtService {

	private final Key signingKey;
	private final long expirationMillis;

	public JwtService(
		@Value("${security.jwt.secret}") String secret,
		@Value("${security.jwt.expiration}") long expirationMillis) {
		this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMillis = expirationMillis;
	}

	public TokenPayload generateToken(Usuario usuario) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", usuario.getEmail());
		claims.put("role", usuario.getRole().name());
		Optional.ofNullable(usuario.getEmpresa())
			.map(emp -> emp.getId().toString())
			.ifPresent(empresaId -> claims.put("empresaId", empresaId));

		Instant now = Instant.now();
		Instant expiration = now.plusMillis(expirationMillis);

		String jwt = Jwts.builder()
			.setSubject(usuario.getId().toString())
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(expiration))
			.addClaims(claims)
			.signWith(signingKey, SignatureAlgorithm.HS256)
			.compact();

		return new TokenPayload(jwt, expiration);
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).get("email", String.class);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		Date expiration = extractAllClaims(token).getExpiration();
		return expiration.before(new Date());
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(signingKey)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public record TokenPayload(String token, Instant expiresAt) {
	}
}

