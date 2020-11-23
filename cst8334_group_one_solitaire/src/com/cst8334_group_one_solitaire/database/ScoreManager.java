package com.cst8334_group_one_solitaire.database;

import com.cst8334_group_one_solitaire.beans.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScoreManager {

    public static void insertScore() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO score (Username, score) VALUES (?,?) ON DUPLICATE KEY UPDATE score = score + "+Game.getInstance().getScore()+"";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, Game.getInstance().getGameSession());
            statement.setInt(2, Game.getInstance().getScore());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
        conn.close();

    }

    public static int fetchScore() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT score FROM score WHERE Username = ?";
        int score = 0;
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, Game.getInstance().getGameSession());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                score = resultSet.getInt("score");
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        conn.close();
        return score;
    }


}
