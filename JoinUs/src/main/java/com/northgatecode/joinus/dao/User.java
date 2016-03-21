package com.northgatecode.joinus.dao;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 20/2/2016.
 */
@Entity
@Table(name = "user", indexes = {@Index(columnList = "mobile", unique = true)})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @Column(name = "mobile", length = 20, nullable = false, unique = true)
    private String mobile;

    @Column(name = "email", length = 20)
    private String email;

    @Column(name = "password", length = 32)
    private String password;

    @Column(name = "salt", length = 4)
    private String salt;

    @Column(name = "token", length = 64)
    private String token;

    @Column(name = "tokenExpDate")
    private Date tokenExpDate;

    @Column(name = "name", length =20)
    private String name;

    @Column(name = "photo", length =50)
    private String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    @Column(name = "create_date")
    private Date createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenExpDate() {
        return tokenExpDate;
    }

    public void setTokenExpDate(Date tokenExpDate) {
        this.tokenExpDate = tokenExpDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
