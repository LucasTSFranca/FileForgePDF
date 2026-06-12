
package config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_PATH = resolveDbPath();

    /**
     * Resolve o caminho do banco de dados SQLite.
     *
     * Usa %APPDATA%\FileForge\banco\pdf_db no Windows (ou ~/FileForge em outros sistemas).
     * Dessa forma o banco fica em um local fixo e gravável, independente de onde
     * o .jar ou o .exe (Launch4j) esteja instalado.
     */
    private static String resolveDbPath() {
        // %APPDATA% no Windows (ex: C:\Users\Jhonny\AppData\Roaming)
        // No Linux/Mac usa o home do usuário como fallback
        String appData = System.getenv("APPDATA");
        File baseDir;
        if (appData != null && !appData.isBlank()) {
            baseDir = new File(appData, "FileForge");
        } else {
            baseDir = new File(System.getProperty("user.home"), "FileForge");
        }

        File dbFile = new File(baseDir, "banco" + File.separator + "pdf_db");
        dbFile.getParentFile().mkdirs(); // cria as pastas se não existirem
        return dbFile.getAbsolutePath();
    }

    public static Connection connectionFactory() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao conectar ao banco: " + DB_PATH, e);
        }
    }

    public static void initSchema() {
        String sql = """
                CREATE TABLE IF NOT EXISTS file (
                    id      INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome    TEXT    NOT NULL,
                    caminho TEXT    NOT NULL,
                    tamanho REAL    NOT NULL
                );
                CREATE TABLE IF NOT EXISTS file_convert (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome         TEXT    NOT NULL,
                    novo_caminho TEXT    NOT NULL,
                    tamanho      REAL    NOT NULL,
                    dt_criacao   TEXT    DEFAULT (date('now'))
                );
                """;

        try (Connection con = connectionFactory();
                Statement stmt = con.createStatement()) {

            for (String s : sql.split(";")) {
                String trimmed = s.strip();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao inicializar o schema do banco", e);
        }
    }
}
