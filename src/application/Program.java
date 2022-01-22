package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.CreateSellerDao();

		Scanner sc = new Scanner(System.in);

		// forma primitiva
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Department obj = new Department(2, "books");
		Seller seller = null;
		try {
			seller = new Seller(1, "Joao Bosco", "jb@hotmail.com", sdf.parse("25/06/2020"), 1800.0, obj);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("forma primitiva ==> " + seller);
		System.out.println();

		System.out.println("=== TEST 1: seller findById ===== ");
		seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println();

		System.out.println("=== TEST 2: seller findByDepartment ===== ");
		List<Seller> list = sellerDao.findByDepartment(2);
		for (Seller sellerItemMap : list) {

			System.out.println(sellerItemMap);
		}

		System.out.println();

		System.out.println("=== TEST 3: seller findByAll ===== ");
		list = sellerDao.findAll();
		for (Seller sellerItemMap : list) {

			System.out.println(sellerItemMap);
		}

		System.out.println();

//		System.out.println("=== TEST 4: seller insert  ===== ");
//		
//		try {
//			Seller newSeller = new Seller(null, "Jonatan Cafu", "joniscafus@hotmail.com", sdf.parse("20/05/1994"), 2150.0, obj);
//			sellerDao.insert(newSeller);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		

		System.out.println("=== TEST 5: seller update ===== ");

		try {
			seller = new Seller(17, "Carlitos Tevez", "carlitos@gmail.com", sdf.parse("10/09/1985"), 7700.0, obj);
			sellerDao.update(seller);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();

		System.out.println("=== TEST 6: seller delete ===== ");

		System.out.print("Digite um Id para remover: ");
		Integer Id = sc.nextInt();
		sellerDao.deleteById(Id);
		System.out.println();

	}

}
