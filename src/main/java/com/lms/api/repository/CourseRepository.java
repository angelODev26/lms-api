package com.lms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.api.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);
}
