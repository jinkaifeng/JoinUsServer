package com.northgatecode.joinus.auth;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by qianliang on 19/3/2016.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticated {}
