package com.internship.insurance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private double price;
    @JsonIgnore
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date time_created;

    @JsonIgnore
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date time_updated;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "insurance_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private InsuranceOffer insurance;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.REFRESH,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "order_properties",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "property_id", referencedColumnName = "id")}
    )
    private Set<Property> properties;

    private String docNumber;
    private String licensePlateNumber;

    private String idnp;
    private String firstName;
    private String lastName;
    private RightOfPossesion rightOfPossesion;

    public Order() {
        this.properties = new HashSet<>();
    }

    public Order(OrderStatus status, double price, Date time_created, Date time_updated, Set<Property> properties) {
        this.status = status;
        this.price = price;
        this.time_created = time_created;
        this.time_updated = time_updated;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getTime_created() {
        return time_created;
    }

    public void setTime_created(Date time_created) {
        this.time_created = time_created;
    }

    public Date getTime_updated() {
        return time_updated;
    }

    public void setTime_updated(Date time_updated) {
        this.time_updated = time_updated;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getIdnp() {
        return idnp;
    }

    public void setIdnp(String idnp) {
        this.idnp = idnp;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public RightOfPossesion getRightOfPossesion() {
        return rightOfPossesion;
    }

    public void setRightOfPossesion(RightOfPossesion rightOfPossesion) {
        this.rightOfPossesion = rightOfPossesion;
    }
}
