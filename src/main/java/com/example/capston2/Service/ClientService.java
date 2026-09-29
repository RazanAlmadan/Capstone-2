package com.example.capston2.Service;

import com.example.capston2.Api.ApiException;
import com.example.capston2.Api.ApiResponse;
import com.example.capston2.Model.*;
import com.example.capston2.Notification.NotificationService;
import com.example.capston2.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final DesignerRepository designerRepository;
    private final ProposalRepository proposalRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final OrderRepository orderRepository;
    private final BillRepository billRepository;
    private final MessageRepository messageRepository;
    private final ProjectRepository projectRepository;
    private final RequestRepository requestRepository;
    private final NotificationService notificationService;

    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    public void addClient(Client client){
        clientRepository.save(client);
    }

    public void updateClient(Integer id, Client client){
        Client oldClient = clientRepository.findClientById(id);
        if (oldClient == null){
            throw new ApiException("client not found");
        }
        oldClient.setName(client.getName());
        oldClient.setEmail(client.getEmail());
        oldClient.setPassword(client.getPassword());
        clientRepository.save(oldClient);
    }

    public void deleteClient(Integer id){
        Client oldClient = clientRepository.findClientById(id);
        if (oldClient == null){
            throw new ApiException("client not found");
        }
        clientRepository.delete(oldClient);
    }

    public Designer searchByName(String name){
        Designer designer = designerRepository.findDesignerByName(name);
        if (designer == null){
            throw new ApiException("designer not found");
        }
        return designer;
    }

    public List<Designer> searchByCategory(String visualField){
        List<Designer> designers = designerRepository.findDesignerByVisualField(visualField);
        if (designers.isEmpty()){
            throw new ApiException("no designer for this category");
        }
        return designers;
    }

    public Object acceptOrRejectProposal(Integer clientId, Integer proposalId, String status){
        Proposal proposal = proposalRepository.findProposalById(proposalId);
        if (proposal == null){
            throw new ApiException("proposal not found");
        }
        Client client = clientRepository.findClientById(clientId);
        if (client == null){
            throw new ApiException("client not found");
        }
        if (!status.equals("Accepted") && !status.equals("Rejected")){
            throw new ApiException("status can either be Accepted or Rejected");
        }
        if (proposal.getStatus().equals("Accepted")){
            throw new ApiException("proposal was already accepted");
        }
        if (proposal.getStatus().equals("Rejected")){
            throw new ApiException("proposal was already rejected");
        }
        if (proposal.getStatus().equals("On Hold")){
            proposal.setStatus(status);
            proposalRepository.save(proposal);
        }
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(proposal.getChatRoomId());
        if (status.equals("Accepted")){
            Order order = new Order();
            order.setClientId(clientId);
            order.setDesignerId(chatRoom.getDesignerId());
            order.setDeadLine(proposal.getDeadLine());
            order.setPrice(proposal.getPrice());
            order.setStatus("Waiting for Down Payment");
            orderRepository.save(order);
            Bill bill = new Bill();
            Double downPayment = order.getPrice() * 0.20;
            Double fullPayment = order.getPrice() * 0.80;
            bill.setDownPayment(downPayment);
            bill.setFullPayment(fullPayment);
            bill.setOrderId(order.getId());
            bill.setStatues("Pay The Down Payment");
            billRepository.save(bill);
            return "proposal have been Accepted! and a new bill was issued for down payment";
        }
        // if rejected
        return "proposal have been Rejected!";
    }

    public List<Designer> getDesignersOrderByRating(){
        List<Designer> designers = designerRepository.getBasedOnRating();
        if (designers.isEmpty()){
            throw new ApiException("no designer found");
        }
        return designers;
    }

    public List<Designer> getDesignersOrderByRatingAndCategory(String category){
        List<Designer> designers = designerRepository.getBasedOnRatingAndCategory(category);
        if (designers.isEmpty()){
            throw new ApiException("no designer found");
        }
        return designers;
    }


    public void sendAMessage(String content, Integer chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null){
            throw new ApiException("chatroom not found");
        }
        Message message = new Message();
        message.setChatRoomId(chatRoomId);
        message.setSenderId(chatRoom.getClientId());
        message.setReceiverId(chatRoom.getDesignerId());
        message.setTimeStamp(LocalDate.now());
        message.setContent(content);
        message.setSenderName(clientRepository.findClientById(chatRoom.getClientId()).getName());
        messageRepository.save(message);
    }


    public void sendAMessage(String content, String attachmentUrl, Integer chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null){
            throw new ApiException("chatroom not found");
        }
        Message message = new Message();
        message.setChatRoomId(chatRoomId);
        message.setSenderId(chatRoom.getClientId());
        message.setReceiverId(chatRoom.getDesignerId());
        message.setTimeStamp(LocalDate.now());
        message.setAttachmentUrl(attachmentUrl);
        message.setContent(content);
        message.setSenderName(clientRepository.findClientById(chatRoom.getClientId()).getName());
        messageRepository.save(message);

    }

    public Object payTheBill(Integer clientId, Integer billId){
        Bill bill = billRepository.findBillById(billId);
        if (bill == null){
            throw new ApiException("bill not found");
        }
        Client client = clientRepository.findClientById(clientId);
        if (client == null){
            throw new ApiException("client was not found");
        }
        Order order = orderRepository.findOrderById(bill.getOrderId());
        if (!order.getClientId().equals(clientId)){
            throw new ApiException("this bill cannot belong to the client");
        }
        if (bill.getStatues().equals("Pay The Down Payment")){
            order.setStatus("Work In Progress");
            bill.setStatues("Soon");
            orderRepository.save(order);
            billRepository.save(bill);
            return "Down Payment was paid, designer can start working on the project";
        }
        if (bill.getStatues().equals("Pay The Full Payment")){
            order.setStatus("Done");
            bill.setStatues("Paid");
            orderRepository.save(order);
            billRepository.save(bill);
            return "Full payment was paid";
        }
        if (bill.getStatues().equals("Soon")){
            throw new ApiException("Can't pay the bill now");
        }
        if (order.getStatus().equals("Done")){
            throw new ApiException("Bill was already paid");
        }
        return null;
    }

    public Object acceptOrRejectProject(Integer clientId, Integer projectId, String status){
        Project project = projectRepository.findProjectById(projectId);
        if (project == null){
            throw new ApiException("project was not found");
        }
        Order order = orderRepository.findOrderById(project.getOrderId());
        if (!order.getClientId().equals(clientId)){
            throw new ApiException("order does not belong to the client");
        }
        if (!status.equals("Accepted") && !status.equals("Rejected")){
            throw new ApiException("status can only be Accepted or Rejected");
        }
        if (!project.getStatus().equals("Waiting For Approval")){
            throw new ApiException("project is not waiting for approval");
        }
        project.setStatus(status);
        projectRepository.save(project);
        if (status.equals("Accepted")){
            order.setStatus("Done");
            orderRepository.save(order);
            Bill bill = billRepository.findBillByOrderId(order.getId());
            bill.setStatues("Pay The Full Payment");
            billRepository.save(bill);
            return "Project was accepted you can now pay the rest of the bill";
        }
        // if project rejected
        return "Project was rejected";
    }

    public void sendRequest(Integer clientId, Integer designerId, String projectDetails) {

        Client client = clientRepository.findClientById(clientId);
        if (client == null){
            throw new ApiException("client not found");
        }

        Designer designer = designerRepository.findDesignerById(designerId);
        if (designer == null){
            throw new ApiException("designer not found");
        }

        Request request = new Request();
        request.setClientId(clientId);
        request.setDesignerId(designerId);
        request.setProjectDetails(projectDetails);
        request.setStatus("On Hold"); // initial status
        request.setRequestTime(LocalDate.now());

        requestRepository.save(request);

        // keep your existing request notification
        notificationService.sendRequestNotification(
                designer.getEmail(),
                projectDetails,
                designer.getName()
        );

    }




}
