package com.ssafy.cafe.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.dto.UserLike;

public interface ProductDao {

	int insert(Product product);

	int update(Product product);

	int delete(Integer productId);

	Product select(Integer productId);

	List<Product> selectAll();

	// backend 관통 과정에서 추가됨.
	List<Map<String, Object>> selectWithComment(Integer productId);
	
	List<Map<String, Object>> selectCommentWithUserId(String userId);

	List<Product> selectAllWithSellCount();
	
	List<Product> selectAllWithRowPrice();
	
	List<Product> selectAllWithHighPrice();
	
	List<Product> selectAllWithHighCommentCnt();
	
	List<Product> selectAllWithHighRating();

	List<Product> selectCoffee();
	
	List<Product> selectDesert();
	
	List<Product> selectUserLikeMenu(String productId);
	int insertUserLikeMenu(UserLike userlike);
	int deleteUserLikeMenu(UserLike userlike);

	List<Product> selectNewProduct();

	List<Product> selectRecommendedProduct();

	List<Product> selectRecommendedDesert();
}
