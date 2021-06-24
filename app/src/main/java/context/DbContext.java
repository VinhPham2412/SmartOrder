package context;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbContext {
    /**
     * Store connection.<br>
     */
    private Connection connection;
    /**
     * Get connection of your database.<br>
     * @return connection
     */
    public Connection getConnection(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(DBConstant.CLASSS);
            connection = DriverManager.getConnection(DBConstant.URL,DBConstant.USERNAME,DBConstant.PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    /**
     * When you are done with using your Connection, you need to close in order
     * to release any other database resources.
     *
     * @param  rs
     * @param ps
     * @param con
     * @throws SQLException
     */
    public void closeConnection(ResultSet rs, PreparedStatement ps, Connection con) throws SQLException {
        if (rs != null && !rs.isClosed()) {
            rs.close();
        }
        if (ps != null && !ps.isClosed()) {
            ps.close();
        }
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
}
