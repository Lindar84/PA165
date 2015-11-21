package cz.fi.muni.pa165.rest.controllers;

import cz.fi.muni.pa165.dto.NewPriceDTO;
import cz.fi.muni.pa165.dto.ProductCreateDTO;
import cz.fi.muni.pa165.dto.ProductDTO;
import cz.fi.muni.pa165.rest.ApiUris;
import cz.fi.muni.pa165.rest.exceptions.InvalidParameterException;
import cz.fi.muni.pa165.rest.exceptions.ResourceAlreadyExistingException;
import cz.fi.muni.pa165.rest.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cz.fi.muni.pa165.dto.CategoryDTO;
import cz.fi.muni.pa165.exceptions.EshopServiceException;
import cz.fi.muni.pa165.facade.ProductFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * REST Controller for Products
 *
 * Annotate the controller with @RestController and add a
 * RequestMapping using ApiUris.ROOT_URI_PRODUCTS
 */
@RestController
@RequestMapping(value = ApiUris.ROOT_URI_PRODUCTS)
public class ProductsController {

    final static Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Inject
    private ProductFacade productFacade;

    /**
     * Get list of Products curl -i -X GET
     * http://localhost:8080/eshop-rest/products
     *
     * 1. annotate the method to have as method RequestMethod.GET
     * and producing application/json
     * 2. return a list of ProductDTOs
     *
     * @return ProductDTO
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)    // ad 1
    public final List<ProductDTO> getProducts() {
        logger.debug("rest getProducts()");
        return productFacade.getAllProducts();
    }

    /**
     * Get Product by identifier id curl -i -X GET
     * http://localhost:8080/eshop-rest/products/1
     *
     * 1. add the mapping using the resource id
     * 2. retrieve the id from the request by using pathvariable
     * 3. you can return HttpStatus.NOT_FOUND 404 if the product is not found
     * (note: at the current time there is an issue at the persistence layer
     * https://github.com/fi-muni/PA165/issues/28
     * so using the facade to get one product with a non-existing id will throw
     * a Dozer exception - you can catch the exception and re-throw it )
     * You can use the class ResourceNotFoundException
     *
     * @param id identifier for a product
     * @return ProductDTO
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)    // ad 1
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason="the product was not found")  // ad 3
    public final ProductDTO getProduct(@PathVariable("id") long id) throws Exception {    // ad 2
        logger.debug("rest getProduct({})", id);

        try {
            ProductDTO productDTO = productFacade.getProductWithId(id);
            return productDTO;
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
//        if (product != null) {
//            return product;
//        } else {
//            throw new ResourceNotFoundException();
//        }
    }

    /**
     * Delete one product by id curl -i -X DELETE
     * http://localhost:8080/eshop-rest/products/1
     *
     * 1. delete one product identified by id
     * 2. what about deleting the same element multiple times?
     *
     * @param id identifier for product
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void deleteProduct(@PathVariable("id") long id) throws Exception {
        logger.debug("rest deleteProduct({})", id);
        try{
            productFacade.deleteProduct(id);
        }catch (Exception e){
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Create a new product by POST method curl -X POST -i -H "Content-Type: application/json" --data
     * '{"name":"test","description":"test","color":"UNDEFINED","price":"200", "currency":"CZK", "categoryId":"1"}'
     * http://localhost:8080/eshop-rest/products/create
     *
     * 1. use the requestbody annotation to get access to the request body
     * 2. use the ProductCreateDTO for the creation of a new product, but return a ProductDTO
     *
     * @param product ProductCreateDTO with required fields for creation
     * @return the created product ProductDTO
     * @throws ResourceAlreadyExistingException
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final ProductDTO createProduct(@RequestBody ProductCreateDTO product) throws Exception {     // TODO study
        logger.debug("rest createProduct()");
        try{
            Long id = productFacade.createProduct(product);     // vraci ID !!!
            return productFacade.getProductWithId(id);
        }catch (Exception e){
            throw new ResourceAlreadyExistingException();
        }
    }

    /**
     * Update the price for one product by PUT method curl -X PUT -i -H
     * "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}'
     * http://localhost:8080/eshop-rest/products/4
     *
     * Update the price for one product (you need to use  NewPriceDTO)
     * take care also about the price validation at the service layer
     *
     * @param id identified of the product to be updated
     * @param newPrice required fields as specified in NewPriceDTO
     * @return the updated product ProductDTO
     * @throws InvalidParameterException
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final ProductDTO changePrice(@PathVariable("id") Long id, @RequestBody NewPriceDTO newPrice) throws Exception {
        logger.debug("rest changePrice({})", id);
        try {
            newPrice.setProductId(id);              // TODO study
            productFacade.changePrice(newPrice);
            return productFacade.getProductWithId(id);
        }catch (Exception e){
            throw new InvalidParameterException();
        }
    }

    /**
     * Add a new category by POST Method
     *
     * @param id the identifier of the Product to have the Category added
     * @param category the category to be added
     * @return the updated product as defined by ProductDTO
     * @throws InvalidParameterException
     */
    @RequestMapping(value = "/{id}/categories", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final ProductDTO addCategory(@PathVariable("id") long id, @RequestBody CategoryDTO category) throws Exception {

        logger.debug("rest addCategory({})", id);

        try {
            productFacade.addCategory(id, category.getId());
            return productFacade.getProductWithId(id);
        } catch (Exception ex) {
            throw new InvalidParameterException();
        }
    }
}
