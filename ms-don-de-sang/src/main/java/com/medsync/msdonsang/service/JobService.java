package com.medsync.msdonsang.service;

import com.medsync.msdonsang.RabbitMQConfig;
import com.medsync.msdonsang.model.Job;
import com.medsync.msdonsang.model.JobDTO;
import com.medsync.msdonsang.repository.JobRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public JobService(JobRepository jobRepository, RabbitTemplate rabbitTemplate) {
        this.jobRepository = jobRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Job saveAndSendJob(Job job) {
        // 1. Sauvegarder en base de données
        Job savedJob = jobRepository.save(job);
        
        // 2. Préparer le DTO à envoyer
        JobDTO jobDTO = new JobDTO();
        jobDTO.setService(savedJob.getService());
        
        // 3. Envoyer via RabbitMQ (Exchange + Routing Key)
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, jobDTO);
        
        return savedJob;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(int id) {
        return jobRepository.findById(id);
    }
}
