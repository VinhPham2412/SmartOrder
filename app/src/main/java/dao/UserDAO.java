package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import context.DbContext;
import model.User;

public class UserDAO {
    /**
     * Get DBContext.<br>
     */
    private DbContext db;
    /**
     * Call Connection.<br>
     */
    private Connection connection = null;
    /**
     * Call PreparedStatement.<br>
     */
    private PreparedStatement ps = null;
    /**
     * Call ResultSet.<br>
     */
    private ResultSet rs = null;

    /**
     * Constructor.<br>
     */
    public UserDAO() {
        db=new DbContext();
    }

    public void insertUser(String id,String firstName,String lastName,String phone, int roleId) throws SQLException {
        String sql="insert in to Users(Id,FirstName,LastName,Phone,Role_Id) values(?,?,?,?,?)";
        try {
            connection = db.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1,id);
            ps.setString(2,firstName);
            ps.setString(3,lastName);
            ps.setString(4,phone);
            ps.setInt(5,roleId);
            ps.execute();

        }catch (Exception e){
            throw  e;
        }finally {
            try {
                db.closeConnection(rs,ps,connection);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
