package com.troublemaker.order.controller;

import com.troublemaker.order.entity.Booker;
import com.troublemaker.order.service.FieldSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Troublemaker
 * @date 2022- 04 29 12:18
 */
@RestController
@CrossOrigin
public class OrderController {

    private FieldSelectionService selectionService;

    @Autowired
    public void setSelectionService(FieldSelectionService selectionService) {
        this.selectionService = selectionService;
    }

    @PostMapping("/addBookerInfo")
    public Integer addBooker(@RequestBody Booker booker) {
        return  selectionService.addBooker(booker);
    }
}
