package com.ssafy.smartcafe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.smartcafe.MobileCafeApplication
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.userId
import com.ssafy.smartcafe.adapter.MenuReviewAdapter
import com.ssafy.smartcafe.databinding.ActivityWriteReviewBinding
import com.ssafy.smartcafe.dto.CommentDTO
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.service.CommentService
import com.ssafy.smartcafe.service.OrderService
import com.ssafy.smartcafe.service.ProductService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WriteReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isitModify = intent!!.getBooleanExtra("modify",false)

        binding.btnBack.setOnClickListener{
            finish()
        }

        //리뷰작성을 어디서 들어가게 할까? -> 해당 메뉴를 주문한 적이 있다면? 리뷰 작성 가능하게?
        //메뉴 상세보기에서 버튼 활성/비활성 으로 리뷰 작성
        //리뷰안쓴메뉴들만 따로 모아서 확인할 수 있도록?

        //INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('ssafy01', 1, 1, '신맛 강한 커피는 좀 별루네요.');


        //리뷰 관리에서 수정 누르면(true리턴) 리뷰 수정으로 이어지게 하기
        if(isitModify){
            binding.tvReviewWriteTitle.text = "리뷰수정"

            val commentId = intent!!.getIntExtra("commentId",0)
            val menuName = intent!!.getStringExtra("menuName")
            val productId = intent!!.getIntExtra("productId",0)
            var rating = intent!!.getFloatExtra("rating",0.0f)
            var content = intent!!.getStringExtra("content").toString()

            binding.tvMenuName.text = menuName
            binding.ratingBar.rating = rating
            binding.editContent.setText(content)

            //수정한 리뷰 업로드
            binding.frameReviewCompleteBtn.setOnClickListener{
                CoroutineScope(Dispatchers.Main).launch {
                    var content = binding.editContent.text.toString()
                    var rating = (binding.ratingBar.rating * 2).toInt()

                    var modifyComment = CommentDTO(content, commentId, productId, rating, userId)
                    //commentId 사용해서 ㄱ
                    uploadComment(modifyComment)

                    finish()
                }
            }
        }
        //새로 작성할 경우
        else{
            binding.tvReviewWriteTitle.text = "리뷰작성"

            val menuName = intent!!.getStringExtra("menuName")
            val productId = intent!!.getIntExtra("productId",0)
            val d_id = intent!!.getIntExtra("d_id",-1)

            binding.tvMenuName.text = menuName

            //수정한 리뷰 업로드
            binding.frameReviewCompleteBtn.setOnClickListener{
                CoroutineScope(Dispatchers.Main).launch {
                    var content = binding.editContent.text.toString()
                    var rating = (binding.ratingBar.rating * 2).toInt()

                    var comment = CommentDTO(content, 0, productId, rating, userId)
                    //commentId 사용해서 ㄱ
                    createComment(comment)

                    //order_detail의 is_write_review 값 'Y'로 변경
                    updateIsWriteComment(d_id)

                    finish()
                }
            }
        }
    }

    private suspend fun uploadComment(commentDTO: CommentDTO) {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(CommentService::class.java)
            val response = service.updateComment(commentDTO)

            if (response.code() == 200) {
                var res = response.body()

                println("uploadComment : ${res}")
            } else {
                print("uploadComment: error code")
            }
        }
    }

    private suspend fun createComment(commentDTO: CommentDTO) {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(CommentService::class.java)
            val response = service.insertComment(commentDTO)

            if (response.code() == 200) {
                var res = response.body()

                println("uploadComment : ${res}")
            } else {
                print("uploadComment: error code")
            }
        }
    }

    private suspend fun updateIsWriteComment(dId: Int) {
        withContext(Dispatchers.IO) {
            val service = MobileCafeApplication.retrofit.create(OrderService::class.java)
            val response = service.updateWriteComment(dId)

            if (response.code() == 200) {
                var res = response.body()

                println("uploadComment : ${res}")
            } else {
                print("uploadComment: error code")
            }
        }
    }

}