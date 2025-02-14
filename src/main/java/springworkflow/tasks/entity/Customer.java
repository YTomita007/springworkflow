package springworkflow.tasks.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

	private String custCode;

	private String custName;

	private String telNo;

	private String postalCode;

	private String address;

	private int discountRate;
}