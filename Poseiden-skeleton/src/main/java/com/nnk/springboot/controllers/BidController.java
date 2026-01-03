package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.trade_bid.bid.BidCreateDto;
import com.nnk.springboot.dto.trade_bid.bid.BidDto;
import com.nnk.springboot.dto.trade_bid.bid.BidUpdateDto;
import com.nnk.springboot.service.BidService;
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
@RequestMapping("/bid")
public class BidController extends AbstractEntityController<BidCreateDto, BidDto, BidUpdateDto> {
    private final BidService bidService;

    public BidController(final BidService bidService) {
        super("bid", bidService);
        this.bidService = bidService;
    }

    @Override
    public String submitCreateForm(@Valid @ModelAttribute("bid") final BidCreateDto bidCreateDto, final BindingResult result) {
        log.info("submit bid creation form");
        if (result.hasErrors()) {
            return getView(ADD.getValue());
        }
        bidService.handleEntityCreation(bidCreateDto);
        return getRedirectedUrl(LIST.getValue());
    }

    @Override
    public String updateEntity(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("bid") final BidUpdateDto bidUpdateDto,
                               final BindingResult result) {

        if (result.hasErrors()) {
            return getView(UPDATE.getValue());
        }
        bidService.handleEntityUpdate(bidUpdateDto);
        return getRedirectedUrl(LIST.getValue());
    }

    @Override
    protected BidCreateDto createEntityDto() {
        return new BidCreateDto();
    }
}
