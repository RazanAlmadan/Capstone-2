package com.example.capston2.Service;

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

    public Integer addProposal(Integer designerId, Integer clientId, Proposal proposal){
        Client client = clientRepository.findClientById(clientId);
        if (client == null){
            return 1;
        }
        Designer designer = designerRepository.findDesignerById(designerId);
        if (designer == null){
            return 2;
        }
        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(proposal.getChatRoomId());
        if (chatRoom == null){
            return 3;
        }
        if (!chatRoom.getClientId().equals(clientId)){
            return 4;
        }
        if (!chatRoom.getDesignerId().equals(designerId)){
            return 5;
        }
        proposalRepository.save(proposal);
        return 0;
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




    public Integer updateProposal(Integer id, Proposal proposal){
        Proposal oldProposal = proposalRepository.findProposalById(id);
        if (oldProposal == null){
            return -1;
        }
        if (oldProposal.getStatus().equals("On Hold") || oldProposal.getStatus().equals("rejected")) {
            oldProposal.setChatRoomId(proposal.getChatRoomId());
            oldProposal.setPrice(proposal.getPrice());
            oldProposal.setDeadLine(proposal.getDeadLine());
            oldProposal.setDetails(proposal.getDetails());
            oldProposal.setStatus("On Hold");
            proposalRepository.save(oldProposal);
            return 0;
        }
        return 1;
    }

    public Boolean deleteProposal(Integer id){
        Proposal oldProposal = proposalRepository.findProposalById(id);
        if (oldProposal == null){
            return false;
        }
        proposalRepository.delete(oldProposal);
        return true;
    }

}