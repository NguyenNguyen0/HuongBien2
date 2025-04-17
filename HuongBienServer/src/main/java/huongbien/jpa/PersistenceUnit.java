package huongbien.jpa;

import lombok.Getter;

@Getter
public enum PersistenceUnit {
    MSSQL_JPA("mssql-jpa"),
    MSSQL_JPA_CREATE("mssql-jpa-create"),
    MARIADB_JPA("mariadb-jpa"),
    MARIADB_JPA_CREATE("mariadb-jpa-create");

    private final String value;

    PersistenceUnit(String value) {
        this.value = value;
    }
}