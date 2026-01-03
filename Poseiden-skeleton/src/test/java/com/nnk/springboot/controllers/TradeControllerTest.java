package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.trade_bid.bid.BidDto;
import com.nnk.springboot.dto.trade_bid.bid.BidUpdateDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeCreateDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeDto;
import com.nnk.springboot.dto.trade_bid.trade.TradeUpdateDto;
import com.nnk.springboot.service.TradeService;
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

@WebMvcTest(controllers = TradeController.class)
@AutoConfigureMockMvc
class TradeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should Return Add View With Empty Model")
    void shouldReturnAddViewWithEmptyModel() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Trade list when valid TradeCreateDto")
    void shouldRedirectToTradeListWhenValidTradeCreateDto() throws Exception {
        final BigDecimal buyQuantity = BigDecimal.valueOf(12.5);
        final String account = "account";
        final String type = "type";

        // when & then
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("buyQuantity", String.valueOf(buyQuantity))
                        .param("account", account)
                        .param("type", type))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).handleEntityCreation(any(TradeCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Trade add form if parameters are invalid")
    void shouldRedirectToTradeAddFormIfParametersAreInvalid() throws Exception {
        //given
        final BigDecimal buyQuantity = BigDecimal.valueOf(1000.553);
        final String account = "";
        final String type = "";

        // when & then
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("buyQuantity", String.valueOf(buyQuantity))
                        .param("account", account)
                        .param("type", type))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeHasFieldErrors("trade", "buyQuantity", "account", "type"));

        verify(tradeService, times(0)).handleEntityCreation(any(TradeCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Trade list after deletion by id")
    void shouldRedirectToTradeListAfterDeletionById() throws Exception {
        //given
        final int id = 1;

        //when & then
        mockMvc.perform(get("/trade/delete/" + id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(flash().attribute("successMessage", "trade deleted successfully"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Trade list after throw EntityNotFoundException")
    void shouldRedirectToTradeListAfterThrowEntityNotFoundException() throws Exception {
        //given
        final int id = 1;
        doThrow(new EntityNotFoundException("trade not found")).when(tradeService).handleEntityDeletion(id);

        //when & then
        mockMvc.perform(get("/trade/delete/" + id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"))
                .andExpect(flash().attribute("errorMessage", "trade not found"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Trade List after retrieving all Trades")
    void shouldRedirectToTradeListAfterRetrievingAllTrades() throws Exception {
        //given
        final int id = 1;
        final BigDecimal buyQuantity = BigDecimal.valueOf(12.5);
        final String account = "account";
        final String type = "type";
        final TradeDto tradeDto = new TradeDto();
        tradeDto.setId(id);
        tradeDto.setAccount(account);
        tradeDto.setType(type);
        tradeDto.setBuyQuantity(buyQuantity);

        final List<TradeDto> tradeDtoList = List.of(tradeDto);
        given(tradeService.findAllEntity()).willReturn(tradeDtoList);

        //when & then
        mockMvc.perform(get("/trade/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attribute("trades", tradeDtoList));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should return Trade/update form")
    void shouldUpdateTradeForm() throws Exception {
        //given
        final int id = 1;
        final BigDecimal buyQuantity = BigDecimal.valueOf(12.5);
        final String account = "account";
        final String type = "type";
        final TradeUpdateDto tradeUpdateDto = new TradeUpdateDto();
        tradeUpdateDto.setId(id);
        tradeUpdateDto.setAccount(account);
        tradeUpdateDto.setType(type);
        tradeUpdateDto.setBuyQuantity(buyQuantity);

        given(tradeService.getEntityUpdateDto(id)).willReturn(tradeUpdateDto);

        //when
        mockMvc.perform(get("/trade/update/" + id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", tradeUpdateDto));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to TradeList after successfully update")
    void shouldRedirectToTradeListAfterSuccessfulUpdate() throws Exception {
        //given
        final int id = 1;
        final BigDecimal buyQuantity = BigDecimal.valueOf(12.5);
        final String account = "account";
        final String type = "type";
        final TradeUpdateDto tradeUpdateDto = new TradeUpdateDto();
        tradeUpdateDto.setId(id);
        tradeUpdateDto.setAccount(account);
        tradeUpdateDto.setType(type);
        tradeUpdateDto.setBuyQuantity(buyQuantity);

        // when & then
        mockMvc.perform(post("/trade/update/{id}", id)
                        .with(csrf())
                        .param("buyQuantity", String.valueOf(buyQuantity))
                        .param("account", account)
                        .param("type", type))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).handleEntityUpdate(tradeUpdateDto);
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Trade update form if parameters are invalids")
    void shouldRedirectToTradeUpdateFormIfParametersAreInvalid() throws Exception {
        //given
        final int id = 1;
        final BigDecimal buyQuantity = BigDecimal.valueOf(12.145);
        final String account = "";
        final String type = "";
        final TradeUpdateDto tradeUpdateDto = new TradeUpdateDto();
        tradeUpdateDto.setId(id);
        tradeUpdateDto.setAccount(account);
        tradeUpdateDto.setType(type);
        tradeUpdateDto.setBuyQuantity(buyQuantity);

        //when & then
        mockMvc.perform(post("/trade/update/{id}", id)
                        .with(csrf())
                        .param("buyQuantity", String.valueOf(buyQuantity))
                        .param("account", account)
                        .param("type", type))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeHasFieldErrors("trade", "buyQuantity", "account", "type"));

        verify(tradeService, times(0)).handleEntityUpdate(tradeUpdateDto);
    }
}