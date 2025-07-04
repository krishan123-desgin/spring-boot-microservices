package com.safalifter.jobservice.controller;

import com.safalifter.jobservice.dto.OfferDto;
import com.safalifter.jobservice.request.offer.MakeAnOfferRequest;
import com.safalifter.jobservice.request.offer.OfferUpdateRequest;
import com.safalifter.jobservice.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/job-service/offer")
@RequiredArgsConstructor
@Tag(name = "Offer Controller", description = "Offer management endpoints")
public class OfferController {
    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @PostMapping("/makeAnOffer")
    @Operation(summary = "Create offer")
    public ResponseEntity<OfferDto> makeAnOffer(@Valid @RequestBody MakeAnOfferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(offerService.makeAnOffer(request), OfferDto.class));
    }

    @GetMapping("/getOfferById/{id}")
    @Operation(summary = "Get offer by id")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(offerService.getOfferById(id), OfferDto.class));
    }

    @GetMapping("/getOffersByUserId/{id}")
    @Operation(summary = "Get offers by user id")
    public ResponseEntity<List<OfferDto>> getOffersByUserId(@PathVariable String id) {
        return ResponseEntity.ok(offerService.getOffersByUserId(id).stream()
                .map(offer -> modelMapper.map(offer, OfferDto.class)).toList());
    }

    @GetMapping("/getOffersByAdvertId/{id}")
    @Operation(summary = "Get offers by advert id")
    public ResponseEntity<List<OfferDto>> getOffersByAdvertId(@PathVariable String id) {
        return ResponseEntity.ok(offerService.getOffersByAdvertId(id).stream()
                .map(offer -> modelMapper.map(offer, OfferDto.class)).toList());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @offerService.isOwner(#request.id, principal)")
    @Operation(summary = "Update offer")
    public ResponseEntity<OfferDto> updateOfferById(@Valid @RequestBody OfferUpdateRequest request) {
        return ResponseEntity.ok(modelMapper.map(offerService.updateOfferById(request), OfferDto.class));
    }

    @DeleteMapping("/deleteOfferById/{id}")
    @PreAuthorize("hasRole('ADMIN') or @offerService.isOwner(#id, principal)")
    @Operation(summary = "Delete offer")
    public ResponseEntity<Void> deleteOfferById(@PathVariable String id) {
        offerService.deleteOfferById(id);
        return ResponseEntity.ok().build();
    }
}
