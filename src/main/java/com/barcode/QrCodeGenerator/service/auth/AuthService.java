package com.barcode.QrCodeGenerator.service.auth;

import com.barcode.QrCodeGenerator.domain.request.LoginRequest;
import com.barcode.QrCodeGenerator.domain.request.RegistrationRequest;
import com.barcode.QrCodeGenerator.domain.response.LoginResponse;
import com.barcode.QrCodeGenerator.jpa.entity.TokenEntity;
import com.barcode.QrCodeGenerator.jpa.entity.UserEntity;
import com.barcode.QrCodeGenerator.jpa.enumtype.TokenType;
import com.barcode.QrCodeGenerator.jpa.repository.TokenRepository;
import com.barcode.QrCodeGenerator.jpa.repository.UserRepository;
import com.barcode.QrCodeGenerator.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public void register(RegistrationRequest request) {
    if (!request.getPassword1().equals(request.getPassword2()))
      throw new InternalAuthenticationServiceException("Password 1 and 2 doesn't match");
    request.setPassword1(passwordEncoder.encode(request.getPassword1()));
    var user = UserMapper.domainToEntity(request);
    userRepository.save(user);
  }

  public LoginResponse login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
    var user = userRepository.findByUserNameOrPhoneNumber(request.getUserName());
    var userDetails =
        user.map(UserDetailService::new)
            .orElseThrow(() -> new UsernameNotFoundException("No user found with this email"));
    this.revokeUserToken(user.get());
    var jwtToken = jwtService.generateAccessToken(userDetails);
    var jwtRefreshToken = jwtService.generateRefreshToken(userDetails);
    this.saveUserToken(jwtToken, user.get());
    return new LoginResponse().setAccessToken(jwtToken).setRefreshToken(jwtRefreshToken);
  }

  public void saveUserToken(String jwtToken, UserEntity user) {
    var tokenEntity =
        new TokenEntity()
            .setToken(jwtToken)
            .setTokenType(TokenType.BEARER)
            .setUser(user)
            .setRevoked(false)
            .setExpired(false);
    tokenRepository.save(tokenEntity);
  }

  public void revokeUserToken(UserEntity user) {
    var allValidUserToken = tokenRepository.findValidTokenByUser(user.getId());
    if (allValidUserToken.isEmpty()) return;
    allValidUserToken.forEach(
        token -> {
          token.setExpired(true).setRevoked(true);
        });
    tokenRepository.saveAll(allValidUserToken);
  }

  public void resetPassword() {}

  public void generateRefreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    final String refreshToken = authHeader.substring(7);
    final String userEmail = jwtService.extractUserName(refreshToken);
    if (userEmail != null) {
      var user = userRepository.findByUserNameOrPhoneNumber(userEmail);
      UserDetails userDetails =
          user.map(UserDetailService::new)
              .orElseThrow(
                  () -> new UsernameNotFoundException("User not found with this username"));
      if (jwtService.isTokenValid(refreshToken, userDetails)) {
        var accessToken = jwtService.generateAccessToken(userDetails);
        this.revokeUserToken(user.get());
        this.saveUserToken(accessToken, user.get());
        LoginResponse loginResponse =
            new LoginResponse().setAccessToken(accessToken).setRefreshToken(refreshToken);
        new ObjectMapper().writeValue(response.getOutputStream(), loginResponse);
      }
    }
  }
}
