package net.guides.springboot2.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.guides.springboot2.crud.exception.ResourceNotFoundException;
import net.guides.springboot2.crud.model.Employee;
import net.guides.springboot2.crud.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/get_all_employees")
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@GetMapping("/get_employees")
	public ResponseEntity<Employee> getEmployeeById(@RequestParam(value = "id") String employeeId)
			throws ResourceNotFoundException {
		Long id = Long.parseLong(employeeId);
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		return ResponseEntity.ok().body(employee);
	}

	@PostMapping("/save_employees")
	public Employee createEmployee(@RequestParam(value = "first_name", required = false) String firstname,
								   @RequestParam(value = "last_name", required = false) String lastname,
								   @RequestParam(value = "email_id", required = false) String emailId) {
		Employee employee = new Employee();
		employee.setFirstName(firstname);
		employee.setLastName(lastname);
		employee.setEmailId(emailId);
		return employeeRepository.save(employee);
	}

	@PutMapping("/edit_employees")
	public ResponseEntity<Employee> updateEmployee(@RequestParam(value = "id") String employeeId,
												   @RequestParam(value = "first_name", required = false) String firstname,
												   @RequestParam(value = "last_name", required = false) String lastname,
												   @RequestParam(value = "email_id", required = false) String emailId) throws ResourceNotFoundException {

		Long id = Long.parseLong(employeeId);
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employee.setEmailId(emailId);
		employee.setLastName(lastname);
		employee.setFirstName(firstname);
		final Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}

	@DeleteMapping("/delete_employees")
	public Map<String, Boolean> deleteEmployee(@RequestParam(value = "id") String employeeId)
			throws ResourceNotFoundException {

		Long id = Long.parseLong(employeeId);
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
