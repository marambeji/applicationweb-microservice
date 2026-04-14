package com.medsync.msuser.service;

import com.medsync.msuser.model.JobDTO;
import com.medsync.msuser.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private List<JobDTO> favoriteJobDTOS = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public void receiveJobService(JobDTO jobDTO) {
        log.info("Traitement du jobDTO received : {}", jobDTO.getService());
        addJobToFavorites(jobDTO);
        sendNotificationToUser(jobDTO);
    }

    private void addJobToFavorites(JobDTO jobDTO) {
        favoriteJobDTOS.add(jobDTO);
        log.info("JobDTO ajouté aux favoris (en mémoire) : {}", jobDTO.getService());
    }

    private void sendNotificationToUser(JobDTO jobDTO) {
        log.info("Notification envoyée à l'utilisateur: Nouveau job disponible - {}", jobDTO.getService());
    }

    public List<JobDTO> getFavoriteJobs() {
        return favoriteJobDTOS;
    }
}
