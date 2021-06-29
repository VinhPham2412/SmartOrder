package dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public Bitmap getImage(int buffet_Id){
        String sql="Select Image from Images where Type like ";
        try {
            connection = db.getConnection();
            ps = connection.prepareStatement(sql);
            rs=ps.executeQuery();
            while (rs.next()){
                 byte [] bytesImage=rs.getBytes(1);
                Bitmap bitmapImage= BitmapFactory.decodeByteArray(bytesImage,0,bytesImage.length);
                return bitmapImage;

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
