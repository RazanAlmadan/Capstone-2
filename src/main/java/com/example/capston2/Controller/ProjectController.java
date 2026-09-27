package com.example.capston2.Controller;

import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.Project;
import com.example.capston2.Service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.status(200).body(projectService.getAllProjects());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody @Valid Project project, Errors errors) {
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Integer results = projectService.addProject(project);
        if (results == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("Order not found, cannot add project."));
        }
        if (results == 1){
            return ResponseEntity.status(400).body(new ApiResponse("Down payment was not paid for this project"));
        }
        if (results == 2){
            return ResponseEntity.status(400).body(new ApiResponse("This project was Done!"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Project was sent successfully."));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Integer id, @RequestBody @Valid Project project, Errors errors) {
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        Integer result = projectService.updateProject(id, project);

        if (result == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("Project not found."));
        }
        if (result == -2) {
            return ResponseEntity.status(400).body(new ApiResponse("Associated order not found."));
        }
        if (result == 1){
            return ResponseEntity.status(400).body(new ApiResponse("project was Accepted you can't update it"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Project updated successfully."));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer id) {
        Boolean results = projectService.deleteProject(id);
        if (!results) {
            return ResponseEntity.status(400).body(new ApiResponse("Project not found."));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Project deleted successfully."));
    }
}