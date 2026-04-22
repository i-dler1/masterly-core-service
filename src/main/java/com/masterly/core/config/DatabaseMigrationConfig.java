//package com.masterly.core.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import jakarta.annotation.PostConstruct;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//
//@Slf4j
//@Configuration
//@Profile("!test")  // НЕ запускается в тестах!
//public class DatabaseMigrationConfig {
//
//    @Value("${spring.datasource.url}")
//    private String appUrl;
//
//    @Value("${spring.datasource.username}")
//    private String appUser;
//
//    @Value("${spring.datasource.password}")
//    private String appPassword;
//
//    @Value("${database.admin.url:jdbc:postgresql://localhost:5432/postgres}")
//    private String adminUrl;
//
//    @Value("${database.admin.username:postgres}")
//    private String adminUser;
//
//    @Value("${database.admin.password:postgres}")
//    private String adminPassword;
//
//    @PostConstruct
//    public void initDatabase() {
//        log.info("🔄 Проверяем существование базы данных...");
//        createDatabaseIfNotExists();
//        log.info("✅ База данных готова к миграциям");
//    }
//
//    private void createDatabaseIfNotExists() {
//        String dbName = extractDatabaseName(appUrl);
//
//        try (Connection conn = DriverManager.getConnection(adminUrl, adminUser, adminPassword);
//             Statement stmt = conn.createStatement()) {
//
//            // Проверяем существование БД
//            var rs = stmt.executeQuery(
//                    "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'"
//            );
//
//            if (!rs.next()) {
//                log.info("📦 База данных '{}' не найдена. Создаём...", dbName);
//
//                // Создаём пользователя
//                try {
//                    stmt.execute(String.format("CREATE USER %s WITH PASSWORD '%s'", appUser, appPassword));
//                    log.info("✅ Пользователь '{}' создан", appUser);
//                } catch (Exception e) {
//                    if (!e.getMessage().contains("already exists")) {
//                        log.warn("⚠️ Ошибка создания пользователя: {}", e.getMessage());
//                    }
//                }
//
//                // Создаём БД
//                stmt.execute(String.format("CREATE DATABASE %s OWNER %s", dbName, appUser));
//                log.info("✅ База данных '{}' создана", dbName);
//            } else {
//                log.info("ℹ️ База данных '{}' уже существует", dbName);
//            }
//
//        } catch (Exception e) {
//            log.error("❌ Ошибка при создании БД: {}", e.getMessage());
//        }
//    }
//
//    private String extractDatabaseName(String url) {
//        int lastSlash = url.lastIndexOf('/');
//        String dbName = url.substring(lastSlash + 1);
//        int questionMark = dbName.indexOf('?');
//        if (questionMark != -1) {
//            dbName = dbName.substring(0, questionMark);
//        }
//        return dbName;
//    }
//}