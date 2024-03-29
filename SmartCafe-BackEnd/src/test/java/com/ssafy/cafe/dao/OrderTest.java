package com.ssafy.cafe.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderDetail;

class OrderTest extends AbstractDaoTest {

	@Test
	@org.junit.jupiter.api.Order(1)
	public void selectTest() {
		Order selected = oDao.select(1);
		assertEquals(selected.getUserId(), "ssafy01");
	}

	static Order last;

	@Test
	@org.junit.jupiter.api.Order(2)
	public void insertTest() {
		Order data = Order.builder().userId("ssafy01").orderTable("order_table_01").build();
		int result = oDao.insert(data);
		assertEquals(result, 1);
	}

	@Test
	@org.junit.jupiter.api.Order(3)
	public void selectAll() {
		List<Order> result = oDao.selectAll();
		last = result.get(0);
		assertEquals(last.getOrderTable(), "order_table_01");
	}


	@Test
	@org.junit.jupiter.api.Order(4)
	public void updateTest() {
		last.setCompleted('Y');
		int result = oDao.update(last);
		assertEquals(result, 1);

		Order selected = oDao.select(last.getId());
		assertEquals(last.getCompleted(), selected.getCompleted());
	}

	@Test
	@org.junit.jupiter.api.Order(5)
	public void deleteTest() {
		int result = oDao.delete(last.getId());
		assertEquals(result, 1);

		Order selected = oDao.select(last.getId());
		assertNull(selected);
	}

	@Test
	@org.junit.jupiter.api.Order(6)
	public void selectWithDetailTest() {

		Order selected = oDao.selectWithDetail(1);
		assertEquals(selected.getUserId(), "ssafy01");

		List<OrderDetail> details = selected.getDetails();
		assertEquals(details.get(0).getProductId(), 2);

	}

	@Test
	public void selectByUser() {
		List<Order> orders = oDao.selectByUser("ssafy03");
		assertEquals(orders.size(), 1);
	}
}
