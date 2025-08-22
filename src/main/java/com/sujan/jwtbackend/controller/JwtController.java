package com.sujan.jwtbackend.controller;

import com.sujan.jwtbackend.dto.JwtRequest;
import com.sujan.jwtbackend.dto.JwtResponse;
import com.sujan.jwtbackend.service.JwtService;
import com.sujan.jwtbackend.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@AllArgsConstructor
public class JwtController {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);

    }
}
