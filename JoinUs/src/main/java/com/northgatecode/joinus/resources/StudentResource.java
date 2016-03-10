package com.northgatecode.joinus.resources;

import com.northgatecode.joinus.models.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 10/3/2016.
 */
@Path("students")
public class StudentResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(0, "Jack", "Male", 21));
        students.add(new Student(1, "Tom", "Male", 22));
        students.add(new Student(2, "Mike", "Male", 23));
        students.add(new Student(3, "Jersey", "Male", 22));
        students.add(new Student(4, "Lily", "Female", 20));
        students.add(new Student(5, "Kate", "Female", 19));
        return students;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Student createStudent(Student student) {

        System.out.println("Save data...");
        return student;
    }

    @GET
    @Path("tom")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getTom() {

        Student student = new Student();

        student.setId(1);
        student.setName("Tom");
        student.setGender("Male");
        student.setAge(22);

        return student;
    }
}
