package lv.kotova.ParcelPatronusApp.repositories;

import lv.kotova.ParcelPatronusApp.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findAllEmployeesByPosition(String position);
    Employee findByFullName(String fullName);
}
