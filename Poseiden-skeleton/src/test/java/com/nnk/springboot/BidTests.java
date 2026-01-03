package com.nnk.springboot;

import com.nnk.springboot.domain.Bid;
import com.nnk.springboot.repositories.BidRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BidTests {

    @Autowired
    private BidRepository bidRepository;

    @Test
    public void bidListTest() {
        //TODO Use .builder() instead of new BidList() to create test instances
        BigDecimal bidQuantity = BigDecimal.valueOf(10d);
        Bid bid = Bid.builder()
                .account("Account Test")
                .type("Type Test")
                .bidQuantity(bidQuantity)
                .build();


        // Save
        bid = bidRepository.save(bid);
        assertNotNull(bid.getId());
        assertEquals(bid.getBidQuantity(), bidQuantity);

        // Update
        BigDecimal bidQuantityUpdate = BigDecimal.valueOf(20d);
        bid.setBidQuantity(bidQuantityUpdate);
        bid = bidRepository.save(bid);
        assertEquals(bid.getBidQuantity(), bidQuantityUpdate);

        // Find
        List<Bid> listResult = bidRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = bid.getId();
        bidRepository.delete(bid);
        Optional<Bid> bidList = bidRepository.findById(id);
        assertFalse(bidList.isPresent());
    }
}
