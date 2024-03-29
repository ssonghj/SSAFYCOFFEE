package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.cafe.model.dao.OrderDao;
import com.ssafy.cafe.model.dao.OrderDetailDao;
import com.ssafy.cafe.model.dao.StampDao;
import com.ssafy.cafe.model.dao.UserDao;
import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderDetail;
import com.ssafy.cafe.model.dto.Stamp;
import com.ssafy.cafe.model.dto.User;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderDao oDao;

	@Autowired
	OrderDetailDao dDao;

	@Autowired
	StampDao sDao;

	@Autowired
	UserDao uDao;

	@Override
	@Transactional
	public void makeOrder(Order order) {
		
		// 주문 및 주문 상세 테이블 저장
		oDao.insert(order);
		List<OrderDetail> details = order.getDetails();
		
		int quantitySum = 0;
		for(OrderDetail detail: details) {
			detail.setOrderId(order.getId());
			dDao.insert(detail);
			quantitySum += detail.getQuantity();
		}
		
		// 스템프 정보 저장
		Stamp stamp = Stamp.builder().userId(order.getUserId()).quantity(quantitySum).orderId(order.getId()).build();
		sDao.insert(stamp);
		// 사용자 정보 업데이트
		User user = User.builder().id(order.getUserId()).stamps(stamp.getQuantity()).build();
		uDao.updateStamp(user);

	}

	@Override
	public Order getOrderWithDetails(Integer orderId) {
		return oDao.selectWithDetail(orderId);
	}

	@Override
	public List<Order> getOrdreByUser(String id) {
		return oDao.selectByUser(id);
	}

	@Override
	public void updateOrder(Order order) {
		oDao.update(order);
	}

	@Override
	public List<Map> selectOrderTotalInfo(int id) {
		return oDao.selectOrderTotalInfo(id);
	}

	@Override
	public List<Map<String, Object>> getLastMonthOrder(String id) {
		return oDao.getLastMonthOrder(id);
	}
	
	@Override
	public List<Map<String, Object>> getThisWeekOrder() {
		return oDao.selectThisWeekOrder();
	}

	@Override
	public List<Map<String, Object>> getCurOrder(String userId) {
		return oDao.selectCurOrder(userId);
	}

	@Override
	public List<Map<String, Object>> getPastOrder(String userId) {
		return oDao.selectPastOrder(userId);
	}

	@Override
	public List<Map<String, Object>> getWriteComment(String userId) {
		return oDao.selectWriteComment(userId);
	}

	@Override
	public int updateIsWriteComment(Integer dId) {
		return oDao.updateIsWriteComment(dId);
	}
}
