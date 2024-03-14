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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    var jwtToken = jwtService.generateToken(userDetails);
    var tokenEntity =
        new TokenEntity()
            .setToken(jwtToken)
            .setTokenType(TokenType.BEARER)
            .setUser(user.get())
            .setRevoked(false)
            .setExpired(false);
    tokenRepository.save(tokenEntity);
    return new LoginResponse().setToken(jwtToken);
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
}
