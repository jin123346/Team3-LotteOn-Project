const productId = document.getElementById("productId").value;
const point = parseInt(document.getElementById("point").value, 10) || 0;
const productName = document.getElementById("productName").value;
const originalPrice = parseFloat(document.getElementById("originalPrice").innerText.replace(/,/g, "")) || 0;
console.log("originalPrice", originalPrice);
const discount = parseInt(document.getElementById("discount").value, 10) || 0;
console.log("discount", discount);
const finalPrice = parseFloat(document.getElementById("finalPrice").innerText.replace(/,/g, ""));
console.log("finalPrice", finalPrice);
const file190 = document.getElementById("file190").value;
const savedPath = document.getElementById("savedPath").value;
const shippingFee = parseInt(document.getElementById("shippingFee").getAttribute("data-shippingfee")) || 0;
let quantity = parseInt(document.getElementById("quantity").value, 10) || 1;
const shippingTerms = document.getElementById("shippingTerms").value;
const buyNowBtn = document.getElementById("buy-now-btn");
const addToCartBtn = document.getElementById("add-to-cart");

const byCart = document.getElementById('buyCart');

document.addEventListener('DOMContentLoaded', function () {


    fetchBestProducts();
    const eventSource = new EventSource('/sse/best-products');
    eventSource.onopen = function(event) {
        console.log("SSE 연결이 열렸습니다.");
    };



    eventSource.onerror = function (error) {
        if (eventSource.readyState === EventSource.CLOSED) {
            console.error("연결이 닫혔습니다.");
        }
        console.error('Error receiving SSE:', error);
        eventSource.close();

        setTimeout(() => {
            // Reconnect after 5 seconds
            new EventSource('http://127.0.0.1:8085/sse/best-products');
        }, 5000);

    };

    eventSource.onmessage = function (event) {
        const data = JSON.parse(event.data);
        updateBestProductList(data);  // Define this function to update the DOM
    };
    // 필요한 요소들 정의
    const optionSelectElements = document.querySelectorAll('.option-select');
    const quantityInput = document.getElementById('quantity');
    const selectResult = document.querySelector('.selectResult');
    const originalTotalPriceElement = document.getElementById('originalTotalPrice');
    const additionalPriceElement = document.getElementById('additionalPrice');
    const totalPriceElement = document.getElementById('totalPrice');
    const totalShippingFeeElement = document.getElementById('totalShippingFee');
    const expectedPriceElement = document.getElementById('expectedPrice');
    const uidElement = document.getElementById("uid");
    const uid = uidElement ? uidElement.value : null;
    const stockStatusElement = document.getElementById("stockStatus"); // Element to show stock status
    const isOptionRequired = optionSelectElements.length > 0; // Checks if options are required

    let selectOptionGroup = [];
    let selectedOptions = []; // Array to store selected options
    let additionalPriceMatch = 0; // 추가 금액 초기화
    let additionalPrice = 0;
    let quantity = parseInt(quantityInput.value, 10) || 1;
    // 수량 증가/감소 버튼 이벤트
    document.getElementById('increase').addEventListener('click', () => {
        quantity++;
        quantityInput.value = quantity;
        updatePrices();
        updateSelectResult();
    });

    document.getElementById('decrease').addEventListener('click', () => {
        if (quantity > 1) {
            quantity--;
            quantityInput.value = quantity;
            updatePrices();
            updateSelectResult();
        }
    });


    optionSelectElements.forEach((select, index) => {
        select.addEventListener('change', function () {

            const selectedOptionValue = this.options[this.selectedIndex].value;
            const itemId = this.options[this.selectedIndex].getAttribute('data-item-id');
            const groupName = this.getAttribute('data-groupname');

            selectedOptions[index] = {
                value: selectedOptionValue,
                itemId: itemId
            };
            selectOptionGroup[index] = `${groupName} ${selectedOptionValue}`;

            console.log(`Selected option (${index}):`, selectedOptionValue);

            // Check if all options are selected
            if (selectedOptions.length === optionSelectElements.length && selectedOptions.every(opt => opt.value)) {
                const matchingCombination = optionCombinations.find(combination => {
                    const optionValues = combination.combination
                        .split(" / ")
                        .map(opt => opt.split(":")[1].trim());
                    return selectedOptions.every(selectedOpt => optionValues.includes(selectedOpt.value));
                });

                additionalPriceMatch = matchingCombination ? parseInt(matchingCombination.additionalPrice) || 0 : 0;
                additionalPrice = additionalPriceMatch;

                if (matchingCombination) {
                    const combinationId = matchingCombination.combinationId;
                    const combinationString = matchingCombination.combination;

                    selectedOptions.forEach(opt => {
                        opt.combinationId = combinationId;
                        opt.combinationString = combinationString;
                    });
                    checkStock(matchingCombination);

                } else {
                    selectedOptions.forEach(opt => {
                        opt.combinationId = null;
                        opt.combinationString = null;
                    });
                }

                applyAdditionalPrice(additionalPriceMatch);


            }

            // Call these functions to ensure `totalPrice` and `selectResult` are updated
            updatePrices();
            updateSelectResult();
        });
    });

    function checkStock(combination) {
        const stock = combination.stock;

        // Display stock status
        if (stock > 0) {
            stockStatusElement.textContent = `주문가능 수량: ${stock} `;
            stockStatusElement.style.color = 'green';
            buyNowBtn.disabled = false;
            addToCartBtn.disabled = false;
        } else {
            stockStatusElement.textContent = "Out of Stock";
            stockStatusElement.style.color = 'red';
            buyNowBtn.disabled = true;
            addToCartBtn.disabled = true;
        }
    }

    function updateSelectResult() {
        const quantity = quantityInput.value;
        console.log("selectOptionGroup:", selectOptionGroup); // Log to check if options are populated
        console.log("Option length check:", selectOptionGroup.length === optionSelectElements.length);
        console.log("All options selected check:", selectOptionGroup.every(opt => opt));

        // 모든 옵션이 선택된 경우 조합과 수량을 표시
        if (selectOptionGroup.length === optionSelectElements.length && selectOptionGroup.every(opt => opt)) {
            const combinationString = selectOptionGroup.join(' / ');
            selectResult.textContent = `[선택한 옵션] ${combinationString} (+${additionalPrice}),  수량: ${quantity}`;
            console.log("Updated selectResult:", selectResult.textContent);

        } else {
            // 옵션이 모두 선택되지 않은 경우 결과 초기화
            selectResult.textContent = '';
            console.log("Not all options selected, cleared selectResult.");

        }
    }


    // 추가 금액을 표시하는 함수
    function applyAdditionalPrice(price) {
        console.log('적용된 추가 금액:', price);
        additionalPrice = parseFloat(price);
        console.log(additionalPrice);
        additionalPriceElement.textContent = `${additionalPrice.toLocaleString()}원`;
    }

    // 가격 계산 및 업데이트 함수
    function updatePrices() {
        const originalPrice = parseFloat(document.getElementById('originalPrice').innerText.replace(/,/g, "")) || 0;
        const discount = parseInt(document.getElementById('discount').value, 10) || 0;

        const totalOriginalPrice = originalPrice * quantity;
        const discountAmount = Math.floor(((discount) * totalOriginalPrice) / 100 / 10) * 10; // 10원 단위 절삭
        console.log("discountAmount", discountAmount);
        console.log("additionalPrice!!!", additionalPrice);
        const finalTotalPrice = totalOriginalPrice + (additionalPrice * quantity) - discountAmount;

        // 배송비 계산 (예시로 3000원을 사용)
        const shippingTerms = parseInt(document.getElementById("shippingTerms").value, 10) || 0;
        const shippingFee = finalTotalPrice >= shippingTerms ? 0 : parseInt(document.getElementById('shippingFee').getAttribute('data-shippingfee')) || 3000;

        // 결제 예상금액 계산
        const expectedPrice = finalTotalPrice + shippingFee;

        // 각 <span> 요소에 값을 업데이트
        originalTotalPriceElement.textContent = `${totalOriginalPrice.toLocaleString()}원`;
        additionalPriceElement.textContent = `${(additionalPrice * quantity).toLocaleString()}원`;
        totalPriceElement.textContent = `-${discountAmount.toLocaleString()}원`;
        totalShippingFeeElement.textContent = `${shippingFee.toLocaleString()}원`;
        expectedPriceElement.textContent = `${expectedPrice.toLocaleString()}원`;
    }

    // 초기 가격 계산
    updatePrices();


    // Event listener for Buy Now button
    document.getElementById("buy-now-btn").addEventListener("click", function (e) {
        if (!uid) {
            alert('로그인 후 이용해 주세요');
            window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
            return;
        } else if (isOptionRequired && selectedOptions.length === 0) {
            alert("옵션을 선택해주세요.");
            return;
        }
        const isConfirmed = confirm("구매하시겠습니까?");
        if (isConfirmed) {
            const productDataArray = [];
            let file = file190;
            console.log("file1",file)

            if (optionSelectElements.length > 0 && selectedOptions.every(opt => opt)) {
                // Include options in the data if options are selected
                productDataArray.push({
                    productId: productId,
                    productName: productName,
                    originalPrice: originalPrice,
                    finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                    quantity: quantity,
                    file190: file,
                    options: selectedOptions.map(opt => ({
                        itemId: opt.itemId,
                        combinationId: opt.combinationId,
                        combinationString: opt.combinationString, // Include combinationString here
                        optionName: opt.value,
                        additionalPrice: additionalPrice
                    })),
                    point: point,
                    discount: discount,
                    shippingFee: shippingFee,
                    shippingTerms: shippingTerms,
                    savedPath: savedPath
                });
            } else {
                // If no options, just send productId and quantity
                productDataArray.push({
                    productId: productId,
                    productName: productName,
                    originalPrice: originalPrice,
                    file190: file,
                    finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                    discount: discount,
                    quantity: quantity,
                    shippingFee: shippingFee,
                    shippingTerms: shippingTerms,
                    savedPath: savedPath

                });
            }
            localStorage.setItem("productDataArray", JSON.stringify(productDataArray));

            console.log("productDataArray", productDataArray);

            fetch("/market/buyNow", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(productDataArray)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.result === "success") {
                        localStorage.setItem("productDataArray", JSON.stringify(productDataArray));
                        window.location.href = `/market/order/${uid}`;
                    } else if (data.result === "login_required") {
                        if (confirm("로그인이 필요합니다. 로그인 하시겠습니까?")) {
                            window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
                        } else {
                            window.reload();
                        }
                    } else if (data.result === "auth") {
                        alert("구매 권한이 없는 계정입니다. 관리자 또는 판매자는 구매할 수 없습니다.");
                    } else {
                        alert("구매 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    alert("구매 처리 중 오류가 발생했습니다.");
                });
        }
    });


    // Initial setting for Add to Cart buttons and functionality
    document.querySelectorAll('.add-to-cart').forEach(btn => {
        btn.addEventListener('click', function () {

            if (!uid) {
                alert('로그인 후 이용해 주세요');
                window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
                return;
            } else if (quantity <= 0) {
                alert('수량을 1 이상으로 설정해 주세요.');
                return;
            } else if (isOptionRequired && selectedOptions.length === 0) {
                alert("옵션을 선택해 주세요");
                return;
            }

            if (confirm("해당 상품을 장바구니에 담으시겠습니까?")) {
                const finalPrice = Math.floor(originalPrice * (100 - discount) / 100);
                const productDataArray = [];

                let file = file190;

                console.log("file1",file)
                if (optionSelectElements.length > 0 && selectedOptions.every(opt => opt)) {


                    // Include options in the data if options are selected
                    productDataArray.push({
                        productId: productId,
                        productName: productName,
                        originalPrice: originalPrice,
                        finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                        quantity: quantity,
                        file190: file,
                        options: selectedOptions.map(opt => ({
                            itemId: opt.itemId,
                            combinationId: opt.combinationId,
                            combinationString: opt.combinationString, // Include combinationString here
                            optionName: opt.value,
                            additionalPrice: additionalPrice
                        })),
                        point: point,
                        discount: discount,
                        shippingFee: shippingFee,
                        shippingTerms: shippingTerms,
                        savedPath: savedPath
                    });
                } else {
                    // If no options, just send productId and quantity
                    productDataArray.push({
                        productId: productId,
                        productName: productName,
                        file190: file,
                        finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                        discount: discount,
                        quantity: quantity,
                        shippingFee: shippingFee,
                        shippingTerms: shippingTerms,
                        savedPath: savedPath
                    });
                }

                console.log("productDataArray", productDataArray);


                localStorage.setItem("productDataArray", JSON.stringify(productDataArray));
                fetch('/api/cart', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(productDataArray)
                })
                    .then(resp => {
                        if (!resp.ok) throw new Error(`서버 응답 오류: ${resp.status}`);
                        return resp.json();
                    })
                    .then(data => {
                        if (data.status === 200) {
                            const Isconfirm = confirm('장바구니에 추가되었습니다. 장바구니로 이동하시겠습니까?');

                            if(Isconfirm) {
                                window.location.href = `/market/cart`;
                            }

                        } else if (data.status === 401) {
                            alert('로그인 후 이용해 주세요. 로그인하시면 더 많은 혜택을 확인하실 수 있습니다!');
                            window.location.href = '/user/login';
                        } else {
                            alert('장바구니에 추가하는 데 문제가 발생했습니다. 다시 시도해 주세요.');
                        }
                    })
                    .catch(error => {
                        console.error('장바구니에 추가하는 데 실패함:', error);
                        alert('장바구니에 추가하는 데 문제가 발생했습니다. 다시 시도해 주세요.');
                    });
            }
        });
    });

    document.querySelectorAll('.rating-display').forEach(display => {
        const rating = parseInt(display.textContent);
        let stars = '';

        for (let i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars += '<span class="star-selected">&#9733;</span>'; // 선택된 별
            } else {
                stars += '<span class="star">&#9734;</span>'; // 선택되지 않은 별
            }
        }

        display.innerHTML = stars; // 별 모양으로 업데이트
    });


    let currentIndex = 0; // 현재 보여주는 이미지 인덱스
    const images = document.querySelector('.review-images');
    const totalImages = document.querySelectorAll('.reviewImg').length; // 전체 이미지 수

    document.addEventListener("DOMContentLoaded", function () {
        const reviewImagesContainer = document.querySelector(".review-images");
        const leftArrow = document.getElementById("leftArrow");
        const rightArrow = document.getElementById("rightArrow");

        // 이미지가 있을 때만 화살표를 표시
        if (reviewImagesContainer && reviewImagesContainer.querySelector(".reviewImg")) {
            leftArrow.style.display = "block";
            rightArrow.style.display = "block";
        }
    });

// 왼쪽 화살표 클릭 이벤트
    document.getElementById('leftArrow').addEventListener('click', function () {
        if (currentIndex > 0) {
            currentIndex--;
            updateSlide();
        }
    });

// 오른쪽 화살표 클릭 이벤트
    document.getElementById('rightArrow').addEventListener('click', function () {
        if (currentIndex < totalImages - 5) {
            currentIndex++;
            updateSlide();
        }
    });

// 슬라이드 업데이트 함수
    function updateSlide() {
        const offset = currentIndex * (152 + 10); // 이미지 너비 + 마진을 고려 (150px + 10px)
        images.style.transform = `translateX(${-offset}px)`; // 이미지 슬라이드

        // 오른쪽 화살표 비활성화 효과
        if (currentIndex === totalImages - 5) {
            document.getElementById('rightArrow').classList.add('disabled');
        } else {
            document.getElementById('rightArrow').classList.remove('disabled');
        }

        // 왼쪽 화살표 비활성화 효과
        if (currentIndex === 0) {
            document.getElementById('leftArrow').classList.add('disabled');
        } else {
            document.getElementById('leftArrow').classList.remove('disabled');
        }
    }

// 초기 버튼 상태 설정
    updateSlide();
// price 클래스를 가진 모든 요소를 선택
    const priceElements = document.querySelectorAll('.price');

// 각 price 요소에 대해 반복하여 처리
    priceElements.forEach(priceElement => {
        let priceValue = priceElement.textContent.trim().replace(/[^0-9]/g, ''); // 숫자가 아닌 문자를 제거
        priceValue = parseInt(priceValue, 10); // 정수로 변환

        if (!isNaN(priceValue)) { // 변환된 값이 NaN이 아닌 경우에만 적용
            priceElement.textContent = priceValue.toLocaleString();  // 천단위로 쉼표 추가
        }
    });



    function calculateShippingFee(totalPrice) {
        // totalPrice와 shippingTerms 비교하여 shippingFee 결정
        const totalShippingFee = totalPrice >= parseInt(shippingTerms) ? 0 : shippingFee;
        document.getElementById("totalShippingFee").innerText = `${totalShippingFee.toLocaleString()}원`;
        return totalShippingFee;
    }

    function updateExpectedTotal(totalPrice, totalShippingFee) {
        // totalPrice와 totalShippingFee 합산하여 결제 예상금액 설정
        const expectedPrice = totalPrice + totalShippingFee;
        document.getElementById("expectedPrice").innerText = `${expectedPrice.toLocaleString()}원`;
    }


    function updateExpectedTotal(totalPrice, totalShippingFee) {
        // totalPrice와 totalShippingFee 합산하여 결제 예상금액 설정
        const expectedPrice = totalPrice + totalShippingFee;
        document.getElementById("expectedPrice").innerText = `${expectedPrice.toLocaleString()}원`;
    }






// 모달 엘리먼트와 버튼, 닫기 버튼 가져오기
    const modal = document.getElementById("discountModal");
    const btn = document.getElementById("openDiscountModalBtn");
    const span = document.getElementsByClassName("discount-modal-close")[0];

// 버튼을 클릭하면 모달 표시
    btn.onclick = function () {
        modal.style.display = "flex";
        document.body.style.overflow = 'hidden';

        const productId = document.getElementById('productId').value;
        fetchCoupons(productId);

        setTimeout(updateCouponButtonState, 100);  // 버튼 상태 업데이트
    }

// 닫기 버튼을 클릭하면 모달 숨기기
    span.onclick = function () {
        modal.style.display = "none";
        document.body.style.overflow = ''; // 모달 닫을 때 배경 스크롤 복구

    }
    modal.onclick = function (event) {
        // 모달 밖을 클릭한 경우
        if (event.target === modal) {
            modal.style.display = "none";
            document.body.style.overflow = ''; // 모달 닫을 때 배경 스크롤 복구
        }
    }
});


function fetchCoupons(productId) {

    let url = productId ? `/api/coupon/${productId}` : '/api/coupon/all/coupons';
    console.log("프로덕트 아이디 : ", productId);
    fetch(url)
        .then(resp => {
            if (!resp.ok) {
                throw new Error('인터넷연결 문제');
            }
            // 응답의 Content-Type이 JSON인지 확인
            const contentType = resp.headers.get("Content-Type");
            if (!contentType || !contentType.includes("application/json")) {
                return resp.text().then(text => {
                    throw new Error('응답이 JSON이 아닙니다');
                });
            }
            return resp.json();
        })
        .then(data => {
            displayCoupons(data);
        })
        .catch(err => {
            console.error(err,'요청중 문제 발생함')
        })
}

// 쿠폰 목록 표시하기
function displayCoupons(coupons) {
    const couponContainer = document.getElementById('discountCouponItems');
    couponContainer.innerHTML = '';

    if (coupons.length === 0) {
        const noCoupon = document.createElement('div');
        noCoupon.className = 'no-coupon';
        noCoupon.textContent = '등록된 쿠폰이 없습니다.';
        couponContainer.appendChild(noCoupon);
    } else {
        coupons.forEach(coupon => {
            const couponItem = document.createElement('div');
            couponItem.className = 'discount-coupon-item';
            couponItem.innerHTML = `
               <div class="discount-coupon-info">
                    <div class="discount-amount">${coupon.couponName} (${coupon.benefit})</div>
                    <div class="discount-description">${coupon.notes}</div>
                    <div class="discount-restrictions">${coupon.restrictions}</div>

                    <div class="discount-dates">
                        <span>유효기간: ${coupon.startDate} ~ ${coupon.endDate}</span>
                    </div>
                </div>
                <button class="discount-apply-btn" onclick="applyCoupon('${coupon.couponId}')">발급받기</button>
            `;
            couponContainer.appendChild(couponItem);
        });
    }
    setTimeout(updateCouponButtonState, 100);  // 버튼 상태 업데이트

}

function applyCoupon(couponId){
    const uid = localStorage.getItem('uid');  // 또는 localStorage.getItem('uid') 사용
    console.log(localStorage.getItem('uid')); // 'user123'
/*
    if (!uid) {
        alert('로그인 후 이용해 주세요');
        window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
        return;
    }*/

    if(!confirm("이 쿠폰을 발급 받으시겠습니까?")){
        return;
    }

    fetch(`/api/coupon/apply/${couponId}`,{
        method: 'POST',
        headers: {'Content-Type': 'application/json',},
    })
        .then(resp => {
            if(resp.ok){
                return resp.json()
            }else {
                throw  new Error("쿠폰 적용에 실패함 ㅋ")
            }
        })
        .then(data => {
            // 쿠폰 발급 성공 후 버튼 상태 변경
            const button = document.querySelector(`button[onclick="applyCoupon('${couponId}')"]`);
            if (button) {
                button.textContent = '발급완료'; // 발급 완료 버튼 텍스트로 변경
                button.disabled = true; // 비활성화
                button.style.backgroundColor = '#28a745'; // 초록색 배경
                button.style.color = 'white'; // 흰색 텍스트
                button.style.border = '1px solid #28a745'; // 초록색 테두리
                button.style.cursor = 'not-allowed'; // 마우스 커서 변경
            }
            alert(`쿠폰이 성공적으로 적용되었습니다.`);

        })
        .catch(err => {
            console.error(err)
        })


}
function updateCouponButtonState() {
    const buttons = document.querySelectorAll('button[onclick^="applyCoupon"]');
    console.log("요청들어옴")

    buttons.forEach(button => {
        const onclickValue = button.getAttribute('onclick');
        console.log(onclickValue);  // 버튼의 onclick 속성 값 출력
        const couponIdMatch = onclickValue.match(/'([^']+)'/);

        if (couponIdMatch) {
            const couponId = couponIdMatch[1];
            console.log(`Coupon ID: ${couponId}`);  // 쿠폰 ID 확인

            // 서버로 쿠폰 발급 여부 확인
            fetch(`/api/coupon/check/${couponId}`, {
                method: 'GET',
                headers: {'Content-Type': 'application/json'},
            })
                .then(resp => resp.json())
                .then(isIssued => {
                    console.log(`Is coupon issued: ${isIssued}`);  // 발급 여부 확인
                    if (isIssued) {
                        button.textContent = '발급완료'; // 발급 완료 버튼 텍스트로 변경
                        button.disabled = true; // 비활성화
                        button.style.backgroundColor = '#28a745'; // 초록색 배경
                        button.style.color = 'white'; // 흰색 텍스트
                        button.style.border = '1px solid #28a745'; // 초록색 테두리
                        button.style.cursor = 'not-allowed'; // 마우스 커서 변경
                    }
                })
                .catch(err => console.error("쿠폰 발급 여부 확인 실패:", err));
        } else {
            console.error("쿠폰 ID 추출 실패");
        }
    });
}



function fetchBestProducts() {
    fetch('/api/best-products')
        .then(response => response.json())
        .then(data => updateBestProductList(data))
        .catch(error => console.error('Error fetching best products:', error));
}

function updateBestProductList(products) {
    const bestProductContainer = document.querySelector('.bestProduct');
    bestProductContainer.innerHTML = `
        <span><img src="/images/common/aside_best_product_tagR.png" alt="태그">베스트 상품</span>
    `;

    // Loop over products and append to the bestProductContainer
    products.forEach((product, index) => {
        const imgSrc = product.savedPath
            ? `/uploads/${product.savedPath}/${product.file230}`
            : `/uploads/productImg/${product.file230}`;

        const productHTML = `
            <div class="products ${index === 0 ? 'first' : ''}">
                <div class="productimg ${index === 0 ? 'first' : ''}">
                    <img src="${imgSrc}" alt="">
                    <p>${index + 1}</p>
                </div>
                <div class="productInfo">
                    <span class="product-name">${product.productName}</span>
                    <span class="original-price">${product.originalPrice.toLocaleString()}원</span>
                    <span class="discounted-price">
                        <p class="discount-rate">${product.discount}%</p> <p>↓</p>
                    </span>
                    <span class="final-price">${product.finalPrice.toLocaleString()}원</span>
                </div>
            </div>
        `;
        bestProductContainer.insertAdjacentHTML('beforeend', productHTML);
    });
}