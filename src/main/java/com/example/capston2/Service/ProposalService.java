package com.example.capston2.Service;

import com.example.capston2.Api.ApiException;
import com.example.capston2.Model.ChatRoom;
import com.example.capston2.Model.Client;
import com.example.capston2.Model.Designer;
import com.example.capston2.Model.Proposal;
import com.example.capston2.Repository.ChatRoomRepository;
import com.example.capston2.Repository.ClientRepository;
import com.example.capston2.Repository.DesignerRepository;
import com.example.capston2.Repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final ClientRepository clientRepository;
    private final DesignerRepository designerRepository;
    private final ChatRoomRepository chatRoomRepository;

    public List<Proposal> getAllProposals(){
        return proposalRepository.findAll();
    }

    public void addProposal(Integer designerId, Integer clientId, Proposal proposal){
        Client client = clientRepository.findClientById(clientId);
        if (client == null){
            throw new ApiException("client not found");
        }
        Designer designer = designerRepository.findDesignerById(designerId);
        if (designer == null){
            throw new ApiException("Designer not found");
        }
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(proposal.getChatRoomId());
        if (chatRoom == null){
            throw new ApiException("chat room not found");
        }
        if (!chatRoom.getClientId().equals(clientId)){
            throw new ApiException("client does not belong to the chatroom");
        }
        if (!chatRoom.getDesignerId().equals(designerId)){
            throw new ApiException("designer does not belong to the chatroom");
        }
        proposalRepository.save(proposal);

    }

    public void addAIProposal(Integer chatRoomId, Double price, LocalDate deadline, String details) {

        Proposal proposal = new Proposal();
        proposal.setChatRoomId(chatRoomId);
        proposal.setPrice(price);
        proposal.setDeadLine(deadline);
        proposal.setDetails(details);
        proposal.setStatus("On Hold");

        proposalRepository.save(proposal);
    }




    public void updateProposal(Integer id, Proposal proposal){
        Proposal oldProposal = proposalRepository.findProposalById(id);
        if (oldProposal == null){
            throw new ApiException("proposal not found");
        }
        if (oldProposal.getStatus().equals("Accepted")){
            throw new ApiException("proposal was accepted you can't update it");
        }
        if (oldProposal.getStatus().equals("On Hold") || oldProposal.getStatus().equals("rejected")) {
            oldProposal.setChatRoomId(proposal.getChatRoomId());
            oldProposal.setPrice(proposal.getPrice());
            oldProposal.setDeadLine(proposal.getDeadLine());
            oldProposal.setDetails(proposal.getDetails());
            oldProposal.setStatus("On Hold");
            proposalRepository.save(oldProposal);

        }
    }

    public void deleteProposal(Integer id){
        Proposal oldProposal = proposalRepository.findProposalById(id);
        if (oldProposal == null){
            throw new ApiException("proposal was not found");
        }
        proposalRepository.delete(oldProposal);
    }

}