package com.springboot.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.employee.dto.EmployeeDetails;
import com.springboot.employee.dto.EmployeeResponse;
import com.springboot.employee.entity.Employee;
import com.springboot.employee.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/employee")
public class EmployeeController {
	private static String isActive="Y";
	@Autowired
	private EmployeeService empService;
	
	@PostMapping("/create")
	public String createEmployee(@RequestBody Employee emp) {
	return	String.valueOf(empService.createEmployee(emp).getEmployeeId());
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteById(@PathVariable("id") Long id) {
		return empService.deleteEmployeeById(id);
	}
    
	@DeleteMapping("/delete")
	public String deleteAll() {
	return empService.deleteAllInactiveEmployees();
	}
	
	@PutMapping("/update")
	public String updateEmployee(@RequestBody Employee emp) {
		return null;
		
	}
	
	@GetMapping("/getDetails")
	public List<EmployeeResponse> getEmployeeDetails(@RequestParam  String isActive){
		System.out.println("isActive::::::::::::::"+isActive);
		return  empService.getAllActiveEmployeesWithPromotion(isActive);
		}
	@GetMapping("/getDetailsWithoutPromotion")
	public List<EmployeeDetails> getDetailsWithoutPromotion(){
		
		return  empService.getAllActiveEmployees(isActive);
		}

}
