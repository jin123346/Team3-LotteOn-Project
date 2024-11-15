document.addEventListener('DOMContentLoaded', function () {
    sessionStorage.setItem("page_reload", "true");
    // 페이지 로드 시 초기값 설정


    // Format prices with thousands separator for all elements with class 'price'
    const priceElements = document.querySelectorAll('.price');
    priceElements.forEach(priceElement => {
        let priceValue = priceElement.textContent.trim().replace(/[^0-9]/g, '');
        priceValue = parseInt(priceValue, 10);
        if (!isNaN(priceValue)) {
            priceElement.textContent = priceValue.toLocaleString();
        }
    });



    // Define necessary elements for scrolling functionality  화면 스크롤
    const aside = document.querySelector('aside');
    const sectionWrapper = document.querySelector('.sectionWrapper');
    const headerHeight = 188;
    const footer = document.querySelector('footer');
    const asideHeight = aside.offsetHeight;
    const sectionHeight = sectionWrapper.offsetHeight;
    const topHeight = headerHeight + sectionHeight;
    function handleAsideScroll() {
        const scrollPosition = window.scrollY;
        const footerTop = footer.getBoundingClientRect().top + window.scrollY;
        if (scrollPosition <= topHeight && (scrollPosition + asideHeight + 50) < footerTop) {
            aside.classList.remove('scroll');
            aside.style.bottom = '';
        } else if ((scrollPosition + asideHeight + 50) >= footerTop) {
            aside.classList.add('scroll');
            aside.style.top = `${footerTop - asideHeight - 50}px`;
        } else {
            aside.classList.add('scroll');
            aside.style.position = '';
            aside.style.top = '';
        }
    }

    window.addEventListener('scroll', handleAsideScroll);
    window.addEventListener('resize', handleAsideScroll);

    // Elements for final order calculations

    let orderItem= {};
    let totalDiscount = 0;
    let totalDiscountPandC=0;
    let orderTotal=0;
    let couponDiscount = 0;
    const currentPoint = isNaN(parseInt(document.getElementById("currentPoint").value)) ? 0 : parseInt(document.getElementById("currentPoint").value);
    const currentIn = document.getElementById("currentIn");
    currentIn.textContent = currentPoint.toLocaleString();
    document.getElementById("usedPointResult").textContent = "0";
    // updateDiscountResult(); // 기본값 초기화

    console.log("CURRENTPOINT",currentPoint);
    const usedPointInput= document.getElementById('used_point');
    const usedPoint = isNaN(parseInt(document.getElementById("used_point").value)) ? 0 : parseInt(document.getElementById("used_point").value);
    console.log("usedPoint!!!!!!",usedPoint);

    const couponSelect = document.querySelector("select[name='coupons']");
    const usedPointResult = document.querySelector(".usedPointResult");
    const usedPointResultspan = document.getElementById("usedPointResult");
    const usedCouponResult = document.querySelector(".usedCouponResult");
    const discountResult = document.querySelector(".DiscountResult");
    const finalDiscountResult = document.getElementById("finalDiscountResult");
    const finalOrderQuantity = document.getElementById("finalOrderQuantity");
    const finalOrderProductPrice = document.getElementById("finalOrderProductPrice");
    const finalOrderDiscount = document.getElementById("finalOrderDiscount");
    const finalOrderDeliveryFee = document.getElementById("finalOrderDeliveryFee");
    const finalOrderTotal = document.getElementById("finalOrderTotal");
    const finalOrderPoint = document.getElementById("finalOrderPoint");
    const memberGrade = document.getElementById("memberGrade").value;
    const memberName=document.getElementById('memberName').value;
    const memberHp = document.getElementById('memberHp').value;
    // Retrieve the elements
    const postcodeElement = document.getElementById("M_postcode");
    const addressElement = document.querySelector(".totalAddress");
    const receiver = document.getElementById('receiver').value;
    const hp = document.getElementById('hp').value;
    const gradePercentages = {
        "VVIP": 5,
        "VIP": 4,
        "GOLD": 3,
        "SILVER": 2,
        "FAMILY": 1
    };
    const pointPercentage = gradePercentages[memberGrade] || 0; // Default to 0 if grade not found

    usedCouponResult.textContent = "0";
    usedPointResult.textContent = "0";
    finalDiscountResult.textContent = "0";
    finalOrderTotal.textContent = totalProductPrice().toLocaleString();

    //수령자
    console.log(receiver);

    //핸드폰
    console.log(hp);

    // Get the values from data attributes
    const postcode = postcodeElement.getAttribute("data-postcode");
    const addr = addressElement.getAttribute("data-addr");
    const addr2 = addressElement.getAttribute("data-addr2");
    // Log or use the values as needed
    console.log("Postcode:", postcode);

    console.log("Address Line 1:", addr);
    console.log("Address Line 2:", addr2);





    console.log(`Grade: ${memberGrade}, Point Percentage: ${pointPercentage}%`);







    // Modal control for address selection  화면 모달
    console.log(document.getElementById("addressSelectModal")); // Should log the modal element
    console.log(document.getElementById("addressRegisterModal")); // Should log the modal element
    const addressSelectModal = document.getElementById("addressSelectModal");
    const addressRegisterModal = document.getElementById("addressRegisterModal");
    const closeAddressSelect = addressSelectModal.querySelector(".close");
    const closeAddressRegister = addressRegisterModal.querySelector(".close");
    const editButtons = document.querySelectorAll(".edit-btn");
    let totalExpectedPoint=0;

    document.querySelector('.address_change').addEventListener('click', () => {
        console.log("Address change button clicked"); // Confirm the click event
        addressSelectModal.style.display = "block";
    });

    closeAddressSelect.addEventListener('click', () => {
        console.log("Close button in address select modal clicked"); // For testing

        addressSelectModal.style.display = "none";
    });

    closeAddressRegister.addEventListener('click', () => {
        addressRegisterModal.style.display = "none";
    });

    window.addEventListener('click', event => {
        if (event.target == addressSelectModal) addressSelectModal.style.display = "none";
        if (event.target == addressRegisterModal) addressRegisterModal.style.display = "none";
    });

    editButtons.forEach(button => {
        button.addEventListener('click', () => {
            addressSelectModal.style.display = "none";
            addressRegisterModal.style.display = "block";
        });
    });

    const newAddressBtn=document.querySelector('.new-address-btn');
    if(newAddressBtn){
        newAddressBtn.addEventListener('click', () => {
            addressSelectModal.style.display = "none";
            addressRegisterModal.style.display = "block";
        });
    }


    //상품 전환
    // Retrieve product data from localStorage and display
    const productDataArray = JSON.parse(localStorage.getItem("productDataArray")) || [];
    const productDataCartArray = JSON.parse(localStorage.getItem("ProductDataCartArray")) || [];
    let orderSource = '';


    try {
        const orderTable = document.querySelector(".productOrder");
        if (productDataArray.length > 0) {
            // Data from direct purchase (productDataArray)
            orderSource = 'directPurchase';
            productDataArray.forEach(data => {
                orderTable.insertAdjacentHTML("beforeend", createProductRow(data));
            });
        } else if (productDataCartArray.length > 0) {
            // Data from cart (productDataCartArray)
            orderSource = 'cartPurchase';
            productDataCartArray.forEach(data => {
                orderTable.insertAdjacentHTML("beforeend", createProductRow(data));
            });
        } else {
            alert('No products selected for order.');
            return;
        }
    } catch (error) {
        console.error("Error parsing product data:", error);
    }


    // 배송비 계산
    // function calculateShippingFee(dataArray) {
    //     const groupedShippingFees = {};
    //
    //     // Group products by productId and calculate the total final price for each group
    //     dataArray.forEach(data => {
    //         const productId = data.productId;
    //         const finalPrice = data.finalPrice;
    //         console.log("productId1:", productId);
    //
    //         // Initialize or accumulate total price for the product group
    //         if (!groupedShippingFees[productId]) {
    //             groupedShippingFees[productId] = {
    //                 totalFinalPrice: 0,
    //                 shippingFee: parseInt(data.shippingFee || 0),
    //                 shippingTerms: parseInt(data.shippingTerms || 0),
    //             };
    //         }
    //
    //         groupedShippingFees[productId].totalFinalPrice += finalPrice;
    //     });
    //
    //     // Set shipping fee to 0 if total price exceeds shipping terms
    //     for (const productId in groupedShippingFees) {
    //         const productGroup = groupedShippingFees[productId];
    //         if (productGroup.totalFinalPrice >= productGroup.shippingTerms) {
    //             productGroup.shippingFee = 0;
    //         }
    //     }
    //
    //     return groupedShippingFees;
    // }

    // function calculateShippingFee(data) {
    //     const finalPrice = data.finalPrice;
    //     const shippingTerms = parseInt(data.shippingTerms || 0);
    //     const shippingFee = parseInt(data.shippingFee || 0);
    //
    //     // 개별 상품의 finalPrice가 shippingTerms를 초과하면 배송비를 0으로 설정
    //     return finalPrice >= shippingTerms ? 0 : shippingFee;
    // }
    //
    // let t_expectPoint =0;
    //
    // // Function to create a new product row in the table
    // function createProductRow(data) {
    //     // Get grouped shipping fees for all products
    //     const shippingFee = calculateShippingFee(data);
    //
    //     console.log("data.finalPrice",data.finalPrice );
    //     console.log("data.originalPrice",data.originalPrice);
    //
    //     let Original = data.originalPrice;
    //     let discountAmount =( data.originalPrice * data.discount /100)/10*10
    //     let Quantity  = data.quantity;
    //     let final = (Original-discountAmount)*Quantity;
    //     let expectPoint= Math.floor(data.originalPrice*pointPercentage/100/10)*10;
    //
    //
    //     if (data.options && data.options.length > 0) {
    //         const totalAdditionalPrice = data.options[0].additionalPrice;
    //         const calcPrice= Original + totalAdditionalPrice;
    //         discountAmount = (calcPrice * data.discount/100)/10 *10 ;
    //         final = (calcPrice - discountAmount)*Quantity;
    //         expectPoint =Math.floor( calcPrice*pointPercentage/100/10 )*10;
    //         totalExpectedPoint += expectPoint;
    //
    //         // const shippingFee = final > data.shippingTerms? 0:shippingFee;
    //
    //         console.log("data.calcPrice",shippingFee );
    //         return `
    //     <tr class="order-row">
    //         <td>
    //             <div><img src="/uploads/${data.file190}" alt="${data.productName}"></div>
    //             <div>
    //                 <span>${data.productName}</span>
    //                 <p class="product_option"> [ 옵션 : ${data.options[0].combinationString} ]</p>
    //             </div>
    //         </td>
    //         <td>
    //             <div class="qnt">
    //                 <input type="number" class="T_quantity" value="${data.quantity}" readonly>
    //             </div>
    //         </td>
    //         <td><span class="T_originalPrice price" data-original="${calcPrice}" data-additional="${calcPrice}">${calcPrice.toLocaleString()}</span></td>
    //         <td><span class="T_discount">${data.discount}</span>%</td>
    //         <td><span class="T_point">${expectPoint}</span></td>
    //         <td><span class="T_shippingFee" data-ship="${shippingFee}">${shippingFee.toLocaleString()}</span></td>
    //         <td><span class="T_finalPrice price">${final}</span></td>
    //         <td><input type="hidden" class="shippingTerms" value="${data.shippingTerms}"></td>
    //     </tr>
    // `;
    //     }else{
    //         totalExpectedPoint += expectPoint;
    //
    //         return `
    //     <tr class="order-row">
    //         <td>
    //             <div><img src="/uploads/productImg/${data.file190}" alt="${data.productName}"></div>
    //             <div>
    //                 <span>${data.productName}</span>
    //
    //             </div>
    //         </td>
    //         <td>
    //             <div class="qnt">
    //                 <input type="number" class="T_quantity" value="${data.quantity}" readonly>
    //             </div>
    //         </td>
    //         <td><span class="T_originalPrice price" data-original="${Original}">${Original}</span></td>
    //         <td><span class="T_discount">${data.discount}</span>%</td>
    //         <td><span class="T_point">${expectPoint}</span></td>
    //         <td><span class="T_shippingFee" data-ship="${shippingFee}">${shippingFee.toLocaleString()}</span></td>
    //         <td><span class="T_finalPrice price">${final}</span></td>
    //         <td><input type="hidden" class="shippingTerms" value="${data.shippingTerms}"></td>
    //     </tr>
    // `;
    //     }
    //
    // }

    // 배송비 계산

    function shippingFeeCalc(productDataArray){
        if(finalPrice > shippingTerms){
            return 0;
        }else{
            return shippingFee;
        }
    }


    function calculateShippingFee(dataArray) {
        const groupedShippingFees = {};

        // Group products by productId and calculate the total final price for each group
        dataArray.forEach(data => {
            const productId = data.productId;
            const additionalPrice = data;

            const finalPrice = (data.finalPrice + additionalPrice) * data.quantity;
            console.log("productId1:", productId);

            // Initialize or accumulate total price for the product group
            if (!groupedShippingFees[productId]) {
                groupedShippingFees[productId] = {
                    totalFinalPrice: 0,
                    shippingFee: parseInt(data.shippingFee || 0),
                    shippingTerms: parseInt(data.shippingTerms || 0),
                };
            }

            groupedShippingFees[productId].totalFinalPrice += finalPrice;
        });

        // Set shipping fee to 0 if total price exceeds shipping terms
        for (const productId in groupedShippingFees) {
            const productGroup = groupedShippingFees[productId];
            if (productGroup.totalFinalPrice >= productGroup.shippingTerms) {
                productGroup.shippingFee = 0;
            }
        }

        return groupedShippingFees;
    }
    let t_expectPoint =0;

    // Function to create a new product row in the table
    function createProductRow(data) {
        // Get grouped shipping fees for all products
        const shippingFees = calculateShippingFee(productDataArray);
        const shippingFee = shippingFees[data.productId].shippingFee;
        console.log("data.finalPrice",data.finalPrice );
        console.log("data.originalPrice",data.originalPrice);

        let Original = data.originalPrice;
        let discountAmount =( data.originalPrice * data.discount /100)* 10/10
        let Quantity  = data.quantity;
        let final = (Original-discountAmount)*Quantity;
        let expectPoint= Math.floor(final*pointPercentage/100/10)*10;

        const imagePath = data.savedPath ==="" || data.savedPath == null ?
            `/uploads/productImg/${data.file190}` :
            `/uploads/${data.savedPath}/${data.file190}`;


        if (data.options && data.options.length > 0 ) {
            const totalAdditionalPrice = data.options[0].additionalPrice;
            const calcPrice= Original + totalAdditionalPrice;
            discountAmount = (calcPrice * data.discount/100) *10 /10;
            final = (calcPrice - discountAmount)*Quantity;
            expectPoint =Math.floor( final*pointPercentage/100/10 )*10 ;
            totalExpectedPoint += expectPoint;

            let shipping = data.shippingFee;
            if(data.shippingTerms <= final){
                shipping =0;
            }

            console.log("data.calcPrice",calcPrice );
            return `
        <tr class="order-row">
            <td>
                <div><img src="${imagePath}" alt="${data.productName}"></div>
                <div>
                    <span>${data.productName}</span>
                    <p class="product_option"> [ 옵션 : ${data.options[0].combinationString} ]</p>
                </div>
            </td>
            <td>
                <div class="qnt">
                    <input type="number" class="T_quantity" value="${data.quantity}" readonly>
                </div>
            </td>
            <td><span class="T_originalPrice price" data-original="${calcPrice}" data-additional="${calcPrice}">${calcPrice.toLocaleString()}</span></td>
            <td><span class="T_discount">${data.discount}</span>%</td>
            <td><span class="T_point">${expectPoint}</span></td>
            <td><span class="T_shippingFee" data-ship="${shipping}">${shipping.toLocaleString()}</span></td>
            <td><span class="T_finalPrice price">${final}</span></td>
            <td><input type="hidden" class="shippingTerms" value="${data.shippingTerms}"></td>
        </tr>
    `;
        }else if(data.cartId >0){
            totalExpectedPoint += expectPoint;

            console.log("여기!!!!",data);
            const original = parseInt(data.originalPrice)+parseInt(data.additionalPrice);

            const optionsHTML = data.combinationString
                ? `<p class="product_option"> [ 옵션 : ${data.combinationString} ]</p>`
                : '';

            return `
        <tr class="order-row">
            <td>
                <div><img src="${imagePath}" alt="${data.productName}"></div>
                <div>
                    <span>${data.productName}</span>
                ${optionsHTML} <!-- Only renders if data.combinationString is present -->
                </div>
            </td>
            <td>
                <div class="qnt">
                    <input type="number" class="T_quantity" value="${data.quantity}" readonly>
                </div>
            </td>
            <td><span class="T_originalPrice price" data-original="${original}" data-additional="${data.additionalPrice}">${original}</span></td>
            <td><span class="T_discount">${data.discount}</span>%</td>
            <td><span class="T_point">${expectPoint}</span></td>
            <td><span class="T_shippingFee" data-ship="${shippingFee}">${shippingFee.toLocaleString()}</span></td>
            <td><span class="T_finalPrice price">${data.finalPrice}</span></td>
            <td><input type="hidden" class="shippingTerms" value="${data.shippingTerms}"></td>
        </tr>
    `;

        } else{
            totalExpectedPoint += expectPoint;


            return `
        <tr class="order-row">
            <td>
                <div><img src="${imagePath}" alt="${data.productName}"></div>
                <div>
                    <span>${data.productName}</span>

                </div>
            </td>
            <td>
                <div class="qnt">
                    <input type="number" class="T_quantity" value="${data.quantity}" readonly>
                </div>
            </td>
            <td><span class="T_originalPrice price" data-original="${Original}">${Original}</span></td>
            <td><span class="T_discount">${data.discount}</span>%</td>
            <td><span class="T_point">${expectPoint}</span></td>
            <td><span class="T_shippingFee" data-ship="${shippingFee}">${final>data.shippingTerms ? 0: shippingFee.toLocaleString()}</span></td>
            <td><span class="T_finalPrice price">${final}</span></td>
            <td><input type="hidden" class="shippingTerms" value="${data.shippingTerms}"></td>
        </tr>
    `;
        }

    }

// Calculate and display discount results based on used points and coupons
    const pointuseBtn = document.getElementById("pointuseBtn");

    pointuseBtn.addEventListener("click", function () {
        const usedPoint = parseInt(usedPointInput.value) || 0;

        if(usedPoint < 5000){
            alert('5000원 이상부터 사용가능합니다.');
            usedPointInput.value=0;
            totalExpectedPoint=0;
            updateDiscountResult();

        }else if (usedPoint > currentPoint) {
            alert("사용가능한 포인트가 부족합니다.");
            usedPointInput.value = 0;
            updateDiscountResult();

        } else {
            updateDiscountResult();
        }
    });


    function totalProductDiscount() {
        return Array.from(document.querySelectorAll('.T_discount')).reduce((total, elem, index) => {
            const originalPrice = parseInt(document.querySelectorAll('.T_originalPrice')[index].dataset.original || 0);
            const quantity = parseInt(document.querySelectorAll('.T_quantity')[index].value || 1);
            const discountPercentage = parseInt(elem.innerText || 0);

            // Calculate discount based on the original price, quantity, and discount percentage
            return total + Math.floor((originalPrice * discountPercentage) / 100) * quantity;
        }, 0);
    }

    couponSelect.addEventListener("change", updateDiscountResult);


    //쿠폰 및 포인트
    function updateDiscountResult() {
        const usedPoint = parseInt(usedPointInput.value) || 0;

        if(usedPoint >=5000){
            totalExpectedPoint=0;
        }

        // 선택된 쿠폰에 따른 할인 금액 계산
        // const selectedCouponValue = couponSelect.options[couponSelect.selectedIndex]?.value || "0";
        const selectedCoupon = couponSelect.options[couponSelect.selectedIndex];

        if (selectedCoupon) {
            totalExpectedPoint = 0;
            const findCouponType = selectedCoupon.getAttribute('data-coupon-type');
            let findCouponValue = selectedCoupon.getAttribute('data-coupon-value');  // 할인 값을 가져옴
            const issuanceNumber = selectedCoupon.getAttribute('data-issuance-number'); // 발급 번호 가져오기

            console.log("선택된 쿠폰 타입:", findCouponType);  // 쿠폰 타입 확인
            console.log("선택된 쿠폰 할인 값:", findCouponValue);  // 쿠폰 할인 값 확인
            console.log("선택된 쿠폰 발급번호", issuanceNumber)
            // 선택된 쿠폰에 따라 할인 금액 계산
            if (findCouponValue) {
                if (findCouponValue.includes('%')) {
                    // 퍼센트 할인
                    findCouponValue = findCouponValue.replace('%', '');  // '%'를 제거
                    findCouponValue = parseFloat(findCouponValue);  // 숫자로 변환

                    if (!isNaN(findCouponValue)) {
                        couponDiscount = Math.floor(totalProductPrice() * (findCouponValue / 100));  // 퍼센트 할인 적용
                        console.log("퍼센트 할인 적용:", couponDiscount);
                    }
                } else {
                    // 고정 금액 할인
                    findCouponValue = parseFloat(findCouponValue);  // 숫자로 변환

                    if (!isNaN(findCouponValue)) {
                        couponDiscount = findCouponValue;  // 고정 금액 할인
                        console.log("고정 금액 할인 적용:", couponDiscount);
                    }
                }
            }
        }


        // 쿠폰 할인 금액을 화면에 표시
        usedCouponResult.textContent = couponDiscount.toLocaleString();

        // 포인트를 정용하기 전의 초기 총 금액 계산
        const initialTotalOrderAmount = totalProductPrice() - couponDiscount + totalShippingFee();
        console.log("초기 무문 금액 (배송비 포함) : " , initialTotalOrderAmount);

        // 포인트를 적용하지만 총 주문 금액을 ㅊ포과하지 않도록 제한
        const limitedUsedPoint = Math.min(usedPoint, totalProductPrice() - couponDiscount + totalShippingFee());

        usedPointResult.textContent = isNaN(limitedUsedPoint) ? 0 : limitedUsedPoint;
        usedPointResultspan.innerText = isNaN(limitedUsedPoint) ? 0 : limitedUsedPoint;


        // 각 할인 금액을 업데이트하고 최종 결과를 화면에 표시
        usedPointResult.textContent = limitedUsedPoint;
        usedPointResultspan.innerText = limitedUsedPoint;
        usedCouponResult.textContent = couponDiscount;


        // 상품 할인금액, 쿠폰 및 사용된 포인트를 포함한 총 할인 금액 계산
        totalDiscount = totalProductDiscount();   // 상품 할인금액
        totalDiscountPandC = limitedUsedPoint + couponDiscount;   // 쿠폰 및 포인트 사용금액
        discountResult.textContent = totalDiscountPandC;
        finalDiscountResult.textContent = totalDiscountPandC;

        // 최종 주문 정보 업데이트
        finalOrderQuantity.textContent = totalQuantity();
        finalOrderProductPrice.textContent = totalProductPrice().toLocaleString();
        finalOrderDiscount.textContent = totalDiscount.toLocaleString();
        finalOrderDeliveryFee.textContent = totalShippingFee().toLocaleString();

        orderTotal = totalProductPrice() - totalDiscountPandC - totalDiscount + totalShippingFee();
        orderTotal = Math.max(orderTotal, 0) // 결제 총액이 음수가 되지 않도록 0이하일경우 0으로 서렂ㅇ
        finalOrderTotal.textContent = (totalProductPrice() - totalDiscountPandC + totalShippingFee()).toLocaleString();


        console.log("결과 값", couponDiscount);
        let pointsEarned = 0;
        if (couponDiscount === 0) {
            pointsEarned = Math.floor(((orderTotal - totalShippingFee()) * pointPercentage) / 100);


            finalOrderPoint.textContent = pointsEarned.toLocaleString(); // Display with thousands separator
            finalOrderTotal.textContent = orderTotal.toLocaleString();
            // finalOrderPoint.textContent = Math.floor(orderTotal * 0.01).toLocaleString();
        }

        console.log(pointsEarned);
        finalOrderPoint.textContent = totalExpectedPoint; // Display with thousands separator
        finalOrderTotal.textContent = orderTotal.toLocaleString();
        // finalOrderPoint.textContent = Math.floor(orderTotal * 0.01).toLocaleString();
    }




    calculateTotals();

    // Calculate the total values based on all selected options
    function totalQuantity() {
        return Array.from(document.querySelectorAll('.T_quantity'))
            .reduce((total, elem) => total + parseInt(elem.value || 0), 0);
    }


    function totalProductPrice() {
        return Array.from(document.querySelectorAll('.T_originalPrice'))
            .reduce((total, elem) => {
                const originalPrice = parseInt(elem.dataset.original || 0);
                const quantity = parseInt(elem.closest('.order-row').querySelector('.T_quantity').value || 1); // Get the quantity

                // Sum total based on original price and any additional price if options exist
                return total + (originalPrice ) * quantity;
            }, 0);
    }

    function totalShippingFee() {
        return Array.from(document.querySelectorAll('.T_shippingFee'))
            .reduce((total, elem) => total + parseInt(elem.dataset.ship || 0), 0);
    }

    function calculateTotals() {
        // Total quantity, product price, and shipping fee calculation
        const totalQuantity = Array.from(document.querySelectorAll('.T_quantity')).reduce((total, elem) => total + parseInt(elem.value || 0), 0);
        const totalProductPrice = Array.from(document.querySelectorAll('.T_originalPrice')).reduce((total, elem) => total + parseInt(elem.dataset.original || 0), 0);
        const totalShippingFee = Array.from(document.querySelectorAll('.T_shippingFee')).reduce((total, elem) => total + parseInt(elem.dataset.ship || 0), 0);

        const totalDiscountAmount = totalProductDiscount();

        // Update final result display
        finalOrderQuantity.textContent = totalQuantity;
        finalOrderProductPrice.textContent = totalProductPrice.toLocaleString();
        console.log(totalProductPrice);

        finalOrderDiscount.textContent = (totalDiscountAmount + totalDiscount).toLocaleString();
        console.log((totalDiscountAmount + totalDiscount));

        finalOrderDeliveryFee.textContent = totalShippingFee.toLocaleString();


    }

    // Call updateDiscountResult on page load to initialize totals
    updateDiscountResult();

    // Get the select element
    const shippingInfoSelect = document.getElementById("shippingInfo");

    const InfoInput = document.getElementById("InfoInput");
    shippingInfoSelect.addEventListener('change', () => {
        console.log("선택됨", shippingInfoSelect.value)
        if (shippingInfoSelect.value === '6') {
            InfoInput.type = 'text';
        } else {
            InfoInput.type = 'hidden';
        }
    });

// Function to get the selected value
    function getSelectedShippingInfo() {
        const selectedValue = shippingInfoSelect.value;
        if (selectedValue === '6') {
            console.log(InfoInput.value);
            return InfoInput.value;
        }
        console.log("Selected shipping info:", selectedValue);
        return selectedValue;
    }

// Example: Call this function on a button click or when the selection changes
    shippingInfoSelect.addEventListener("change", getSelectedShippingInfo);

    const paymentOptions = document.getElementsByName('payment');
    let selectedPayment = null;
    paymentOptions.forEach(option => {
        option.addEventListener('change', () => {
            selectedPayment = document.querySelector('input[name="payment"]:checked').value;
            console.log("선택된 결제 방식:", selectedPayment);
        });
    })
    let couponValue = "";
    let couponText = 0;
    couponSelect.addEventListener('change', () => {
        const selectedOption = couponSelect.options[couponSelect.selectedIndex];
        couponValue = selectedOption.value || 1;
        couponText = selectedOption.text || 1;

        /*console.log("쿠폰 값 (value):", couponValue);
        console.log("쿠폰 사용 금액 (text):", couponText);*/
    });



    function updateOrderItem() {
        orderItem = {
            productDataArray: productDataArray,            // List<BuyNowRequestDTO>로 매핑
            receiver: receiver,                            // 일치
            hp: hp,                                        // 일치
            postcode: postcode,                            // 일치
            addr1: addr,                                   // 일치
            addr2: addr2,                                  // 일치
            TotalDiscount: finalOrderDiscount.textContent,   //총 할인금액
            productDiscount: finalOrderDiscount.textContent,
            totalPointandCoupon: totalDiscountPandC,
            usedPointResult: Number(usedPointResult.textContent),  // 일치
            usedCouponResult: Number(couponDiscount),         // usedCoupon -> usedCouponResult로 매핑
            usedCouponName: couponText,
            totalOrderQuantity: finalOrderQuantity.textContent,
            totalOriginalPrice: finalOrderProductPrice.textContent,
            totalShippingFee: finalOrderDeliveryFee.textContent,                           // 기본값 설정 필요
            totalFinalPrice: finalOrderTotal.textContent,                            // 기본값 설정 필요
            credit: selectedPayment,                       // 일치
            couponId: couponValue,            // couponValue를 couponId로 매핑 (확인 필요) => issuedNumber
            shippingInfo: getSelectedShippingInfo(),
            finalOrderPoint: finalOrderPoint.textContent,
            gradePercentage: pointPercentage,
            memberName: memberName,
            memberHp: memberHp
        };
    }

    console.log("Product Data Array Before Order Submission:", productDataArray);


    const orderBtn = document.querySelector('.order-Btn');
    const receiverInput = document.getElementById('receiver');
    const hpInput = document.getElementById('hp');

// Function to validate all required fields
    function validateOrderForm() {
        const address = addressElement.getAttribute("data-addr") || '';
        const shippingInfo = getSelectedShippingInfo();
        const paymentSelected = Array.from(paymentOptions).some(option => option.checked);
        const receiver = receiverInput.value.trim();
        const hp = hpInput.value.trim();
        const postcode = postcodeElement.getAttribute("data-postcode") || '';

        // Check if any required fields are empty
        if (!address || !shippingInfo || !paymentSelected || !receiver || !hp || !postcode) {
            return false;
        }

        return true;
    }


    updateOrderItem();
    console.log(orderItem);
    document.querySelector('.order-Btn').addEventListener('click', function (event) {
        event.preventDefault();  // 기본 제출 동작 방지

        if (productDataArray.length > 0 && validateOrderForm()) {
            const isConfirm = confirm("주문하시겠습니까?");
            if (isConfirm) {
                updateOrderItem();
                console.log(orderItem)
                // 서버에 데이터를 전송
                fetch('/market/order/saveOrder', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(orderItem),
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.result > 0) {
                            alert('주문이 완료되었습니다!');
                            localStorage.removeItem('productDataArray');  // 성공 시 로컬 데이터 삭제
                            window.location.href = '/market/completed/' + data.result; // 완료 후 페이지 이동


                        } else {
                            alert('주문 처리 중 오류가 발생했습니다.');
                            alert("모든 필수 항목을 채워주세요: 수령자 정보, 주소, 배송 요청사항, 결제수단");

                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('서버와의 통신 중 오류가 발생했습니다.');
                    });
            }
        } else if (!validateOrderForm()) {
            alert('주문정보를 확인해주세요');
        } else {
            alert('주문할 상품이 없습니다.');
        }
    });

});

document.addEventListener("DOMContentLoaded", function () {
    // 로컬 스토리지에서 두 가지 상품 데이터를 가져옵니다.
    const productDataArray = JSON.parse(localStorage.getItem("productDataArray")) || [];

    // 배열이 비어있으면 경고 메시지 표시
    if (productDataArray.length === 0) {
        alert('구매할 상품이 없습니다.');
        return;
    }

    // 첫 번째 상품 ID만 사용 (단일 값으로 변경)
    const productId = productDataArray[0].productId;
    console.log("보낼 상품 아이디:", productId);  // 서버로 전송할 상품 ID 확인

    // 각 상품에 대해 쿠폰 목록을 받아오는 함수
    function fetchCoupons(productId) {
        return fetch(`/api/coupon/getCouponsForMember?productId=${productId}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(resp => {
                console.log("응답 상태:", resp.status);  // 응답 상태 코드 확인
                return resp.json();  // JSON으로 바로 파싱하여 반환
            })
            .then(data => {
                console.log("서버 응답:", data);  // 서버 응답 내용 확인
                if (data.result === "success") {
                    return data.coupons;  // 쿠폰 목록 반환
                } else {
                    console.error("쿠폰 목록을 불러오는 데 실패했습니다.");
                    return [];  // 실패 시 빈 배열 반환
                }
            })
            .catch(error => {
                console.error("Error fetching coupons:", error);  // fetch 에러
                return [];  // fetch 오류 발생 시 빈 배열 반환
            });
    }


    // DOM에 쿠폰 목록을 업데이트하는 함수
    function updateCouponSelect(couponIssuedList) {
        const couponSelect = document.getElementById("couponSelect");
        console.log("받아온 쿠폰 목록:", couponIssuedList);  // 쿠폰 목록 확인

        // 기존 옵션을 지우고 새로 추가
        couponSelect.innerHTML = ""; // 기존 옵션들 제거
        const availableCoupons = couponIssuedList.filter(couponIssued => couponIssued.status === "사용가능");

        availableCoupons.forEach(couponIssued => {
            console.log("발급된 쿠폰 정보:", couponIssued);  // 발급된 쿠폰 정보 출력
            console.log("쿠폰명:", couponIssued.couponName);  // 쿠폰 이름 출력
            console.log("할인 값 (couponIssued.benefit):", couponIssued.benefit);  // 쿠폰 할인 값 출력


            const option = document.createElement("option");

            option.value = couponIssued.issuanceNumber;  // couponId로 설정

            let displayText = `${couponIssued.couponName}`;  // 쿠폰 이름
            if (couponIssued.benefit) {
                displayText += ` (${couponIssued.benefit})`;  // 예: 40% 또는 5000원
            }
            option.textContent = displayText;

            // 할인 값(benefit)을 올바르게 설정
            if (couponIssued.benefit !== null && couponIssued.benefit !== undefined) {
                option.setAttribute("data-coupon-value", couponIssued.benefit);  // 할인 값 설정
            } else {
                option.setAttribute("data-coupon-value", "0");  // 기본값 설정
            }

            option.setAttribute("data-coupon-type", couponIssued.couponType);  // 쿠폰 타입 설정
            option.setAttribute("data-product-id", couponIssued.productId);  // 상품 ID 설정
            option.setAttribute("data-issuance-number", couponIssued.issuanceNumber);  // 상품 ID 설정

            couponSelect.appendChild(option);
        });

        // 디폴트 선택 칸 추가 (첫 번째 옵션)
        const defaultOption = document.createElement("option");
        defaultOption.value = "";  // 선택되지 않도록 빈 값으로 설정
        defaultOption.textContent = "쿠폰을 선택하세요";  // 기본 텍스트
        defaultOption.selected = true;  // 기본적으로 선택되도록 설정
        couponSelect.insertBefore(defaultOption, couponSelect.firstChild);  // 첫 번째 자리에 추가
    }

    // 쿠폰 목록을 받아와서 업데이트
    fetchCoupons(productId).then(couponIssuedList => {  // couponIssuedList를 전달
        updateCouponSelect(couponIssuedList);  // 발급된 쿠폰 정보(couponIssued)를 전달
    });
});




