package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.rule_name.RuleNameCreateDto;
import com.nnk.springboot.dto.rule_name.RuleNameDto;
import com.nnk.springboot.dto.rule_name.RuleNameUpdateDto;
import com.nnk.springboot.service.RuleNameService;
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

@WebMvcTest(controllers = RuleNameController.class)
@AutoConfigureMockMvc
class RuleNameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should Return Add View With Empty Model")
    void shouldReturnAddViewWithEmptyModel() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Rule list when valid RuleNameCreateDto")
    void shouldRedirectToRuleListWhenValidRuleNameCreateDto() throws Exception {
        //given
        final String json = "json";
        final String template = "template";
        final String sqlStr = "sqlStr";
        final String sqlPart = "sqlPart";
        final String description = "description";
        final String name = "name";

        // when & then
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("json", json)
                        .param("template", template)
                        .param("sqlStr", sqlStr)
                        .param("sqlPart", sqlPart)
                        .param("description", description)
                        .param("name", name))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).handleEntityCreation(any(RuleNameCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Rule add form if parameters are invalid")
    void shouldRedirectToRuleAddFormIfParameterIsInvalid() throws Exception {
        //given
        final String json = "json";
        final String template = "template";
        final String sqlStr = "sqlStr";
        final String sqlPart = "sqlPart";
        final String description = "";
        final String name = "";

        // when & then
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("json", json)
                        .param("template", template)
                        .param("sqlStr", sqlStr)
                        .param("sqlPart", sqlPart)
                        .param("description", description)
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeHasFieldErrors("ruleName", "description", "name"));

        verify(ruleNameService, times(0)).handleEntityCreation(any(RuleNameCreateDto.class));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Rule list after deletion by id")
    void shouldRedirectToRuleListAfterDeletionById() throws Exception {
        //given
        final int id = 1;

        //when & then
        mockMvc.perform(get("/ruleName/delete/" + id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(flash().attribute("successMessage", "ruleName deleted successfully"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Rule list after throw EntityNotFoundException")
    void shouldRedirectToRuleListAfterThrowEntityNotFoundException() throws Exception {
        //given
        final int id = 1;
        doThrow(new EntityNotFoundException("ruleName not found")).when(ruleNameService).handleEntityDeletion(id);

        //when & then
        mockMvc.perform(get("/ruleName/delete/" + id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"))
                .andExpect(flash().attribute("errorMessage", "ruleName not found"));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Rule List after retrieving all Rules")
    void shouldRedirectToRuleListAfterRetrievingAllRules() throws Exception {
        //given
        final int id = 1;
        final String json = "json";
        final String template = "template";
        final String sqlStr = "sqlStr";
        final String sqlPart = "sqlPart";
        final String description = "description";
        final String name = "name";
        final RuleNameDto ruleNameDto = RuleNameDto.builder()
                .id(id)
                .description(description)
                .name(name)
                .json(json)
                .template(template)
                .sqlStr(sqlStr)
                .sqlPart(sqlPart)
                .build();

        final List<RuleNameDto> ruleNameDtos = List.of(ruleNameDto);
        given(ruleNameService.findAllEntity()).willReturn(ruleNameDtos);

        //when & then
        mockMvc.perform(get("/ruleName/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attribute("ruleNames", ruleNameDtos));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should return Rule/update form")
    void shouldReturnUpdateRuleForm() throws Exception {
        //given
        final int id = 1;
        final String json = "json";
        final String template = "template";
        final String sqlStr = "sqlStr";
        final String sqlPart = "sqlPart";
        final String description = "description";
        final String name = "name";
        final RuleNameUpdateDto ruleNameUpdateDto = RuleNameUpdateDto.builder()
                .id(id)
                .description(description)
                .name(name)
                .json(json)
                .template(template)
                .sqlStr(sqlStr)
                .sqlPart(sqlPart)
                .build();
        given(ruleNameService.getEntityUpdateDto(id)).willReturn(ruleNameUpdateDto);

        //when
        mockMvc.perform(get("/ruleName/update/" + id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attribute("ruleName", ruleNameUpdateDto));
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to RuleList after successfully update")
    void shouldRedirectToRuleListAfterSuccessfulUpdate() throws Exception {
        //given
        final int id = 1;
        final String json = "json";
        final String template = "template";
        final String sqlStr = "sqlStr";
        final String sqlPart = "sqlPart";
        final String description = "description";
        final String name = "name";
        final RuleNameUpdateDto ruleNameUpdateDto = RuleNameUpdateDto.builder()
                .id(id)
                .description(description)
                .name(name)
                .json(json)
                .template(template)
                .sqlStr(sqlStr)
                .sqlPart(sqlPart)
                .build();

        // when & then
        mockMvc.perform(post("/ruleName/update/{id}", id)
                        .with(csrf())
                        .param("json", json)
                        .param("template", template)
                        .param("sqlStr", sqlStr)
                        .param("sqlPart", sqlPart)
                        .param("description", description)
                        .param("name", name))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).handleEntityUpdate(ruleNameUpdateDto);
    }

    @Test
    @WithMockUser(username = "Admin", roles = "ADMIN")
    @DisplayName("should redirect to Rule update form if parameters are invalids")
    void shouldRedirectToRuleUpdateFormIfParametersAreInvalid() throws Exception {
        //given
        final int id = 1;
        final String json = "json";
        final String template = "template";
        final String sqlStr = "sqlStr";
        final String sqlPart = "sqlPart";
        final String description = "";
        final String name = "";
        final RuleNameUpdateDto ruleNameUpdateDto = RuleNameUpdateDto.builder()
                .id(id)
                .description(description)
                .name(name)
                .json(json)
                .template(template)
                .sqlStr(sqlStr)
                .sqlPart(sqlPart)
                .build();

        //when & then
        mockMvc.perform(post("/ruleName/update/{id}", id)
                        .with(csrf())
                        .param("json", json)
                        .param("template", template)
                        .param("sqlStr", sqlStr)
                        .param("sqlPart", sqlPart)
                        .param("description", description)
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeHasFieldErrors("ruleName", "description", "name"));

        verify(ruleNameService, times(0)).handleEntityUpdate(ruleNameUpdateDto);

    }

}