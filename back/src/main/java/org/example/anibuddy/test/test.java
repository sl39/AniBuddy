package org.example.anibuddy.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class test {
	
	@PostMapping("/connectionTwo")
	public ResponseEntity<String> testConnection(@RequestBody String message) {
        return ResponseEntity.ok("Received: " + message);
	}
}
