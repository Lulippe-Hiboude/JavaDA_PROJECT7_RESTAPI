package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeTests {

	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeTest() {
		String tradeAccount = "Trade Account";
		String type = "Type";
		Trade trade = Trade.builder()
				.account(tradeAccount)
				.type(type)
				.build();

		// Save
		trade = tradeRepository.save(trade);
		Assert.assertNotNull(trade.getTradeId());
        Assert.assertEquals(tradeAccount, trade.getAccount());

		// Update
		String tradeAccountUpdate = "Trade Account Update";
		trade.setAccount(tradeAccountUpdate);
		trade = tradeRepository.save(trade);
        Assert.assertEquals(tradeAccountUpdate, trade.getAccount());

		// Find
		List<Trade> listResult = tradeRepository.findAll();
        Assert.assertFalse(listResult.isEmpty());

		// Delete
		Integer id = trade.getTradeId();
		tradeRepository.delete(trade);
		Optional<Trade> tradeList = tradeRepository.findById(id);
		Assert.assertFalse(tradeList.isPresent());
	}
}
