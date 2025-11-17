package workwell.WorkWell.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
		
		// Configuração para cache de dashboard (5 minutos)
		cacheManager.registerCustomCache("dashboard", Caffeine.newBuilder()
			.maximumSize(100)
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.recordStats()
			.build());
		
		// Configuração para cache de enquetes/atividades ativas (2 minutos)
		cacheManager.registerCustomCache("enquetesAtivas", Caffeine.newBuilder()
			.maximumSize(200)
			.expireAfterWrite(2, TimeUnit.MINUTES)
			.recordStats()
			.build());
		
		cacheManager.registerCustomCache("atividadesAtivas", Caffeine.newBuilder()
			.maximumSize(200)
			.expireAfterWrite(2, TimeUnit.MINUTES)
			.recordStats()
			.build());
		
		// Configuração para cache de avaliações ativas (3 minutos)
		cacheManager.registerCustomCache("avaliacoesAtivas", Caffeine.newBuilder()
			.maximumSize(200)
			.expireAfterWrite(3, TimeUnit.MINUTES)
			.recordStats()
			.build());
		
		// Configuração para cache de empresa por token (30 minutos - raramente muda)
		cacheManager.registerCustomCache("empresaToken", Caffeine.newBuilder()
			.maximumSize(500)
			.expireAfterWrite(30, TimeUnit.MINUTES)
			.recordStats()
			.build());
		
		// Configuração para cache de estatísticas (3 minutos)
		cacheManager.registerCustomCache("estatisticas", Caffeine.newBuilder()
			.maximumSize(500)
			.expireAfterWrite(3, TimeUnit.MINUTES)
			.recordStats()
			.build());
		
		// Configuração para cache de agenda do dia (1 minuto - dados mais dinâmicos)
		cacheManager.registerCustomCache("agendaDia", Caffeine.newBuilder()
			.maximumSize(500)
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.recordStats()
			.build());
		
		// Configuração para cache de insights de IA (10 minutos - respostas caras)
		cacheManager.registerCustomCache("insightsAI", Caffeine.newBuilder()
			.maximumSize(100)
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.recordStats()
			.build());
		
		// Cache padrão para outros casos
		cacheManager.setCaffeine(Caffeine.newBuilder()
			.maximumSize(200)
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.recordStats());
		
		return cacheManager;
	}
}

