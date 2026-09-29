package com.example.capston2.Service;

import com.example.capston2.Api.ApiException;
import com.example.capston2.Model.Client;
import com.example.capston2.Model.Designer;
import com.example.capston2.Model.Order;
import com.example.capston2.Model.Rating;
import com.example.capston2.Repository.ClientRepository;
import com.example.capston2.Repository.DesignerRepository;
import com.example.capston2.Repository.OrderRepository;
import com.example.capston2.Repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final ClientRepository clientRepository;
    private final DesignerRepository designerRepository;
    private final OrderRepository orderRepository;

    public List<Rating> getAllRatings(){
        return ratingRepository.findAll();
    }

    public void addRating(Rating rating){
        Client client = clientRepository.findClientById(rating.getClientId());
        if (client == null){
            throw new ApiException("client was not found");
        }
        Designer designer = designerRepository.findDesignerById(rating.getDesignerId());
        if (designer == null){
            throw new ApiException("designer was not found");
        }
        Order order = orderRepository.findOrderById(rating.getOrderId());
        if (order == null){
            throw new ApiException("order was not found");
        }
        if (!order.getStatus().equals("Done")){
            throw new ApiException("Order status is not Done");
        }
        if (!order.getClientId().equals(rating.getClientId()) && !order.getDesignerId().equals(rating.getDesignerId())){
            throw new ApiException("Order does not belong to client or designer");
        }
        rating.setTimeStamp(LocalDate.now());
        ratingRepository.save(rating);
        // update designer average rating
        designer.setAverageRating(calculateAverageRating(designer.getId()));
        designer.setRatingCount(ratingRepository.findRatingByDesignerId(designer.getId()).size());
        designerRepository.save(designer);

    }

    public Boolean updateRating(Integer id, Rating rating){
        Rating oldRating = ratingRepository.findRatingById(id);
        if (oldRating == null){
            return false;
        }
        oldRating.setOrderId(rating.getOrderId());
        oldRating.setRate(rating.getRate());
        oldRating.setComment(rating.getComment());
        oldRating.setTimeStamp(LocalDate.now());
        ratingRepository.save(oldRating);
        Designer designer = designerRepository.findDesignerById(oldRating.getDesignerId());
        designer.setAverageRating(calculateAverageRating(designer.getId()));
        designer.setRatingCount(ratingRepository.findRatingByDesignerId(oldRating.getDesignerId()).size());
        designerRepository.save(designer);
        return true;
    }

    public void deleteRating(Integer id){
        Rating oldRating = ratingRepository.findRatingById(id);
        if (oldRating == null){
            throw new ApiException("rating was not found");
        }
        Designer designer = designerRepository.findDesignerById(oldRating.getDesignerId());
        ratingRepository.delete(oldRating);
        designer.setAverageRating(calculateAverageRating(designer.getId()));
        designer.setRatingCount(ratingRepository.findRatingByDesignerId(oldRating.getDesignerId()).size());
        designerRepository.save(designer);
    }

    // this method is not in the controller
    public Double calculateAverageRating(Integer designerId){
        List<Rating> ratings = ratingRepository.findRatingByDesignerId(designerId);
        if (ratings.isEmpty()){
            return 0.0;
        }
        Double sum = 0.0;
        for (int i = 0; i<ratings.size(); i++){
            sum += ratings.get(i).getRate();
        }
        sum = sum/ratings.size();
        return sum;
    }
}