package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.curve.CurvePointCreateDto;
import com.nnk.springboot.dto.curve.CurvePointDto;
import com.nnk.springboot.dto.curve.CurvePointUpdateDto;
import com.nnk.springboot.service.impl.CurvePointService;
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

@WebMvcTest(controllers = CurveController.class)
@AutoConfigureMockMvc
class CurveControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointService curvePointService;

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    void displayAddCurvePointForm_shouldReturnAddViewWithEmptyModel() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to curvePoint list when valid curvePointCreateDto")
    void redirectToCurvePointListWhenRatingCreateDtoValid() throws Exception {
        //given
        final double term = 12.5;
        final double value = 20.3;
        final int curveId = 10;

        // when & then
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("term", String.valueOf(term))
                        .param("value", String.valueOf(value))
                        .param("curveId", String.valueOf(curveId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).handleEntityCreation(any(CurvePointCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to curvePoint add form if parameters are invalid")
    void redirectToCurvePointAddFormIfParametersIsInvalid() throws Exception {
        //given
        final double term = 300;
        final double value = 300;
        final int curveId = 300;

        // when & then
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("term", String.valueOf(term))
                        .param("value", String.valueOf(value))
                        .param("curveId", String.valueOf(curveId)))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "term", "value", "curveId"));

        verify(curvePointService, times(0)).handleEntityCreation(any(CurvePointCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to curvePoint list after deletion by id")
    void redirectToCurvePointListAfterDeletionById() throws Exception {
        //given
        final int curvePointId = 1;

        //when & then
        mockMvc.perform(get("/curvePoint/delete/" + curvePointId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(flash().attribute("successMessage", "CurvePoint deleted successfully"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to curvePoint list after throw EntityNotFoundException")
    void redirectToCurvePointListAfterThrowEntityNotFoundException() throws Exception {
        //given
        final int curvePointId = 1;
        doThrow(new EntityNotFoundException("curvePoint not found")).when(curvePointService).handleEntityDeletion(curvePointId);

        //when & then
        mockMvc.perform(get("/curvePoint/delete/" + curvePointId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"))
                .andExpect(flash().attribute("errorMessage", "curvePoint not found"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to curvePoint List after retrieving all curvePoints")
    void redirectToCurvePointListAfterRetrievingAllCurvePoints() throws Exception {
        //given
        final int id = 1;
        final double term = 12.5;
        final double value = 20.3;
        final int curveId = 10;
        final CurvePointDto curvePointDto = new CurvePointDto();
        curvePointDto.setId(id);
        curvePointDto.setCurveId(curveId);
        curvePointDto.setTerm(BigDecimal.valueOf(term));
        curvePointDto.setValue(BigDecimal.valueOf(value));

        final List<CurvePointDto> curvePointDtoList = List.of(curvePointDto);
        given(curvePointService.findAllEntity()).willReturn(curvePointDtoList);

        //when & then
        mockMvc.perform(get("/curvePoint/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attribute("curvePoints", curvePointDtoList));

    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should return curvePoint/update form")
    void redirectToCurvePointUpdateForm() throws Exception {
        //given
        final int id = 1;
        final double term = 12.5;
        final double value = 20.3;
        final int curveId = 10;
        final CurvePointUpdateDto curvePointUpdateDto = new CurvePointUpdateDto();
        curvePointUpdateDto.setId(id);
        curvePointUpdateDto.setCurveId(curveId);
        curvePointUpdateDto.setTerm(term);
        curvePointUpdateDto.setValue(value);

        given(curvePointService.getEntityUpdateDto(id)).willReturn(curvePointUpdateDto);

        //when
        mockMvc.perform(get("/curvePoint/update/" + id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attribute("curvePoint", curvePointUpdateDto));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to curvePointList after successfully update")
    void redirectToCurvePointListAfterSuccessfulUpdate() throws Exception {
        //given
        final int id = 1;
        final double term = 12.5;
        final double value = 20.3;
        final int curveId = 10;
        final CurvePointUpdateDto curvePointUpdateDto = new CurvePointUpdateDto();
        curvePointUpdateDto.setId(id);
        curvePointUpdateDto.setCurveId(curveId);
        curvePointUpdateDto.setTerm(term);
        curvePointUpdateDto.setValue(value);

        // when & then
        mockMvc.perform(post("/curvePoint/update/{id}", id)
                        .with(csrf())
                        .param("term", String.valueOf(term))
                        .param("value", String.valueOf(value))
                        .param("curveId", String.valueOf(curveId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).handleEntityUpdate(curvePointUpdateDto);
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to curvePoint update form if parameters are invalids")
    void redirectToCurvePointUpdateFormInvalidParameters() throws Exception {
        //given
        final int id = 1;
        final double term = 300;
        final double value = 300;
        final int curveId = 300;
        final CurvePointUpdateDto curvePointUpdateDto = new CurvePointUpdateDto();
        curvePointUpdateDto.setId(id);
        curvePointUpdateDto.setCurveId(curveId);
        curvePointUpdateDto.setTerm(term);
        curvePointUpdateDto.setValue(value);

        //when & then
        mockMvc.perform(post("/curvePoint/update/{id}", id)
                        .with(csrf())
                        .param("term", String.valueOf(term))
                        .param("value", String.valueOf(value))
                        .param("curveId", String.valueOf(curveId)))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "term", "value", "curveId"));

        verify(curvePointService, times(0)).handleEntityUpdate(curvePointUpdateDto);
    }
}