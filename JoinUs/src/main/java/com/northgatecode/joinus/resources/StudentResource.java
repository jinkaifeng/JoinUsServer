package com.northgatecode.joinus.resources;

import com.northgatecode.joinus.models.Student;
import com.northgatecode.joinus.models.StudentList;
import com.northgatecode.joinus.services.StudentService;
import com.northgatecode.joinus.utils.JpaHelper;
import org.omg.CORBA.portable.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
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

        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(new Student("Jack", "Male", 21));
            entityManager.persist(new Student("Tom", "Male", 22));
            entityManager.persist(new Student("Mike", "Male", 23));
            entityManager.persist(new Student("Tome", "Male", 22));
            entityManager.persist(new Student("Lily", "Female", 20));
            entityManager.persist(new Student("Kate", "Female", 19));
            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }

        return Response.ok().build();
    }

    // localhost:8080/joinus/api/students?name=mike
    @GET
    public Response getAll(@QueryParam("name") String name) throws Exception {
        List<Student> students;

        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            TypedQuery<Student> query = entityManager.createQuery("select s from Student as s " +
                    "where name like '%" + name + "%'", Student.class);
            students = query.getResultList();

        } finally {
            entityManager.close();
        }

        return Response.ok(new StudentList(students)).build();
    }

    // localhost:8080/joinus/api/students/1
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        Student student;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            student = entityManager.find(Student.class, id);
        } finally {
            entityManager.close();
        }


        if (student == null) {
            throw new NotFoundException("找不到您需要的学生ID");
        }
        return Response.ok(student).build();
    }

    @POST
    public Response createStudent(Student student) {
        Student studentToDb;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            studentToDb = new Student(student.getName(), student.getGender(), student.getAge());
            entityManager.getTransaction().begin();
            entityManager.persist(studentToDb);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }

        return Response.ok(studentToDb).build();
    }

    @PUT
    public Response updateStudent(Student student) {
        Student studentFromDb;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            studentFromDb = entityManager.find(Student.class, student.getId());
            if (studentFromDb == null) {
                throw new NotFoundException("Student does't exist.");
            }

            entityManager.getTransaction().begin();

            studentFromDb.setName(student.getName());
            studentFromDb.setGender(student.getGender());
            studentFromDb.setAge(student.getAge());

            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }
        return Response.ok(studentFromDb).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteStudent(@PathParam("id") int id) {
        Student student;
        EntityManager entityManager = JpaHelper.getFactory().createEntityManager();
        try {
            student = entityManager.find(Student.class, id);
            if (student == null) {
                throw new NotFoundException("Student does't exist.");
            }

            entityManager.getTransaction().begin();
            entityManager.remove(student);
            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        } finally {
            entityManager.close();
        }
        return Response.ok().build();
    }
}
