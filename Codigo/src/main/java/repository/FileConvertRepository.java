
package repository;

import config.Database;

import java.sql.*;
import java.util.ArrayList;
import model.FileConvertModel;

public class FileConvertRepository {

    public void save(FileConvertModel file) {
        String sql = """
                INSERT INTO file_convert (nome, novo_caminho, tamanho)
                VALUES (?, ?, ?)
                """;

        try (Connection con = Database.connectionFactory();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, file.getNome());
            stmt.setString(2, file.getNovoCaminho());
            stmt.setDouble(3, file.getTamanho());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<FileConvertModel> showAll() {
        String sql = "SELECT * FROM file_convert";
        ArrayList<FileConvertModel> lista = new ArrayList<>();

        try (Connection con = Database.connectionFactory();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FileConvertModel file = new FileConvertModel();
                file.setId(rs.getLong("id"));
                file.setNome(rs.getString("nome"));
                file.setNovoCaminho(rs.getString("novo_caminho"));
                file.setTamanho(rs.getDouble("tamanho"));

                String dt = rs.getString("dt_criacao");
                if (dt != null) {
                    file.setDt_criacao(java.time.LocalDate.parse(dt));
                }
                lista.add(file);
            }
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM file_convert WHERE id = ?";

        try (Connection con = Database.connectionFactory();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void limparFileConvert() {
        try (Connection con = Database.connectionFactory();
                Statement stmt = con.createStatement()) {

            stmt.execute("DELETE FROM file_convert");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name = 'file_convert'");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
