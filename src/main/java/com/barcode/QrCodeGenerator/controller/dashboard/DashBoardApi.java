package com.barcode.QrCodeGenerator.controller.dashboard;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Dashboard")
@RequestMapping(path = "api/dashboard")
public interface DashBoardApi {
  @Operation(description = "Get Statistics")
  @GetMapping
  ResponseEntity<String> getStatistics();
}
