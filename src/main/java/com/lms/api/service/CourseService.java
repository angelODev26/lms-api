package com.lms.api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lms.api.dto.CourseDto;
import com.lms.api.model.Course;
import com.lms.api.repository.CourseRepository;
import com.lms.api.repository.UserRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(course -> new CourseDto(course.getId(), course.getTitle(), course.getDescription()))
                .toList();
    }

    public Optional<CourseDto> getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(course -> new CourseDto(course.getId(), course.getTitle(), course.getDescription()));
    }

    public CourseDto createCourse(CourseDto courseDto) {
        if (courseRepository.existsByTitle(courseDto.getTitle())) {
            throw new IllegalArgumentException("Course with title '" + courseDto.getTitle() + "' already exists.");
        }

        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());

        Course savedCourse = courseRepository.save(course);
        return new CourseDto(savedCourse.getTitle(), savedCourse.getDescription());
    }

    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        if (courseRepository.existsByTitleAndIdNot(courseDto.getTitle(),id)) {
            throw new IllegalArgumentException("Course with title '" + courseDto.getTitle() + "' already exists.");
        }

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));

        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());

        Course updatedCourse = courseRepository.save(course);
        return new CourseDto(updatedCourse.getTitle(), updatedCourse.getDescription());

    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
        courseRepository.delete(course);
    }

}
