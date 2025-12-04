package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
        assertNotNull(trade.getTradeId());
        assertEquals(tradeAccount, trade.getAccount());

        // Update
        String tradeAccountUpdate = "Trade Account Update";
        trade.setAccount(tradeAccountUpdate);
        trade = tradeRepository.save(trade);
        assertEquals(tradeAccountUpdate, trade.getAccount());

        // Find
        List<Trade> listResult = tradeRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = trade.getTradeId();
        tradeRepository.delete(trade);
        Optional<Trade> tradeList = tradeRepository.findById(id);
        assertFalse(tradeList.isPresent());
    }
}
