package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
    private BidListRepository bidListRepository;

    @Test
    public void bidListTest() {
        //TODO Use .builder() instead of new BidList() to create test instances
        BigDecimal bidQuantity = BigDecimal.valueOf(10d);
        BidList bid = BidList.builder()
                .account("Account Test")
                .type("Type Test")
                .bidQuantity(bidQuantity)
                .build();


        // Save
        bid = bidListRepository.save(bid);
        assertNotNull(bid.getId());
        assertEquals(bid.getBidQuantity(), bidQuantity);

        // Update
        BigDecimal bidQuantityUpdate = BigDecimal.valueOf(20d);
        bid.setBidQuantity(bidQuantityUpdate);
        bid = bidListRepository.save(bid);
        assertEquals(bid.getBidQuantity(), bidQuantityUpdate);

        // Find
        List<BidList> listResult = bidListRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = bid.getId();
        bidListRepository.delete(bid);
        Optional<BidList> bidList = bidListRepository.findById(id);
        assertFalse(bidList.isPresent());
    }
}
