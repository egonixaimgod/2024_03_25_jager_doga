import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        String url = "172.16.16.246/konyvek";
        String username = "diak";
        String password = "diak";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            importKolcsonzok(connection, "kolcsonzok.csv");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void importKolcsonzok(Connection connection, String filename) {
        String line;
        String delimiter = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Fejléc átugrása

            String sql = "INSERT INTO Kolcsonzok (nev, szulIdo) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            while ((line = br.readLine()) != null) {
                String[] data = line.split(delimiter);
                String nev = data[0];
                String szulIdo = data[1];

                preparedStatement.setString(1, nev);
                preparedStatement.setString(2, szulIdo);

                preparedStatement.executeUpdate();
            }
            System.out.println("Az adatok sikeresen importálva a Kolcsonzok táblába.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
