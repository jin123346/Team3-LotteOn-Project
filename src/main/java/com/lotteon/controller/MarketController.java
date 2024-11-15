package com.lotteon.controller;

import com.lotteon.dto.QnaDTO;
import com.lotteon.dto.User.DeliveryDTO;
import com.lotteon.dto.User.MemberDTO;
import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.admin.PageResponseDTO;
import com.lotteon.dto.adminQnaDTO;
import com.lotteon.dto.order.OrderCompletedResponseDTO;
import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.order.OrderResponseDTO;
import com.lotteon.dto.product.*;
import com.lotteon.dto.product.cart.CartItemDTO;
import com.lotteon.dto.product.cart.CartSummary;
import com.lotteon.dto.product.request.BuyNowRequestDTO;
import com.lotteon.dto.order.OrderRequestDTO;
import com.lotteon.entity.User.User;
import com.lotteon.entity.admin.Adminqna;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.entity.product.ProductCategory;
import com.lotteon.entity.product.Review;
import com.lotteon.repository.ReviewRepository;
import com.lotteon.repository.cart.CartItemRepository;
import com.lotteon.repository.product.ProductOptionCombinationRepository;
import com.lotteon.repository.product.ProductOptionCombinationRepository;
import com.lotteon.security.MyUserDetails;
import com.lotteon.repository.product.ProductOptionCombinationRepository;
import com.lotteon.service.AdminService;
import com.lotteon.service.ReviewService;
import com.lotteon.service.admin.CouponIssuedService;
import com.lotteon.service.admin.QnaService;
import com.lotteon.service.order.CartItemService;
import com.lotteon.service.order.OrderService;
import com.lotteon.service.product.MarketCartService;
import com.lotteon.service.product.ProductCategoryService;
import com.lotteon.service.product.ProductService;
import com.lotteon.service.user.DeliveryService;
import com.lotteon.service.user.CouponDetailsService;
import com.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
/*
    2024.10.28 하진희 - marketorder => 구매하기 버튼 기능 추가 (/buynow)
 */

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/market")
public class MarketController {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final UserService userService;
    private final CouponDetailsService couponDetailsService;
    private final MarketCartService marketCartService;
    private final ReviewService reviewService;
    private final OrderService orderService;
    private final AdminService adminService;
    private final ProductOptionCombinationRepository productOptionCombinationRepository;
    private final DeliveryService deliveryService;
    private final CartItemService cartItemService;
    private final QnaService qnaService;
    private final ReviewRepository reviewRepository;
    private final CouponIssuedService couponIssuedService;

    @GetMapping("/main/{category}")
    public String marketMain(Model model,@PathVariable long category) {
        ProductCategoryDTO productCategoryDTO =  productCategoryService.getCategoryName(category);
        String name= productCategoryDTO.getName();
        log.info("ddddddddddddddd231:"+name);
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        log.info(name);


        List<ProductDTO> hitProduct =  productService.selectMainList(category,"hit");
        log.info("hitProduct!!!:"+hitProduct);
        List<ProductDTO> soldProduct =  productService.selectMainList(category,"sold");
        List<ProductDTO> rDateProduct =  productService.selectMainList(category,"rdate");
        List<ProductDTO> discountProduct =  productService.selectMainList(category,"discount");
        List<ProductDTO> ratingProduct =  productService.selectMainList(category,"rating");


        model.addAttribute("hitProduct",hitProduct);
        model.addAttribute("soldProduct",soldProduct);
        model.addAttribute("rDateProduct",rDateProduct);
        model.addAttribute("discountProduct",discountProduct);
        model.addAttribute("ratingProduct",ratingProduct);


        model.addAttribute("categoryName",name);
        model.addAttribute("active",category);

        model.addAttribute("content", "main");
        model.addAttribute("banners", banners2);
        return "content/market/marketMain"; // Points to the "content/market/marketMain" template
    }

    @GetMapping("/list/{category}")
    public String marketList(PageRequestDTO pageRequestDTO, @PathVariable long category
            , @RequestParam(required = false, defaultValue = "popularity") String sort
            , @RequestParam(required = false, defaultValue = "1") int page
            , Model model) {
        pageRequestDTO.setCategoryId(category);
        pageRequestDTO.setPage(page);
        log.debug("Debugging category: " + category);

        log.info("page"+page);

        List<ProductCategoryDTO> categoryDTOs = productCategoryService.getAllParentCategoryDTOs(category);
        log.info("@222222222222222222222" + categoryDTOs);

        log.info("11111111111111" + pageRequestDTO.getCategoryId());
        log.info("category:" + category);
//        log.info("dsdsdsdsd2222"+pageRequestDTO);
        ProductListPageResponseDTO responseDTO = productService.getSortProductList(pageRequestDTO, sort);
        log.info("controlllermarket::::" + responseDTO.getProductSummaryDTOs());
        model.addAttribute("categoryDTOs", categoryDTOs);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("sort", sort);



        return "content/market/marketList"; // Points to the "content/market/marketList" template
    }







    @GetMapping("/view")
    public String marketView(Model model) {
        model.addAttribute("content", "view");


        return "content/market/marketview"; // Points to the "content/market/marketview" template
    }

    @GetMapping("/view/{categoryId}/{productId}")
    public String marketView (@PathVariable Long productId, @PathVariable Long categoryId,
        Model model, Authentication authentication, com.lotteon.dto.admin.PageRequestDTO pageRequestDTO) {
        log.info(productId);
        log.info(categoryId);

        // PageRequest 설정
        pageRequestDTO.setSize(6);

        // 조회수 업데이트
        productService.updatehit(productId);

        // 카테고리 및 상품 정보 추가
        List<ProductCategoryDTO> categoryDTOs = productCategoryService.selectCategory(categoryId);
        ProductDTO productdto = productService.getProduct(productId);

        log.info("productVIew Controller:::::"+productdto);
        List<Review> allReviews = reviewService.getAllReviewsByProductId(productId);

        // 리뷰 정보 추가
        PageResponseDTO<ReviewDTO> pageResponseReviewDTO = reviewService.getAllReviewsss(pageRequestDTO, productId);
        List<Review> ReviewImgs = reviewService.getAllReviews();

        // 현재 사용자 아이디로 Q&A 데이터 필터링
        List<adminQnaDTO> userQnaList = qnaService.getQnaByWriterAndProductId(productId);


        // 모델에 필요한 데이터 추가
        model.addAttribute("pageResponseReviewDTO", pageResponseReviewDTO);
        model.addAttribute("reviewImgs", ReviewImgs);
        model.addAttribute("categoryDTOs", categoryDTOs);
        model.addAttribute("products", productdto);
        model.addAttribute("qnaList", userQnaList); // Q&A 데이터 추가
        double averageRating = allReviews.stream()
                .mapToDouble(Review::getRating) // 각 리뷰의 평점 가져오기
                .average()
                .orElse(0.0);  // 리뷰가 없을 경우 0.0으로 설정
        int reviewCount = allReviews.size(); // Count of reviews

        model.addAttribute("reviewImgs", ReviewImgs);
        model.addAttribute("categoryDTOs",categoryDTOs);
        model.addAttribute("products",productdto);
        model.addAttribute("averageRating", String.format("%.1f", averageRating));
        model.addAttribute("reviewCount", reviewCount);

        return "content/market/marketview"; // Points to the "content/market/marketview" template
    }


    @GetMapping("/cart")
    public String marketCart(Model model) {


        List<CartItem> cartItems = marketCartService.selectCartAll();
        log.info("cartItems1111"+cartItems);

        List<CartItemDTO> cartItemDTOS = marketCartService.convertToCartItemDTO(cartItems);
        log.info("cartItems2222"+cartItemDTOS);

        CartSummary cartSummary = marketCartService.calculateSelectedCartSummary(cartItemDTOS);

        model.addAttribute("cartItemDTOS", cartItemDTOS);
        model.addAttribute("cartSummary", cartSummary);
        log.info("카트 총집합! cart items: {}", cartItemDTOS);

        return "content/market/marketcart"; // Points to the "content/market/marketcart" template
    }


    @GetMapping("/order/{uid}")
    public String marketOrder(@PathVariable String uid, Model model, String productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String memberUid = (userDetails.getId());  // 로그인한 사용자의 Member ID (String 타입)

        log.info("uid ::::::::::" + uid);
        MemberDTO memberDTO = userService.getByUsername(uid);

        log.info("멤버 아이디다"+memberUid);


        log.info(memberDTO);
        model.addAttribute("memberDTO", memberDTO);

        Long memberId = memberDTO.getMemberId(); // memberId가 MemberDTO에 있다고 가정
        List<DeliveryDTO> deliveryDTOList = deliveryService.getByMemberId(memberId);
        ; // memberId로 DeliveryDTO 가져옴
        log.info("deliveryDTO ::::::::::" + deliveryDTOList);
        model.addAttribute("deliveryDTO", deliveryDTOList);
        model.addAttribute("productId", productId);

        return "content/market/marketorder"; // Points to the "content/market/marketorder" template
    }

    @PostMapping("/order/saveOrder")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> saveOrder(@RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication) {
        Map<String, Long> response = new HashMap<>();
        response.put("result", 0L);
        log.info("요기!!!!!!!!!!!!!!!!!" + orderRequestDTO);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(orderRequestDTO);
        if (orderResponseDTO.getCartId() > 0) {
            log.info("cartId 가 없다?"+orderResponseDTO.getOrder());
            List<Long> cartItems = orderResponseDTO.getCartItems();
            boolean result = cartItemService.deleteCartItems(cartItems, orderResponseDTO.getCartId());
            if (!result) {
                response.put("result", 0L);
                return ResponseEntity.ok(response);
            }
        }
        long orderId = orderService.saveOrder(orderResponseDTO);
        log.info("구매 처리 완료");
        log.info("쿠폰 변경 요청함");
        couponIssuedService.updateCouponStatus(orderRequestDTO.getCouponId(),"사용완료","사용완료");


        if (orderId > 0) {
            response.put("result", orderId);

        }


        return ResponseEntity.ok(response);
    }


    @PostMapping("/buyNow")
    @ResponseBody
    public ResponseEntity<Map<String, String>> processOrder(@RequestBody List<BuyNowRequestDTO> productDataList, Authentication authentication) {
        Map<String, String> response = new HashMap<>();

        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("result", "login_required");
            return ResponseEntity.ok(response);
        }

        // Check if product data list is provided
        if (productDataList == null || productDataList.isEmpty()) {
            response.put("result", "fail");
            return ResponseEntity.ok(response);
        }

        // Retrieve user details using authentication
        String uid = authentication.getName();
        Optional<User> opt = userService.findUserByUid(uid);

        if (opt.isPresent()) {
            User user = opt.get();

            // Check if the user role is not allowed for purchase
            if (user.getRole() != User.Role.MEMBER) {
                response.put("result", "auth"); // Admin or seller accounts cannot purchase
                return ResponseEntity.ok(response);
            }

            // Process each product item (additional business logic can be added here)
            for (BuyNowRequestDTO productData : productDataList) {
                // Here, implement any logic needed for each product in the order
                System.out.println("Processing order for product: " + productData.getProductName());
            }

            // Purchase is successful for member role
            response.put("result", "success");


        } else {
            // No user account found
            response.put("result", "none");
        }

        return ResponseEntity.ok(response);

    }

    @GetMapping("/completed/{orderId}")
    public String marketOrderCompleted(@PathVariable long orderId, Model model) {
        model.addAttribute("content", "completed");
        OrderCompletedResponseDTO orderDTO = orderService.selectOrderById(orderId);
        log.info("여기!!!!!!!!!!!!!!!! : " + orderDTO);
        model.addAttribute("orderDTO", orderDTO);


        return "content/market/marketorderCompleted"; // Points to the "content/market/marketorderCompleted" template
    }

//    @PostMapping("/cart/cartOrder/{cartId}")
//    public ResponseEntity<Cart> cartOrder(
//            @PathVariable long cartId,
//            @RequestBody List<BuyNowRequestDTO> cartOrders
//    ){
//        log.info("상품 주문 오더 들어왔다");
//        for (BuyNowRequestDTO cartOrder : cartOrders) {
//            log.info("오더들"+cartOrder);
//        }
//
//        return ResponseEntity.ok().build();
//    }
}
