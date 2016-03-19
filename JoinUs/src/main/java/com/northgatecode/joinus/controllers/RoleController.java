package com.northgatecode.joinus.controllers;

import com.northgatecode.joinus.dao.Role;
import com.northgatecode.joinus.dto.RoleInfo;
import com.northgatecode.joinus.dto.RolePagedList;
import com.northgatecode.joinus.utils.JpaHelper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by qianliang on 2/3/2016.
 */

@Path("role")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleController {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @GET
    public Response searchRole(@QueryParam("name") String name) {
        List<Role> roles;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            TypedQuery<Role> query = entityManager.createQuery("select r from Role as r where r.name like '" + name + "'", Role.class);
            roles = query.getResultList();
        }
        finally {
            entityManager.close();
        }

        List<RoleInfo> roleInfos = new ArrayList<>();
        for (Role role : roles) {
            roleInfos.add(new RoleInfo(role));
        }

        RolePagedList rolePagedList = new RolePagedList();
        rolePagedList.setRoleInfos(roleInfos);
        rolePagedList.setTotalRecords(roleInfos.size());

        return Response.ok(rolePagedList).build();
    }

    @GET
    @Path("/{roleId}")
    public Response getRole(@PathParam("roleId") int roleId) {
        Role role = JpaHelper.find(Role.class, roleId);
        if (role != null) {
            RoleInfo roleInfo = new RoleInfo(role);
            return Response.ok(roleInfo).build();
        } else {
            throw new NotFoundException("Role doesn't exist.");
        }
    }

    @POST
    public Response createRole(RoleInfo roleInfo) {
        Role role = new Role();
        role.setId(roleInfo.getId());
        role.setName(roleInfo.getName());
        JpaHelper.persist(role);
        return Response.ok(new RoleInfo(role)).build();
    }

    @PUT
    public Response updateRole(RoleInfo roleInfo) {
        Role role;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            role = entityManager.find(Role.class, roleInfo.getId());

            if (role != null) {
                entityManager.getTransaction().begin();
                role.setName(roleInfo.getName());
                entityManager.flush();
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }

        if (role != null) {
            return Response.ok(new RoleInfo(role)).build();
        } else {
            throw new NotFoundException();
        }

    }

    @DELETE
    @Path("/{roleId}")
    public Response deleteRole(@PathParam("roleId") int roleId) {
        Role role = JpaHelper.remove(Role.class, roleId);
        if (role == null) {
            throw new NotFoundException();
        } else {
            return Response.ok().build();
        }
    }
}
