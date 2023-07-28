package org.Stasy.PublicPrivacyAppBackendHeroku.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/collaborator/login")
                .allowedOriginPatterns("https://th-hsieh.github.io")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .exposedHeaders("loginToken")//Exposing custom headers like jwtToken allows the client to access the token value if needed, for example, to extract authentication information from the response headers.
                .exposedHeaders("dashboardToken")
                .maxAge(3600);

        registry.addMapping("/collaborator/logout")
                .allowedOriginPatterns("https://th-hsieh.github.io")
                //.allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .exposedHeaders("loginToken")//Exposing custom headers like jwtToken allows the client to access the token value if needed, for example, to extract authentication information from the response headers.
                .exposedHeaders("dashboardToken")
                .maxAge(3600);

        registry.addMapping("/collaborator/register")
                .allowedOriginPatterns("https://th-hsieh.github.io")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept");
                //.exposedHeaders("loginToken")//Exposing custom headers like jwtToken allows the client to access the token value if needed, for example, to extract authentication information from the response headers.
                //.exposedHeaders("dashboardToken")
                //.maxAge(3600);

        registry.addMapping("/collaborator/dashboard")
                .allowedOriginPatterns("https://th-hsieh.github.io")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .exposedHeaders("loginToken")//Exposing custom headers like jwtToken allows the client to access the token value if needed, for example, to extract authentication information from the response headers.
                .exposedHeaders("dashboardToken")
                .maxAge(3600);

        registry.addMapping("/collaborator/resetPassword")
                .allowedOriginPatterns("https://th-hsieh.github.io")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .exposedHeaders("loginToken")//Exposing custom headers like jwtToken allows the client to access the token value if needed, for example, to extract authentication information from the response headers.
                .exposedHeaders("dashboardToken")
                .maxAge(3600);

        registry.addMapping("/collaborator/deleteAccount")
                .allowedOriginPatterns("https://th-hsieh.github.io")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .exposedHeaders("loginToken")//Exposing custom headers like jwtToken allows the client to access the token value if needed, for example, to extract authentication information from the response headers.
                //.exposedHeaders("dashboardToken")
                .maxAge(3600);

//        registry.addMapping("/forum/opinions")
//                .allowedOriginPatterns("http://localhost:3000")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("Origin", "Content-Type", "Accept")
//                .exposedHeaders("loginToken","dashboardToken")
//                .maxAge(3600);

        registry.addMapping("/forum/opinions/**")
                .allowedOriginPatterns("https://th-hsieh.github.io")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .exposedHeaders("loginToken")
                .exposedHeaders("dashboardToken")
                .maxAge(3600);


        registry.addMapping("/forum/opinions/add")
                .allowedOriginPatterns("https://th-hsieh.github.io")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .exposedHeaders("loginToken")
                .exposedHeaders("dashboardToken")
                .maxAge(3600);

        registry.addMapping("/forum/opinions/edit/{id}")
                .allowedOriginPatterns("https://th-hsieh.github.io")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS","PUT")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .exposedHeaders("loginToken")
                .exposedHeaders("dashboardToken")
                .maxAge(3600);
    }
}
