package cz.fi.muni.pa165.rest.assemblers;

import cz.fi.muni.pa165.dto.ProductDTO;
import cz.fi.muni.pa165.rest.controllers.ProductsController;
import cz.fi.muni.pa165.rest.controllers.ProductsControllerHateoas;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;    // To create links among resources, to take care of mapping our DTOs
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * This class shows a resource assembler for a HATEOAS REST Service we are mapping the DTO to a resource
 * that can provide links to the different parts of the API
 * See http://docs.spring.io/spring-hateoas/docs/current/reference/html/
 *
 * @author brossi
 */
@Component
public class ProductResourceAssembler implements ResourceAssembler<ProductDTO, Resource<ProductDTO>> {

    @Override
    public Resource<ProductDTO> toResource(ProductDTO productDTO){  // map one ProductDTO to a Resource

        long id = productDTO.getId();
//        wrapping the original DTO in a resource that we will populate then with links to resources:
        Resource<ProductDTO> productResource = new Resource<ProductDTO>(productDTO);
//        will add to the resource something similar to
//          "links":[{"rel":"self","href":"http://localhost:8080/eshop-rest/products_hateoas/1"}]
        Link link = linkTo(ProductsControllerHateoas.class).slash(productDTO.getId()).withSelfRel();
        Link linkDel = linkTo(ProductsControllerHateoas.class)
                .slash(productDTO.getId()).slash(productDTO.getId()).withRel("DELETE");
        productResource.add(link);
        productResource.add(linkDel);

        return productResource;
    }
}