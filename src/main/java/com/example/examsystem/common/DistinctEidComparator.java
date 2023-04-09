package com.example.examsystem.common;

import com.example.examsystem.entity.StudentPaper;

import java.util.Comparator;
import java.util.Objects;

public class DistinctEidComparator implements Comparator<StudentPaper> {

    @Override
    public int compare(StudentPaper o1, StudentPaper o2) {
        return Objects.equals(o1.getEid(), o2.getEid()) ? 0 : 1;
    }
}
