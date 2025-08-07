package com.lms.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.api.dto.CourseDto;
import com.lms.api.payload.ApiResponse;
import com.lms.api.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDto>>> getAllCourses() {
        List<CourseDto> courses = courseService.getAllCourses();
        ApiResponse<List<CourseDto>> response = new ApiResponse<>(true, "Courses retrieved successfully", courses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseById(@PathVariable Long id) {
        Optional<CourseDto> courseDto = courseService.getCourseById(id);

        if (courseDto.isPresent()) {
            ApiResponse<CourseDto> response = new ApiResponse<>(true, "Course found", courseDto.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<CourseDto> response = new ApiResponse<>(false, "Course with id " + id + " not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        }
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@Valid @RequestBody CourseDto courseDto) {
        CourseDto createdCourse = courseService.createCourse(courseDto);
        ApiResponse<CourseDto> response = new ApiResponse<>(true, "Course created successfully", createdCourse);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto) {
        CourseDto updatedCourse = courseService.updateCourse(id, courseDto);
        ApiResponse<CourseDto> response = new ApiResponse<>(true, "Course updated successfully", updatedCourse);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        ApiResponse<String> response = new ApiResponse<>(true, "Course deleted successfully", "Course with id " + id + " has been deleted.");
        return ResponseEntity.ok(response);

    }
}
