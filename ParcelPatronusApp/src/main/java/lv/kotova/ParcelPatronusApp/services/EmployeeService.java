package lv.kotova.ParcelPatronusApp.services;

import lv.kotova.ParcelPatronusApp.models.DeliveryDetails;
import lv.kotova.ParcelPatronusApp.models.Employee;
import lv.kotova.ParcelPatronusApp.repositories.EmployeeRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }

    public List<Employee> findCouriers() {
        List<Employee> couriers = employeeRepository.findAllEmployeesByPosition("COURIER");
        couriers.add(findById(6)); // let's include COO in the list of persons authorized to fulfill courier duties
        return couriers;
    }

    public Employee findByFullName(String fullName) {
        return employeeRepository.findByFullName(fullName);
    }

    @Transactional
    public void addTaskForEmployee(Employee employee, DeliveryDetails deliveryDetails) {
        if(employee.getPosition().equals("COURIER") || employee.getPosition().equals("COO")) {
            deliveryDetails.setCourier(employee);
        }
        if(employee.getPosition().equals("WAREHOUSE_MANAGER"))
            deliveryDetails.setWarehouseManager(employee);
        Hibernate.initialize(employee.getAssignedDeliveries());
    }


}
