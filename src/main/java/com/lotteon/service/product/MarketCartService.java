package com.lotteon.service.product;

import com.lotteon.config.RedirectToLoginException;
import com.lotteon.dto.order.DeliveryStatus;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.dto.product.OptionItemDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.dto.product.ProductOptionCombinationDTO;
import com.lotteon.dto.product.cart.CartItemDTO;
import com.lotteon.dto.product.cart.CartRequestDTO;
import com.lotteon.dto.product.cart.CartSummary;
import com.lotteon.dto.product.request.BuyNowRequestDTO;
import com.lotteon.entity.User.User;
import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductOptionCombination;
import com.lotteon.repository.cart.CartItemRepository;
import com.lotteon.repository.cart.CartRepository;
import com.lotteon.repository.product.OptionRepository;
import com.lotteon.repository.product.ProductOptionCombinationRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MarketCartService {
    private final ModelMapper modelMapper;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;
    private final ProductOptionCombinationRepository productOptionCombinationRepository;


    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RedirectToLoginException("사용자가 인증되지 않았습니다.");
        }

        String username = authentication.getName();

        try {
            return userRepository.findByUid(username)
                    .orElseThrow(() -> new UsernameNotFoundException("유저가 없습니다."));
        } catch (UsernameNotFoundException e) {
            // 사용자 정보를 찾지 못한 경우 로그인 페이지로 리다이렉트
            throw new RedirectToLoginException("사용자가 인증되지 않았습니다.");
        }
    }

    public CartItem insertCartItem(BuyNowRequestDTO cartRequestDTO) {


        Optional<Product> opt = productRepository.findByProductId(Long.valueOf(cartRequestDTO.getProductId()));
        ProductDTO productDTO = new ProductDTO();
        if(opt.isPresent()) {
            Product product =opt.get();
            productDTO = product.toDTO(product);

        }
        List<OptionItemDTO> options = cartRequestDTO.getOptions();

        ProductOptionCombination optionCombination = ProductOptionCombination.builder().build();
        if(options != null && !options.isEmpty()) {
            Optional<ProductOptionCombination> optC = productOptionCombinationRepository.findById(options.get(0).getCombinationId());
            if(optC.isPresent()) {
                optionCombination = optC.get();
            }
        }



        if (cartRequestDTO.getQuantity() <= 0) {
            throw new RuntimeException("수량은 1 이상이어야 합니다,.");
        }




        User user = getUser();

        // 사용자의 장바구니를 가져오고, 없으면 새로 생성
        Cart cart = cartRepository.findByUserWithItems(user)
                .orElseGet(() -> {
                    log.info("카트가 없어서 새로 생성합니다.");
                    return createCart(user);
                });

        log.info("현재 카트 ID: {}", cart.getCartId());

        Product product = productRepository.findById(Long.parseLong(cartRequestDTO.getProductId()))
                .orElseThrow(() -> new RuntimeException("상품이 없습니다."));

//        Option option = optionRepository.findById(Long.valueOf(cartRequestDTO.()))
//                .orElseThrow(() -> {
//                    log.error("옵션 ID가 유효하지 않습니다: {}", cartRequestDTO.getOptionId());
//                    return new RuntimeException("옵션이 없습니다.");
//                });

        Optional<CartItem> existingItem;
        if (optionCombination != null && optionCombination.getCombinationId() != null) {
            existingItem = cartItemRepository.findByCart_CartIdAndProduct_ProductIdAndOptionCombination_CombinationId(
                    cart.getCartId(), product.getProductId(), optionCombination.getCombinationId()
            );

        } else {
            existingItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(cart.getCartId(), product.getProductId());
        }

        CartItem SaveItem = null;
//
        if (existingItem.isPresent()) {
            // 아이템이 이미 존재하는 경우 수량 업데이트
            SaveItem = existingItem.get();

            long quantity =cartRequestDTO.getQuantity();
            int newQuantity = SaveItem.getQuantity() + cartRequestDTO.getQuantity();
            SaveItem.getTotalPrice();

            SaveItem.setQuantity(newQuantity); // 수량 업데이트
            cartItemRepository.save(SaveItem);

            log.info("기존 아이템 업데이트: {}, 새로운 수량: {}", SaveItem.getProduct().getProductName(), newQuantity);

            return SaveItem; // 업데이트된 아이템 반환
        } else {
            // 새로운 아이템 생성 및 저장


            CartItem newCartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .price(product.getPrice())
                    .productName(product.getProductName())
                    .quantity(cartRequestDTO.getQuantity())
                    .deliveryFee(parseLongOrDefault(cartRequestDTO.getShippingFee(),0))
                    .discount(product.getDiscount())
                    .deliveryFee(Integer.parseInt(cartRequestDTO.getShippingFee()))
                    .price( parseLongOrDefault(cartRequestDTO.getFinalPrice(),0))
                    .imageUrl(cartRequestDTO.getFile190())
                    .build();

            newCartItem.getTotalPrice();
            // Conditionally add option combination if available
            if (optionCombination != null && optionCombination.getCombinationId() != null) {
                newCartItem.setProductOptionCombination(optionCombination);
            }

            newCartItem.totalPrice();
            cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem); // 카트에 새 아이템 추가

            log.info("새로운 아이템 추가: {}, 수량: {}", newCartItem.getProductName(), newCartItem.getQuantity());
                // 카트의 총 수량과 총 가격 업데이트
                updateCartSummary(cart);
            return null;

        }



    }
    private long parseLongOrDefault(String value, long defaultValue) {
        try {
            return value != null ? Long.parseLong(value.replaceAll(",", "")) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void updateCartSummary(Cart cart) {

        int totalItemCount = (int) cart.getCartItems().stream()
                .map(item -> item.getProductOptionCombination() != null && item.getProductOptionCombination().getCombinationId() != null
                        ? item.getProductOptionCombination().getCombinationId()
                        : item.getProduct().getProductId())
                .distinct()
                .count();

        cart.setItemQuantity(totalItemCount);
        cartRepository.save(cart); // 카트 저장
        log.info("업데이트된 카트 아이템 수량: {}", cart.getTotalPrice());
        log.info("업데이트된 카트 아이템 수량: {}", totalItemCount);

    }


    // 새로운 장바구니를 생성
    private Cart createCart(User user) {
        Cart newCart = Cart.builder()
                .user(user)
                .cartItems(new ArrayList<>())
                .rdate(LocalDateTime.now())
                .build();

        log.info("cart --------------" + newCart);
        return cartRepository.save(newCart);
    }

    //유저 검색후
    public List<CartItem> selectCartAll(){

        User user = getUser();
        log.info("user uid`````````------------------" + user.getUid());

        Cart cart = cartRepository.findByUser_Uid(user.getUid()).orElse(null);

        if (cart == null) {
            log.info("카트가 없습니다.");
            return Collections.emptyList(); // 빈 리스트 반환
        }

        List<CartItem> cartItems = cart.getCartItems();

        log.info("cartItems count!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " + cartItems.size());
        return cartItems;
    }

    public List<CartItemDTO> convertToCartItemDTO(List<CartItem> cartItems) {

        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        for(CartItem cartItem : cartItems) {
           long additionalPrice=0;
            if(cartItem.getProductOptionCombination() != null) {
                additionalPrice = cartItem.getProductOptionCombination().getAdditionalPrice();
            }
            long discount = cartItem.getDiscount();
            long basePrice = cartItem.getProduct().getPrice() + additionalPrice; // 기본 가격 (원래 가격 + 추가 가격)
            long discountAmount = Math.round(basePrice * discount / 100.0); // 할인 금액 계산 (반올림 적용)
            long finalPrice = (basePrice - discountAmount) * cartItem.getQuantity(); // 최종 가격 계산

// 배송비 조건 확인 및 적용
            long shippingTerm = cartItem.getProduct().getShippingTerms();
            long deliveryFee = cartItem.getProduct().getShippingFee();
            if (finalPrice >= shippingTerm) {
                deliveryFee = 0; // 무료 배송 조건 충족 시 배송비 0
            }

            // CartItemDTO 생성 및 필드 설정
            CartItemDTO cartItemDTO = CartItemDTO.builder()
                    .cartId(cartItem.getCart().getCartId())
                    .cartItemId(cartItem.getCartItemId())
                    .productId(String.valueOf(cartItem.getProduct().getProductId()))
                    .productName(cartItem.getProductName())
                    .discount(cartItem.getDiscount())
                    .originalPrice(cartItem.getPrice())
                    .additionalPrice(additionalPrice)
                    .finalPrice(finalPrice)
                    .calcOriginalPrice(basePrice)
                    .quantity(cartItem.getQuantity())
                    .file190(cartItem.getProduct().getFile190())
                    .optionGroupId(cartItem.getOptionGroupId())
                    .optionName(cartItem.getOptionName())
                    .point(String.valueOf(cartItem.getPoints()))
                    .shippingFee(cartItem.getDeliveryFee())
                    .deliveryFee((int) deliveryFee)
                    .ShippingTerms(cartItem.getProduct().getShippingTerms())
                    .expectedPoint(cartItem.getPoints())
                    .combinationDTO(cartItem.getProductOptionCombination() != null
                            ? cartItem.getProductOptionCombination().toDTO()
                            : null) // 변환된 옵션 리스트 추가
                    .stock(Math.toIntExact(cartItem.getProduct().getStock()))
                    .price(cartItem.getPrice())
                    .productDTO(cartItem.getProduct().toDTO(cartItem.getProduct()))
                    .total(cartItem.getTotalPrice())
                    .imageUrl(cartItem.getImageUrl())
                    .points(cartItem.getPoints())
                    .calcShippingCost(deliveryFee)
                    .build();

            // 리스트에 추가
            cartItemDTOS.add(cartItemDTO);

        }



        return cartItemDTOS;
    }


    public CartSummary calculateSelectedCartSummary(List<CartItemDTO> selectedItems) {

        int totalQuantity = selectedItems.stream().mapToInt(CartItemDTO::getQuantity).sum();
        long totalPrice = selectedItems.stream()
                .mapToLong(item -> (long) item.getCalcOriginalPrice()*item.getQuantity()).sum();
        long totalDiscount = selectedItems.stream()
                .mapToLong(item -> (long)Math.round( item.getCalcOriginalPrice() *  item.getDiscount() / 100.0)*item.getQuantity()).sum();
        long totalDeliveryFee = selectedItems.stream()
                .mapToLong(CartItemDTO::getDeliveryFee).sum();
        long totalOrderPrice = totalPrice - totalDiscount + totalDeliveryFee; // 배송비 더하기
        long totalPoints = selectedItems.stream().mapToLong(CartItemDTO::getPoints).sum();

        log.info("totalQuantity :"+totalQuantity);
        log.info("totalPrice :"+totalPrice);
        log.info("totalDiscount :"+totalDiscount);
        log.info("totalDeliveryFee :"+totalDeliveryFee);
        log.info("totalOrderPrice :"+totalOrderPrice);
        log.info("totalPoints :"+totalPoints);
        return CartSummary.builder()
                .finalTotalDeliveryFee(totalDeliveryFee)
                .finalTotalPrice(totalPrice)
                .finalTotalQuantity(totalQuantity)
                .finalTotalDiscount(totalDiscount)
                .finalTotalOrderPrice(totalOrderPrice)
                .finalTotalPoints(totalPoints)
                .build();
    }


    @Transactional
    public void updateQuantity(Long cartItemId, int quantity) {

        log.info("일단 여기까진 들어옴");
        if (quantity < 1) {
            throw new RuntimeException("수량은 1 이상이어야 합니다.");
        }
        log.info("Received cartItemId: {}, quantity: {}", cartItemId, quantity);

        log.info("1111111111111111:"+cartItemId);
        log.info("2222222222222222:"+quantity);

        try {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("장바구니에 해당 상품이 없습니다."));
            log.info("카트 아이템이다"+cartItemId);

            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            log.info("상품 {}의 수량이 {}로 업데이트되었습니다.", cartItemId, quantity);
        } catch (RuntimeException e) {
            log.error("오류 발생: {}", e.getMessage());
            throw e; // 예외를 다시 던져서 컨트롤러에서 처리
        }

    }

    public void deleteCartItem(List<Long> cartItemIds) {
        Long cartId = null; // 카트 ID를 저장할 변수
        int deletedItemCount = cartItemIds.size(); // 삭제할 아이템 수

        // 카트의 아이템 수 확인 (초기 아이템 수)
        if (!cartItemIds.isEmpty()) {
            CartItem firstItem = cartItemRepository.findById(cartItemIds.get(0))
                    .orElseThrow(() -> new RuntimeException("장바구니에 해당 상품이 읍다"));
            cartId = firstItem.getCart().getCartId();
        }

        for (Long cartItemId : cartItemIds) {
            // 카트 아이템을 먼저 조회하여 카트 ID를 저장
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("장바구니에 해당 상품이 읍다"));
            // 카트 ID 저장
            if (cartId == null) {
                cartId = cartItem.getCart().getCartId();
            }

            cartItemRepository.delete(cartItem);
            cartItemRepository.flush();

            log.info("삭제 완료했다: cartItemId = " + cartItemId);

        }
        // 카트에 남은 아이템 수 확인
        long itemCount = cartItemRepository.countByCart_CartId(cartId);
        log.info("남은 아이템수!!!!!!!!!"+itemCount);
        log.info("삭제한 아이템 수: " + deletedItemCount + ", 삭제된 아이템 IDs: " + cartItemIds);

        if (itemCount == 0) {
           cartRepository.deleteById(cartId);
            log.info("카트 삭제 완료: cartId = " + cartId);
        } else {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("상품 아이디가 읍다"));

            updateCartSummary(cart);

        }
    }






}
