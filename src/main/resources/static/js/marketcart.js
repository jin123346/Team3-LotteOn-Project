const cartItems = [];




// 버튼 요소 선택
const modifyButtons = document.querySelectorAll('.countModifyBtn');
const cancelButtons = document.querySelectorAll('.cancel-btn');
const applyButtons = document.querySelectorAll('.apply-btn');



let initialQuantity = parseInt(document.querySelector('.orderQnt span').innerText.trim()) || 0;
let initialPrice = parseInt(document.querySelector('.orderOriginPrice .price').innerText.replace(/,/g, '')) || 0;
let initialDiscount = parseInt(document.querySelector('.orderSalePrice .price').innerText.replace(/,/g, '')) || 0;
let initialDelivery = parseInt(document.querySelector('.delivery-fee .price').innerText.replace(/,/g, '')) || 0;
let initialPoints = parseInt(document.querySelector('.orderPoint .price').innerText.replace(/,/g, '')) || 0;


document.addEventListener('DOMContentLoaded', function () {
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

    const aside = document.querySelector('aside');
    const headerHeight = 188; // 헤더 높이
    const footer = document.querySelector('footer');
    const footerHeight = 700; // 푸터 높이
    const asideHeight = aside.offsetHeight; // aside 높이
    console.log('asdieHeight:'+asideHeight)
    function handleAsideScroll() {
        const scrollPosition = window.scrollY; // 현재 스크롤 위치
        console.log(scrollPosition)
        const footerTop = footer.getBoundingClientRect().top + window.scrollY; // Footer의 상단 위치
        console.log('footerTop:'+footerTop);
        // 스크롤 위치가 헤더 아래에 있고, 푸터에 도달하기 전이면 aside에 scroll 클래스를 추가
        if (scrollPosition <= headerHeight || (scrollPosition + asideHeight + 50) < footerTop ) {
            aside.classList.remove('scroll');
            aside.style.position = 'fixed';
            aside.style.top = ''; // 헤더 아래에 고정
            aside.style.left = ''; // 푸터 닿지 않음
        } else if ((scrollPosition + asideHeight + 50) >= footerTop) {
            // 푸터에 도달하면 aside를 푸터 상단에서 멈추게 하기
            aside.classList.add('scroll');
            aside.style.position = 'absolute';
            aside.style.top = `${footerTop - asideHeight - 50}px`;  // 푸터 상단에 고정
            // console.log('asdide top : '+`${footerTop - asideHeight - 50}px`)
        } else {
            aside.classList.add('scroll');
            aside.style.position = ''; // 초기 위치로 돌아감
            aside.style.top = `${headerHeight}`; // 초기 위치로 돌아감

        }
    }

    // 스크롤할 때마다 실행
    window.addEventListener('scroll', handleAsideScroll);
    // 화면 크기가 변경될 때도 실행
    window.addEventListener('resize', handleAsideScroll);


    const checkBoxAll = document.getElementById('checkBoxAll');
    const itemCheckBoxes = document.querySelectorAll('input[name="select"]'); // 'checkBox' 대신 여러 체크박스를 선택

    checkBoxAll.addEventListener('change', function () {
        itemCheckBoxes.forEach(function (checkbox) {
            checkbox.checked = checkBoxAll.checked; // 전체 체크박스의 상태에 따라 모든 체크박스를 체크 또는 해제
        });
    });

    itemCheckBoxes.forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            if (!checkbox.checked) {
                checkBoxAll.checked = false; // 하나의 체크박스가 해제되면 전체 체크박스도 해제
            } else if (Array.from(itemCheckBoxes).every(cb => cb.checked)) {
                checkBoxAll.checked = true; // 모든 체크박스가 체크되면 전체 체크박스도 체크
            }
        });
    });

    const selectAllButton = document.querySelector('.selected-all');
    selectAllButton.addEventListener('click', function (e){
        e.preventDefault();
        checkBoxAll.checked = !checkBoxAll.checked; //현재 상태 반전
        itemCheckBoxes.forEach(function (checkbox){
            checkbox.checked = checkBoxAll.checked;
        });
    });

    function adjustQuantity(button, delta) {
        const inputField = delta < 0 ? button.nextElementSibling : button.previousElementSibling;
        let currentValue = parseInt(inputField.value) || 1;
        inputField.value = Math.max(1, currentValue + delta); // 최소값 1로 설정
    }

// - 버튼 이벤트 핸들러
    document.querySelectorAll('.qnt-decrease').forEach(button => {
        button.addEventListener('click', function () {
            adjustQuantity(this, -1);
        });
    });

// + 버튼 이벤트 핸들러
    document.querySelectorAll('.qnt-increase').forEach(button => {
        button.addEventListener('click', function () {
            adjustQuantity(this, 1);
        });
    });

    // 수정하기 버튼 이벤트 핸들러
    modifyButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();
            const row = this.closest('tr');
            toggleEditMode(row, true);
        });
    });

// 취소 버튼 이벤트 핸들러
    cancelButtons.forEach(button => {
        button.addEventListener('click', function () {
            const row = this.closest('tr');
            toggleEditMode(row, false);
        });
    });

// 변경하기 버튼 이벤트 핸들러
    applyButtons.forEach(button => {
        button.addEventListener('click', function () {
            const row = this.closest('tr');
            const cartItemId = row.getAttribute('data-cartItemId'); // Product ID

            const inputField = row.querySelector('input[name="quantity"]');
            const newQuantity = row.querySelector('.quantity-input').value.trim(); // 공백 제거

            if (!cartItemId) {
                console.log("아이디가 읍댜")
                return;
            }

            // 수량 검증
            if (isNaN(newQuantity) || newQuantity < 1) {
                alert('수량은 1 이상이여야 합니다.')
                return;
            }

            const requestBody = {
                cartItemId: parseInt(cartItemId, 10), // 숫자로 변환
                quantity: parseInt(newQuantity, 10)  // 숫자로 변환
            };
            console.log("전송할 requestBody:", requestBody);


            fetch(`/api/cart/${cartItemId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody),

            })

                .then(resp => {
                    if (!resp.ok) {
                        alert('서버 요청중 요류발생')
                    }
                    return resp;
                })
                .then(data => {
                    inputField.value = newQuantity;
                    alert('수정되었습니다');
                    toggleEditMode(row, false); // 수정 모드 해제

                })
                .catch(error => {
                    console.log('error', error)
                    alert('수량 오류')
                })


        });


        // 선택삭제 버튼 이벤트
        document.querySelector('.selected-delete').addEventListener('click', function() {
            // 선택된 항목 삭제 로직
            const selectedItems = document.querySelectorAll('input[name="select"]:checked');

            if (selectedItems.length === 0) {
                console.log('삭제할 항목이 없다.')
                return
            }

            const cartItemIds = [];

            selectedItems.forEach(function (checkbox){
                const cartItemRow = checkbox.closest('tr');
                const cartItemId = cartItemRow.getAttribute('data-cartItemId'); // 카트 아이템 ID 가져오기
                cartItemIds.push(cartItemId)
            })

            if (confirm(`정말로 삭제하시겠습니까?`)) {
                fetch('/api/delete', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(cartItemIds) // cartItemIds 배열을 JSON으로 변환하여 전송
                })
                    .then(resp => {
                        if (resp.ok) {
                            console.log('선택된 아이템들 삭제 성공');
                            selectedItems.forEach(function(checkbox) {
                                const cartItemRow = checkbox.closest('tr');
                                cartItemRow.remove(); // 삭제 후 해당 행 제거
                            });
                        } else {
                            console.log('선택된 아이템들 삭제 실패');
                        }
                    })
                    .catch(error => {
                        console.log('오류 발생', error);
                    });
            } else {
                console.log('삭제 취소');
            }
        });


        const quantityInputs = document.querySelectorAll('.quantity');
        quantityInputs.forEach(input => {
            console.log(input.value); // Ensure all quantity fields are accessible
        });

    });

    document.querySelector('.order-Btn').addEventListener('click', function (event) {
        event.preventDefault(); // Prevent default action
        console.log('Order button clicked');

        // Get user and cart IDs
        const userId = document.getElementById('uid')?.value;
        const cartId = document.getElementById('cartId')?.value;
        const savedPath = document.getElementById('savedPath').value;

        if (!userId || !cartId) {
            alert("User ID or Cart ID is missing.");
            return;
        }

        const productDataArray = [];
        const checkedItems = document.querySelectorAll('input[name="select"]:checked');

        if (checkedItems.length === 0) {
            alert("주문할 상품을 선택해 주세요");

            return;
        }

        const isConfirmed = confirm("주문하시겠습니까?");
        if (isConfirmed) {
            checkedItems.forEach(checkbox => {
                const row = checkbox.closest('tr');

                // Skip rows that contain the 'skip' ID input
                if (row.querySelector('#skip')) {
                    return; // Skip this iteration if 'skip' element is found
                }

                // Get the quantity and handle undefined/null cases
                const quantityInput = row.querySelector('.quantity');
                if (!quantityInput || quantityInput.value === "") {
                    return; // Exit this iteration if quantity is null or empty
                }

                let additionalPrice = 0;
                let combinationId = 0;
                let combinationString = "";
                const additionalPriceInput = row.querySelector('.additionalPrice-Input');
                const combinationIdInput = row.querySelector('.combinationId');
                const combinationStringInput = row.querySelector('.combinationString');

                if (additionalPriceInput) {
                    combinationId = parseInt(combinationIdInput.value) || 0;
                    additionalPrice = parseInt(additionalPriceInput.value) || 0;
                    combinationString = combinationStringInput.value || "";
                }




                const productData = {
                    cartItemId: parseInt(row.getAttribute('data-cartItemId')) || 0,
                    cartId: cartId,
                    productId: row.getAttribute('data-product-id'),
                    productName: row.getAttribute('data-product-name'),
                    discount: row.getAttribute('data-discount'),
                    originalPrice: row.getAttribute('data-original-price'),
                    finalPrice: row.getAttribute('data-final-price'),
                    quantity: parseInt(quantityInput.value) || 1,
                    file190: row.getAttribute('data-file190'),
                    point: row.getAttribute('data-point'),
                    shippingFee: row.getAttribute('data-shipping-fee'),
                    ShippingTerms: row.getAttribute('data-shipping-terms'),
                    expectedPoint: row.getAttribute('data-expected-point'),
                    additionalPrice: additionalPrice,
                    combinationId: combinationId,
                    combinationString: combinationString,
                    savedPath: savedPath

                };

                productDataArray.push(productData);
            });

            console.log("Selected items:", productDataArray);
            // Save selected items to localStorage
            localStorage.setItem('productDataArray', JSON.stringify(productDataArray));
            console.log("Saved product data to localStorage:", productDataArray);

            fetch("/market/buyNow", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(productDataArray)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.result === "success") {
                        localStorage.setItem("productDataCartArray", JSON.stringify(productDataArray));
                        window.location.href = `/market/order/${userId}`;
                    } else if (data.result === "login_required") {
                        if (confirm("로그인이 필요합니다. 로그인 하시겠습니까?")) {
                            window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
                        } else {
                            location.reload();
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

            // Redirect to the order confirmation page or process further as needed
            console.log("userID", userId);
        }

    });

    function updateOrderSummary() {
        let totalQuantity = 0;
        let totalPrice = 0;
        let totalDiscount = 0;
        let totalDelivery = 0;
        let totalPoints = 0;

        // 체크된 항목을 순회
        const checkedCheck = document.querySelectorAll('input[name="select"]:checked');
        if (checkedCheck.length > 0) {
            checkedCheck.forEach(checkbox => {
                const row = checkbox.closest('tr');

                // 수량, 가격, 할인, 배송비, 포인트 가져오기
                totalQuantity += parseInt(row.querySelector('input[name="quantity"]').value);
                totalPrice += parseInt(row.querySelector('td:nth-child(4)').innerText.replace(/,/g, '')) ||0;
                totalDiscount += parseInt(row.querySelector('td:nth-child(5)').innerText.replace(/,/g, '')) ||0;
                totalDelivery += parseInt(row.querySelector('td:nth-child(7)').innerText.replace(/,/g, '')) ||0;
                totalPoints += parseInt(row.querySelector('td:nth-child(6)').innerText.replace(/,/g, '')) ||0;
            });

            // 전체 주문 금액 계산
            const totalOrderPrice = totalPrice - totalDiscount + totalDelivery;

            // 화면에 값 업데이트
            document.querySelector('.orderQnt span').innerText = totalQuantity;
            document.querySelector('.orderOriginPrice .price').innerText = totalPrice.toLocaleString();
            document.querySelector('.orderSalePrice .price').innerText = totalDiscount.toLocaleString();
            document.querySelector('.delivery-fee .price').innerText = totalDelivery.toLocaleString();
            document.querySelector('.orderTotalPrice .price').innerText = totalOrderPrice.toLocaleString();
            document.querySelector('.orderPoint .price').innerText = totalPoints.toLocaleString();
        } else {
            // 체크박스가 하나도 체크되지 않은 경우 기본값으로 되돌리기
            document.querySelector('.orderQnt span').innerText = initialQuantity;
            document.querySelector('.orderOriginPrice .price').innerText = initialPrice.toLocaleString();
            document.querySelector('.orderSalePrice .price').innerText = initialDiscount.toLocaleString();
            document.querySelector('.delivery-fee .price').innerText = initialDelivery.toLocaleString();
            document.querySelector('.orderTotalPrice .price').innerText = (initialPrice - initialDiscount + initialDelivery).toLocaleString();
            document.querySelector('.orderPoint .price').innerText = initialPoints.toLocaleString();
        }
    }

    // 수정 모드 토글 함수
    function toggleEditMode(row, isEditMode) {
        const inputField = row.querySelector('input[name="quantity"]');
        const numberControls = row.querySelector('.number-controls');
        const editButtons = row.querySelector('.edit-buttons');
        const modifyButton = row.querySelector('.countModifyBtn');

        // 수정 모드로 전환할 때 요소 표시 여부 조정
        inputField.style.display = isEditMode ? 'none' : 'block';
        numberControls.style.display = isEditMode ? 'flex' : 'none';
        editButtons.style.display = isEditMode ? 'flex' : 'none';
        modifyButton.classList.toggle('hidden', isEditMode);

        // 수정 모드라면 기존 값 복사
        if (isEditMode) {
            row.querySelector('.quantity-input').value = inputField.value;
        }
    }
});

// function updateCartSummary(cartItems) {
//     const totalQuantity = cartItems.reduce((total, item) => total + item.quantity, 0);
//     const totalPrice = cartItems.reduce((total, item) => total + item.price * item.quantity, 0 );
//     const totalDiscount = cartItems.reduce((total, item) => total + item.discount * item.quantity, 0);
//     const deliveryFee = 0;
//     const totalOrderPrice = totalPrice - totalDiscount + deliveryFee;
//     const totalPoints = Math.floor(totalPrice * 0.01);
//
//     // ui 업데이트
//     document.querySelector('.orderQnt span').innerText = totalQuantity;
//     document.querySelector('.orderOriginPrice  .price').innerText = totalPrice;
//     document.querySelector('.orderSalePrice .price').innerText = totalDiscount;
//     document.querySelector('.delivery-fee .price').innerText = deliveryFee;
//     document.querySelector('.orderTotalPrice .price').innerText = totalOrderPrice;
//     document.querySelector('.orderPoint .price').innerText = totalPoints;
// }





//
// // + - 버튼 클릭 시 숫자 조정 함수
// function adjustQuantity(button, delta) {
//     const inputField = delta < 0 ? button.nextElementSibling : button.previousElementSibling;
//     let currentValue = parseInt(inputField.value) || 1;
//     inputField.value = Math.max(1, currentValue + delta); // 최소값 1로 설정
// }
//
// // - 버튼 이벤트 핸들러
// document.querySelectorAll('.qnt-decrease').forEach(button => {
//     button.addEventListener('click', function () {
//         adjustQuantity(this, -1);
//     });
// });
//
// // + 버튼 이벤트 핸들러
// document.querySelectorAll('.qnt-increase').forEach(button => {
//     button.addEventListener('click', function () {
//         adjustQuantity(this, 1);
//     });
// });








    // const checkBoxAll = document.getElementById('checkBoxAll');
    // const itemCheckBoxes = document.querySelectorAll('input[name="select"]');
    //
    // // 전체 선택 체크박스 이벤트
    // checkBoxAll.addEventListener('change', function () {
    //     itemCheckBoxes.forEach(function (checkbox) {
    //         checkbox.checked = checkBoxAll.checked;
    //     });
    //     if (!checkBoxAll.checked) {
    //         updateOrderSummary();
    //     }
    // });
    //
    // // 개별 체크박스 변경 이벤트
    // itemCheckBoxes.forEach(checkbox => {
    //     checkbox.addEventListener('change', () => {
    //         if (!checkbox.checked) {
    //             checkBoxAll.checked = false;
    //         } else if (Array.from(itemCheckBoxes).every(cb => cb.checked)) {
    //             checkBoxAll.checked = true;
    //         }
    //         updateOrderSummary();
    //     });
    // });

// 주문 요약 업데이트 함수







