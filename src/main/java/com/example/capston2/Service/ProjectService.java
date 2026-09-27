package com.example.capston2.Service;

import com.example.capston2.Model.Client;
import com.example.capston2.Model.Order;
import com.example.capston2.Model.Project;
import com.example.capston2.Notification.NotificationService;
import com.example.capston2.Repository.ClientRepository;
import com.example.capston2.Repository.OrderRepository;
import com.example.capston2.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final NotificationService notificationService;

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Integer addProject(Project project){
        Order order = orderRepository.findOrderById(project.getOrderId());
        if (order == null){
            return -1;
        }
        if (order.getStatus().equals("Waiting for Down Payment")){
            return 1;
        }
        if (order.getStatus().equals("Done")){
            return 2;
        }
        if (order.getStatus().equals("Work In Progress")){
            projectRepository.save(project);
            order.setStatus("Draft Sent");
            orderRepository.save(order);
            Client client = clientRepository.findClientById(order.getClientId());
            notificationService.sendDraftNotification(client.getEmail(), client.getName());
        }
        return 0;
    }
    public Integer updateProject(Integer id, Project project){
        Project oldProject = projectRepository.findProjectById(id);
        if (oldProject == null){
            return -1;
        }
        Order order = orderRepository.findOrderById(project.getOrderId());
        if (order == null){
            return -2;
        }
        if (oldProject.getStatus().equals("Waiting For Approval") || oldProject.getStatus().equals("Rejected")) {
            oldProject.setName(project.getName());
            oldProject.setOrderId(project.getOrderId());
            oldProject.setAttachmentFile(project.getAttachmentFile());
            project.setStatus("Waiting For Approval");
            return 0;
        }
        return 1;
    }

    public Boolean deleteProject(Integer id){
        Project oldProject = projectRepository.findProjectById(id);
        if (oldProject == null){
            return false;
        }
        projectRepository.delete(oldProject);
        return true;
    }
}
