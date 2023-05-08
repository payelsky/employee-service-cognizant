package com.springboot.employee.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.springboot.employee.dto.EmployeeDetails;
import com.springboot.employee.dto.EmployeeResponse;
import com.springboot.employee.entity.Employee;

import com.springboot.employee.repository.EmployeeRepository;



@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository empRepo;
	
	@Autowired
	@Lazy
	private RestTemplate restTemplate;
	
//	public static final String EMPLOYEE_SERVICE="employeeService";
	
	public Employee createEmployee(Employee emp) {
	return 	empRepo.save(emp);
	}
	
	public String deleteEmployeeById(Long id) {
	Employee emp= empRepo.getById(id.intValue());
	
	if(emp.getIsActive().equalsIgnoreCase("Y")) {
	return "FAILED";	
	}else {
	empRepo.delete(emp);	
	return "SUCCESS";
	}
	}
	
	public String deleteAllInactiveEmployees() {
		List<Employee> list=empRepo.findByisActive("Y");
		if(!list.isEmpty()) {
		empRepo.deleteAll(list);
		return "SUCCESS";
		}else {
		return "No data found";	
		}
		
		
	}
	
	public String updateEmployee(Employee emp) {
		Employee empl=empRepo.findById(emp.getEmployeeId()).get();	
		if(empl!=null) {
			empl=Employee.build(empl.getEmployeeId(), emp.getName(), emp.getAge(), emp.getGender(), emp.getAddress(), emp.getIsActive());
			empRepo.save(empl);
			return "Success";
		}else {
		   return "No data Found";
		}
	}
	
	public List<EmployeeDetails> getAllActiveEmployees(String isActive){
		 List<Employee>  emplist=empRepo.findByisActive(isActive);
		List<EmployeeDetails> list = getHcmDetails(emplist);
		System.out.println("empreslist::::::::::::::"+list);
		return list;
	}
	
	public List<EmployeeResponse> getAllActiveEmployeesWithPromotion(String isActive){

	List<EmployeeDetails> list = getAllActiveEmployees(isActive);
				 
	List<EmployeeResponse> empreslist = checkPromotionEligibility(list);
	System.out.println("empreslist::::::::::::::"+empreslist);
	return empreslist;
	}

	/* @CircuitBreaker(name =EMPLOYEE_SERVICE,fallbackMethod = "hcmFallBack") */
	public List<EmployeeDetails> getHcmDetails(List<Employee> emplist){
		 HttpHeaders headers = new HttpHeaders();
		 HttpEntity<List<Employee>> request = new HttpEntity<List<Employee>>(emplist, 
				    headers);
		 ResponseEntity<EmployeeDetails[]> responses = 
				  restTemplate.postForEntity("http://HCM-SERVICE/hcm/getDetails", request , EmployeeDetails[].class );

		List<EmployeeDetails> list = Arrays.asList(responses.getBody());
		return list;
	}
	
	public List<EmployeeResponse> checkPromotionEligibility(List<EmployeeDetails> list){
		 HttpHeaders headers = new HttpHeaders();
		 HttpEntity<List<EmployeeDetails>> request2 = new HttpEntity<List<EmployeeDetails>>(list, 
				    headers);
		  
		  ResponseEntity<EmployeeResponse[]> responses2 = 
				  restTemplate.postForEntity("http://PROMOTION-SERVICE/promotion/checkEligibility", request2 , EmployeeResponse[].class );
		  List<EmployeeResponse> empreslist = Arrays.asList(responses2.getBody());
		  return empreslist;
		 
		
	}
	public List<EmployeeDetails> hcmFallBack(List<Employee> emplist){
		System.out.println("Hi::::::::::::::::::::::::::::::::::");
		List<EmployeeDetails> empdetailslist=emplist.stream().map(emp->EmployeeDetails.build(emp.getEmployeeId(), emp.getName(), emp.getAge(), emp.getGender(), emp.getAddress(), emp.getIsActive(), "DOWN", "DOWN", "DOWN","DOWN"))
                .collect(Collectors.toList());
		
		return empdetailslist;
	}
    
	

}
