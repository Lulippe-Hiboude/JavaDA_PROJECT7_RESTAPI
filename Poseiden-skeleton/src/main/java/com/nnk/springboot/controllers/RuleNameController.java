package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.rule_name.RuleNameCreateDto;
import com.nnk.springboot.dto.rule_name.RuleNameDto;
import com.nnk.springboot.dto.rule_name.RuleNameUpdateDto;
import com.nnk.springboot.service.RuleNameService;
import com.nnk.springboot.service.impl.RuleNameServiceImpl;
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
@RequestMapping("/ruleName")
public class RuleNameController extends AbstractEntityController<RuleNameCreateDto, RuleNameDto, RuleNameUpdateDto> {

    private final RuleNameService ruleNameService;

    public RuleNameController(final RuleNameService ruleNameService) {
        super("ruleName", ruleNameService);
        this.ruleNameService = ruleNameService;
    }

    @Override
    public String submitCreateForm(@Valid @ModelAttribute("ruleName") final RuleNameCreateDto ruleNameCreateDto, final BindingResult result) {
        log.info("submit rule name creation form");
        if (result.hasErrors()) {
            return getView(ADD.getValue());
        }
        ruleNameService.handleEntityCreation(ruleNameCreateDto);
        return getRedirectedUrl(LIST.getValue());
    }

    @Override
    public String updateEntity(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("ruleName") final RuleNameUpdateDto ruleNameUpdateDto,
                               final BindingResult result) {

        if (result.hasErrors()) {
            return getView(UPDATE.getValue());
        }
        ruleNameService.handleEntityUpdate(ruleNameUpdateDto);
        return getRedirectedUrl(LIST.getValue());
    }

    @Override
    protected RuleNameCreateDto createEntityDto() {
        return new RuleNameCreateDto();
    }
}
