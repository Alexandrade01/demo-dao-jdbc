package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection connect;

	public SellerDaoJDBC(Connection connect) {

		this.connect = connect;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Seller seller = null;

		try {
			ps = connect.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			// indicador dos ? (interrogação das querys)
			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				seller = instantiateSeller(rs, dep);
			}

			return seller;
		}

		catch (SQLException e) {

			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}

	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {

		return new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), rs.getDate("BirthDate"),
				rs.getDouble("BaseSalary"), dep);
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {

		return new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Integer departmentID) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		

		try {
			ps = connect.prepareStatement("SELECT seller.*, department.name as DepName "
					+"FROM seller INNER JOIN  department "
					+"ON seller.DepartmentId = department.Id "
					+"where DepartmentId  = ? "
					+"Order by Name ");

			List<Seller> list = new ArrayList<>();
			Map<Integer,Department> map  = new HashMap<Integer,Department>();
			ps.setInt(1, departmentID);

			rs = ps.executeQuery();

			while (rs.next()) {
				Department dep = map.get(departmentID);
				if(dep == null) {
					
					map.put(departmentID, instantiateDepartment(rs));
				}
				
				Seller seller =instantiateSeller(rs,map.get(departmentID)); 
				list.add(seller);
			}
			
			return list;
		} 
		
		catch (SQLException e) {
			
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

}
