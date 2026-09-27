package com.example.capston2.Service;

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

    public Boolean updateClient(Integer id, Client client){
        Client oldClient = clientRepository.findClientById(id);
        if (oldClient == null){
            return false;
        }
        oldClient.setName(client.getName());
        oldClient.setEmail(client.getEmail());
        oldClient.setPassword(client.getPassword());
        clientRepository.save(oldClient);
        return true;
    }

    public Boolean deleteClient(Integer id){
        Client oldClient = clientRepository.findClientById(id);
        if (oldClient == null){
            return false;
        }
        clientRepository.delete(oldClient);
        return true;
    }

    public Designer searchByName(String name){
        Designer designer = designerRepository.findDesignerByName(name);
        if (designer == null){
            return null;
        }
        return designer;
    }

    public List<Designer> searchByCategory(String visualField){
        List<Designer> designers = designerRepository.findDesignerByVisualField(visualField);
        return designers;
    }

    public Integer acceptOrRejectProposal(Integer clientId, Integer proposalId, String status){
        Proposal proposal = proposalRepository.findProposalById(proposalId);
        if (proposal == null){
            return -1;
        }
        Client client = clientRepository.findClientById(clientId);
        if (client == null){
            return 1;
        }
        if (!status.equals("Accepted") && !status.equals("Rejected")){
            return 2;
        }
        if (proposal.getStatus().equals("Accepted")){
            return 3;
        }
        if (proposal.getStatus().equals("Rejected")){
            return 4;
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
            return 0;
        }
        // if rejected
        return 5;
    }

    public List<Designer> getDesignersOrderByRating(){
        List<Designer> designers = designerRepository.getBasedOnRating();
        return designers;
    }

    public List<Designer> getDesignersOrderByRatingAndCategory(String category){
        List<Designer> designers = designerRepository.getBasedOnRatingAndCategory(category);
        return designers;
    }


    public Boolean sendAMessage(String content, Integer chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null){
            return false;
        }
        Message message = new Message();
        message.setChatRoomId(chatRoomId);
        message.setSenderId(chatRoom.getClientId());
        message.setReceiverId(chatRoom.getDesignerId());
        message.setTimeStamp(LocalDate.now());
        message.setContent(content);
        message.setSenderName(clientRepository.findClientById(chatRoom.getClientId()).getName());
        messageRepository.save(message);
        return true;
    }


    public Boolean sendAMessage(String content, String attachmentUrl, Integer chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null){
            return false;
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
        return true;
    }

    public Integer payTheBill(Integer clientId, Integer billId){
        Bill bill = billRepository.findBillById(billId);
        if (bill == null){
            return -1;
        }
        Client client = clientRepository.findClientById(clientId);
        if (client == null){
            return -2;
        }
        Order order = orderRepository.findOrderById(bill.getOrderId());
        if (!order.getClientId().equals(clientId)){
            return 0;
        }
        if (bill.getStatues().equals("Pay The Down Payment")){
            order.setStatus("Work In Progress");
            bill.setStatues("Soon");
            orderRepository.save(order);
            billRepository.save(bill);
            return 1;
        }
        if (bill.getStatues().equals("Pay The Full Payment")){
            order.setStatus("Done");
            bill.setStatues("Paid");
            orderRepository.save(order);
            billRepository.save(bill);
            return 2;
        }
        if (bill.getStatues().equals("Soon")){
            return 3;
        }
        // if project status is "Done"
        return 4;
    }

    public Integer acceptOrRejectProject(Integer clientId, Integer projectId, String status){
        Project project = projectRepository.findProjectById(projectId);
        if (project == null){
            return -1;
        }
        Order order = orderRepository.findOrderById(project.getOrderId());
        if (!order.getClientId().equals(clientId)){
            return 1;
        }
        if (!status.equals("Accepted") && !status.equals("Rejected")){
            return 2;
        }
        if (!project.getStatus().equals("Waiting For Approval")){
            return 3;
        }
        project.setStatus(status);
        projectRepository.save(project);
        if (status.equals("Accepted")){
            order.setStatus("Done");
            orderRepository.save(order);
            Bill bill = billRepository.findBillByOrderId(order.getId());
            bill.setStatues("Pay The Full Payment");
            billRepository.save(bill);
            return 0;
        }
        // if project rejected
        return 4;
    }

    public Integer sendRequest(Integer clientId, Integer designerId, String projectDetails) {

        Client client = clientRepository.findClientById(clientId);
        if (client == null){
            return 1;
        }

        Designer designer = designerRepository.findDesignerById(designerId);
        if (designer == null){
            return 2;
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

        return 0;
    }




}
