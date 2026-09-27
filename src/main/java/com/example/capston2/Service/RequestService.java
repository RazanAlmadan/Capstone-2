package com.example.capston2.Service;

import com.example.capston2.Model.Client;
import com.example.capston2.Model.Designer;
import com.example.capston2.Model.Request;
import com.example.capston2.Notification.NotificationService;
import com.example.capston2.Repository.ClientRepository;
import com.example.capston2.Repository.DesignerRepository;
import com.example.capston2.Repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final ClientRepository clientRepository;
    private final DesignerRepository designerRepository;
    private final NotificationService notificationService;

    public List<Request> getAllRequests(){
        return requestRepository.findAll();
    }

    public Integer addRequest(Request request){
        Client client = clientRepository.findClientById(request.getClientId());
        if (client == null){
            return 1;
        }
        Designer designer = designerRepository.findDesignerById(request.getDesignerId());
        if (designer == null){
            return 2;
        }
        if (request.getStatus().equals("Accepted") || request.getStatus().equals("Rejected")){
            return 3;
        }
        request.setRequestTime(LocalDate.now());
        requestRepository.save(request);
        String requestInfo = request.getProjectDetails();
        notificationService.sendRequestNotification(designer.getEmail(), requestInfo, designer.getName());
        return 0;
    }

    public Boolean updateRequest(Integer id, Request request){
        Request oldRequest = requestRepository.findRequestById(id);
        if (oldRequest == null){
            return false;
        }
        oldRequest.setClientId(request.getClientId());
        oldRequest.setDesignerId(request.getDesignerId());
        oldRequest.setRequestTime(LocalDate.now());
        oldRequest.setProjectDetails(request.getProjectDetails());
        oldRequest.setStatus(request.getStatus());
        requestRepository.save(oldRequest);
        return true;
    }

    public Boolean deleteRequest(Integer id){
        Request oldRequest = requestRepository.findRequestById(id);
        if (oldRequest == null){
            return false;
        }
        requestRepository.delete(oldRequest);
        return true;
    }
}
