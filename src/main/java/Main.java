
import java.sql.*;
import java.util.Scanner;
public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/apartment?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "1356";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            initDB();
            addFlat(sc);
            addFlat(sc);
            viewFlats();
            chooseFlat(sc);

        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    private static void chooseFlat(Scanner sc) throws SQLException{
        System.out.println("how many rooms you wanna in apartment?");
        System.out.println("-> ");
        String rooms = sc.nextLine();
        PreparedStatement prepS = conn.prepareStatement("SELECT * FROM Flats WHERE rooms = ?");
        try {
            prepS.setString(1, rooms);
            ResultSet rs = prepS.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            prepS.close();
        }

    }
    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Flats");
            st.execute("CREATE TABLE Flats (id INT NOT NULL " +
                    "AUTO_INCREMENT PRIMARY KEY, district VARCHAR(20) " +
                    "NOT NULL, address VARCHAR(20)" +
                    "NOT NULL, square VARCHAR(20)" +
                    "NOT NULL, rooms INT " +
                    "NOT NULL, price INT)");
        } finally {
            st.close();
        }
    }
    private static void viewFlats() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Flats");
        try {
            ResultSet rs = ps.executeQuery();

            try {
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }
    private static void addFlat(Scanner sc) throws SQLException {
        System.out.print("Enter district: ");
        String district = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter square: ");
        String square = sc.nextLine();
        System.out.print("Enter rooms count: ");
        String sRooms = sc.nextLine();
        System.out.print("Enter price: ");
        String sPrice = sc.nextLine();
        int price = Integer.parseInt(sPrice);
        int rooms = Integer.parseInt(sRooms);
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Flats (district, address, square, rooms, price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.setString(3, square);
            ps.setInt(4, rooms);
            ps.setInt(5, price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

}
