package org.example.jakartabookstore.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    // Serve como ponto de entrada para a API REST
}
