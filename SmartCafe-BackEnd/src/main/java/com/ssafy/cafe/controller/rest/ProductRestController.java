package com.ssafy.cafe.controller.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.dto.User;
import com.ssafy.cafe.model.dto.UserLike;
import com.ssafy.cafe.model.service.ProductService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/product")
@CrossOrigin(allowCredentials = "true", originPatterns = { "*" })
public class ProductRestController {

	@Autowired
	ProductService pService;

	@GetMapping("/productAll")
	@ApiOperation(value = "전체 상품의 목록을 반환한다.", response = List.class)
	public List<Product> getProductList() {
		return pService.getProductList();
	}

	@GetMapping("/{productId}")
	@ApiOperation(value = "{productId}에 해당하는 상품의 정보를 comment와 함께 반환한다."
			+ "이 기능은 상품의 comment를 조회할 때 사용된다.", response = List.class)
	public List<Map<String, Object>> getProductWithComments(@PathVariable Integer productId) {
		return pService.selectWithComment(productId);
	}
	
	@GetMapping("/userComment/{userId}")
	@ApiOperation(value = "{userId}에 해당하는 상품의 정보를 comment와 함께 반환한다."
			+ "이 기능은 특정유저의 comment를 조회할 때 사용된다.", response = List.class)
	public List<Map<String, Object>> getProductWithUserId(@PathVariable String userId) {
		return pService.selectCommentWithUserId(userId);
	}
	
	@GetMapping("/sellCount")
	@ApiOperation(value = "전체 상품의 인기순 목록을 반환한다.", response = List.class)
	public List<Product> getProductListWithSellCount() {
		return pService.getProductWithSellCount();
	}
	
	@GetMapping("/rowPrice")
	@ApiOperation(value = "전체 상품의 낮은 가격순 목록을 반환한다.", response = List.class)
	public List<Product> getProductListWithRowPrice() {
		return pService.getProductWithRowPrice();
	}
	
	@GetMapping("/highPrice")
	@ApiOperation(value = "전체 상품의 높은 가격순 목록을 반환한다.", response = List.class)
	public List<Product> getProductListWithHighPrice() {
		return pService.getProductWithHighPrice();
	}
	
	@GetMapping("/highCommentCnt")
	@ApiOperation(value = "전체 상품의 리뷰많은순 목록을 반환한다.", response = List.class)
	public List<Product> getProductListWithHighCommentCnt() {
		return pService.getProductWithHighCommentCnt();
	}
	
	@GetMapping("/highRating")
	@ApiOperation(value = "전체 상품의 평점순 목록을 반환한다.", response = List.class)
	public List<Product> getProductListWithHighRating() {
		return pService.getProductWithHighRating();
	}
	
	@GetMapping("/coffee")
	@ApiOperation(value = "전체 커피메뉴를 반환한다.", response = List.class)
	public List<Product> getAllCoffee() {
		return pService.getAllCoffee();
	}
	
	@GetMapping("/desert")
	@ApiOperation(value = "전체 디저트 메뉴를 반환한다.", response = List.class)
	public List<Product> getAllDesert() {
		return pService.getAllDesert();
	}
	
	@GetMapping("/userLike/{userId}")
	@ApiOperation(value = "{userId}에 해당하는 찜한메뉴들 반환한다."
			+ "이 기능은 상품의 comment를 조회할 때 사용된다.", response = List.class)
	public List<Product> getLikeMenuOfUser(@PathVariable String userId) {
		return pService.getLikeMenu(userId);
	}
	
	@GetMapping("/newProduct")
	@ApiOperation(value = "신상 메뉴를 반환한다.", response = List.class)
	public List<Product> getNewProduct() {
		return pService.getNewProduct();
	}
	
	@GetMapping("/recommendedProduct")
	@ApiOperation(value = "추천 메뉴를 반환한다.", response = List.class)
	public List<Product> getRecommendedProduct() {
		return pService.getRecommendedProduct();
	}
	
	@GetMapping("/recommendedDesert")
	@ApiOperation(value = "디저트 추천 메뉴를 반환한다.", response = List.class)
	public List<Product> getRecommendedDesert() {
		return pService.getRecommendedDesert();
	}
	
	@PostMapping("/insertLikeMenu")
	@ApiOperation(value = "찜한 메뉴를 추가한다.", response = List.class)
	public int insertLikeMenu(@RequestBody UserLike userlike) {
		return pService.insertLikeMenu(userlike);
	}
	
	@PostMapping("/removeLikeMenu")
	@ApiOperation(value = "찜한 메뉴를 삭제한다.", response = List.class)
	public int removeLikeMenu(@RequestBody UserLike userlike) {
		return pService.removeLikeMenu(userlike);
	}
	
}