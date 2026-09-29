package com.example.capston2.Service;

import com.example.capston2.Api.ApiException;
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

    public void addRequest(Request request){
        Client client = clientRepository.findClientById(request.getClientId());
        if (client == null){
            throw new ApiException("client not found");
        }
        Designer designer = designerRepository.findDesignerById(request.getDesignerId());
        if (designer == null){
            throw new ApiException("designer not found");
        }
        if (request.getStatus().equals("Accepted") || request.getStatus().equals("Rejected")){
            throw new ApiException("Can't create a request with Accepted or Rejected status");
        }
        request.setStatus("On Hold");
        request.setRequestTime(LocalDate.now());
        requestRepository.save(request);
        String requestInfo = request.getProjectDetails();
        notificationService.sendRequestNotification(designer.getEmail(), requestInfo, designer.getName());

    }

    public void updateRequest(Integer id, Request request){
        Request oldRequest = requestRepository.findRequestById(id);
        if (oldRequest == null){
            throw new ApiException("request not found");
        }
        oldRequest.setClientId(request.getClientId());
        oldRequest.setDesignerId(request.getDesignerId());
        oldRequest.setRequestTime(LocalDate.now());
        oldRequest.setProjectDetails(request.getProjectDetails());
        oldRequest.setStatus(request.getStatus());
        requestRepository.save(oldRequest);
    }

    public void deleteRequest(Integer id){
        Request oldRequest = requestRepository.findRequestById(id);
        if (oldRequest == null){
            throw new ApiException("request not found");
        }
        requestRepository.delete(oldRequest);
    }
}
