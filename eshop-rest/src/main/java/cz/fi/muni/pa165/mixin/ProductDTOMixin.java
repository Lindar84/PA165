package cz.fi.muni.pa165.mixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 *
 * @author Ludmila Fialova
 */
@JsonIgnoreProperties({"imageMimeType"})    // It filter out "imageMimeType" from the products.
public class ProductDTOMixin {


}
