package com.ssafy.cafe.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLike {
	private Integer id;
    private String userId;
    private Integer productId;
    
    @Builder
	public UserLike(Integer id, String userId, Integer productId) {
		super();
		this.id = id;
		this.userId = userId;
		this.productId = productId;
	}
    
    @Builder
	public UserLike(String userId, Integer productId) {
		super();
		this.userId = userId;
		this.productId = productId;
	}
    
}
