package com.example.capston2.Notification;

import com.example.capston2.Model.Designer;
import com.example.capston2.Model.Order;
import com.example.capston2.Model.Project;
import com.example.capston2.Repository.DesignerRepository;
import com.example.capston2.Repository.OrderRepository;
import com.example.capston2.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeadlineScheduler {

    private final NotificationService notificationService;
    private final OrderRepository orderRepository;
    private final DesignerRepository designerRepository;
    private final ProjectRepository projectRepository;

    @Scheduled(cron = "0 0 9 * * *") // runs every day at 9 AM
    public void checkDeadlines() {
        LocalDate today = LocalDate.now();

        List<Order> orders = orderRepository.findAll();

        for (int i = 0; i<orders.size(); i++){
            if (orders.get(i).getDeadLine().equals(today)){

                Project project = projectRepository.findProjectByOrderId(orders.get(i).getId());
                if (project != null){
                    continue;
                }
                // check if designer is still working
                if (orders.get(i).getStatus().equals("Work In Progress")) {

                    Designer designer = designerRepository.findDesignerById(orders.get(i).getDesignerId());

                    String orderInfo = "Order #" + orders.get(i).getId() + " deadline is today.";

                    notificationService.sendDeadlineReminder(designer.getEmail(), orderInfo);
                }
            }
        }
    }
}
