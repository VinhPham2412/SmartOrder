package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import context.DbContext;
import model.Buffet;
import model.Food;

public class FoodDAO {
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
    public FoodDAO() {
        db=new DbContext();
    }

    /**
     * Get all Foods belong to buffet.<br>
     * @return foodList
     * @throws SQLException
     */
    public List<Food> getFoodsOfBuffet(int buffetId) throws SQLException {
        List<Food> foodList=new ArrayList<>();
        String sql="select distinct f.Id,f.Name,f.Price,f.Description,f.Calories,f.Category_Id from Foods f,Buffets b, Buffets_Foods bf where f.Id=bf.Food_Id and bf.Buffet_Id=?";
        try {
            connection = db.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1,buffetId);
            rs=ps.executeQuery();
            while (rs.next()){
                 Food food=new Food(rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getFloat("Price"),
                        rs.getString("Description"),
                         rs.getInt("Calories"),
                         rs.getInt("Category_Id"));
                foodList.add(food);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            db.closeConnection(rs,ps,connection);
        }
        return foodList;
    }
}
