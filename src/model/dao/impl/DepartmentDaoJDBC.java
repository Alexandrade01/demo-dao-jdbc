package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	Connection connect;

	public DepartmentDaoJDBC(Connection connection) {

		connect = connection;
	}

	@Override
	public void insert(Department obj) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = connect.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, obj.getName());

			int linhasAfetadas = ps.executeUpdate();
			if (linhasAfetadas > 0) {

				rs = ps.getGeneratedKeys();

				if (rs.next()) {
					int id = rs.getInt(1);
					System.out.println("Nova linha inserida ID:" + id);
					obj.setId(id);
					System.out.println(obj);
				}
			} else {
				throw new DbException("0 linhas alteradas.");
			}

		} catch (SQLException e) {
			e.getMessage();
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public void update(Department obj) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = connect.prepareStatement("UPDATE department SET Name = ? " + "WHERE Id = ?");

			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			
			int linhasafetadas = ps.executeUpdate();
			System.out.println(linhasafetadas + " linhas afetadas");

		}

		catch (SQLException e) {
			e.getMessage();
		} 
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public void deleteById(Integer id) {
		
			PreparedStatement ps = null;
			
			
			try {
				ps = connect.prepareStatement("delete from department where id = ?");
				
				ps.setInt(1, id);
				
				int linhasAfetadas = ps.executeUpdate();
				if(linhasAfetadas > 0) {
					System.out.println("Linha deletada com sucesso!");
				}
				else {
					System.out.println("Não houve alterações !");
				}
			} 
			catch (SQLException e) {
				
				throw new DbException(e.getMessage());
			}
			finally {
				DB.closeStatement(ps);
				
			}

	}

	@Override
	public Department findById(Integer id) {

		ResultSet rs = null;
		PreparedStatement ps = null;
		Department department = null;
		
		try {
			ps = connect.prepareStatement("select Id,Name from department where Id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				department = instantiateDepartment(rs);
				
			}
			
			
			else {
				System.out.println("Não houve alterações !");
			}
		} 
		catch (SQLException e) {
			
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return department;
	}

	@Override
	public List<Department> findAll() {
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		Department department = null;
		List<Department> list = new ArrayList<>();
		
		try {
			
			ps = connect.prepareStatement("SELECT Id, Name FROM department Order by Name ");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				
				department = instantiateDepartment(rs);
				list.add(department);
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
		return list;
		
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {

		return new Department(rs.getInt("Id"), rs.getString("Name"));
	}

}
