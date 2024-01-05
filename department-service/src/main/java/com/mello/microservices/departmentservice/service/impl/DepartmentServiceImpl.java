package com.mello.microservices.departmentservice.service.impl;

import com.mello.basedomains.basedomains.dto.Order;
import com.mello.basedomains.basedomains.dto.OrderEvent;
import com.mello.microservices.departmentservice.dto.DepartmentDto;
import com.mello.microservices.departmentservice.entity.Department;
import com.mello.microservices.departmentservice.kafka.KafkaBuyEmployeeProducer;
import com.mello.microservices.departmentservice.mapper.DepartmentMapper;
import com.mello.microservices.departmentservice.repository.DepartmentRepository;
import com.mello.microservices.departmentservice.service.DepartmentService;
import com.mello.microservices.employeeservice.dto.EmployeeDto;
import com.mello.microservices.employeeservice.dto.EmployeeEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// TODO add exception handling
@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private DepartmentRepository departmentRepo;

    private KafkaBuyEmployeeProducer kafkaBuyEmployeeProducer;


    @Override
    public DepartmentDto save(DepartmentDto departmentDto)
    {
        Department department = DepartmentMapper.MAPPER.mapToDepartment(departmentDto);
        return DepartmentMapper.MAPPER.mapToDepartmentDto(departmentRepo.save(department));
    }

    @Override
    public DepartmentDto update(Long id, DepartmentDto departmentDto)
    {
        // TODO Validate ID with FindById throw exception
        departmentDto.setId(id);
        Department department = DepartmentMapper.MAPPER.mapToDepartment(departmentDto);
        return DepartmentMapper.MAPPER.mapToDepartmentDto(departmentRepo.save(department));
    }

    @Override
    public DepartmentDto getDptById(Long id)
    {
        return departmentRepo.findById(id).map(DepartmentMapper.MAPPER::mapToDepartmentDto).orElse(null);
    }

    @Override
    public DepartmentDto getDptByDepartmentCode(String departmentCode)
    {
        return DepartmentMapper.MAPPER.mapToDepartmentDto(departmentRepo.findDepartmentByDepartmentCode(departmentCode));
    }

    @Override
    public List<DepartmentDto> listAll()
    {
        List<Department> list = departmentRepo.findAll();
        return list.stream().map(DepartmentMapper.MAPPER::mapToDepartmentDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id)
    {
        departmentRepo.deleteById(id);
    }

    @Override
    public void sendOrder(EmployeeDto employeeDto)
    {
        OrderEvent orderEvent = new OrderEvent();
        Order order = new Order();

        order.setOrderId("01");
        order.setName(employeeDto.getFirstName() + " " + employeeDto.getLastName());
        order.setQty(1);
        order.setPrice(10.00);

        orderEvent.setStatus("InProgress");
        orderEvent.setMessage("Employee changed");
        orderEvent.setOrder(order);

        kafkaBuyEmployeeProducer.sendMessage(orderEvent);

    }
}
