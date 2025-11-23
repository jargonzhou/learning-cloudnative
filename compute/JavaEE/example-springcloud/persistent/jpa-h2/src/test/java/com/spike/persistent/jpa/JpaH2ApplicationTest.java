package com.spike.persistent.jpa;

import com.spike.persistent.jpa.model.Customer;
import com.spike.persistent.jpa.repository.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

//@SpringBootTest
@DataJpaTest // JPA测试
public class JpaH2ApplicationTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindByLastName() {
        Customer customer = new Customer("first", "last");
        customerRepository.save(customer);

        List<Customer> customers = customerRepository.findByLastName("last");
        System.err.println(customers);
        // assertj
        Assertions.assertThat(customers)
                .extracting(Customer::getLastName)
                .containsOnly(customer.getLastName());
    }
}
