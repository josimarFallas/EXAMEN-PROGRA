package com.ejemplo.dao;

import com.ejemplo.modelo.Item;
import com.ejemplo.util.DB;
import java.sql.*;
import java.util.*;

public class ItemDAO {

    public List<Item> findAll() {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT id, titulo, descripcion, precio, imagen FROM items ORDER BY id DESC";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Item(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getString("imagen")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Item findById(int id) {
        String sql = "SELECT id, titulo, descripcion, precio, imagen FROM items WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Item(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("imagen")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Item i) {
        String sql = "INSERT INTO items (titulo, descripcion, precio, imagen) VALUES (?, ?, ?, ?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, i.getTitulo());
            ps.setString(2, i.getDescripcion());
            ps.setDouble(3, i.getPrecio());
            ps.setString(4, i.getImagen());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Item i) {
        String sql = "UPDATE items SET titulo=?, descripcion=?, precio=?, imagen=? WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, i.getTitulo());
            ps.setString(2, i.getDescripcion());
            ps.setDouble(3, i.getPrecio());
            ps.setString(4, i.getImagen());
            ps.setInt(5, i.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM items WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
