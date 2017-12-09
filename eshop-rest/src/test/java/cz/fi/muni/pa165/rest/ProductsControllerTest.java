package cz.fi.muni.pa165.rest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.fi.muni.pa165.rest.exceptions.ResourceNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import cz.fi.muni.pa165.RootWebContext;
import cz.fi.muni.pa165.dto.CategoryDTO;
import cz.fi.muni.pa165.dto.Color;
import cz.fi.muni.pa165.dto.NewPriceDTO;
import cz.fi.muni.pa165.dto.PriceDTO;
import cz.fi.muni.pa165.dto.ProductCreateDTO;
import cz.fi.muni.pa165.dto.ProductDTO;
import cz.fi.muni.pa165.enums.Currency;
import cz.fi.muni.pa165.facade.ProductFacade;
import cz.fi.muni.pa165.rest.controllers.GlobalExceptionController;
import cz.fi.muni.pa165.rest.controllers.ProductsController;
import java.lang.reflect.Method;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

@WebAppConfiguration
@ContextConfiguration(classes = {RootWebContext.class})
public class ProductsControllerTest extends AbstractTestNGSpringContextTests {

    @Mock
    private ProductFacade productFacade;

    @Autowired
    @InjectMocks
    private ProductsController productsController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(productsController).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }
    
     /**
      * We need to register the GlobalExceptionController if @ControllerAdvice is used
     * this can be used in SetHandlerExceptionResolvers() standaloneSetup() configured above
     * 
     * Note that new Spring version from 4.2 has already a setControllerAdvice() method on 
     * MockMVC builders, so in that case it is only needed to pass one or more
     * @ControllerAdvice(s) to have them available in tests
     * 
     */
    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(GlobalExceptionController.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new GlobalExceptionController(), method);
            }
        };
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

    /**
     * This is not a real test, but it is here to show the usage of mockMvc to perform a GET request
     * and also how both request and response can be printed out
     *
     * you can run this test and see the information that is outputted
     */
    @Test
    public void debugTest() throws Exception {
        doReturn(Collections.unmodifiableList(this.createProducts())).when(
                productFacade).getAllProducts();
        mockMvc.perform(get("/products")).andDo(print());
    }

    /**
     * in this test we want to ensure the following:
     * 1. the status of the response is OK 200
     * 2. the content type is MediaType.APPLICATION_JSON_VALUE
     * 3. we check that the name for two products with ids 10 and 20 are Raspberry PI and Arduino
     * (see preloaded data in createProducts()
     *
     * The first point is already implemented,
     * we can chain further andExpect() for 2nd and 3rd point
     *
     * To do this, you can use
     * org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get()
     * org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
     * org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath()
     * org.springframework.test.web.servlet.result.MockMvcResultMatchers.status()
     *
     * You can read about Jsonpath notation from the documentation
     * https://github.com/jayway/JsonPath to define the expression in jsonPath() method
     * so that you will have similar statement as
     * JsonPath("<expression>").value("Raspberry PI")
     *
     * From the previous debug method, what we are expected to parse is
     * [{"id":10,"image":null,"imageMimeType":null,"name":"Raspberry PI",
     *
     */
    @Test
    public void getAllProducts() throws Exception {

        doReturn(Collections.unmodifiableList(this.createProducts())).when(
                productFacade).getAllProducts();

        mockMvc.perform(get(ApiUris.ROOT_URI_PRODUCTS))     //////// (get("/products")) ???
                .andExpect(status().isOk())      // ad 1
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))     // ad 2
                .andExpect(jsonPath("$.[?(@.id==10)].name").value("Raspberry PI"))  // ad 3
                .andExpect(jsonPath("$.[?(@.id==20)].name").value("Arduino"));
    }

    /**
     * In this method we are testing the creation of one product use mockMvc to:
     * 1. perform a POST
     * 2. Set the content type to APPLICATION_JSON
     * 3. convert the ProductCreateDTO instance to JSON with the helper method
     * convertObjectToJsonBytes() and pass it with content() in mockMvc perform()
     * 4. test also that the status is 200 OK
     */
    @Test
    public void getValidProduct() throws Exception {

        List<ProductDTO> products = this.createProducts();

        doReturn(products.get(0)).when(productFacade).getProductWithId(10l);
        doReturn(products.get(1)).when(productFacade).getProductWithId(20l);

        mockMvc.perform(get("/products/10"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value("Raspberry PI"));
        mockMvc.perform(get("/products/20"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value("Arduino"));
    }

    @Test
    public void getInvalidProduct() throws Exception {
        doThrow(new RuntimeException()).when(productFacade).getProductWithId(1l);

        mockMvc.perform(get("/products/1")).andExpect(
                status().is4xxClientError());
    }

    @Test
    public void deleteProduct() throws Exception {

        List<ProductDTO> products = this.createProducts();

        mockMvc.perform(delete("/products/10"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProductNonExisting() throws Exception {

        List<ProductDTO> products = this.createProducts();

        doThrow(new RuntimeException("the product does not exist")).when(productFacade).deleteProduct(20l);

        mockMvc.perform(delete("/products/20"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createProduct() throws Exception {

        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setName("Raspberry PI");

        doReturn(1l).when(productFacade).createProduct(any(ProductCreateDTO.class));

        String json = this.convertObjectToJsonBytes(productCreateDTO);

        mockMvc.perform(post("/products/create")     // ad 1
                .contentType(MediaType.APPLICATION_JSON)     // ad 2
                .content(json))    // ad 3          // pozor na zavorky !!!
                .andDo(print())
                .andExpect(status().isOk());    // ad 4
    }

    /**
     * Test the POST to products for the addition of a category the mapping is at
     * "/products/{prod_id}/categories"
     * 1 - Use again the json content,
     * 2 - set the content type,
     * 3 - expect to get OK 200
     *
     * @throws Exception
     */
    @Test
    public void updateProduct() throws Exception {
        List<ProductDTO> products = this.createProducts();

        doReturn(products.get(0)).when(productFacade).getProductWithId(10l);
        doReturn(products.get(1)).when(productFacade).getProductWithId(20l);

        doNothing().when(productFacade).changePrice(any(NewPriceDTO.class));
        NewPriceDTO newPrice = new NewPriceDTO();

        String json = this.convertObjectToJsonBytes(newPrice);

        mockMvc.perform(
                put("/products/10").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addCategory() throws Exception {
        List<ProductDTO> products = this.createProducts();

        doReturn(products.get(0)).when(productFacade).getProductWithId(10l);
        doReturn(products.get(1)).when(productFacade).getProductWithId(20l);

        CategoryDTO category = new CategoryDTO();
        category.setId(1l);

        String json = this.convertObjectToJsonBytes(category);

        mockMvc.perform(post("/products/20/categories")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * Let's test for a non-existing product
     * 
     * 1. use mockito to throw an exception when a specific productId is not available
     * (Note: we would return here null but see comment in the RestController implementation)
     * 2. test that the return code is 404 or in 4xx HTTP range
     */
    @Test
    public void getNonExistingProduct() throws Exception {
        doThrow(new RuntimeException()).when(productFacade).getProductWithId(1l);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().is4xxClientError());

        //List<ProductDTO> products = this.createProducts();

        doThrow(new ResourceNotFoundException()).when(productFacade).getProductWithId(30L);     // ad 1

        mockMvc.perform(get("/products/30"))
                .andExpect(status().isNotFound());  // ad 2
    }

    private List<ProductDTO> createProducts() {
        ProductDTO productOne = new ProductDTO();
        productOne.setId(10L);
        productOne.setName("Raspberry PI");
        PriceDTO currentPrice = new PriceDTO();
        currentPrice.setCurrency(Currency.EUR);
        currentPrice.setValue(new BigDecimal("34"));
        productOne.setCurrentPrice(currentPrice);
        productOne.setColor(Color.BLACK);

        ProductDTO productTwo = new ProductDTO();
        productTwo.setId(20L);
        productTwo.setName("Arduino");
        PriceDTO price = new PriceDTO();
        price.setCurrency(Currency.EUR);
        price.setValue(new BigDecimal("44"));
        productTwo.setCurrentPrice(price);
        productTwo.setColor(Color.WHITE);

        return Arrays.asList(productOne, productTwo);
    }

    private static String convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }
}
