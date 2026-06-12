
package repository;

import config.Database;

import java.sql.*;
import java.util.ArrayList;
import model.FileModel;

public class FileRepository {

    public void save(FileModel file) {
        String sql = "INSERT INTO file(nome, caminho, tamanho) VALUES (?, ?, ?)";

        try (Connection con = Database.connectionFactory();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, file.getNome());
            stmt.setString(2, file.getCaminho());
            stmt.setDouble(3, file.getTamanho());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<FileModel> listar() {
        String sql = "SELECT * FROM file";
        ArrayList<FileModel> lista = new ArrayList<>();

        try (Connection con = Database.connectionFactory();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FileModel file = new FileModel();
                file.setId(rs.getLong("id"));
                file.setNome(rs.getString("nome"));
                file.setCaminho(rs.getString("caminho"));
                file.setTamanho(rs.getDouble("tamanho"));
                lista.add(file);
            }
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM file WHERE id = ?";

        try (Connection con = Database.connectionFactory();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void limparFile() {
        try (Connection con = Database.connectionFactory();
                Statement stmt = con.createStatement()) {

            stmt.execute("DELETE FROM file");

            stmt.execute("DELETE FROM sqlite_sequence WHERE name = 'file'");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
