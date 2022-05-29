package com.troublemaker.clockin.controller;

import com.troublemaker.clockin.service.ClockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Troublemaker
 * @date 2022- 04 28 21:25
 */
@CrossOrigin
@RestController
public class UserController {

    private ClockInService service;

    @Autowired
    public void setService(ClockInService service) {
        this.service = service;
    }

    @PostMapping("/addClockInfo")
    public Integer addUserInfo(@RequestBody Map<String,String> map) {
        return service.addSchoolClockInfo(map);
    }
}

