package com.safalifter.jobservice.controller;

import com.safalifter.jobservice.dto.JobDto;
import com.safalifter.jobservice.request.job.JobCreateRequest;
import com.safalifter.jobservice.request.job.JobUpdateRequest;
import com.safalifter.jobservice.service.JobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/job-service/job")
@RequiredArgsConstructor
@Tag(name = "Job Controller", description = "Job management endpoints")
public class JobController {
    private final JobService jobService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create job")
    ResponseEntity<JobDto> createJob(@Valid @RequestPart JobCreateRequest request,
                                     @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(jobService.createJob(request, file), JobDto.class));
    }

    @PostMapping("/getJobsThatFitYourNeeds/{needs}")
    @Operation(summary = "Suggest jobs")
    ResponseEntity<List<JobDto>> getJobsThatFitYourNeeds(@PathVariable String needs) {
        return ResponseEntity.ok(jobService.getJobsThatFitYourNeeds(needs).stream()
                .map(job -> modelMapper.map(job, JobDto.class)).toList());
    }

    @GetMapping("/getAll")
    @Operation(summary = "List jobs")
    ResponseEntity<List<JobDto>> getAll() {
        return ResponseEntity.ok(jobService.getAll().stream()
                .map(job -> modelMapper.map(job, JobDto.class)).toList());
    }

    @GetMapping("/getJobById/{id}")
    @Operation(summary = "Get job by id")
    ResponseEntity<JobDto> getJobById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(jobService.getJobById(id), JobDto.class));
    }

    @GetMapping("/getJobsByCategoryId/{id}")
    @Operation(summary = "Get jobs by category id")
    ResponseEntity<List<JobDto>> getJobsByCategoryId(@PathVariable String id) {
        return ResponseEntity.ok(jobService.getJobsByCategoryId(id).stream()
                .map(job -> modelMapper.map(job, JobDto.class)).toList());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update job")
    ResponseEntity<JobDto> updateJob(@Valid @RequestPart JobUpdateRequest request,
                                     @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.ok(modelMapper.map(jobService.updateJob(request, file), JobDto.class));
    }

    @DeleteMapping("/deleteJobById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete job")
    ResponseEntity<Void> deleteJobById(@PathVariable String id) {
        jobService.deleteJobById(id);
        return ResponseEntity.ok().build();
    }
}
