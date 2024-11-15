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
const shippingFee = parseInt(document.getElementById("shippingFee").getAttribute("data-shippingfee")) || 0;
let quantity = parseInt(document.getElementById("quantity").value, 10) || 1;
const shippingTerms = document.getElementById("shippingTerms").value;
const buyNowBtn = document.getElementById("buy-now-btn");
const addToCartBtn = document.getElementById("add-to-cart");

const byCart = document.getElementById('buyCart');
const optionOElements=document.getElementById("option-o");
const optionGroupContainer=document.getElementById("optionGroupContainer");
document.addEventListener('DOMContentLoaded', function () {
    // 공통 요소 초기화
    const uidElement = document.getElementById("uid");
    const uid = uidElement ? uidElement.value : null;
    const productId = document.getElementById("productId").value;
    const originalPrice = parseFloat(document.getElementById("originalPrice").innerText.replace(/,/g, "")) || 0;
    const discount = parseInt(document.getElementById("discount").value, 10) || 0;
    const shippingFee = parseInt(document.getElementById("shippingFee").getAttribute("data-shippingfee")) || 0;
    const shippingTerms = document.getElementById("shippingTerms").value;
    let quantity = parseInt(document.getElementById("quantity").value, 10) || 1;

    // 모듈 선택 및 실행
    const optionOElements = document.getElementById("option-o");
    const optionGroupContainer = document.getElementById("optionGroupContainer");

    if (!optionOElements && !optionGroupContainer) {
        noOptionModule();
    } else if (optionOElements) {
        singleOptionModule();
    } else if (optionGroupContainer) {
        optionGroupModule();
    }

    const quantityInput = document.getElementById("quantity");




    function noOptionModule() {

        function updateTotalPrice() {
            const totalOriginalPrice = originalPrice * quantity;
            const discountAmount = Math.floor((originalPrice * discount) / 100) * quantity;
            const finalPrice = totalOriginalPrice - discountAmount;
            document.getElementById("originalTotalPrice").innerText = `${totalOriginalPrice.toLocaleString()}원`;
            document.getElementById("totalPrice").innerText = `-${discountAmount.toLocaleString()}원`;

            const shippingFeeAmount = finalPrice >= shippingTerms ? 0 : shippingFee;
            updateExpectedTotal(finalPrice, shippingFeeAmount);
        }
        document.getElementById('increase').addEventListener('click', () => {
            quantity++;
            quantityInput.value = quantity;  // Update the input field value
            updateTotalPrice();
        });

        document.getElementById('decrease').addEventListener('click', () => {
            if (quantity > 1) {
                quantity--;
                quantityInput.value = quantity;  // Update the input field value
                updateTotalPrice();
            }
        });


        updateTotalPrice();
        const uid = document.getElementById("uid").value;

        function getProductData() {
            return [{
                productId: productId,
                productName: productName,
                originalPrice: originalPrice,
                finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                quantity: quantity,
                file190: file190,
                point: point,
                discount: discount,
                shippingFee: shippingFee,
                shippingTerms: shippingTerms
            }];
        }

        document.getElementById("buy-now-btn").addEventListener("click", function() {
            const productDataArray = getProductData();
            buyNowHandler(productDataArray, uid);
        });

        document.querySelector('.add-to-cart').addEventListener("click", function() {
            const productDataArray = getProductData();
            addToCartHandler(productDataArray, uid);
        });


    }

    function singleOptionModule() {
        let selectedOptions = [];

        function addOrUpdateSelection(optionId, optionText, optionDesc, quantity) {
            const existingOption = selectedOptions.find(option => option.optionId === optionId);
            if (existingOption) {
                existingOption.quantity = quantity;
            } else {
                selectedOptions.push({ optionId, optionText, optionDesc, quantity });
            }
            updateSelectedResult();
        }

        function updateSelectedResult() {
            const selectResult = document.querySelector(".selectResult");
            selectResult.innerHTML = "";
            selectedOptions.forEach((option, index) => {
                selectResult.innerHTML += `
                <div class="option-detail">
                    ${option.optionText} (${option.optionDesc}) - 수량: ${option.quantity}
                    <button class="remove-option" onclick="removeOption(${index})">제거</button>
                </div>`;
            });
            updateTotalPrice();
            const uid = document.getElementById("uid").value;

            function getProductData() {
                return selectedOptions.map(option => ({
                    productId: productId,
                    productName: productName,
                    originalPrice: originalPrice,
                    finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
                    quantity: option.quantity,
                    file190: file190,
                    optionId: option.optionId,
                    optionName: option.optionText,
                    optionDesc: option.optionDesc,
                    point: point,
                    discount: discount,
                    shippingFee: shippingFee,
                    shippingTerms: shippingTerms
                }));
            }

            document.getElementById("buy-now-btn").addEventListener("click", function() {
                const productDataArray = getProductData();
                buyNowHandler(productDataArray, uid);
            });

            document.querySelector('.add-to-cart').addEventListener("click", function() {
                const productDataArray = getProductData();
                addToCartHandler(productDataArray, uid);
            });
        }

        function removeOption(index) {
            selectedOptions.splice(index, 1);
            updateSelectedResult();
        }

        function updateTotalPrice() {
            let totalOriginalPrice = selectedOptions.reduce((sum, option) => sum + originalPrice * option.quantity, 0);
            const totalDiscount = Math.floor(totalOriginalPrice * discount / 100);
            const finalPrice = totalOriginalPrice - totalDiscount;

            document.getElementById("originalTotalPrice").innerText = `${totalOriginalPrice.toLocaleString()}원`;
            document.getElementById("totalPrice").innerText = `-${totalDiscount.toLocaleString()}원`;

            const shippingFeeAmount = finalPrice >= shippingTerms ? 0 : shippingFee;
            updateExpectedTotal(finalPrice, shippingFeeAmount);
        }

        document.getElementById('increase').addEventListener('click', () => {
            quantity++;
            quantityInput.value = quantity;
            updateTotalPrice();
        });

        document.getElementById('decrease').addEventListener('click', () => {
            if (quantity > 1) {
                quantity--;
                quantityInput.value = quantity;
                updateTotalPrice();
            }
        });

        updateTotalPrice();

        document.getElementById("option-o").addEventListener("change", function () {
            const optionId = this.value;
            const optionText = this.options[this.selectedIndex].text;
            addOrUpdateSelection(optionId, optionText, "", quantity);
        });

        updateTotalPrice();


    }


    function optionGroupModule() {
        let selectedOptions = [];

        function updateSelectResult() {
            const selectResult = document.querySelector(".selectResult");
            const selectedText = selectedOptions.map(opt => `${opt.groupName}: ${opt.value}`).join(", ");
            selectResult.innerText = `[선택한 옵션 조합] ${selectedText}`;
            updateTotalPrice();
        }

        function updateTotalPrice() {
            let totalOriginalPrice = selectedOptions.reduce((sum, option) => sum + originalPrice * option.quantity, 0);
            const totalDiscount = Math.floor(totalOriginalPrice * discount / 100);
            const finalPrice = totalOriginalPrice - totalDiscount;

            document.getElementById("originalTotalPrice").innerText = `${totalOriginalPrice.toLocaleString()}원`;
            document.getElementById("totalPrice").innerText = `-${totalDiscount.toLocaleString()}원`;

            const shippingFee = finalPrice >= shippingTerms ? 0 : shippingFee;
            updateExpectedTotal(finalPrice, shippingFee);
        }

        function addOptionGroupListeners() {
            document.querySelectorAll('.option-select').forEach(select => {
                select.addEventListener('change', function () {
                    const value = this.value;
                    const groupName = this.getAttribute('data-groupname');
                    const index = Array.from(this.parentNode.children).indexOf(this);

                    selectedOptions[index] = { groupName, value, quantity };
                    updateSelectResult();
                });
            });
        }

        addOptionGroupListeners();
        updateTotalPrice();
        const uid = document.getElementById("uid").value;

        function getProductData() {
            return selectedOptions.map(option => ({
                productId: productId,
                productName: productName,
                originalPrice: originalPrice,
                finalPrice: Math.floor(originalPrice * (100 - discount) / 100) + additionalPrice,
                quantity: quantity,
                file190: file190,
                optionId: option.optionId,
                optionName: option.optionText,
                combinationId: option.combinationId,
                combinationString: option.combinationString,
                point: point,
                discount: discount,
                shippingFee: shippingFee,
                shippingTerms: shippingTerms
            }));
        }

        document.getElementById("buy-now-btn").addEventListener("click", function() {
            const productDataArray = getProductData();
            buyNowHandler(productDataArray, uid);
        });

        document.querySelector('.add-to-cart').addEventListener("click", function() {
            const productDataArray = getProductData();
            addToCartHandler(productDataArray, uid);
        });
        addOptionGroupListeners();
        updateTotalPrice();
    }


    function updateExpectedTotal(totalPrice, shippingFee) {
        const expectedPrice = totalPrice + shippingFee;
        document.getElementById("expectedPrice").innerText = `${expectedPrice.toLocaleString()}원`;
    }



    function buyNowHandler(productDataArray, uid) {
        if (!uid) {
            alert('로그인 후 이용해 주세요');
            window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
            return;
        }

        if (confirm("구매하시겠습니까?")) {
            fetch("/market/buyNow", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(productDataArray)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.result === "success") {
                        localStorage.setItem("productDataArray", JSON.stringify(productDataArray));
                        window.location.href = `/market/order/${uid}`;
                    } else {
                        handleBuyNowErrors(data);
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    alert("구매 처리 중 오류가 발생했습니다.");
                });
        }
    }

    function addToCartHandler(productDataArray, uid) {
        if (!uid) {
            alert('로그인 후 이용해 주세요');
            window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
            return;
        }

        if (confirm("장바구니에 추가 하시겠습니까?")) {
            fetch('/api/cart', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(productDataArray)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.status === 200) {
                        alert('장바구니에 추가 되었습니다!');
                    } else {
                        handleAddToCartErrors(data);
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    alert('장바구니에 추가하는 데 실패하셨습니다.');
                });
        }
    }

    function handleBuyNowErrors(data) {
        if (data.result === "login_required") {
            if (confirm("로그인이 필요합니다. 로그인 하시겠습니까?")) {
                window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
            }
        } else if (data.result === "auth") {
            alert("구매 권한이 없는 계정입니다. 관리자 또는 판매자는 구매할 수 없습니다.");
        } else {
            alert("구매 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
        }
    }

    function handleAddToCartErrors(data) {
        if (data.status === 401) {
            alert('로그인 없이 이곳은 접근 금지! 빨리 로그인해 주세요');
            window.location.href = '/user/login';
        } else {
            alert('장바구니에 추가하는 데 실패하셨습니다.');
        }
    }




});




//
//     // Event listener for Buy Now button
//     document.getElementById("buy-now-btn").addEventListener("click", function (e) {
//         if (!uid) {
//             alert('로그인 후 이용해 주세요');
//             window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
//             return;
//         } else if (selectedOptions.length === 0) {
//             alert("옵션을 선택해주세요.");
//             return;
//         }
//         const isConfirmed = confirm("구매하시겠습니까?");
//         if (isConfirmed) {
//             const productDataArray = [];
//
//             if (optionSelectElements.length > 0 && selectedOptions.every(opt => opt)) {
//                 // Include options in the data if options are selected
//                 productDataArray.push({
//                     productId: productId,
//                     productName: productName,
//                     originalPrice: originalPrice,
//                     finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
//                     quantity: quantity,
//                     file190: file190,
//                     options: selectedOptions.map(opt => ({
//                         itemId: opt.itemId,
//                         combinationId: opt.combinationId,
//                         combinationString: opt.combinationString, // Include combinationString here
//                         optionName: opt.value,
//                         additionalPrice: additionalPrice
//                     })),
//                     point: point,
//                     discount: discount,
//                     shippingFee: shippingFee,
//                     shippingTerms: shippingTerms
//                 });
//             } else if (optionSelectElements.length === 0) {
//                 // If no options, just send productId and quantity
//                 productDataArray.push({
//                     productId: productId,
//                     productName: productName,
//                     file190: file190,
//                     finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
//                     discount: discount,
//                     quantity: quantity,
//                     shippingFee: shippingFee,
//                     shippingTerms: shippingTerms
//                 });
//             }
//             localStorage.setItem("productDataArray", JSON.stringify(productDataArray));
//
//             console.log("productDataArray", productDataArray);
//
//             fetch("/market/buyNow", {
//                 method: "POST",
//                 headers: {"Content-Type": "application/json"},
//                 body: JSON.stringify(productDataArray)
//             })
//                 .then(response => response.json())
//                 .then(data => {
//                     if (data.result === "success") {
//                         localStorage.setItem("productDataArray", JSON.stringify(productDataArray));
//                         window.location.href = `/market/order/${uid}`;
//                     } else if (data.result === "login_required") {
//                         if (confirm("로그인이 필요합니다. 로그인 하시겠습니까?")) {
//                             window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
//                         } else {
//                             location.reload();
//                         }
//                     } else if (data.result === "auth") {
//                         alert("구매 권한이 없는 계정입니다. 관리자 또는 판매자는 구매할 수 없습니다.");
//                     } else {
//                         alert("구매 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
//                     }
//                 })
//                 .catch(error => {
//                     console.error("Error:", error);
//                     alert("구매 처리 중 오류가 발생했습니다.");
//                 });
//         }
//     });
// }
//
//
//
// // Initial setting for Add to Cart buttons and functionality
// document.querySelectorAll('.add-to-cart').forEach(btn => {
//     btn.addEventListener('click', function () {
//
//         if (!uid) {
//             alert('로그인 후 이용해 주세요');
//             window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
//             return;
//         } else if (quantity <= 0) {
//             alert('수량을 1 이상으로 설정해 주세요.');
//             return;
//         } else if (optionSelectElements.length > 0 && !selectedOptions) {
//             alert("옵션을 선택해 주세요");
//             return;
//         }
//
//         if (confirm("장바구니에 추가 하시겠습니까.")) {
//             const finalPrice = Math.floor(originalPrice * (100 - discount) / 100);
//             const productDataArray = [];
//
//             if (optionSelectElements.length > 0 && selectedOptions.every(opt => opt)) {
//                 // Include options in the data if options are selected
//                 productDataArray.push({
//                     productId: productId,
//                     productName: productName,
//                     originalPrice: originalPrice,
//                     finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
//                     quantity: quantity,
//                     file190: file190,
//                     options: selectedOptions.map(opt => ({
//                         itemId: opt.itemId,
//                         combinationId: opt.combinationId,
//                         combinationString: opt.combinationString, // Include combinationString here
//                         optionName: opt.value,
//                         additionalPrice: additionalPrice
//                     })),
//                     point: point,
//                     discount: discount,
//                     shippingFee: shippingFee,
//                     shippingTerms: shippingTerms
//                 });
//             } else if (optionSelectElements.length === 0) {
//                 // If no options, just send productId and quantity
//                 productDataArray.push({
//                     productId: productId,
//                     productName: productName,
//                     file190: file190,
//                     finalPrice: Math.floor(originalPrice * (100 - discount) / 100),
//                     discount: discount,
//                     quantity: quantity,
//                     shippingFee: shippingFee,
//                     shippingTerms: shippingTerms
//                 });
//             }
//
//             console.log("productDataArray", productDataArray);
//
//
//             localStorage.setItem("productDataArray", JSON.stringify(productDataArray));
//             fetch('/api/cart', {
//                 method: 'POST',
//                 headers: {'Content-Type': 'application/json'},
//                 body: JSON.stringify(productCart)
//             })
//                 .then(resp => {
//                     if (!resp.ok) throw new Error(`서버 응답 오류: ${resp.status}`);
//                     return resp.json();
//                 })
//                 .then(data => {
//                     if (data.status === 200) {
//                         alert('장바구니에 추가 되었습니다!');
//                     } else if (data.status === 401) {
//                         alert('로그인 없이 이곳은 접근 금지! 빨리 로그인해 주세요');
//                         window.location.href = '/user/login';
//                     } else {
//                         alert('장바구니에 추가하는 데 실패하셨습니다.');
//                     }
//                 })
//                 .catch(error => {
//                     console.error('장바구니에 추가하는 데 실패함:', error);
//                     alert('장바구니에 추가하는 데 실패하셨습니다.');
//                 });
//         }
//     });
// });


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


// Initial setting for Add to Cart buttons and functionality
//     document.querySelectorAll('.add-to-cart').forEach(btn => {
//         btn.addEventListener('click', function () {
//
//             if (!uid) {
//                 alert('로그인 후 이용해 주세요');
//                 window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
//                 return;
//             } else if (quantity <= 0) {
//                 alert('수량을 1 이상으로 설정해 주세요.');
//                 return;
//             } else if (optionSelectElements.length > 0 && !selectedOptions) {
//                 alert("옵션을 선택해 주세요");
//                 return;
//             }
//
//             if (confirm("장바구니에 추가 하시겠습니까.")) {
//                 const finalPrice = Math.floor(originalPrice * (100 - discount) / 100);
//                 const productCart = {
//                     productId: parseInt(productId, 10),
//                     cartItemId: 0,
//                     productName: productName,
//                     originalPrice: originalPrice,
//                     finalPrice: finalPrice,
//                     quantity: quantity,
//                     file190: file190,
//                     shippingFee: shippingFee,
//                     optionId: parseInt(selectedOptionValue, 10),
//                     optionName: selectedOptionText,
//                     point: point,
//                     discount: discount,
//                     totalShippingFee: totalShippingFee
//                 };
//
//                 localStorage.setItem("productCart", JSON.stringify(productCart));
//                 fetch('/api/cart', {
//                     method: 'POST',
//                     headers: {'Content-Type': 'application/json'},
//                     body: JSON.stringify(productCart)
//                 })
//                     .then(resp => {
//                         if (!resp.ok) throw new Error(`서버 응답 오류: ${resp.status}`);
//                         return resp.json();
//                     })
//                     .then(data => {
//                         if (data.status === 200) {
//                             alert('장바구니에 추가 되었습니다!');
//                         } else if (data.status === 401) {
//                             alert('로그인 없이 이곳은 접근 금지! 빨리 로그인해 주세요');
//                             window.location.href = '/user/login';
//                         } else {
//                             alert('장바구니에 추가하는 데 실패하셨습니다.');
//                         }
//                     })
//                     .catch(error => {
//                         console.error('장바구니에 추가하는 데 실패함:', error);
//                         alert('장바구니에 추가하는 데 실패하셨습니다.');
//                     });
//             }
//         });

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
    const productId = document.getElementById('productId').value;
    fetchCoupons(productId);

}

// 닫기 버튼을 클릭하면 모달 숨기기
span.onclick = function () {
    modal.style.display = "none";
}



function fetchCoupons(productId) {

    let url = productId ? `/seller/coupon/${productId}` : '/seller/coupon/all/coupons';

    fetch(url)
        .then(resp => {
            if (!resp.ok) {
                throw new Error('인터넷연결 문제');
            }
            // 응답의 Content-Type이 JSON인지 확인
            const contentType = resp.headers.get("Content-Type");
            if (!contentType || !contentType.includes("application/json")) {
                return resp.text().then(text => {
                    displayErrorMessage('로그인 후 이용해 주세요.'); // 메시지 수정
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

function displayErrorMessage(message) {
    const couponContainer = document.getElementById('discountCouponItems');
    couponContainer.innerHTML = ''; // 이전 내용 제거

    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message'; // CSS 클래스를 추가하여 스타일링 가능
    errorDiv.textContent = message;
    couponContainer.appendChild(errorDiv);
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
}




function applyCoupon(couponId){

    if (!uid) {
        alert('로그인 후 이용해 주세요');
        window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
        return;
    }
    if(!confirm("이 쿠폰을 발급 받으시겠습니까?")){
        return;
    }

    fetch(`/seller/coupon/apply/${couponId}`,{
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
            alert(`쿠폰이 성공적으로 적용되었습니다.: ${data.message}`);

        })
        .catch(err => {
            console.error(err)
        })

}
