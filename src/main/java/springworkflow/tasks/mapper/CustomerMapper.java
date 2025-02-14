package springworkflow.tasks.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import springworkflow.tasks.entity.Customer;

@Mapper
public interface CustomerMapper {
	public Customer find(String custCode);
	public boolean insert(Customer customer);
	public boolean delete(Customer customer);
	public boolean update(Customer customer);
	public ArrayList<Customer> findAll();
	public ArrayList<Customer> findByTelNo(String telNo);
	public ArrayList<Customer> findAllWithSort(String column, String sortType);
	public Customer findIgnoreDeleteFlag(String custCode);
}
