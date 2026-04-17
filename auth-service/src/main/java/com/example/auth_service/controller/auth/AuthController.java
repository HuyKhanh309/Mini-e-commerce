package com.example.auth_service.controller.auth;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_service.config.CustomUserDetailsService;
import com.example.auth_service.config.JwtService;
import com.example.auth_service.config.Login;
import com.example.auth_service.controller.ApiResponse;
import com.example.auth_service.model.dto.user.LoginDTO;
import com.example.auth_service.model.dto.user.UserDTO;
import com.example.auth_service.model.entity.user.User;
import com.example.auth_service.model.enums.Roles;
import com.example.auth_service.service.user.UserService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	
    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;
	
	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginDTO dto) {
		ApiResponse resp = new ApiResponse(null);
		
		try {
            // 1. Xác thực user/password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		dto.getUsername(),
                    		dto.getPassword()
                    )
            );

            // 2. Lấy thông tin user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
            Login u = (Login) authentication.getPrincipal();

            // 3. Generate JWT token
            String jwt = jwtService.generateToken(userDetails);
            
            // 4. Trả token cho client
            resp.code(200).message("Login success");
            resp.setData("accessToken", jwt);
            resp.setData("expiredAt", jwtService.extractClaim(jwt, Claims::getExpiration));
            resp.setData("user", u.toDTO());

        } catch (BadCredentialsException e) {
        	resp.code(400).message("Bad credentials");
        }
		
		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> register(@Valid @RequestBody LoginDTO dto) {
		ApiResponse resp = new ApiResponse(null);
		
		try {
			// 1. Tạo tài khoản user mới
			UserDTO userDTO = new UserDTO(dto.getUsername(), dto.getPassword(), Roles.USER.getRole());
			userDTO.setUpdateBy(dto.getUsername());

			User newUser = userService.create(userDTO.toEntity());

            // 2. Lấy thông tin user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getUsername());

            // 3. Generate JWT token
            String jwt = jwtService.generateToken(userDetails);
            
            // 4. Trả token cho client
            resp.code(200).message("Register success");
            resp.setData("accessToken", jwt);
            resp.setData("expiredAt", jwtService.extractClaim(jwt, Claims::getExpiration));
            resp.setData("user", newUser.toDTO());

        } catch (RuntimeException e) {
        	resp.code(400).message(e.getMessage());
        }
		
		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}
	
}
