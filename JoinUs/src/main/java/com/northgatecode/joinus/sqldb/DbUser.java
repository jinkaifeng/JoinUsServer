package com.northgatecode.joinus.sqldb;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by qianliang on 25/3/2016.
 */
@Entity
@Table(name = "user")
public class DbUser {
    @Id
    private String id;

    @Column(name = "mobile", length = 20, nullable = false, unique = true)
    private String mobile;

    @Column(name = "create_date")
    private Date createDate;

}
