package cz.fi.muni.pa165.tasks;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;

 
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {

	@PersistenceUnit
	private EntityManagerFactory emf;

	private Category categoryElectro = new Category();
	private Category categoryKitchen = new Category();
	private Product productFlashlight = new Product();
	private Product productRobot = new Product();
	private Product productPlate = new Product();

	@BeforeClass
	public void createTestData() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		categoryElectro.setName("Electro");
		categoryKitchen.setName("Kitchen");
        productFlashlight.setName("Flashlight");
		productRobot.setName("Kitchen Robot");
		productPlate.setName("Plate");

		productFlashlight.addCategory(categoryElectro);
		categoryElectro.addProduct(productFlashlight);      ////////////// oboustranne vazby !!!
		productRobot.addCategory(categoryKitchen);
		categoryKitchen.addProduct(productRobot);
		productRobot.addCategory(categoryElectro);
		categoryElectro.addProduct(productRobot);
		productPlate.addCategory(categoryKitchen);
		categoryKitchen.addProduct(productPlate);

        em.persist(categoryElectro);
        em.persist(categoryKitchen);
		em.persist(productFlashlight);
		em.persist(productRobot);
		em.persist(productPlate);

		em.getTransaction().commit();
        em.close();     ///////// nutne ?
	}

    @Test(expectedExceptions=ConstraintViolationException.class)
    public void testDoesntSaveNullName() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Product p = new Product();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testCategoryKitchen(){
        EntityManager em = emf.createEntityManager();
        Category found = em.find(Category.class, categoryKitchen.getId());

        Assert.assertEquals(found.getProducts().size(), 2);
        assertContainsProductWithName(found.getProducts(), "Kitchen Robot");
        assertContainsProductWithName(found.getProducts(), "Plate");

        em.close();
    }

    @Test
    public void testCategoryElectro(){
        EntityManager em = emf.createEntityManager();
        Category found = em.find(Category.class, categoryElectro.getId());

        Assert.assertEquals(found.getProducts().size(), 2);
        assertContainsProductWithName(found.getProducts(), "Kitchen Robot");
        assertContainsProductWithName(found.getProducts(), "Flashlight");

        em.close();
    }

    @Test
    public void testFlashlight(){
        EntityManager em = emf.createEntityManager();
        Product found = em.find(Product.class, productFlashlight.getId());

        Assert.assertEquals(found.getCategories().size(), 1);
        assertContainsCategoryWithName(found.getCategories(), "Electro");

//        Assert.assertEquals(found.getCategories().iterator().next().getName(), "Electro");

        em.close();
    }

    @Test
    public void testKitchenRobot(){
        EntityManager em = emf.createEntityManager();
        Product found = em.find(Product.class, productRobot.getId());

        Assert.assertEquals(found.getCategories().size(), 2);
        assertContainsCategoryWithName(found.getCategories(),"Electro");
        assertContainsCategoryWithName(found.getCategories(),"Kitchen");

        em.close();
    }

    @Test
    public void testPlate(){
        EntityManager em = emf.createEntityManager();
        Product found = em.find(Product.class, productPlate.getId());

        Assert.assertEquals(found.getCategories().size(), 1);
        assertContainsCategoryWithName(found.getCategories(), "Kitchen");

        em.close();
    }


    private void assertContainsCategoryWithName(Set<Category> categories,
			String expectedCategoryName) {
		for(Category cat: categories){
			if (cat.getName().equals(expectedCategoryName))
				return;
		}
			
		Assert.fail("Couldn't find category "+ expectedCategoryName+ " in collection "+categories);
	}
	private void assertContainsProductWithName(Set<Product> products,
			String expectedProductName) {
		
		for(Product prod: products){
			if (prod.getName().equals(expectedProductName))
				return;
		}
			
		Assert.fail("Couldn't find product "+ expectedProductName+ " in collection "+products);
	}
}
