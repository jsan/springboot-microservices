package com.mello.microservices.departmentservice.kafka;

import com.mello.microservices.departmentservice.dto.DepartmentDto;
import com.mello.microservices.departmentservice.service.DepartmentService;
import com.mello.microservices.employeeservice.dto.EmployeeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeKafkaConsumer
{
    private DepartmentService departmentService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeKafkaConsumer.class);

    public EmployeeKafkaConsumer(DepartmentService departmentService)
    {
        this.departmentService = departmentService;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(EmployeeEvent employeeEvent)
    {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName(employeeEvent.getStatus());
        departmentDto.setDepartmentCode("Employee");
        departmentDto.setDepartmentDescription(employeeEvent.getEmployeeDto().getFirstName().concat(" ").concat(employeeEvent.getEmployeeDto().getLastName()));
        departmentService.save(departmentDto);
        LOGGER.info(String.format("JSON Consumer message received: %s - %s", employeeEvent.getMessage(), employeeEvent.getEmployeeDto().getFirstName()));
    }
}
