package cz.fi.muni.pa165.entity;

import cz.fi.muni.pa165.enums.Color;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.Date;
import java.util.Objects;

import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "ESHOP_PRODUCTS")     //////////////////////// TODO - ???
public class Product {
    // id - autogenerated @Id using IDENTITY column
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

    // name - nonnullable, unique
    @NotNull
    @Column(nullable=false,unique=true)
    private String name;

    // color - you will have to create new ENUM for this
    @Enumerated
	private Color color;

    // java.util.Date addedDate - this field should contain only date in the database. Use @Temporal annotation
    @Temporal(DATE)
    private Date addedDate;

	public void product(String name, Color color, Date addedDate){
	    this.name = name;
	    this.color = color;
	    this.addedDate = addedDate;
    }
    public void product(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

// Business key equality means that the equals() method compares only the properties that form the business key,
// a key that would identify our instance in the real world (a natural candidate key)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Product)) return false;

        Product product = (Product) o;

        if (!this.getName().equals(product.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.name.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", addedDate=" + addedDate +
                '}';
    }
}
//QUIZ:
//        What is the main configuration file for JPA in your application?
// persistence.xml
//        Where is the following text used and what is the effect of it (use Hibernate dev guide to find answer)? "hibernate.format_sql"
// v persistence.xml, meni format vypisu logu (Pretty-print the SQL in the log and console.)
//        What is hibernate.hbm2ddl.auto property in persistence.xml file?
// Envers automatically creates audit tables if it's option is set to create, create-drop or update.