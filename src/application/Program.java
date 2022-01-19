package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Department obj = new Department(1, "books");
		Seller seller = null;
		try {
			seller = new Seller(1, "Joao Bosco", "jb@hotmail.com", sdf.parse("25/06/2020"), 1800.0, obj);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//SellerDaoJDBC sellerDaoJDBC = (SellerDaoJDBC) DaoFactory.CreateSellerDao();
		
		SellerDao sellerDao = DaoFactory.CreateSellerDao();
		
		System.out.println(seller);

	}

}
