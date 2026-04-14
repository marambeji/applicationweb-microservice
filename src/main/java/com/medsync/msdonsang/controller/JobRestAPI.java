package com.medsync.msdonsang.controller;

import com.medsync.msdonsang.model.Job;
import com.medsync.msdonsang.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobRestAPI {

    private final JobService jobService;
    private String hello="Hello, i'm the Job (Don de sang) MS";

    @Autowired
    public JobRestAPI(JobService jobService) {
        this.jobService = jobService;
    }

    @RequestMapping("/helloJ")
    public String sayHello(){
        return hello;
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @PostMapping("/send")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job savedJob = jobService.saveAndSendJob(job);
        return ResponseEntity.ok(savedJob);
    }

    @RequestMapping("/{id}")
    public Optional<Job> getJobById(@PathVariable int id) {
        return jobService.getJobById(id);
    }
}
