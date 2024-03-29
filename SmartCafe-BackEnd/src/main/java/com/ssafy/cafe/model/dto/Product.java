package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {

	private Integer id;
	private String name;
	private String type;
	private Integer price;
	private String img;
	private Integer cnt;

	@Builder
	public Product(Integer id, String name, String type, Integer price, String img) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.price = price;
		this.img = img;
	}

	public Product(Integer id, String name, String type, Integer price, String img, Integer cnt) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.price = price;
		this.img = img;
		this.cnt = cnt;
	}
	
	public Product(String name, String type, Integer price, String img) {
		this.name = name;
		this.type = type;
		this.price = price;
		this.img = img;
	}
}
