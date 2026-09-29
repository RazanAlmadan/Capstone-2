package com.example.capston2.Service;

import com.example.capston2.Api.ApiException;
import com.example.capston2.Model.*;
import com.example.capston2.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignerService {
    private final DesignerRepository designerRepository;
    private final RequestRepository requestRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final RatingRepository ratingRepository;
    private final ProposalRepository proposalRepository;
    private final ClientRepository clientRepository;

    public List<Designer> getAllDesigners(){
        return designerRepository.findAll();
    }

    public void addDesigner(Designer designer){
        designerRepository.save(designer);
    }

    public void updateDesigner(Integer id, Designer designer){
        Designer oldDesigner = designerRepository.findDesignerById(id);
        if (oldDesigner == null){
            throw new ApiException("designer not found");
        }
        oldDesigner.setName(designer.getName());
        oldDesigner.setEmail(designer.getEmail());
        oldDesigner.setPassword(designer.getPassword());
        oldDesigner.setVisualField(designer.getVisualField());
        oldDesigner.setBio(designer.getBio());
        oldDesigner.setCatalogs(designer.getCatalogs());
        designerRepository.save(oldDesigner);

    }

    public void deleteDesigner(Integer id){
        Designer oldDesigner = designerRepository.findDesignerById(id);
        if (oldDesigner == null){
            throw new ApiException("designer not found");
        }
        designerRepository.delete(oldDesigner);
    }


    public List<Request> getRequests(Integer id){
        List<Request> requests = requestRepository.findRequestByDesignerId(id);
        if (requests.isEmpty()){
            throw new ApiException("no request were found");
        }
        return requests;
    }

    public Object approveOrRejectRequest(Integer id, Integer requestId, String status){
        Request request = requestRepository.findRequestById(requestId);
        if (request == null){
            throw new ApiException("request not found");
        }
        if (!request.getDesignerId().equals(id)){
            throw new ApiException("request does not belong to designer");
        }
        if (!status.equals("Accepted") && !status.equals("Rejected")){
            throw new ApiException("status must be either Accepted or Rejected");
        }
        if (request.getStatus().equals("Accepted")){
            throw new ApiException("Request was already accepted");
        }
        if (request.getStatus().equals("Rejected")){
            throw new ApiException("Request was already rejected");
        }
        if (request.getStatus().equals("On Hold")){
            request.setStatus(status);
            requestRepository.save(request);
        }
        if (status.equals("Accepted")){
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setRequestId(requestId);
            chatRoom.setCreatedAt(LocalDate.now());
            chatRoom.setClientId(request.getClientId());
            chatRoom.setDesignerId(request.getDesignerId());
            chatRoomRepository.save(chatRoom);
            return "Request have been Accepted!";
        }
        return "Request have been Rejected!";

    }

    public void sendAMessage(String content, Integer chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null){
            throw new ApiException("chatroom not found");
        }
        Message message = new Message();
        message.setChatRoomId(chatRoomId);
        message.setSenderId(chatRoom.getDesignerId());
        message.setReceiverId(chatRoom.getClientId());
        message.setTimeStamp(LocalDate.now());
        message.setContent(content);
        message.setSenderName(designerRepository.findDesignerById(chatRoom.getDesignerId()).getName());
        messageRepository.save(message);

    }


    public void sendAMessage(String content, String attachmentUrl, Integer chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null){
            throw new ApiException("chatroom not found");
        }
        Message message = new Message();
        message.setChatRoomId(chatRoomId);
        message.setSenderId(chatRoom.getDesignerId());
        message.setReceiverId(chatRoom.getClientId());
        message.setTimeStamp(LocalDate.now());
        message.setAttachmentUrl(attachmentUrl);
        message.setContent(content);
        message.setSenderName(designerRepository.findDesignerById(chatRoom.getDesignerId()).getName());
        messageRepository.save(message);
    }


    public void sendProposal(Integer designerId,
                                Integer clientId,
                                Integer chatRoomId,
                                Double price,
                                String details,
                                LocalDate deadLine) {

        Client client = clientRepository.findClientById(clientId);
        if (client == null) {
            throw new ApiException("client not found");
        }

        Designer designer = designerRepository.findDesignerById(designerId);
        if (designer == null) {
            throw new ApiException("designer not found");
        }

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null) {
            throw new ApiException("chatroom not found");
        }

        if (!chatRoom.getClientId().equals(clientId)) {
            throw new ApiException("chatroom does not belong to client");
        }

        if (!chatRoom.getDesignerId().equals(designerId)) {
            throw new ApiException("chatroom does not belong to designer");
        }

        Proposal proposal = new Proposal();
        proposal.setChatRoomId(chatRoomId);
        proposal.setPrice(price);
        proposal.setDetails(details);
        proposal.setDeadLine(deadLine);
        proposal.setStatus("On Hold");

        proposalRepository.save(proposal);

    }




}
