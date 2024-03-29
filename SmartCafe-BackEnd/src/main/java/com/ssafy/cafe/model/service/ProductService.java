package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.dto.UserLike;

public interface ProductService {
	
	/**
	 * 모든 상품 정보를 반환한다.
	 * @return
	 */
	List<Product> getProductList();
	
	/**
	 * 모든 상품정보를 인기순으로 반환 
	 * @return
	 */
	List<Product> getProductWithSellCount();
	
	//모든 상품정보를 낮은가격순 으로 반환 
	List<Product> getProductWithRowPrice();
	
	//모든 상품정보를 높은가격순 으로 반환 
	List<Product> getProductWithHighPrice();
	
	//모든 상품정보를 리뷰많은순 으로 반환 
	List<Product> getProductWithHighCommentCnt();
	
	//모든 상품정보를 평점순 으로 반환 
	List<Product> getProductWithHighRating();
	
	//모든 상품정보를 이름순으로 반환
	List<Product> getProductOrderbyName();
	
	//음료수만 반환
	List<Product> getAllCoffee();
	
	//디저트만 반환 
	List<Product> getAllDesert();
	
	//찜한 메뉴 반환
	List<Product> getLikeMenu(String user_id);
	
	//찜한 메뉴 추가
	int insertLikeMenu(UserLike userlike);
	
	//찜한 메뉴 삭제 
	int removeLikeMenu(UserLike userlike);

	//신상 메뉴 반환
	List<Product> getNewProduct();
	
	//추천 메뉴 반환
	List<Product> getRecommendedProduct();

	//디저트 추천 메뉴 반환
	List<Product> getRecommendedDesert();
	
	/**
	 * backend 관통 과정에서 추가됨
	 * 상품의 정보, 판매량, 평점 정보를 함께 반환
	 * @param productId
	 * @return
	 */
	List<Map<String, Object>> selectWithComment(Integer productId);
	
	List<Map<String, Object>> selectCommentWithUserId(String userId);

	

	

	

	

}
