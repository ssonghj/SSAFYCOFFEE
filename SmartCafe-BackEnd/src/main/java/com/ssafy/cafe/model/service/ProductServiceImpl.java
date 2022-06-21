package com.ssafy.cafe.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.cafe.model.dao.ProductDao;
import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.dto.UserLike;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao pDao;

	@Override
	public List<Product> getProductList() {
		return pDao.selectAll();
	}

	@Override
	public List<Map<String, Object>> selectWithComment(Integer productId) {
		return pDao.selectWithComment(productId);
	}

	@Override
	public List<Product> getProductWithSellCount() {
		return pDao.selectAllWithSellCount();
	}

	@Override
	public List<Product> getProductWithRowPrice() {
		return pDao.selectAllWithRowPrice();
	}

	@Override
	public List<Product> getProductWithHighPrice() {
		return pDao.selectAllWithHighPrice();
	}

	@Override
	public List<Product> getProductWithHighCommentCnt() {
		return pDao.selectAllWithHighCommentCnt();
	}

	@Override
	public List<Product> getProductWithHighRating() {
		return pDao.selectAllWithHighRating();
	}

	@Override
	public List<Product> getAllCoffee() {
		return pDao.selectCoffee();
	}

	@Override
	public List<Product> getAllDesert() {
		return pDao.selectDesert();
	}

	@Override
	public List<Product> getLikeMenu(String user_id) {
		return pDao.selectUserLikeMenu(user_id);
	}
	
	@Override
	public int insertLikeMenu(UserLike userlike) {
		return pDao.insertUserLikeMenu(userlike);
	}

	@Override
	public int removeLikeMenu(UserLike userlike) {
		return pDao.deleteUserLikeMenu(userlike);
	}

	@Override
	public List<Product> getNewProduct() {
		return pDao.selectNewProduct();
	}
	
	@Override
	public List<Product> getRecommendedProduct() {
		return pDao.selectRecommendedProduct();
	}

	@Override
	public List<Product> getRecommendedDesert() {
		return pDao.selectRecommendedDesert();
	}

	@Override
	public List<Map<String, Object>> selectCommentWithUserId(String userId) {
		return pDao.selectCommentWithUserId(userId);
	}

}
