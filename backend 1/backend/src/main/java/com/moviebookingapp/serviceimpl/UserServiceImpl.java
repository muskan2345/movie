package com.moviebookingapp.serviceimpl;

import java.security.SecureRandom;

import java.util.*;

import com.moviebookingapp.dto.ForgetPasswordDto;
import com.moviebookingapp.excpetion.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.moviebookingapp.model.User;
import com.moviebookingapp.repository.UserRepository;
import com.moviebookingapp.service.UserService;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {


	@Autowired
	UserRepository userRepository;
	int strength = 10;
	BCryptPasswordEncoder bCryptPasswordEncoder =
			new BCryptPasswordEncoder(strength, new SecureRandom());


	@Override
	public ForgetPasswordDto forgetPassword(String userName, ForgetPasswordDto forgetPasswordDto) throws LoginException {

		Optional<User> user = userRepository.findByUserName(userName);

		if(user.isPresent()){

			if(forgetPasswordDto.getSecurityQuestion() == user.get().getSecurityQuestion()){

				if(forgetPasswordDto.getSecurityAnswer().equalsIgnoreCase(user.get().getSecurityAnswer())){

					user.get().setPassword(bCryptPasswordEncoder.encode(forgetPasswordDto.getPassword()));
					userRepository.save(user.get());

					return  forgetPasswordDto;

				}
				throw new LoginException("Security answer is wrong");
			}

			throw new LoginException("Security question selected is wrong");
		}

		throw new LoginException("User not found!!");
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		userRepository.findAll().forEach(e -> users.add(e));
		return users;
	}

	@Override
	public User createUser(User user) throws UsernameNotFoundException  {
		
		if (userRepository.findByUserName(user.getUserName()).isPresent()) {
			throw new UsernameNotFoundException("User already exist!!");
		}else {
			if(userRepository.findByEmail(user.getEmail()).isPresent()){
				throw new UsernameNotFoundException("Email already exist!!");
			}else{
				user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				user.setRoleName("User");
				return userRepository.save(user);
			}

		}

		
		
	}

	@Override
	public User findUser(String username) throws UsernameNotFoundException {
		if (userRepository.findByUserName(username).isPresent()) {
			return userRepository.findByUserName(username).get();
		}
		throw new UsernameNotFoundException("User not found!!");

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (userRepository.findByUserName(username).isPresent()) {
			User user = userRepository.findByUserName(username).get();
			return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthority(user));
		}

		throw new UsernameNotFoundException("User not found!!");
	}

	private Set getAuthority(User user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRoleName()));
		return authorities;
	}



}
