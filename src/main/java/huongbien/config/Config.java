package huongbien.config;

import lombok.Getter;

@Getter
public enum Config {
    MSSQL_JPA("mssql-jpa"),
    MSSQL_JPA_CREATE("mssql-jpa-create"),
    MARIADB_JPA("mariadb-jpa"),
    MARIADB_JPA_CREATE("mariadb-jpa-create");

    private final String value;

    Config(String value) {
        this.value = value;
    }
}