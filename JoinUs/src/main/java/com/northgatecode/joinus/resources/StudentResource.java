package com.northgatecode.joinus.resources;

import com.northgatecode.joinus.models.Student;
import com.northgatecode.joinus.models.StudentList;
import com.northgatecode.joinus.services.StudentService;
import org.omg.CORBA.portable.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by qianliang on 10/3/2016.
 */
@Path("students")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {

    @GET
    @Path("dbtest")
    public Response dbTest() {
        Student student = new Student(0, "Tom", "Male", 18);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.northgatecode.joinus.jpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(student);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }

        return Response.ok(student).build();
    }

    // localhost:8080/joinus/api/students?name=mike
    @GET
    public Response getAll(@QueryParam("name") String name) throws Exception {
        List<Student> students;

        if (name == null)
            students = StudentService.getInstance().getAll();
        else
            students = StudentService.getInstance().getByName(name);

        if (students.size() == 0) {
            throw new Exception("我是异常");
        }

        return Response.ok(new StudentList(students)).build();
    }

    // localhost:8080/joinus/api/students/1
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        Student student = StudentService.getInstance().get(id);

        if (student == null) {
            throw new NotFoundException("找不到您需要的学生ID");
        }
        return Response.ok(student).build();
    }

    @POST
    public Response createStudent(Student student) {
        StudentService.getInstance().add(student);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response updateStudent(Student student) {
        StudentService.getInstance().update(student);
        return Response.ok(student).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteStudent(@PathParam("id") int id) {
        StudentService.getInstance().delete(id);
        return Response.ok().build();
    }
}
