package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		DepartmentDao departmentDao = DaoFactory.CreateDepartmentDao();
		String name = null;
		Integer Id = null;
		Department department = null;
		Scanner sc = new Scanner(System.in);

		
		System.out.print("Enter a name for department: ");
		name = sc.next();
		System.out.println("=== TEST 2: Department insert ===== ");
		department = new Department(null,name);
		departmentDao.insert(department);	
		
		
		System.out.println();
		
		
		System.out.print("Enter a Id for department: ");
		Id = sc.nextInt();
		System.out.print("Enter a name for department: ");
		name = sc.next();
		sc.nextLine();
		System.out.println("=== TEST 2: Department insert ===== ");
		department = new Department(Id,name);
		departmentDao.update(department);

		
		System.out.println();

		
		System.out.print("Enter a department id to delete: ");
		Id = sc.nextInt();
		departmentDao.deleteById(Id);

		System.out.println();
		
		
		System.out.print("Enter a department id to find: ");
		Id = sc.nextInt();
		System.out.println(departmentDao.findById(Id));

		System.out.println();

		List<Department> list = new ArrayList<>();
		list = departmentDao.findAll();

		for (Department departmentList : list) {
			System.out.println(departmentList);
		}

	}

}
