package com.singular.barrister.Util;

import java.util.*;

public class ArrayListSorting {

    public static void main(String args[]) {
        ArrayList<Student> arraylist = new ArrayList<Student>();
        arraylist.add(new Student(223, "Chaitanya", 26));
        arraylist.add(new Student(245, "Rahul", 24));
        arraylist.add(new Student(209, "Ajeet", 32));

        Collections.sort(arraylist, Student.StuNameComparator);

        for (Student str : arraylist) {
            System.out.println(str.getName());
        }
    }

    public static class Student {
        int id;

        public Student(int id, String name, int sti_id) {
            this.id = id;
            this.name = name;
            this.sti_id = sti_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSti_id() {
            return sti_id;
        }

        public void setSti_id(int sti_id) {
            this.sti_id = sti_id;
        }

        public static Comparator<Student> StuNameComparator = new Comparator<Student>() {

            public int compare(Student s1, Student s2) {
                String StudentName1 = s1.getName().toUpperCase();
                String StudentName2 = s2.getName().toUpperCase();

                return StudentName1.compareTo(StudentName2);

            }
        };

        String name;
        int sti_id;
    }
}