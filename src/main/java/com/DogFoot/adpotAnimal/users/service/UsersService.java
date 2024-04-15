package com.DogFoot.adpotAnimal.users.service;

import com.DogFoot.adpotAnimal.jwt.JwtToken;
import com.DogFoot.adpotAnimal.jwt.JwtTokenProvider;
import com.DogFoot.adpotAnimal.tokenBlack.TokenBlack.TokenType;
import com.DogFoot.adpotAnimal.tokenBlack.TokenBlackService;
import com.DogFoot.adpotAnimal.users.dto.LoginDto;
import com.DogFoot.adpotAnimal.users.dto.SignUpDto;
import com.DogFoot.adpotAnimal.users.dto.UpdateUsersDto;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import com.DogFoot.adpotAnimal.users.dto.UsersTableDto;
import com.DogFoot.adpotAnimal.users.entity.CustomUserDetails;
import com.DogFoot.adpotAnimal.users.entity.Users;
import com.DogFoot.adpotAnimal.users.entity.UsersRole;
import com.DogFoot.adpotAnimal.users.exhandler.InvalidLoginException;
import com.DogFoot.adpotAnimal.users.exhandler.InvalidPasswordException;
import com.DogFoot.adpotAnimal.users.exhandler.InvalidSignUpException;
import com.DogFoot.adpotAnimal.users.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlackService tokenBlackService;

    @Transactional
    public JwtToken login(LoginDto loginDto, HttpServletResponse response) {
        String userId = loginDto.getUserId();
        String password = loginDto.getPassword();

        // userId, password를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userId, password, null);

        // 요청된 Member에 대한 검증 진행
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new InvalidLoginException("아이디나 비밀번호가 잘못되었습니다.");
        }


        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        jwtTokenProvider.storeTokens(response, jwtToken.getAccessToken(),
            jwtToken.getRefreshToken(), false);

        return jwtToken;
    }

    @Transactional
    public UsersDto signUp(SignUpDto signUpDto) {

        // 유효성 체크
        if (usersRepository.existsByUserId(signUpDto.getUserId())) {
            throw new InvalidSignUpException("이미 사용 중인 아이디입니다.");
        } else if (usersRepository.existsByEmail(signUpDto.getEmail())) {
            throw new InvalidSignUpException("이미 사용 중인 이메일입니다.");
        } else if(usersRepository.existsByPhoneNumber(signUpDto.getPhoneNumber())) {
            throw new InvalidSignUpException("이미 전화번호가 등록된 계정이 존재합니다.");
        }

        // 패스워드 검증
        String password = signUpDto.getPassword();
        if (password.length() < 8) {
            throw new InvalidPasswordException("비밀번호는 최소 8자 이상이어야 합니다.");
        } else if (!password.matches("(?=.*[a-z]|[A-Z])(?=.*[~!@#$%^&*+=()_-])(?=.*[0-9]).+$")) {
            throw new InvalidPasswordException("비밀번호는 영문 소/대문자, 숫자, 특수문자를 조합하여 작성해야 합니다.");
        }

        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 멤버 리포지터리에 저장
        Users signUsers = usersRepository.save(
            signUpDto.toEntity(encodedPassword, signUpDto.getUserRole()));

        // 저장된 멤버 엔티티를 dto로 변환
        return signUsers.toDto();
    }

    @Transactional
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 헤더에서 accessToken을 추출하여 유효한 지 검증하고 블랙리스트에 저장한다.
        String accessToken = jwtTokenProvider.resolveToken(request);
        if (jwtTokenProvider.validateToken(accessToken)) {
            Instant expiredTimeInstant = jwtTokenProvider.getExpirationDate(accessToken)
                .toInstant();
            LocalDateTime expiredTime = expiredTimeInstant.atZone(ZoneId.systemDefault())
                .toLocalDateTime();
            tokenBlackService.saveTokenToBlacklist(accessToken, TokenType.ACCESS, expiredTime);
        }

        // 쿠키에서 refresh 토큰을 추출하여 유효한 지 검증하여 블랙리스트에 저장한다.
        String refreshToken = jwtTokenProvider.getRefreshToken(request);
        if (jwtTokenProvider.validateToken(refreshToken)) {

            Instant expiredTimeInstant = jwtTokenProvider.getExpirationDate(refreshToken)
                .toInstant();
            LocalDateTime expiredTime = expiredTimeInstant.atZone(ZoneId.systemDefault())
                .toLocalDateTime();
            tokenBlackService.saveTokenToBlacklist(refreshToken, TokenType.REFRESH, expiredTime);
        }

        // 헤더와 쿠키에서 토큰 삭제
        jwtTokenProvider.deleteStoreTokens(response);

        return ResponseEntity.ok("로그아웃 완료");
    }

    @Transactional
    public UsersDto update(long id, UpdateUsersDto updateDto, HttpServletResponse response) {
        //해당 멤버 가져오기
        Users updateUsers = usersRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND));
        String userId = updateUsers.getUserId();

        // 유효성 체크
        if (usersRepository.existsByEmail(updateDto.getEmail()) && (!updateUsers.getEmail()
            .equals(updateDto.getEmail()))) {
            throw new InvalidSignUpException("이미 사용 중인 이메일입니다.");
        }else if(usersRepository.existsByPhoneNumber(updateDto.getPhoneNumber()) && (!updateUsers.getPhoneNumber()
            .equals(updateDto.getPhoneNumber()))) {
            throw new InvalidSignUpException("이미 전화번호가 등록된 계정이 존재합니다.");
        }

        // 패스워드 검증
        String password = updateDto.getPassword();
        if (password.length() < 8) {
            throw new InvalidPasswordException("비밀번호는 최소 8자 이상이어야 합니다.");
        } else if (!password.matches("(?=.*[a-z]|[A-Z])(?=.*[~!@#$%^&*+=()_-])(?=.*[0-9]).+$")) {
            throw new InvalidPasswordException("비밀번호는 영문 소/대문자, 숫자, 특수문자를 조합하여 작성해야 합니다.");
        }

        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 멤버 리포지터리 업데이트
        Users users = updateDto.toEntity(userId, encodedPassword, UsersRole.USER);
        updateUsers.updateUsers(users);
        usersRepository.save(updateUsers);

        // jwt 토큰 새로 발급
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            updateUsers.getUserId(), updateDto.getPassword(), null);

        // Member에 대한 검증 진행
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new InvalidLoginException("아이디나 비밀번호가 잘못되었습니다.");
        }

        // 인증 정보를 기반으로 jwt 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        jwtTokenProvider.storeTokens(response, jwtToken.getAccessToken(),
            jwtToken.getRefreshToken(), false);

        // 저장된 멤버 엔티티를 dto로 반환
        return updateUsers.toDto();
    }

    @Transactional
    public ResponseEntity<String> deleteUsers(long id) {
        //멤버 삭제
        usersRepository.deleteById(id);

        return ResponseEntity.ok("삭제 완료");
    }

    public Users getUsers() {
        Authentication authentication;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("로그인한 상태가 아닙니다.");
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users users = userDetails.getUser();

        return users;
    }

    @Transactional(readOnly = true)
    public Page<UsersTableDto> getUserTable(Pageable pageable) {
        Page<Users> usersPage = usersRepository.findAll(pageable);
        return usersPage.map(Users::toTableDto);
    }

    @Transactional(readOnly = true)
    public long getUsersCount() {
        return usersRepository.count();
    }

    @Transactional(readOnly = true)
    public Users findUserById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
