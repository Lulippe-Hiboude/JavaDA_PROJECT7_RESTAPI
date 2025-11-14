package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
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
		Assert.assertNotNull(bid.getBidListId());
		Assert.assertEquals(bid.getBidQuantity(), bidQuantity);

		// Update
		BigDecimal bidQuantityUpdate = BigDecimal.valueOf(20d);
		bid.setBidQuantity(bidQuantityUpdate);
		bid = bidListRepository.save(bid);
		Assert.assertEquals(bid.getBidQuantity(), bidQuantityUpdate);

		// Find
		List<BidList> listResult = bidListRepository.findAll();
        Assert.assertFalse(listResult.isEmpty());

		// Delete
		Integer id = bid.getBidListId();
		bidListRepository.delete(bid);
		Optional<BidList> bidList = bidListRepository.findById(id);
		Assert.assertFalse(bidList.isPresent());
	}
}
