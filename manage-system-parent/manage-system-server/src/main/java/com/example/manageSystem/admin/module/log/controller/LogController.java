package com.example.manageSystem.admin.module.log.controller;

import com.example.manageSystem.admin.model.LogAnnotation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/log")
@LogAnnotation(name="LogController")
public class LogController {
}
