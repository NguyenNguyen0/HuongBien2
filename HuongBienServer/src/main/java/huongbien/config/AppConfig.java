package huongbien.config;

import io.github.cdimascio.dotenv.Dotenv;

public class AppConfig {
    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key);
    }

    public static String getDbUrl() {
        return dotenv.get("DB_URL");
    }

    public static String getDbUser() {
        return dotenv.get("DB_USER");
    }

    public static String getDbPassword() {
        return dotenv.get("DB_PASSWORD");
    }

    public static String getEmailUsername() {
        return dotenv.get("EMAIL_USERNAME");
    }

    public static String getEmailPassword() {
        return dotenv.get("EMAIL_PASSWORD");
    }
}
