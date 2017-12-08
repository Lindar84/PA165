package cz.fi.muni.pa165.rest.controllers;

import cz.fi.muni.pa165.dto.ProductDTO;
import cz.fi.muni.pa165.dto.UserDTO;
import cz.fi.muni.pa165.facade.ProductFacade;
import cz.fi.muni.pa165.rest.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.hateoas.Resource;

import java.util.ArrayList;
import java.util.List;
import cz.fi.muni.pa165.rest.assemblers.ProductResourceAssembler;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static org.springframework.hateoas.jaxrs.JaxRsLinkBuilder.linkTo;

/**
 * REST HATEOAS Controller for Products
 * This class shows Spring support for full HATEOAS REST services
 * it uses the {@link cz.fi.muni.pa165.rest.assemblers.ProductResourceAssembler} to create resources
 * to be returned as ResponseEntities
 *
 * to mimic what done in the old ProductsController class
 */
@RestController
@RequestMapping("/products_hateoas")
@ExposesResourceFor(UserDTO.class)
public class ProductsControllerHateoas {

    final static Logger logger = LoggerFactory.getLogger(ProductsControllerHateoas.class);

    @Inject
    private ProductFacade productFacade;
    @Inject
    private ProductResourceAssembler productResourceAssembler;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final HttpEntity<Resources<Resource<ProductDTO>>> getProducts() {    // to return anymore DTOs
                                                        // but the new resources with the mapped links.
        logger.debug("hateoas getProducts()");

        List<ProductDTO> productsDTO = productFacade.getAllProducts();
        List<Resource<ProductDTO>> productResourceList = new ArrayList<>();
        for (ProductDTO p : productsDTO){
                productResourceList.add(productResourceAssembler.toResource(p));
        }

        Resources<Resource<ProductDTO>> productResources = new Resources<Resource<ProductDTO>>(productResourceList);
        productResources.add(linkTo(ProductsControllerHateoas.class).withSelfRel());

        return new ResponseEntity<Resources<Resource<ProductDTO>>>(productResources, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final HttpEntity<Resource<ProductDTO>> getProduct(@PathVariable("id") long id) throws Exception {
        logger.debug("hateoas getProduct({})", id);
        try {
            ProductDTO productDTO = productFacade.getProductWithId(id);
            Resource<ProductDTO> productResource = productResourceAssembler.toResource(productDTO);
            return new ResponseEntity<Resource<ProductDTO>>(productResource, HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void deleteProduct(@PathVariable("id") long id) throws Exception {
        logger.debug("hateoas deleteProduct({})", id);
        try {
            productFacade.deleteProduct(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }
}
