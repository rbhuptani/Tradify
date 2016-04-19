package com.hello_world.ronak.tradify;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/**
 * Created by ronak_000 on 4/15/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Description",
        "ListOfItems",
        "Location",
        "Mode",
        "PostedDate",
        "ProductId",
        "ProductImage",
        "ProductName",
        "Sold",
        "UserID"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Products implements Serializable {
    @JsonProperty("Description")
    private String Description;
    @JsonProperty("ListOfItems")
    private String ListOfItems;
    @JsonProperty("Location")
    private String Location;
    @JsonProperty("Mode")
    private String Mode;
    @JsonProperty("PostedDate")
    private String PostedDate;
    @JsonProperty("ProductId")
    private String ProductId;
    @JsonProperty("ProductImage")
    private String ProductImage;
    @JsonProperty("ProductName")
    private String ProductName;
    @JsonProperty("Sold")
    private String Sold;
    @JsonProperty("UserID")
    private String UserID;
    public Products(){}
	@JsonProperty("Mode")
    public String getMode() {
        return Mode;
    }
	@JsonProperty("Mode")
    public void setMode(String Mode) {
        this.Mode = Mode;
    }
	@JsonProperty("PostedDate")
    public String getPostedDate() {
        return PostedDate;
    }
	@JsonProperty("PostedDate")
    public void setPostedDate(String PostedDate) {
        this.PostedDate = PostedDate;
    }
	@JsonProperty("ProductId")
    public String getProductId() {
        return ProductId;
    }
	@JsonProperty("ProductId")
    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }
	@JsonProperty("ProductImage")
    public String getProductImage() {return ProductImage; }
	@JsonProperty("ProductImage")
    public void setProductImage(String ProductImage) {

        this.ProductImage = ProductImage;
    }
	@JsonProperty("ProductName")
    public String getProductName() {
        return ProductName;
    }
	@JsonProperty("ProductName")
    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }
	@JsonProperty("Sold")
    public String getSold() {
        return Sold;
    }
	@JsonProperty("Sold")
    public void setSold(String Sold) {
        this.Sold = Sold;
    }
	@JsonProperty("UserID")
    public String getUserID() {
        return UserID;
    }
	@JsonProperty("UserID")
    public void setUserID(String UserID) {
        this.UserID = UserID;
    }
	@JsonProperty("Description")
    public String getDescription() {
        return Description;
    }
	@JsonProperty("Description")
    public void setDescription(String Description) {
        this.Description = Description;
    }
	@JsonProperty("ListOfItems")
    public String getListOfItems() {
        return ListOfItems;
    }
	@JsonProperty("ListOfItems")
    public void setListOfItems(String ListOfItems) {
        this.ListOfItems = ListOfItems;
    }
	@JsonProperty("Location")
    public String getLocation() {
        return Location;
    }
	@JsonProperty("Location")
    public void setLocation(String Location) {
        this.Location = Location;
    }


}
