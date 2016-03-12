package com.northgatecode.joinus.resources;

import com.northgatecode.joinus.models.Student;
import com.northgatecode.joinus.services.StudentService;
import org.omg.CORBA.portable.ApplicationException;

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
    public Response getById(@PathParam("id") int id) {
        Student student = StudentService.getInstance().get(id);

        if (student == null) {
            throw new NotFoundException("找不到您需要的学生ID");
        }
        return Response.ok(student).build();
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
