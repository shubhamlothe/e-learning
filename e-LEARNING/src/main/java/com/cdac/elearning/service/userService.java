package com.cdac.elearning.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cdac.elearning.model.User;
import com.cdac.elearning.repository.userRepository;
import com.cdac.elearning.security.JwtProvider;

@Service
public class userService {

	@Autowired
	private userRepository userRepo;
	
	 @Autowired
	   private AuthenticationManager authenticationManager;
	 
	 @Autowired
	 BCryptPasswordEncoder bCryptPasswordEncoder;
	 
	 
	 @Autowired
	  private JwtProvider jwtProvider;
	
	public void createUser(User user)
	{
		userRepo.save(user);
	}
	
	 public AuthenticationResponse login(User loginRequest) {
	        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getPassword(),
	                loginRequest.getPassword()));
	        System.out.print(authenticate);
	        SecurityContextHolder.getContext().setAuthentication(authenticate);
	        String authenticationToken = jwtProvider.generateToken(authenticate);
	        System.out.print("dd");
	        return new AuthenticationResponse(authenticationToken, loginRequest.getEmailId());
	  }
	 
	 	
	 public void updateResetPasswordToken(String token, String email)  {
	        User user = userRepo.findByEmail(email);
	        if (user != null) {
	            user.setReset_password_token(token);
	            userRepo.save(user);
	        }
	    }
	     
	 
	    public User getByResetPasswordToken(String token) {
	        return userRepo.findByResetPasswordToken(token);
	    }
	   
	    
	    public void updatePassword(User user, String newPassword) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedPassword = passwordEncoder.encode(newPassword);
	        user.setPassword(encodedPassword);
	         
	        user.setReset_password_token(null);
	        userRepo.save(user);
	    }

}
