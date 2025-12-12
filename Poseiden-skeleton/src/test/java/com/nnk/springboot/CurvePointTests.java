package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CurvePointTests {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    public void curvePointTest() {
        BigDecimal term = BigDecimal.valueOf(10d);
        BigDecimal value = BigDecimal.valueOf(30d);
        CurvePoint curvePoint = CurvePoint.builder()
                .curveId(10)
                .term(term)
                .value(value)
                .asOfDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .build();

        // Save
        curvePoint = curvePointRepository.save(curvePoint);
        assertNotNull(curvePoint.getId());
        assertEquals(10, (int) curvePoint.getCurveId());

        // Update
        curvePoint.setCurveId(20);
        curvePoint = curvePointRepository.save(curvePoint);
        assertEquals(20, (int) curvePoint.getCurveId());

        // Find
        List<CurvePoint> listResult = curvePointRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = curvePoint.getId();
        curvePointRepository.delete(curvePoint);
        Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
        assertFalse(curvePointList.isPresent());
    }

}
