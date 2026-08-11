package com.project.taskmanager.infrastructure.scheduler;

import com.project.taskmanager.application.service.TaskAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskPenaltyScheduler {

    private final TaskAssignmentService taskAssignmentService;

    @Scheduled(cron = "0 0 * * * *")
    public void processOverdueTaskPenalties() {
        taskAssignmentService.applyPenaltiesForOverdueTasks();
    }
}