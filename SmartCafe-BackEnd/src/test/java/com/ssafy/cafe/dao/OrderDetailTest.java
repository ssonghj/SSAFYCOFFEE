package com.ssafy.cafe.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ssafy.cafe.model.dto.OrderDetail;

class OrderDetailTest extends AbstractDaoTest {

	private static OrderDetail last;

	@Test
	@org.junit.jupiter.api.Order(1)
	public void selectTest() {
		OrderDetail selected = dDao.select(1);
		assertEquals(selected.getProductId(), 1);
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	public void insertTest() {
		OrderDetail detail = new OrderDetail(1,1,10);
		int result = dDao.insert(detail);
		assertEquals(result, 1);
	}

	@Test
	@org.junit.jupiter.api.Order(3)
	public void selectAll() {
		List<OrderDetail> result = dDao.selectAll();
		last = result.get(0);

		assertEquals(last.getQuantity(), 10);
	}

	@Test
	@org.junit.jupiter.api.Order(4)
	public void deleteTest() {
		System.out.println(last);
		int result = dDao.delete(last.getId());
		assertEquals(result, 1);

		OrderDetail selected = dDao.select(last.getId());
		assertNull(selected);
	}
}
