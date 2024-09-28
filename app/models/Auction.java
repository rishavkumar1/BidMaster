package models;

import javax.persistence.Entity;

@Entity
public class Auction extends BaseIdModel{
    private String name;
    private Long vendorId;

    public Auction(String name, Long vendorId) {
        this.name = name;
        this.vendorId = vendorId;
    }

    public Auction() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
}
