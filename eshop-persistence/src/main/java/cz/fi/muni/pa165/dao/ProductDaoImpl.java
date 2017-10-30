package cz.fi.muni.pa165.dao;

import cz.fi.muni.pa165.entity.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * TODO: create  javadoc
 *
 * @author Ludmila Fialova
 */
@Repository
public class ProductDaoImpl implements ProductDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Product p) {
        em.persist(p);
    }

    @Override
    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class).getResultList();
    }

    @Override
    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    @Override
    public void remove(Product p) {     //////// misto delete ???
        em.remove(findById(p.getId()));
    }

    @Override
    public List<Product> findByName(String name) {
        return em.createQuery("select p from Product p where p.name like :name", Product.class).setParameter("name", "%"+name+"%").getResultList();
    }
}
