package com.example.capston2.Service;

import com.example.capston2.Api.ApiException;
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

    public void addProject(Project project){
        Order order = orderRepository.findOrderById(project.getOrderId());
        if (order == null){
            throw new ApiException("Order not found, cannot add project.");
        }
        if (order.getStatus().equals("Waiting for Down Payment")){
            throw new ApiException("Down payment was not paid for this project");
        }
        if (order.getStatus().equals("Done")){
            throw new ApiException("This project was Done!");
        }
        if (order.getStatus().equals("Work In Progress")){
            project.setStatus("Waiting For Approval");
            projectRepository.save(project);
            order.setStatus("Draft Sent");
            orderRepository.save(order);
            Client client = clientRepository.findClientById(order.getClientId());
            notificationService.sendDraftNotification(client.getEmail(), client.getName());
        }
    }
    public void updateProject(Integer id, Project project){
        Project oldProject = projectRepository.findProjectById(id);
        if (oldProject == null){
            throw new ApiException("project not found");
        }
        Order order = orderRepository.findOrderById(project.getOrderId());
        if (order == null){
            throw new ApiException("Associated order not found");
        }
        if (oldProject.getStatus().equals("Accepted")){
            throw new ApiException("project was accepted you can't update it");
        }
        if (oldProject.getStatus().equals("Waiting For Approval") || oldProject.getStatus().equals("Rejected")) {
            oldProject.setName(project.getName());
            oldProject.setOrderId(project.getOrderId());
            oldProject.setAttachmentFile(project.getAttachmentFile());
            project.setStatus("Waiting For Approval");
        }
    }

    public void deleteProject(Integer id){
        Project oldProject = projectRepository.findProjectById(id);
        if (oldProject == null){
            throw new ApiException("project not found");
        }
        projectRepository.delete(oldProject);
    }
}
