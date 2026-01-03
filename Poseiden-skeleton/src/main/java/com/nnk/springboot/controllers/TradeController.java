package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.trade_bid.trade.TradeCreateDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeUpdateDto;
import com.nnk.springboot.service.TradeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.nnk.springboot.enums.ViewEnum.*;


@Controller
@Slf4j
@RequestMapping("/trade")
public class TradeController extends AbstractEntityController<TradeCreateDto, TradeDto, TradeUpdateDto> {
    private final TradeService tradeService;

    public TradeController(final TradeService tradeService) {
        super("trade", tradeService);
        this.tradeService = tradeService;
    }


    @Override
    public String submitCreateForm(@Valid @ModelAttribute("trade") final TradeCreateDto tradeCreateDto, final BindingResult result) {
        log.info("submit bid creation form");
        if (result.hasErrors()) {
            return getView(ADD.getValue());
        }
        tradeService.handleEntityCreation(tradeCreateDto);
        return getRedirectedUrl(LIST.getValue());
    }

    @Override
    public String updateEntity(@PathVariable("id") final Integer id,
                               @Valid @ModelAttribute("trade") final TradeUpdateDto tradeUpdateDto,
                               final BindingResult result) {

        if (result.hasErrors()) {
            return getView(UPDATE.getValue());
        }
        tradeService.handleEntityUpdate(tradeUpdateDto);
        return getRedirectedUrl(LIST.getValue());
    }

    @Override
    protected TradeCreateDto createEntityDto() {
        return new TradeCreateDto();
    }
}
