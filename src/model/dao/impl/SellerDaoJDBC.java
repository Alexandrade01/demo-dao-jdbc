package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = connect.prepareStatement("insert into " + "seller (Name,Email,BirthDate,BaseSalary, DepartmentId) "
					+ "values " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			// preparação dos dados para associar ao banco de dados
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				// getgeneratedkey é um metodo que retorna a key que foi gerada automaticamente
				rs = ps.getGeneratedKeys();
				if (rs.next()) {

					int id = rs.getInt(1);

					System.out.printf("Insert ID: %d sucessuful \n", id);
					// associacao do objeto com o id do banco de dados
					obj.setId(id);
					System.out.println(obj);
				}
			}

			else {
				throw new DbException("0 linhas adicionadas");
			}
		}

		catch (SQLException e) {

			e.getMessage();
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void update(Seller obj) {
		
		PreparedStatement ps = null;
		
		
		try {
			ps = connect.prepareStatement("update seller "
					+ "set Name = ?, "
					+ "Email = ?, BirthDate = ?,BaseSalary = ?,DepartmentId = ? "
					+ "where Id = ?");
			
			// preparação dos dados para associar ao banco de dados
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
			
			int linhasafetadas = ps.executeUpdate();
			System.out.println(linhasafetadas + " linhas afetadas");
		
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			
		}
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement ps = null;
		
		try {
				ps = connect.prepareStatement("DELETE FROM seller where Id = ?");
				ps.setInt(1, id);
				
				int linhasAfetadas = ps.executeUpdate();
				if(linhasAfetadas > 0) {
					System.out.println("Linha deletada com sucesso!");
				}
				else {
					System.out.println("Não houve alterações !");
				}
				
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			
		}

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

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Seller> list = new ArrayList<>();

		try {

			Map<Integer, Department> map = new HashMap<Integer, Department>();

			ps = connect.prepareStatement("SELECT seller.*, department.name as DepName "
					+ "FROM seller INNER JOIN  department " + "ON seller.DepartmentId = department.Id ");
			rs = ps.executeQuery();

			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {

					map.put(rs.getInt("DepartmentId"), instantiateDepartment(rs));
				}

				Seller seller = instantiateSeller(rs, map.get(rs.getInt("DepartmentId")));
				list.add(seller);
			}

			return list;
		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Integer departmentID) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = connect.prepareStatement("SELECT seller.*, department.name as DepName "
					+ "FROM seller INNER JOIN  department " + "ON seller.DepartmentId = department.Id "
					+ "where DepartmentId  = ? " + "Order by Name ");

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			ps.setInt(1, departmentID);

			rs = ps.executeQuery();

			// devemos ter apenas um objeto para cada departmento
			// por isso criamos um map que caso ja exista o department ele o ira utilizar
			while (rs.next()) {
				Department dep = map.get(departmentID);
				if (dep == null) {

					map.put(departmentID, instantiateDepartment(rs));
				}

				Seller seller = instantiateSeller(rs, map.get(departmentID));
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
