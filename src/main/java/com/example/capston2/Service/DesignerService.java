package com.example.capston2.Service;

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

    public Boolean updateDesigner(Integer id, Designer designer){
        Designer oldDesigner = designerRepository.findDesignerById(id);
        if (oldDesigner == null){
            return false;
        }
        oldDesigner.setName(designer.getName());
        oldDesigner.setEmail(designer.getEmail());
        oldDesigner.setPassword(designer.getPassword());
        oldDesigner.setVisualField(designer.getVisualField());
        oldDesigner.setBio(designer.getBio());
        oldDesigner.setCatalogs(designer.getCatalogs());
        designerRepository.save(oldDesigner);
        return true;
    }

    public Boolean deleteDesigner(Integer id){
        Designer oldDesigner = designerRepository.findDesignerById(id);
        if (oldDesigner == null){
            return false;
        }
        designerRepository.delete(oldDesigner);
        return true;
    }


    public List<Request> getRequests(Integer id){
        List<Request> requests = requestRepository.findRequestByDesignerId(id);
        return requests;
    }

    public Integer approveOrRejectRequest(Integer id, Integer requestId, String status){
        Request request = requestRepository.findRequestById(requestId);
        if (request == null){
            return -1;
        }
        if (!request.getDesignerId().equals(id)){
            return 1;
        }
        if (!status.equals("Accepted") && !status.equals("Rejected")){
            return 2;
        }
        if (request.getStatus().equals("Accepted")){
            return 3;
        }
        if (request.getStatus().equals("Rejected")){
            return 4;
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
            return 0;
        }
        return 5;

    }

    public Boolean sendAMessage(String content, Integer chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null){
            return false;
        }
        Message message = new Message();
        message.setChatRoomId(chatRoomId);
        message.setSenderId(chatRoom.getDesignerId());
        message.setReceiverId(chatRoom.getClientId());
        message.setTimeStamp(LocalDate.now());
        message.setContent(content);
        message.setSenderName(designerRepository.findDesignerById(chatRoom.getDesignerId()).getName());
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
        message.setSenderId(chatRoom.getDesignerId());
        message.setReceiverId(chatRoom.getClientId());
        message.setTimeStamp(LocalDate.now());
        message.setAttachmentUrl(attachmentUrl);
        message.setContent(content);
        message.setSenderName(designerRepository.findDesignerById(chatRoom.getDesignerId()).getName());
        messageRepository.save(message);
        return true;
    }


    public Integer sendProposal(Integer designerId,
                                Integer clientId,
                                Integer chatRoomId,
                                Double price,
                                String details,
                                LocalDate deadLine) {

        Client client = clientRepository.findClientById(clientId);
        if (client == null) {
            return 1;
        }

        Designer designer = designerRepository.findDesignerById(designerId);
        if (designer == null) {
            return 2;
        }

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(chatRoomId);
        if (chatRoom == null) {
            return 3;
        }

        if (!chatRoom.getClientId().equals(clientId)) {
            return 4;
        }

        if (!chatRoom.getDesignerId().equals(designerId)) {
            return 5;
        }

        Proposal proposal = new Proposal();
        proposal.setChatRoomId(chatRoomId);
        proposal.setPrice(price);
        proposal.setDetails(details);
        proposal.setDeadLine(deadLine);
        proposal.setStatus("On Hold");

        proposalRepository.save(proposal);

        return 0;
    }




}
