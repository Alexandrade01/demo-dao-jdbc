package model.dao;

import model.dao.impl.SellerDaoJDBC;

//classe DaoFactory sendo responsavel receber e enviar as classes Daos
public class DaoFactory {
	
	public static SellerDao CreateSellerDao() {
		
		return new SellerDaoJDBC();
	}

}
