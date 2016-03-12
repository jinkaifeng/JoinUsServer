package com.northgatecode.joinus.resources;

import com.northgatecode.joinus.models.Student;
import com.northgatecode.joinus.services.StudentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 10/3/2016.
 */
@Path("students")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource {

    // localhost:8080/joinus/api/students?name=mike
    @GET
    public List<Student> getAll(@QueryParam("name") String name) {
        if (name == null)
            return  StudentService.getInstance().getAll();
        else
            return StudentService.getInstance().getByName(name);
    }

    // localhost:8080/joinus/api/students/1
    @GET
    @Path("/{id}")
    public Student getById(@PathParam("id") int id) {
        return StudentService.getInstance().get(id);
    }

    @POST
    public Student createStudent(Student student) {
        return StudentService.getInstance().add(student);
    }

    @PUT
    public Student updateStudent(Student student) {
        return StudentService.getInstance().update(student);
    }

//    @PUT
//    @Path("/{id}")
//    public Student updateStudentById(Student student) {
//
//    }

    @DELETE
    @Path("/{id}")
    public void deleteStudent(@PathParam("id") int id) {
        StudentService.getInstance().delete(id);
    }
}
