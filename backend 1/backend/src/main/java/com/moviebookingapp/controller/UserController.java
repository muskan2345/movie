package com.moviebookingapp.controller;

import java.util.List;
import java.util.Optional;

import com.moviebookingapp.dto.*;
import com.moviebookingapp.excpetion.LoginException;
import com.moviebookingapp.repository.UserRepository;
import com.moviebookingapp.util.JwtUtil;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.moviebookingapp.model.User;
import com.moviebookingapp.service.UserService;

@RestController
@CrossOrigin
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/v1.0/moviebooking/")
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	ValidateStatusDto validateStatus;

	@Autowired
	private AuthenticationManager authenticationManager;

//	@GetMapping("/welcome")
//	public String welcome() {
//		return "Welcome this endpoint is secure";
//	}
//
//	@GetMapping({ "/forAdmin" })
//	@PreAuthorize("hasRole('Admin')")
//	public String forAdmin() {
//		return "This URL is only accessible to the admin";
//	}
//
//	@GetMapping({ "/forUser" })
//	@PreAuthorize("hasRole('User') || hasRole('Admin')")
//	public String forUser() {
//		return "This URL is only accessible to the user and admin";
//	}

	@PostMapping("/register")
	 public ResponseEntity<User> createUser(@RequestBody User user)  {
	 return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
	 }


	@GetMapping("/forgot/{userName}")
	public  ResponseEntity<User> forgetPassword(@PathVariable String userName) throws LoginException {
		return new ResponseEntity<User>(userService.findUser(userName), HttpStatus.CREATED);
	}

	@PostMapping("/updatePassword/{userName}")
	public  ResponseEntity<ForgetPasswordDto> forgetPassword(@PathVariable String userName,@RequestBody ForgetPasswordDto forgetPasswordDto) throws LoginException {
		return new ResponseEntity<ForgetPasswordDto>(userService.forgetPassword(userName,forgetPasswordDto), HttpStatus.CREATED);
	}



	@PostMapping("/login")
	public ResponseEntity<Object> createAuthorizationToken(@RequestBody AuthRequestDto authReq)
			throws AuthenticationException, LoginException {
		try {

			final Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authReq.getUsername(),
							authReq.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			final UserDetails userDetails = userService.loadUserByUsername(authReq.getUsername());
			Optional<User> user = userRepository.findByUserName(userDetails.getUsername());
			if(user.isPresent()){
				return new ResponseEntity<>(
						new AuthResponse(new UserResponseDTO(user.get().getUserId(),user.get().getUserName(),user.get().getRoleName(), user.get().getFirstName(), user.get().getLastName()), jwtTokenUtil.generateToken(authentication),
								jwtTokenUtil.getCurrentTime(), jwtTokenUtil.getExpirationTime()),
						HttpStatus.OK);
			}else{
				throw new LoginException("User not found");
			}


		} catch (Exception e) {
			throw new LoginException("Invalid Username or Password");
		}

	}



}
