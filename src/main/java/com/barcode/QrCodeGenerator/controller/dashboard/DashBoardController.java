package com.barcode.QrCodeGenerator.controller.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class DashBoardController implements DashBoardApi {
  @Override
  public ResponseEntity<String> getStatistics() {
    return ResponseEntity.ok("Welcome from DashBoard");
  }
}
