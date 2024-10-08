/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.github.angel.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.angel.dto.CustomerDTO;
import com.github.angel.service.CustomerService;
import com.github.angel.utils.PageRenderUtils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.FieldError;
import java.util.stream.Collectors;

/**
 *
 * @author aguero
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('WRITE')")
    public String getCustomer(final Model model) {
        model.addAttribute("customer", new CustomerDTO());
        return "customer/add-customer";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE')")
    public String createCustomer(@Valid final CustomerDTO customer, final BindingResult result, final Model model,
            final RedirectAttributes attributes) {
        if (result.hasErrors()) {
            // Recopila los errores de validación y los agrega al modelo
            Map<String, Object> errores = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage));
            model.addAttribute("errors", errores);
            model.addAttribute("customer", customer);
            return "customer/add-customer";
        }

        model.addAttribute("customer", customer);
        customerService.save(customer);
        // Añade un mensaje de éxito y redirige a la lista de clientes
        attributes.addFlashAttribute("message", "Customer added successfully!");
        return "redirect:/customer/list";
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('READ')")
    public String listCustomers(@RequestParam(name = "pages", defaultValue = "0") int pages, final Model model) {
        PageRequest pageRequest = PageRequest.of(pages, 5);
        Page<CustomerDTO> customers = customerService.findAllDtosPages(pageRequest);
        PageRenderUtils<CustomerDTO> pageableUtils = new PageRenderUtils<>("/customer/list", customers);
        model.addAttribute("pageRenderUtils", pageableUtils);
        model.addAttribute("customers", customers);
        return "customer/list-customer";
    }

    @GetMapping("/edit/{customerId}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public String updateCustomer(@PathVariable @Min(1) Long customerId, final Model model) {
        CustomerDTO customerDTO = customerService.findById(customerId);
        model.addAttribute("customer", customerDTO);
        return "customer/edit-customer";
    }

    @PostMapping("/edit/{customerId}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public String update(
            @Valid CustomerDTO customerDTO, final BindingResult result,
            final Model model, final RedirectAttributes attributes, @PathVariable @Min(1) Long customerId) {
        if (result.hasErrors()) {
            // Recopila los errores de validación y los agrega al modelo
            Map<String, Object> errores = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            FieldError::getDefaultMessage));
            model.addAttribute("errors", errores);
            model.addAttribute("customer", customerDTO);
            return "customer/edit-customer";
        }
        customerService.update(customerId, customerDTO);
        model.addAttribute("customer", customerDTO);
        attributes.addFlashAttribute("message", "The customer has been updated successfully");

        return "redirect:/customer/list";
    }

    @GetMapping("/delete/{customerId}")
    @PreAuthorize("hasAuthority('DELETE')")
    public String deleteCustomer(@PathVariable(name = "customerId") @Min(1) Long customerId,
            final RedirectAttributes attributes) {
        customerService.delete(customerId);
        attributes.addFlashAttribute("message", "The customer has been deleted successfully");
        return "redirect:/customer/list";
    }

}
