package com.spike.springcloud.shardingsphere.domain.service;

import com.spike.springcloud.shardingsphere.domain.model.Address;
import com.spike.springcloud.shardingsphere.domain.model.Order;
import com.spike.springcloud.shardingsphere.domain.model.OrderItem;
import com.spike.springcloud.shardingsphere.infra.repository.AddressRepository;
import com.spike.springcloud.shardingsphere.infra.repository.OrderItemRepository;
import com.spike.springcloud.shardingsphere.infra.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public final class ExampleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleService.class);

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final AddressRepository addressRepository;

    public ExampleService(final OrderRepository orderRepository,
                          final OrderItemRepository orderItemRepository,
                          final AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.addressRepository = addressRepository;
    }

    public void run() throws SQLException {
        try {
//            this.initEnvironment();
            this.processSuccess();
        } finally {
//            this.cleanEnvironment();
        }
    }

    /**
     * 初始化环境: 创建表, 清空表.
     *
     * @throws SQLException
     */
    public void initEnvironment() throws SQLException {
        orderRepository.createTableIfNotExists();
        orderItemRepository.createTableIfNotExists();
        addressRepository.createTableIfNotExists();

        orderRepository.truncateTable();
        orderItemRepository.truncateTable();
        addressRepository.truncateTable();
    }

    private void processSuccess() throws SQLException {
        LOGGER.info("-------------- Process Success Begin ---------------");
        // 插入数据
        List<Long> orderIds = insertData();
        printData();

        // 删除数据
        deleteData(orderIds);
        printData();
        LOGGER.info("-------------- Process Success Finish --------------");
    }

    public List<Long> processInsertData() throws SQLException {
        List<Long> orderIds = insertData();
        printData();

        return orderIds;
    }

    public void processDeleteData(List<Long> orderIds) throws SQLException {
        deleteData(orderIds);
        printData();
    }

    private List<Long> insertData() throws SQLException {
        LOGGER.info("---------------------------- Insert Data ----------------------------");
        List<Long> result = new ArrayList<>(10);
        for (int i = 1; i <= 20; i++) {
            Order order = new Order();
            order.setUserId(i);
            order.setOrderType(i % 2);
            order.setAddressId(i);
            order.setStatus("INSERT_TEST");
            orderRepository.insert(order);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setUserId(i);
            orderItem.setPhone("13800000001");
            orderItem.setStatus("INSERT_TEST");
            orderItemRepository.insert(orderItem);

            Address address = new Address();
            address.setAddressId((long) i);
            address.setAddressName("address_test_" + i);
            addressRepository.insert(address);

            result.add(order.getOrderId());
        }
        return result;
    }

    private void deleteData(final List<Long> orderIds) throws SQLException {
        LOGGER.info("---------------------------- Delete Data ----------------------------");
        long count = 1;
        for (Long each : orderIds) {
            orderRepository.delete(each);
            orderItemRepository.delete(each);
            addressRepository.delete(count++);
        }
    }

    private void printData() throws SQLException {
        LOGGER.info("---------------------------- Print Order Data -----------------------");
        for (Object each : this.selectAll()) {
            LOGGER.info(each.toString());
        }
        LOGGER.info("---------------------------- Print OrderItem Data -------------------");
        for (Object each : orderItemRepository.selectAll()) {
            LOGGER.info(each.toString());
        }
        LOGGER.info("---------------------------- Print Address Data -------------------");
        for (Object each : addressRepository.selectAll()) {
            LOGGER.info(each.toString());
        }
    }

    private List<Order> selectAll() throws SQLException {
        List<Order> result = orderRepository.selectAll();
        return result;
    }

    /**
     * 清理环境: 删除表.
     *
     * @throws SQLException
     */
    public void cleanEnvironment() throws SQLException {
        orderRepository.dropTable();
        orderItemRepository.dropTable();
        addressRepository.dropTable();
    }
}
