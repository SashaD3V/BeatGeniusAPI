package com.WeAre.BeatGenius.services.beat.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.WeAre.BeatGenius.api.dto.requests.beat.CreateBeatRequest;
import com.WeAre.BeatGenius.api.dto.responses.beat.BeatResponse;
import com.WeAre.BeatGenius.domain.entities.License;
import com.WeAre.BeatGenius.domain.entities.User;
import com.WeAre.BeatGenius.domain.entities.beat.Beat;
import com.WeAre.BeatGenius.domain.entities.beat.BeatCredit;
import com.WeAre.BeatGenius.domain.enums.Genre;
import com.WeAre.BeatGenius.domain.enums.UserRole;
import com.WeAre.BeatGenius.domain.mappers.beat.BeatMapper;
import com.WeAre.BeatGenius.domain.repositories.UserRepository;
import com.WeAre.BeatGenius.domain.repositories.beat.BeatCreditRepository;
import com.WeAre.BeatGenius.domain.repositories.beat.BeatRepository;
import com.WeAre.BeatGenius.services.marketplace.interfaces.LicenseService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class BeatServiceImplTest {

  @Mock private BeatRepository beatRepository;
  @Mock private BeatMapper beatMapper;
  @Mock private UserRepository userRepository;
  @Mock private LicenseService licenseService;
  @Mock private BeatCreditRepository beatCreditRepository;

  @InjectMocks private BeatServiceImpl beatService;

  @Test
  void getCurrentProducer_ShouldReturnProducer() {
    // Setup
    User mockProducer = new User();
    mockProducer.setId(1L);
    mockProducer.setRole(UserRole.PRODUCER);

    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(mockProducer);
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockProducer));

    // Test
    User result = beatService.getCurrentProducer();

    // Verify
    assertNotNull(result);
    assertEquals(mockProducer.getId(), result.getId());
    verify(userRepository).findById(1L);
  }

  @Test
  void createAndSaveBeat_ShouldCreateAndReturnBeat() {
    // Setup
    User mockProducer = new User();
    mockProducer.setId(1L);
    mockProducer.setRole(UserRole.PRODUCER);

    Beat mockBeat = new Beat();
    mockBeat.setId(1L);
    mockBeat.setProducer(mockProducer);

    CreateBeatRequest mockRequest =
        CreateBeatRequest.builder()
            .title("Test Title")
            .audioUrl("test-audio-url")
            .genre(Genre.TRAP)
            .licenses(new ArrayList<>())
            .build();

    when(beatMapper.toEntity(mockRequest)).thenReturn(mockBeat);
    when(beatRepository.save(any(Beat.class))).thenReturn(mockBeat);

    // Test
    Beat result = beatService.createAndSaveBeat(mockRequest, mockProducer);

    // Verify
    assertNotNull(result);
    assertEquals(mockBeat.getId(), result.getId());
    assertEquals(mockProducer, result.getProducer());
    verify(beatRepository).save(any(Beat.class));
    verify(beatRepository).flush();
  }

  @Test
  void addStandardLicenses_ShouldAddThreeLicenses() {
    // Setup
    Beat mockBeat = new Beat();
    mockBeat.setId(1L);
    License mockLicense = mock(License.class);
    when(licenseService.createStandardLicense(any(), any())).thenReturn(mockLicense);

    // Test
    beatService.addStandardLicenses(mockBeat);

    // Verify
    verify(licenseService, times(3)).createStandardLicense(any(), any());
    verify(beatRepository).save(mockBeat);
    assertEquals(3, mockBeat.getLicenses().size());
  }

  @Test
  void create_ShouldCreateCompleteBeadWithAllComponents() {
    // Setup
    User mockProducer = new User();
    mockProducer.setId(1L);
    mockProducer.setRole(UserRole.PRODUCER);

    Beat mockBeat = new Beat();
    mockBeat.setId(1L);
    mockBeat.setProducer(mockProducer);

    CreateBeatRequest mockRequest =
        CreateBeatRequest.builder()
            .title("Test Title")
            .audioUrl("test-audio-url")
            .genre(Genre.TRAP)
            .licenses(new ArrayList<>())
            .build();

    BeatResponse mockResponse = new BeatResponse();

    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);
    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(mockProducer);
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockProducer));
    when(beatMapper.toEntity(any())).thenReturn(mockBeat);
    when(beatRepository.save(any())).thenReturn(mockBeat);
    when(beatRepository.findById(any())).thenReturn(Optional.of(mockBeat));
    when(beatMapper.toDto(any())).thenReturn(mockResponse);
    when(licenseService.createStandardLicense(any(), any())).thenReturn(mock(License.class));

    // Test
    BeatResponse result = beatService.create(mockRequest);

    // Verify
    assertNotNull(result);
    verify(beatRepository, times(2)).save(any(Beat.class));
    verify(beatCreditRepository).save(any(BeatCredit.class));
    verify(beatMapper).toDto(any(Beat.class));
  }
}
