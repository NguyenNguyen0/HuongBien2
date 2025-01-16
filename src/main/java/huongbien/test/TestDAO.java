package huongbien.test;

import huongbien.dao.CategoryDAO;
import huongbien.entity.Category;
import huongbien.jpa.PersistenceUnit;

public class TestDAO {
    public static void main(String[] args) {
//         Sài persistence nào thì truyền vào
         CategoryDAO categoryDAO = new CategoryDAO(PersistenceUnit.MARIADB_JPA_CREATE);
         Category category = new Category();
         category.setId("C001");
         category.setName("Category 1");
//         Thêm
         categoryDAO.add(category);

//         Sửa
         category.setName("Category 2");
         categoryDAO.update(category);

//         Get
         System.out.println(categoryDAO.getById("C001"));
         System.out.println(categoryDAO.getByName("2"));
         System.out.println(categoryDAO.countAll());
    }
}
