package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import context.DbContext;
import model.Buffet;

public class BuffetDAO {
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
    public BuffetDAO() {
        db=new DbContext();
    }

    /**
     * Get all Buffet.<br>
     * @return buffetList
     *
     */
    public List<Buffet> getAllBuffets(){
        List<Buffet> buffetList=new ArrayList<>();
        String sql="select * from Buffets";
        try {
            connection = db.getConnection();
            ps = connection.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                Buffet buffet=new Buffet(rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getFloat("Price"),
                        rs.getString("Description"));
                buffetList.add(buffet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                db.closeConnection(rs,ps,connection);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return buffetList;
    }
}
