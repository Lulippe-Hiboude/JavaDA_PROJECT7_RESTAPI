package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.rating.RatingCreateDto;
import com.nnk.springboot.dto.rating.RatingDto;
import com.nnk.springboot.dto.rating.RatingUpdateDto;
import com.nnk.springboot.service.impl.RatingServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RatingController.class)
@AutoConfigureMockMvc
class RatingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingServiceImpl ratingServiceImpl;

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    void displayAddRatingForm_shouldReturnAddViewWithEmptyModel() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to rating list when valid ratingCreateDto")
    void redirectToRatingListWhenRatingCreateDtoValid() throws Exception {
        //given
        final String fitchRating = "fitchRating";
        final String moodysRating = "moodysRating";
        final String sandPRating = "sandPRating";
        final int orderNumber = 1;

        // when & then
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("fitchRating", fitchRating)
                        .param("moodysRating", moodysRating)
                        .param("sandPRating", sandPRating)
                        .param("orderNumber", String.valueOf(orderNumber)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingServiceImpl).handleEntityCreation(any(RatingCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to rating add form if parameters are invalid")
    void redirectToRatingAddFormIfParametersIsInvalid() throws Exception {
        //given
        final String fitchRating = "";
        final String moodysRating = "";
        final String sandPRating = "";
        final int orderNumber = -3;

        // when & then
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("fitchRating", fitchRating)
                        .param("moodysRating", moodysRating)
                        .param("sandPRating", sandPRating)
                        .param("orderNumber", String.valueOf(orderNumber)))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeHasFieldErrors("rating", "fitchRating", "moodysRating", "sandPRating", "orderNumber"));

        verify(ratingServiceImpl, times(0)).handleEntityCreation(any(RatingCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to rating list after deletion by id")
    void redirectToRatingListAfterDeletionById() throws Exception {
        //given
        final int ratingId = 1;

        //when & then
        mockMvc.perform(get("/rating/delete/" + ratingId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attribute("successMessage", "rating deleted successfully"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to rating list after throw EntityNotFoundException")
    void redirectToRatingListAfterThrowEntityNotFoundException() throws Exception {
        //given
        final int ratingId = 1;
        doThrow(new EntityNotFoundException("rating not found")).when(ratingServiceImpl).handleEntityDeletion(ratingId);

        //when & then
        mockMvc.perform(get("/rating/delete/" + ratingId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"))
                .andExpect(flash().attribute("errorMessage", "rating not found"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to rating List after retrieving all ratings")
    void redirectToRatingListAfterRetrievingAllRatings() throws Exception {
        //given
        final int id = 1;
        final String fitchRating = "fitchRating";
        final String moodysRating = "moodysRating";
        final String sandPRating = "sandPRating";
        final int orderNumber = 1;
        final RatingDto ratingDto = new RatingDto();
        ratingDto.setId(id);
        ratingDto.setFitchRating(fitchRating);
        ratingDto.setMoodysRating(moodysRating);
        ratingDto.setSandPRating(sandPRating);
        ratingDto.setOrderNumber(orderNumber);

        final List<RatingDto> ratingDtoList = List.of(ratingDto);
        given(ratingServiceImpl.findAllEntity()).willReturn(ratingDtoList);

        //when & then
        mockMvc.perform(get("/rating/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attribute("ratings", ratingDtoList));

    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should return rating/update form")
    void redirectToRatingUpdateForm() throws Exception {
        //given
        final int id = 1;
        final String fitchRating = "fitchRating";
        final String moodysRating = "moodysRating";
        final String sandPRating = "sandPRating";
        final int orderNumber = 1;
        final RatingUpdateDto ratingUpdateDto = new RatingUpdateDto();
        ratingUpdateDto.setId(id);
        ratingUpdateDto.setFitchRating(fitchRating);
        ratingUpdateDto.setMoodysRating(moodysRating);
        ratingUpdateDto.setSandPRating(sandPRating);
        ratingUpdateDto.setOrderNumber(orderNumber);

        given(ratingServiceImpl.getEntityUpdateDto(id)).willReturn(ratingUpdateDto);

        //when
        mockMvc.perform(get("/rating/update/" + id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attribute("rating", ratingUpdateDto));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to ratingList after successfully update")
    void redirectToRatingListAfterSuccessfulUpdate() throws Exception {
        //given
        final int id = 1;
        final String fitchRating = "fitchRating";
        final String moodysRating = "moodysRating";
        final String sandPRating = "sandPRating";
        final int orderNumber = 1;
        final RatingUpdateDto ratingUpdateDto = new RatingUpdateDto();
        ratingUpdateDto.setId(id);
        ratingUpdateDto.setFitchRating(fitchRating);
        ratingUpdateDto.setMoodysRating(moodysRating);
        ratingUpdateDto.setSandPRating(sandPRating);
        ratingUpdateDto.setOrderNumber(orderNumber);

        // when & then
        mockMvc.perform(post("/rating/update/{id}", id)
                        .with(csrf())
                        .param("fitchRating", fitchRating)
                        .param("moodysRating", moodysRating)
                        .param("sandPRating", sandPRating)
                        .param("orderNumber", String.valueOf(orderNumber)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingServiceImpl).handleEntityUpdate(ratingUpdateDto);
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to rating update form if parameters are invalids")
    void redirectToRatingUpdateFormInvalidParameters() throws Exception {
        //given
        final int id = 1;
        final String fitchRating = "";
        final String moodysRating = "";
        final String sandPRating = "";
        final int orderNumber = -1;
        final RatingUpdateDto ratingUpdateDto = new RatingUpdateDto();
        ratingUpdateDto.setId(id);
        ratingUpdateDto.setFitchRating(fitchRating);
        ratingUpdateDto.setMoodysRating(moodysRating);
        ratingUpdateDto.setSandPRating(sandPRating);
        ratingUpdateDto.setOrderNumber(orderNumber);

        //when & then
        mockMvc.perform(post("/rating/update/{id}", id)
                        .with(csrf())
                        .param("fitchRating", fitchRating)
                        .param("moodysRating", moodysRating)
                        .param("sandPRating", sandPRating)
                        .param("orderNumber", String.valueOf(orderNumber)))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeHasFieldErrors("rating", "fitchRating", "moodysRating", "sandPRating", "orderNumber"));

        verify(ratingServiceImpl, times(0)).handleEntityUpdate(ratingUpdateDto);
    }
}