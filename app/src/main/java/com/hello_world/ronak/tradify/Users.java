
package com.hello_world.ronak.tradify;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Address",
    "ContactNumber",
    "Email",
    "Password",
    "UserId",
    "UserImage",
    "Username"
})
public class Users implements Serializable {

    @JsonProperty("Address")
    private String Address;
    @JsonProperty("ContactNumber")
    private String ContactNumber;
    @JsonProperty("Email")
    private String Email;
    @JsonProperty("Password")
    private String Password;
    @JsonProperty("UserId")
    private String UserId;
    @JsonProperty("UserImage")
    private String UserImage;
    @JsonProperty("Username")
    private String Username;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The Address
     */
    @JsonProperty("Address")
    public String getAddress() {
        return Address;
    }

    /**
     * 
     * @param Address
     *     The Address
     */
    @JsonProperty("Address")
    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     * 
     * @return
     *     The ContactNumber
     */
    @JsonProperty("ContactNumber")
    public String getContactNumber() {
        return ContactNumber;
    }

    /**
     * 
     * @param ContactNumber
     *     The ContactNumber
     */
    @JsonProperty("ContactNumber")
    public void setContactNumber(String ContactNumber) {
        this.ContactNumber = ContactNumber;
    }

    /**
     * 
     * @return
     *     The Email
     */
    @JsonProperty("Email")
    public String getEmail() {
        return Email;
    }

    /**
     * 
     * @param Email
     *     The Email
     */
    @JsonProperty("Email")
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     * 
     * @return
     *     The Password
     */
    @JsonProperty("Password")
    public String getPassword() {
        return Password;
    }

    /**
     * 
     * @param Password
     *     The Password
     */
    @JsonProperty("Password")
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     * 
     * @return
     *     The UserId
     */
    @JsonProperty("UserId")
    public String getUserId() {
        return UserId;
    }

    /**
     * 
     * @param UserId
     *     The UserId
     */
    @JsonProperty("UserId")
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    /**
     * 
     * @return
     *     The UserImage
     */
    @JsonProperty("UserImage")
    public String getUserImage() {
        return UserImage;
    }

    /**
     * 
     * @param UserImage
     *     The UserImage
     */
    @JsonProperty("UserImage")
    public void setUserImage(String UserImage) {
        this.UserImage = UserImage;
    }

    /**
     * 
     * @return
     *     The Username
     */
    @JsonProperty("Username")
    public String getUsername() {
        return Username;
    }

    /**
     * 
     * @param Username
     *     The Username
     */
    @JsonProperty("Username")
    public void setUsername(String Username) {
        this.Username = Username;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
