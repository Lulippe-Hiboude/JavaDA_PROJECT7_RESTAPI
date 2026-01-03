package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.trade_bid.bid.BidCreateDto;
import com.nnk.springboot.dto.trade_bid.bid.BidDto;
import com.nnk.springboot.dto.trade_bid.bid.BidUpdateDto;
import com.nnk.springboot.service.BidService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BidController.class)
@AutoConfigureMockMvc
class BidControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidService bidService;

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should Return Add View With Empty Model")
    void shouldReturnAddViewWithEmptyModel() throws Exception {
        mockMvc.perform(get("/bid/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/add"))
                .andExpect(model().attributeExists("bid"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to bid list when valid bidCreateDto")
    void shouldRedirectToBidListWhenValidBidCreateDto() throws Exception {
        //given
        final BigDecimal bidQuantity = BigDecimal.valueOf(12.5);
        final String account = "account";
        final String type = "type";

        // when & then
        mockMvc.perform(post("/bid/validate")
                        .with(csrf())
                        .param("bidQuantity", String.valueOf(bidQuantity))
                        .param("account", account)
                        .param("type", type))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bid/list"));

        verify(bidService).handleEntityCreation(any(BidCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to bid add form if parameters are invalid")
    void shouldRedirectToBidAddFormIfParametersAreInvalid() throws Exception {
        //given
        final BigDecimal bidQuantity = BigDecimal.valueOf(1000.553);
        final String account = "";
        final String type = "";

        // when & then
        mockMvc.perform(post("/bid/validate")
                        .with(csrf())
                        .param("bidQuantity", String.valueOf(bidQuantity))
                        .param("account", account)
                        .param("type", type))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/add"))
                .andExpect(model().attributeHasFieldErrors("bid", "bidQuantity", "account", "type"));

        verify(bidService, times(0)).handleEntityCreation(any(BidCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to bid list after deletion by id")
    void shouldRedirectToBidListAfterDeletionById() throws Exception {
        //given
        final int bidId = 1;

        //when & then
        mockMvc.perform(get("/bid/delete/" + bidId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bid/list"))
                .andExpect(flash().attribute("successMessage", "bid deleted successfully"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to bid list after throw EntityNotFoundException")
    void shouldRedirectToBidListAfterThrowEntityNotFoundException() throws Exception {
        //given
        final int id = 1;
        doThrow(new EntityNotFoundException("bid not found")).when(bidService).handleEntityDeletion(id);

        //when & then
        mockMvc.perform(get("/bid/delete/" + id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bid/list"))
                .andExpect(flash().attribute("errorMessage", "bid not found"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to bid List after retrieving all bids")
    void shouldRedirectToBidListAfterRetrievingAllBids() throws Exception {
        //given
        final int id = 1;
        final BigDecimal bidQuantity = BigDecimal.valueOf(12.5);
        final String account = "account";
        final String type = "type";
        final BidDto bidDto = new BidDto();
        bidDto.setId(id);
        bidDto.setAccount(account);
        bidDto.setType(type);
        bidDto.setBidQuantity(bidQuantity);

        final List<BidDto> bidDtoList = List.of(bidDto);
        given(bidService.findAllEntity()).willReturn(bidDtoList);

        //when & then
        mockMvc.perform(get("/bid/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/list"))
                .andExpect(model().attribute("bids", bidDtoList));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should return bid/update form")
    void shouldReturnUpdateBidForm() throws Exception {
        //given
        final int id = 1;
        final BigDecimal bidQuantity = BigDecimal.valueOf(12.5);
        final String account = "account";
        final String type = "type";
        final BidUpdateDto bidUpdateDto = new BidUpdateDto();
        bidUpdateDto.setId(id);
        bidUpdateDto.setAccount(account);
        bidUpdateDto.setType(type);
        bidUpdateDto.setBidQuantity(bidQuantity);

        given(bidService.getEntityUpdateDto(id)).willReturn(bidUpdateDto);

        //when
        mockMvc.perform(get("/bid/update/" + id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/update"))
                .andExpect(model().attribute("bid", bidUpdateDto));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to bidList after successfully update")
    void shouldRedirectToBidListAfterSuccessfulUpdate() throws Exception {
        //given
        final int id = 1;
        final BigDecimal bidQuantity = BigDecimal.valueOf(12.5);
        final String account = "account";
        final String type = "type";
        final BidUpdateDto bidUpdateDto = new BidUpdateDto();
        bidUpdateDto.setId(id);
        bidUpdateDto.setAccount(account);
        bidUpdateDto.setType(type);
        bidUpdateDto.setBidQuantity(bidQuantity);

        // when & then
        mockMvc.perform(post("/bid/update/{id}", id)
                        .with(csrf())
                        .param("bidQuantity", String.valueOf(bidQuantity))
                        .param("account", account)
                        .param("type", type))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bid/list"));

        verify(bidService).handleEntityUpdate(bidUpdateDto);
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to bid update form if parameters are invalids")
    void shouldRedirectToBidUpdateFormIfParametersAreInvalid() throws Exception {
        //given
        final int id = 1;
        final BigDecimal bidQuantity = BigDecimal.valueOf(12.145);
        final String account = "";
        final String type = "";
        final BidUpdateDto bidUpdateDto = new BidUpdateDto();
        bidUpdateDto.setId(id);
        bidUpdateDto.setAccount(account);
        bidUpdateDto.setType(type);
        bidUpdateDto.setBidQuantity(bidQuantity);

        //when & then
        mockMvc.perform(post("/bid/update/{id}", id)
                        .with(csrf())
                        .param("bidQuantity", String.valueOf(bidQuantity))
                        .param("account", account)
                        .param("type", type))
                .andExpect(status().isOk())
                .andExpect(view().name("bid/update"))
                .andExpect(model().attributeHasFieldErrors("bid", "bidQuantity", "account", "type"));

        verify(bidService, times(0)).handleEntityUpdate(bidUpdateDto);
    }
}