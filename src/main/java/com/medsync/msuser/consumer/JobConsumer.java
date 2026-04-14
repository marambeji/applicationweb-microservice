package com.medsync.msuser.consumer;

import com.medsync.msuser.RabbitMQConfig;
import com.medsync.msuser.model.JobDTO;
import com.medsync.msuser.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class JobConsumer {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(JobConsumer.class);

    public JobConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = RabbitMQConfig.CANDID_JOB_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void receiveJob(JobDTO jobDTO) {
        log.info("JobDTO reçu depuis RabbitMQ : {}", jobDTO.getService());
        // Déléguer la logique métier
        userService.receiveJobService(jobDTO);
    }
}
