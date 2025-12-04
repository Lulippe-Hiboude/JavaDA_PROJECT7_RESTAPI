package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RatingTests {

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    public void ratingTest() {
        String moodysRating = "Moodys Rating";
        String sandPRating = "Sand PRating";
        String fitchRating = "Fitch Rating";
        int orderNumber = 10;
        Rating rating = Rating.builder()
                .moodysRating(moodysRating)
                .sandPRating(sandPRating)
                .fitchRating(fitchRating)
                .orderNumber(orderNumber)
                .build();

        // Save
        rating = ratingRepository.save(rating);
        assertNotNull(rating.getId());
        assertEquals(10, (int) rating.getOrderNumber());

        // Update
        rating.setOrderNumber(20);
        rating = ratingRepository.save(rating);
        assertEquals(20, (int) rating.getOrderNumber());

        // Find
        List<Rating> listResult = ratingRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = rating.getId();
        ratingRepository.delete(rating);
        Optional<Rating> ratingList = ratingRepository.findById(id);
        assertFalse(ratingList.isPresent());
    }
}
