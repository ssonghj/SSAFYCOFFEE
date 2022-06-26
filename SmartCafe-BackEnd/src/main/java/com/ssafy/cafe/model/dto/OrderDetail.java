package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetail {

	private Integer id;
	private Integer orderId;
	private Integer productId;
	private Integer quantity;
	private Character is_write_reivew;

	@Builder
	public OrderDetail(Integer id, Integer orderId, Integer productId, Integer quantity) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public OrderDetail(Integer productId, Integer quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public OrderDetail(Integer orderId, Integer productId, Integer quantity) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public OrderDetail(Integer id, Integer orderId, Integer productId, Integer quantity, Character is_write_reivew) {
		this.id = id;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.is_write_reivew = is_write_reivew;
	}
	
	public OrderDetail( Integer orderId, Integer productId, Integer quantity, Character is_write_reivew) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.is_write_reivew = is_write_reivew;
	}

}
