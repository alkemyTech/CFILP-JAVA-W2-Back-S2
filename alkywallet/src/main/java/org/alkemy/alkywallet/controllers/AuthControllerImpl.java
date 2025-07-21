package org.alkemy.alkywallet.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.alkemy.alkywallet.controllers.dto.AuthCreateRequest;
import org.alkemy.alkywallet.controllers.dto.AuthLoginRequest;
import org.alkemy.alkywallet.controllers.dto.AuthResponse;
import org.alkemy.alkywallet.services.UserDetailServiceImpl;
import org.alkemy.alkywallet.swagger.save.ApiResponseCrear;
import org.alkemy.alkywallet.swagger.save.ApiResponseUsuarioLogin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "AuthController", description = "Register & Login controller for users")
public class AuthControllerImpl {

    private final UserDetailServiceImpl userDetailService;

    @ApiResponseUsuarioLogin
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest request) {
        return new ResponseEntity<>(this.userDetailService.loginUser(request), HttpStatus.OK);
    }


    @ApiResponseCrear
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthCreateRequest request) {
        return new ResponseEntity<>(userDetailService.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/renew")
    public ResponseEntity<AuthResponse> renew(@RequestHeader("Authorization") String authHeader) {
        // Extrae el token del header, típicamente con el prefijo "Bearer "
        String token = authHeader.replace("Bearer ", "");
        return new ResponseEntity<>(userDetailService.renewToken(token), HttpStatus.OK);
    }


}
