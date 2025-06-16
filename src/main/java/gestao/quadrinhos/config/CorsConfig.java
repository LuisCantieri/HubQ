package gestao.quadrinhos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);
        // sua origem React
        cfg.addAllowedOrigin("https://hub-q.vercel.app");
        // todos os métodos, incluindo OPTIONS
        cfg.addAllowedMethod("*");
        // todos os headers de request
        cfg.addAllowedHeader("*");
        // exponha também qualquer header que precise
        cfg.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        // aplica para todas as rotas
        src.registerCorsConfiguration("/**", cfg);
        return new CorsFilter(src);
    }
}
