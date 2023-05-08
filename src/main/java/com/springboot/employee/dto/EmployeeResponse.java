package com.springboot.employee.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName="build")
public class EmployeeResponse {
	private int employeeId;
	private String name;
	private String age;
    private String gender;
	private String address;
	private String isActive;
	private String experience;
	private String yearsInCurrentRole;
	private String goalCompletedForCurrentYear;
	private String ClientAppreciationForCurrentYear;
	private String isPromotionEligible;
}
