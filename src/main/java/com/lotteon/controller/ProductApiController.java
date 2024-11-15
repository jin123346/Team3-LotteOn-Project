package com.lotteon.controller;


import com.lotteon.dto.admin.MainDataResponseDTO;
import com.lotteon.dto.order.CategoryOrderCountDTO;
import com.lotteon.dto.product.*;
import com.lotteon.dto.product.request.OptionCombinationRequestDTO;
import com.lotteon.entity.product.ProductCategory;
import com.lotteon.service.order.OrderService;
import com.lotteon.service.product.BestProductService;
import com.lotteon.service.product.OptionService;
import com.lotteon.service.product.ProductCategoryService;
import com.lotteon.service.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@RestController
public class ProductApiController {

    private final ProductCategoryService productCategoryService;
    private final ProductService productService;
    private final RedisTemplate redisTemplate;
    private final BestProductService bestProductService;
    private final OptionService optionService;
    private final OrderService orderService;

    @GetMapping("/api/categories")
    public List<ProductCategory> getCategories() {
        return productCategoryService.getCategoryHierarchy();
    }

    @GetMapping("/api/categories/level/{level}")
    public List<ProductCategoryDTO> getCategoriesByLevel(@PathVariable int level) {

        log.info("category : "+productCategoryService.getCategoriesByLevel(level));
        return productCategoryService.getCategoriesByLevel(level);

    }

    @GetMapping("/api/categories/parent/{parentId}")
    public List<ProductCategoryDTO> getCategoriesByParentId(@PathVariable long parentId) {
        log.info("ParentId : "+productCategoryService.getCategoriesByParentId(parentId));

        return productCategoryService.getCategoriesByParentId(parentId);

    }




    @GetMapping("/product/list/ajax")
    public ResponseEntity<ProductListPageResponseDTO> productList(Model model, PageRequestDTO pageRequestDTO, Authentication authentication,
                                                                  @RequestParam(value="pg",required=false,defaultValue = "1") int pg,
                                                                  @RequestParam(value = "type",required = false) String type,
                                                                  @RequestParam(value = "keyword",required = false) String keyword) {
        log.info("일단 여기!!!");
        String user = authentication.getName();
        String role = authentication.getAuthorities().toString();
        log.info("rolE!!!!!!!!!"+role);
        log.info("type, keyword : "+type+keyword);
        pageRequestDTO.setPage(pg);
        pageRequestDTO.setType(type);
        pageRequestDTO.setKeyword(keyword);

        ProductListPageResponseDTO productPageResponseDTO =new ProductListPageResponseDTO();
        if(role.contains("ROLE_ADMIN")) {

            productPageResponseDTO = productService.selectProductAll(pageRequestDTO);
            log.info("확인!!!!!!!!!!!!!!!!!!ADMIN"+productPageResponseDTO);


            log.info("ROLE!!!! : "+productPageResponseDTO);
        }else if(role.contains("ROLE_SELLER")){
            productPageResponseDTO = productService.selectProductBySellerId(user, pageRequestDTO);
            log.info("ROLE_SELLER!!!! : "+productPageResponseDTO);
            log.info("확인!!!!!!!!!!!!!!!!!!"+productPageResponseDTO.getKeyword());

        }

        model.addAttribute("productPageResponseDTO", productPageResponseDTO);
        model.addAttribute("productList", "productList");

        return ResponseEntity.ok(productPageResponseDTO);
    }

    @ResponseBody
    @RequestMapping("/api/best-products")
    public List<ProductRedisDTO> getBestProducts() {
        return bestProductService.getBestProductOrderBySelling();
        // Fetch best products from Redis cache or DB
    }


    @GetMapping(value = "/sse/best-products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamBestProducts() {
        SseEmitter emitter = new SseEmitter(0L); // Timeout을 무제한으로 설정
        try {
            emitter.send(SseEmitter.event()
                    .name("best-products-event")
                    .data(fetchBestProductsFromRedis())); // 예시 메서드로 실제 데이터 가져오기

            emitter.onCompletion(() -> {
                log.info("SSE connection completed");
                emitter.complete();
            });

            emitter.onTimeout(() -> {
                log.warn("SSE connection timed out");
                emitter.complete();
            });

            emitter.onError((ex) -> {
                log.error("Error in SSE connection", ex);
                emitter.completeWithError(ex);
            });

        } catch (IOException e) {
            log.error("Error sending SSE event", e);
            emitter.completeWithError(e);
        }
        return emitter;
    }

//    @RequestMapping(value = "/sse/best-products", produces = "text/event-stream")
//    @ResponseBody
//    public Flux<ServerSentEvent<List<ProductRedisDTO>>> streamBestProducts() {
//        return Flux.interval(Duration.ofSeconds(10))
//                .map(sequence -> ServerSentEvent.<List<ProductRedisDTO>>builder()
//                        .id(String.valueOf(sequence))
//                        .event("best-products-event")
//                        .data(fetchBestProductsFromRedis()) // 캐시에서 가져오되 null일 경우 기본값 설정
//                        .build())
//                .onErrorResume(e -> {
//                    // 에러 발생 시 로그 남기기
//                    System.err.println("Error fetching best products: " + e.getMessage());
//                    // 빈 리스트를 전달하여 스트림 유지
//                    return Flux.just(ServerSentEvent.<List<ProductRedisDTO>>builder()
//                            .id("error-event")
//                            .event("best-products-event")
//                            .data(List.of()) // 에러 시 빈 리스트 전달
//                            .build());
//                });
//    }

    private List<ProductRedisDTO> fetchBestProductsFromRedis() {
        List<ProductRedisDTO> bestProducts = bestProductService.getBestProductOrderBySelling();
        // 캐시에서 데이터가 없을 경우 기본 값 설정 (빈 리스트 등)
        return bestProducts != null ? bestProducts : List.of();
    }

    @PostMapping("/generate-combinations")
    @ResponseBody
    public ResponseEntity<List<String>> generateCombinations(@RequestBody OptionCombinationRequestDTO request) {
        List<OptionCombinationRequestDTO.OptionGroupDTO> optionGroups = request.getOptionGroups();

        log.info("Received option groups: " + optionGroups);

        // 서비스에서 조합을 생성하고 결과를 응답으로 반환
        List<String> combinations = optionService.generateCombinations(optionGroups);
        log.info("combinationsssss " + combinations);

        return ResponseEntity.ok(combinations);
    }


    @ResponseBody
    @GetMapping("/api/getNewData")
    public ResponseEntity<MainDataResponseDTO> adminMainNewData(Model model) {
        List<Long> emptyList=new ArrayList<>();
        log.info("111111111111");
        System.out.println("Request received at /api/getNewData");

        try {
            log.info("222222222");

            // Existing data fetch logic
            List<ProductCategoryDTO> productCategoryDTOS = productCategoryService.getCategoriesByLevel(1);
            List<String> categoryNames = productCategoryDTOS.stream()
                    .map(ProductCategoryDTO::getName)
                    .collect(Collectors.toList());

            List<CategoryOrderCountDTO> newOrderData = orderService.getOrderCountGroupedByCategoryFirstId();
            List<CategoryOrderCountDTO> newCancelData = orderService.getCountCancelGroupedByCategoryFirstId();
            List<CategoryOrderCountDTO> newPaymentData = orderService.getCountPaymentGroupedByCategoryFirstId();

            List<Long> orderCounts = newOrderData.stream()
                    .map(CategoryOrderCountDTO::getOrderCount)
                    .collect(Collectors.toList());

            List<Long> cancelCounts = newCancelData.stream()
                    .map(CategoryOrderCountDTO::getOrderCount)
                    .collect(Collectors.toList());

            List<Long> paymentCounts = newPaymentData.stream()
                    .map(CategoryOrderCountDTO::getOrderCount)
                    .collect(Collectors.toList());

            System.out.println("newOrderData: " + newOrderData);
            System.out.println("newCancelData: " + newCancelData);
            System.out.println("newPaymentData: " + newPaymentData);

            MainDataResponseDTO responseDTO = new MainDataResponseDTO(orderCounts, paymentCounts, cancelCounts, categoryNames);
            log.info("responseDTO!!!!!!"+responseDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error fetching data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MainDataResponseDTO(emptyList,emptyList,emptyList,List.of()));
        }

    }

//    @ResponseBody
//    @GetMapping("/api/list/{category}")
//    public ResponseEntity<ProductListPageResponseDTO> marketList(PageRequestDTO pageRequestDTO, @PathVariable long category
//            , @RequestParam(required = false, defaultValue = "popularity") String sort
//            , @RequestParam(required = false, defaultValue = "1") int page
//            , Model model) {
//        pageRequestDTO.setCategoryId(category);
//        pageRequestDTO.setPage(page);
//        log.debug("Debugging category: " + category);
//
//        log.info("page"+page);
//
//        List<ProductCategoryDTO> categoryDTOs = productCategoryService.getAllParentCategoryDTOs(category);
//        log.info("@222222222222222222222" + categoryDTOs);
//
//        log.info("11111111111111" + pageRequestDTO.getCategoryId());
//        log.info("category:" + category);
////        log.info("dsdsdsdsd2222"+pageRequestDTO);
//        ProductListPageResponseDTO responseDTO = productService.getSortProductList(pageRequestDTO, sort);
//        log.info("controlllermarket::::" + responseDTO.getProductSummaryDTOs());
//        model.addAttribute("categoryDTOs", categoryDTOs);
//        model.addAttribute("responseDTO", responseDTO);
//        model.addAttribute("sort", sort);
//
//
//
//        return ResponseEntity<>
//    }

}
