package com.safalifter.jobservice.controller;

import com.safalifter.jobservice.dto.AdvertDto;
import com.safalifter.jobservice.enums.Advertiser;
import com.safalifter.jobservice.request.advert.AdvertCreateRequest;
import com.safalifter.jobservice.request.advert.AdvertUpdateRequest;
import com.safalifter.jobservice.service.AdvertService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/job-service/advert")
@RequiredArgsConstructor
@Tag(name = "Advert Controller", description = "Advert management endpoints")
public class AdvertController {
    private final AdvertService advertService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    @Operation(summary = "Create advert")
    public ResponseEntity<AdvertDto> createAdvert(@Valid @RequestPart AdvertCreateRequest request,
                                                  @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(advertService.createAdvert(request, file), AdvertDto.class));
    }

    @GetMapping("/getAll")
    @Operation(summary = "List adverts")
    public ResponseEntity<List<AdvertDto>> getAll() {
        return ResponseEntity.ok(advertService.getAll().stream()
                .map(advert -> modelMapper.map(advert, AdvertDto.class)).toList());
    }

    @GetMapping("/getAdvertById/{id}")
    @Operation(summary = "Get advert by id")
    public ResponseEntity<AdvertDto> getAdvertById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(advertService.getAdvertById(id), AdvertDto.class));
    }

    @GetMapping("/getAdvertsByUserId/{id}")
    @Operation(summary = "Get adverts by user id")
    public ResponseEntity<List<AdvertDto>> getAdvertsByUserId(@PathVariable String id,
                                                              @RequestParam Advertiser type) {
        return ResponseEntity.ok(advertService.getAdvertsByUserId(id, type).stream()
                .map(advert -> modelMapper.map(advert, AdvertDto.class)).toList());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @advertService.isOwner(#request.id, principal)")
    @Operation(summary = "Update advert")
    public ResponseEntity<AdvertDto> updateAdvertById(@Valid @RequestPart AdvertUpdateRequest request,
                                                      @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.ok(modelMapper.map(advertService.updateAdvertById(request, file), AdvertDto.class));
    }

    @DeleteMapping("/deleteAdvertById/{id}")
    @PreAuthorize("hasRole('ADMIN') or @advertService.isOwner(#id, principal)")
    @Operation(summary = "Delete advert")
    public ResponseEntity<Void> deleteAdvertById(@PathVariable String id) {
        advertService.deleteAdvertById(id);
        return ResponseEntity.ok().build();
    }
}
