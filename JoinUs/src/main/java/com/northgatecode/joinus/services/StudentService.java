package com.northgatecode.joinus.services;

import com.northgatecode.joinus.models.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianliang on 12/3/2016.
 */
public class StudentService {
    private static StudentService studentService = new StudentService();

    private List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
        students.add(new Student(0, "Jack", "Male", 21));
        students.add(new Student(1, "Tom", "Male", 22));
        students.add(new Student(2, "Mike", "Male", 23));
        students.add(new Student(3, "Tome", "Male", 22));
        students.add(new Student(4, "Lily", "Female", 20));
        students.add(new Student(5, "Kate", "Female", 19));
    }

    public static StudentService getInstance() {
        return studentService;
    }

    public List<Student> getAll() {
        return students;
    }

    public List<Student> getByName(String name) {
        List<Student> results = new ArrayList<>();
        for (Student stu : students) {
            if (stu.getName().toLowerCase().indexOf(name.toLowerCase()) >= 0) {
                results.add(stu);
            }
        }
        return results;
    }

    public Student get(int id) {
        if (id < students.size() && id >= 0 ) {
            return students.get(id);
        } else {
            return null;
        }
    }

    public Student add(Student student) {
        students.add(student);
        int id = students.indexOf(student);
        student.setId(id);
        return student;
    }

    public Student update(Student student) {
        students.set(student.getId(), student);
        return students.get(student.getId());
    }

    public void delete(int id) {
        students.remove(id);
    }
}
