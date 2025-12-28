package com.salessavvy.adminControllers;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salessavvy.adminServices.AdminBusinessService;

@RestController
@RequestMapping("/admin/business")
public class AdminBusinessController {

	private final AdminBusinessService adminBusinessService;

	public AdminBusinessController(AdminBusinessService adminBusinessService) {
		super();
		this.adminBusinessService = adminBusinessService;
	}

	
	@GetMapping("/daily")
	public ResponseEntity<?> getDailyBusiness(@RequestParam String date) {
		try {
			LocalDate localDate = LocalDate.parse(date);
			Map<String, Object> businessReport = adminBusinessService.calculateDailyBusiness(localDate);
			return ResponseEntity.status(HttpStatus.OK).body(businessReport);
			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong");
		}
	}
	

	@GetMapping("/monthly")
	public ResponseEntity<?> getMonthlyBusiness(@RequestParam int month, @RequestParam int year) {
		try {
			Map<String, Object> businessReport = adminBusinessService.calculateMonthlyBusiness(month, year);
			return ResponseEntity.status(HttpStatus.OK).body(businessReport);
			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong");
		}
	}

	@GetMapping("/yearly")
	public ResponseEntity<?> getYearlyBusiness(@RequestParam int year) {
		try {
			Map<String, Object> businessReport = adminBusinessService.calculateYearlyBusiness(year);
			return ResponseEntity.status(HttpStatus.OK).body(businessReport);
			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong");
		}
	}

	@GetMapping("/overall")
	public ResponseEntity<?> getOverallBusiness() {
		try {
			Map<String, Object> businessReport = adminBusinessService.calculateOverallBusiness();
			return ResponseEntity.status(HttpStatus.OK).body(businessReport);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong While Calculating Overall Business");
		}
	}

}
