package com.github.santavaran.super_mario_235.code;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private final String url = "jdbc:postgresql://127.0.0.1:5588/mario";
    private final String user = "postgres";
    private final String password = "postgres";
    private final Connection connection;
    private final String creatingTable = """
            CREATE TABLE IF NOT EXISTS scores (
            UID bigint PRIMARY KEY,
            score INTEGER,
            date VARCHAR(255)
            )""";
    private final String insertData = """
            INSERT INTO scores (UID, score, date)
            VALUES (?, ?, ?)
            """;

    private final String getOne = """
            SELECT * FROM scores WHERE UID = ? ORDER BY score DESC
            """;

    private final String getAll = """
            SELECT * FROM scores ORDER BY score DESC
            """;

    public DataBase() {
        this.connection = connect();
        if (!createDB()) {
            System.out.println("DataBase is not created");
        }
    }

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public boolean createDB() {
        try {
            Statement createDbStatement = connection.createStatement();
            createDbStatement.execute(this.creatingTable);
            createDbStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error occured: " + e.getMessage());
            return false;
        }
    }

    public String[] getByUID(long UID) {
        try {
            PreparedStatement queryingState = connection.prepareStatement(this.getOne);
            queryingState.setLong(1, UID);
            ResultSet rs = queryingState.executeQuery();
            while (rs.next()) {
                if (!rs.getString("UID").isBlank()) {
                    String[] prev = new String[3];
                    prev[0] = rs.getString("UID");
                    prev[1] = rs.getString("score");
                    prev[2] = rs.getString("date");
                    return prev;
                } else {
                    System.out.println("Data not queried");
                }
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error occured: " + e.getMessage());
            return null;
        }
    }

    public List<String[]> getAll() {
        try {
            Statement getAllStmt = connection.createStatement();
            ResultSet rs = getAllStmt.executeQuery(getAll);
            List<String[]> res = new ArrayList<>();
            while (rs.next()) {
                String[] prev = new String[3];
                prev[0] = rs.getString("UID");
                prev[1] = rs.getString("score");
                prev[2] = rs.getString("date");
                res.add(prev);
            }
            return res;
        } catch (SQLException e) {
            System.out.println("Error occured: " + e.getMessage());
            return null;
        }

    }


    public boolean saveData(int score) {
        long UID = System.currentTimeMillis();
        LocalDate currentdate = LocalDate.now();
        String today = currentdate.getDayOfMonth() + "-"
                + currentdate.getMonthValue() + "-"
                + currentdate.getYear();
        try {
            PreparedStatement insertingStatement = connection.prepareStatement(this.insertData,
                    Statement.RETURN_GENERATED_KEYS);
            insertingStatement.setLong(1, UID);
            insertingStatement.setInt(2, score);
            insertingStatement.setString(3, today);
            int d = insertingStatement.executeUpdate();
            if (d > 0) {
                System.out.printf("Data inserted, %d\n", d);
                return true;
            } else {
                System.out.println("Data not inserted");
                return true;
            }
        } catch (SQLException e) {
            System.out.println(score + " " + UID + " " + currentdate);
            System.out.println("Error occured: " + e.getMessage());
            return false;
        }
    }
}