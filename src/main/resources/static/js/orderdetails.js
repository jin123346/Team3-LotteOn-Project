function formatDate(date) {
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// 오늘 날짜를 max 속성으로 설정
function setMaxDate() {
    const today = new Date();
    document.getElementById("startDate").setAttribute("max", formatDate(today));
    document.getElementById("endDate").setAttribute("max", formatDate(today));
}

// 선택한 기간만큼 시작일을 계산하는 함수
function setPeriod(days) {
    const today = new Date(); // 오늘 날짜
    const startDate = new Date(); // 시작일을 오늘 날짜로 설정
    startDate.setDate(today.getDate() - days); // 선택된 기간만큼 이전 날짜 설정

    // 시작일과 종료일을 input 필드에 설정
    document.getElementById("startDate").value = formatDate(startDate);
    document.getElementById("endDate").value = formatDate(today);
}

// 폼을 제출할 때 처리하는 함수
function submitForm() {
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;

    // 날짜 검증 (시작일이 종료일보다 이후일 수 없도록 설정)
    if (new Date(startDate) > new Date(endDate)) {
        alert("시작일이 종료일보다 클 수 없습니다.");
        return;
    }

    // 폼 데이터 처리 (여기서는 간단히 콘솔에 출력)
    console.log("선택된 기간:", startDate, "~", endDate);

}

// 페이지 로드 시 max 날짜 설정
window.onload = setMaxDate;

const modal = document.getElementById("orderModal");
const closeModalBtn = document.querySelector(".modal .close");
const orderNumbers = document.querySelectorAll(".order-number");

function openOrderModal(orderElement) {
    // 데이터 속성 값들을 모달에 채우기
    document.getElementById("orderDate").textContent = orderElement.getAttribute("data-order-date");
    document.getElementById("orderIdText").textContent = `주문번호: ${orderElement.getAttribute("data-order-id")}`;
    document.getElementById("companyName").textContent = orderElement.getAttribute("data-company");
    document.getElementById("productName").textContent = orderElement.getAttribute("data-product-name");
    document.getElementById("quantityText").textContent = `수량: ${orderElement.getAttribute("data-quantity")}`;
    document.getElementById("productPrice").textContent = orderElement.getAttribute("data-price");
    document.getElementById("salePrice").textContent = orderElement.getAttribute("data-price"); // 판매가
    document.getElementById("discountPrice").textContent = orderElement.getAttribute("data-discount"); // 할인액
    document.getElementById("totalPrice").textContent = orderElement.getAttribute("data-total-price"); // 결제금액
    document.getElementById("orderStatus").textContent = orderElement.getAttribute("data-status");
    document.getElementById("customerName").textContent = orderElement.getAttribute("data-customer-name");
    document.getElementById("customerPhone").textContent = orderElement.getAttribute("data-customer-phone");
    document.getElementById("customerAddress").textContent = orderElement.getAttribute("data-customer-address");
    document.getElementById("deliveryRequests").textContent = orderElement.getAttribute("data-delivery-requests");

    // 이미지 경로 업데이트
    const imagePath = orderElement.getAttribute("data-product-image");
    const savedPath = orderElement.getAttribute("data-product-path");
    console.log("savedPath", savedPath);

    const fullPath = savedPath != null ?
        `/uploads/${savedPath}/${imagePath}` :
        `/uploads/productImg/${imagePath}`;
    console.log("Full Path:", fullPath);


    document.getElementById("productImage").src = `${fullPath}`; // 이미지 경로 반영

    modal.style.display = "block"; // Show the modal
}

// 모달 닫기 버튼 클릭시
closeModalBtn.addEventListener("click", function() {
    modal.style.display = "none"; // 모달 숨기기
});

// 모달 외부를 클릭하면 모달 닫기
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none"; // 모달 숨기기
    }
};

// 각 주문 항목에 클릭 이벤트 추가
orderNumbers.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        openOrderModal(order); // openOrderModal 함수 호출
    });
});

// 수취확인 모달 관련 코드
const qreceiptbtn = document.querySelectorAll(".receipt-btn");
const receiptModal = document.getElementById("receiptModal");
const closeModalBtn4 = document.querySelector(".modal.receipt .close.receipt");
const confirmReceiptBtn = document.getElementById("confirmReceiptBtn");
const cancelModalBtn = document.getElementById("cancelModalBtn");

// 주문 아이템 ID를 저장할 변수
let currentOrderItemId = null;

// 수취확인 버튼 클릭 시 모달 열기
qreceiptbtn.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        currentOrderItemId = order.getAttribute("data-order-item-id");  // 해당 주문 아이템 ID 저장
        receiptModal.style.display = "block"; // 모달 열기
    });
});

// 모달 닫기 (닫기 버튼 클릭)
closeModalBtn4.addEventListener("click", function() {
    receiptModal.style.display = "none"; // 모달 닫기
});

// 모달 닫기 (취소 버튼 클릭)
cancelModalBtn.addEventListener("click", function() {
    receiptModal.style.display = "none"; // 모달 닫기
});

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target == receiptModal) {
        receiptModal.style.display = "none"; // 모달 닫기
    }
};

// 수취 확인 버튼 클릭 시 상태 변경
confirmReceiptBtn.addEventListener("click", function() {
    if (currentOrderItemId !== null) {
        // 서버에 상태 변경 요청
        fetch(`/mypage/confirmReceipt/${currentOrderItemId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: "CONFIRMATION" })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // 성공적으로 상태가 변경되면 페이지 업데이트 (수취확인 상태 표시)
                    const orderItemStatus = document.querySelector(`[data-order-item-id="${currentOrderItemId}"]`)
                        .querySelector('.order-status'); // .order-status 클래스를 가진 요소 찾기

                    if (orderItemStatus) {
                        orderItemStatus.textContent = "CONFIRMATION"; // 상태 텍스트 업데이트
                    } else {
                        console.error("Order item status element not found.");
                    }

                    // 모달 닫기
                    receiptModal.style.display = "none";

                    // 페이지 새로고침
                    location.reload();
                } else {
                    alert("수취 확인에 실패했습니다.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
    }
});

// 반품 신청 모달 관련 코드
const returnBtn = document.querySelectorAll(".return-btn");
const returnModal = document.getElementById("returnModal");
const closeReturnModalBtn = document.querySelector(".modal.return .close.return");
const confirmReturnBtn = document.getElementById("confirmReturnBtn");
const cancelReturnModalBtn = document.getElementById("ReturncancelModalBtn");

// 반품 신청 아이템 ID를 저장할 변수
let currentOrderItemId2 = null;

// 반품 신청 버튼 클릭 시 모달 열기
returnBtn.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        currentOrderItemId2 = order.getAttribute("data-order-item-id");  // 해당 주문 아이템 ID 저장
        returnModal.style.display = "block"; // 모달 열기
    });
});

// 모달 닫기 (닫기 버튼 클릭)
closeReturnModalBtn.addEventListener("click", function() {
    returnModal.style.display = "none"; // 모달 닫기
});

// 모달 닫기 (취소 버튼 클릭)
cancelReturnModalBtn.addEventListener("click", function() {
    returnModal.style.display = "none"; // 모달 닫기
});

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target == returnModal) {
        returnModal.style.display = "none"; // 모달 닫기
    }
};

// 반품 신청 확인 버튼 클릭 시 상태 변경
confirmReturnBtn.addEventListener("click", function() {
    if (currentOrderItemId2 !== null) {
        // 서버에 반품 요청
        fetch(`/mypage/confirmReturn/${currentOrderItemId2}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: "RETURN_REQUESTED" })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // 성공적으로 반품 신청되면 페이지 업데이트
                    const orderItemStatus = document.querySelector(`[data-order-item-id="${currentOrderItemId2}"]`)
                        .querySelector('.order-status'); // .order-status 클래스를 가진 요소 찾기
                    if (orderItemStatus) {
                        orderItemStatus.textContent = "RETURN_REQUESTED"; // 상태 텍스트 업데이트
                    } else {
                        console.error("Order item status element not found.");
                    }

                    // 모달 닫기
                    returnModal.style.display = "none";

                    // 페이지 새로고침
                    location.reload();
                } else {
                    alert("구매 확정 상품은 환불이 불가능합니다. 고객센터로 문의주세요.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
    }
});

// 교환 신청 모달 관련 코드
const exchangeBtn = document.querySelectorAll(".exchange-btn");
const exchangeModal = document.getElementById("exchangeModal");
const closeExchangeModalBtn = document.querySelector(".modal.exchange .close.exchange");
const confirmExchangeBtn = document.getElementById("confirmExchangeBtn");
const cancelExchangeModalBtn = document.getElementById("ExchangecancelModalBtn");

// 교환 신청 아이템 ID를 저장할 변수
let currentOrderItemId3 = null;

// 교환 신청 버튼 클릭 시 모달 열기
exchangeBtn.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        currentOrderItemId3 = order.getAttribute("data-order-item-id");  // 해당 주문 아이템 ID 저장
        exchangeModal.style.display = "block"; // 모달 열기
    });
});

// 모달 닫기 (닫기 버튼 클릭)
closeExchangeModalBtn.addEventListener("click", function() {
    exchangeModal.style.display = "none"; // 모달 닫기
});

// 모달 닫기 (취소 버튼 클릭)
cancelExchangeModalBtn.addEventListener("click", function() {
    exchangeModal.style.display = "none"; // 모달 닫기
});

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target == exchangeModal) {
        exchangeModal.style.display = "none"; // 모달 닫기
    }
};

// 교환 신청 확인 버튼 클릭 시 상태 변경
confirmExchangeBtn.addEventListener("click", function() {
    if (currentOrderItemId3 !== null) {
        // 서버에 교환 요청
        fetch(`/mypage/confirmExchange/${currentOrderItemId3}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: "EXCHANGE_REQUESTED" })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // 성공적으로 교환 신청되면 페이지 업데이트
                    const orderItemStatus = document.querySelector(`[data-order-item-id="${currentOrderItemId3}"]`)
                        .querySelector('.order-status'); // .order-status 클래스를 가진 요소 찾기
                    if (orderItemStatus) {
                        orderItemStatus.textContent = "EXCHANGE_REQUESTED"; // 상태 텍스트 업데이트
                    } else {
                        console.error("Order item status element not found.");
                    }

                    // 모달 닫기
                    exchangeModal.style.display = "none";

                    // 페이지 새로고침
                    location.reload();
                } else {
                    alert("구매 확정 상품은 교환이 불가능합니다. 고객센터로 문의주세요.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
    }
});

// 판매자 정보 모달창
const sellerModal = document.getElementById("sellerModal");
const sellerNumbers = document.querySelectorAll(".seller-number");
const closeModalBtn2 = document.querySelector(".modal.seller .close");

// 각 판매자 정보를 클릭할 때 모달에 데이터를 반영하고 열기
sellerNumbers.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();

        // 클릭한 요소의 data- 속성에서 각 데이터 가져오기
        const company = order.getAttribute("data-company");
        const ceo = order.getAttribute("data-ceo");
        const phone = order.getAttribute("data-phone");
        const fax = order.getAttribute("data-fax");
        const email = order.getAttribute("data-email");
        const bno = order.getAttribute("data-bno");
        const address = order.getAttribute("data-address");

        // 모달 내용에 해당 데이터 반영
        document.querySelector("#sellerModal td[data-field='company']").textContent = company;
        document.querySelector("#sellerModal td[data-field='ceo']").textContent = ceo;
        document.querySelector("#sellerModal td[data-field='phone']").textContent = phone;
        document.querySelector("#sellerModal td[data-field='fax']").textContent = fax;
        document.querySelector("#sellerModal td[data-field='email']").textContent = email;
        document.querySelector("#sellerModal td[data-field='bno']").textContent = bno;
        document.querySelector("#sellerModal td[data-field='address']").textContent = address;

        // 모달 보이기
        sellerModal.style.display = "block";
    });
});

// 닫기 버튼 클릭 시 모달 닫기
closeModalBtn2.addEventListener("click", function() {
    sellerModal.style.display = "none";
});

// 모달 외부 클릭 시 모달 닫기
window.onclick = function(event) {
    if (event.target == sellerModal) {
        sellerModal.style.display = "none";
    }
};

//pReview-btn
const pReviewbtn = document.querySelectorAll(".pReview-btn");
const pReviewModel = document.getElementById("pReviewModal");
const closeModalBtn5 = document.querySelector(".modal.pReview .close.pReview");

document.querySelectorAll('.pReview-btn').forEach(button => {
    button.onclick = function() {
        const productId = this.dataset.productNo; // 상품 ID 가져오기
        const productName = this.dataset.productName; // 상품 이름 가져오기

        // 모달에 상품 이름과 ID 설정
        document.getElementById("modalProductName").textContent = productName;
        document.getElementById("modalProductId").value = productId; // hidden 필드에 상품 ID 설정

        // 모달 표시
        document.getElementById("pReviewModal").style.display = "block";
    };
});

document.getElementById("submitReviewBtn").onclick = function() {
    console.log("Submitted Rating:", document.getElementById('rating').value);
    const productId = document.getElementById("modalProductId").value;
    const rating = document.getElementById('rating').value;
    if (!rating) {
        alert("만족도를 선택해주세요.");
        return;
    }
    const content = document.getElementById("contents").value;

    // FormData 객체 생성
    const formData = new FormData();
    formData.append("productId", productId);
    formData.append("rating", rating);
    formData.append("content", content);

    // 모든 파일 입력 요소에서 파일을 가져오기
    const fileInputs = document.querySelectorAll('.file-input');
    fileInputs.forEach(input => {
        if (input.files.length > 0) {
            for (let i = 0; i < input.files.length; i++) {
                formData.append("pReviewFiles", input.files[i]);
            }
        }
    });

    fetch("/mypage/myInfo/review", {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("상품평이 등록되었습니다!");
                pReviewModal.style.display = "none";
                document.getElementById("contents").value = "";
                document.getElementById("rating").value = "";
                document.querySelectorAll('.star').forEach(s => {
                    s.classList.remove('selected');
                });
            } else {
                alert("상품평 등록에 실패했습니다.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("상품평 등록 중 오류가 발생했습니다.");
        });
};
console.log(closeModalBtn4);
pReviewbtn.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        pReviewModel.style.display = "block"; // Show the modal

    });
});

// Close modal when clicking the close button
closeModalBtn5.addEventListener("click", function() {
    pReviewModel.style.display = "none"; // Hide the modal
});

// Close modal when clicking outside the modal content
window.onclick = function(event) {
    if (event.target == pReviewModel) {
        pReviewModel.style.display = "none"; // Hide the modal
    }
};

const stars = document.querySelectorAll('.star');

// 별점을 클릭했을 때의 이벤트 설정
stars.forEach(star => {
    star.addEventListener('click', function() {
        const ratingValue = this.getAttribute('data-value');

        // 선택한 별점에 맞춰 배경색 및 선택 상태를 업데이트합니다.
        stars.forEach(s => {
            if (s.getAttribute('data-value') <= ratingValue) {
                s.classList.add('selected'); // 선택된 별점
            } else {
                s.classList.remove('selected'); // 선택되지 않은 별점
            }
        });

        // rating hidden input에 값을 설정합니다.
        document.getElementById('rating').value = ratingValue; // 선택된 별점 값을 저장
    });
});

// 파일 추가 시 새로운 input[type="file"] 동적 추가
const fileContainer = document.getElementById('fileContainer');

fileContainer.addEventListener('change', function (event) {
    const target = event.target;
    if (target.classList.contains('file-input') && target.files.length > 0) {
        // 새로운 파일 input 추가
        const newFileInput = document.createElement('input');
        newFileInput.type = 'file';
        newFileInput.name = 'pReviewFiles'; // 동일한 이름 사용
        newFileInput.classList.add('file-input');
        newFileInput.multiple = true; // 여러 파일 선택 가능하도록 설정
        fileContainer.appendChild(newFileInput);
    }
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


//판매자정보- 문의하기 모달창
const qnabtn = document.querySelectorAll(".qna-btn");
const qnaModel = document.getElementById("qnaModal");
const closeModalBtn3 = document.querySelector(".modal.qna .close");

qnabtn.forEach(order => {
    order.addEventListener("click", function(e) {
        e.preventDefault();
        qnaModel.style.display = "block"; // Show the modal
        sellerModal.style.display="none"
    });
});

// Close modal when clicking the close button
closeModalBtn3.addEventListener("click", function() {
    qnaModel.style.display = "none"; // Hide the modal
});

// Close modal when clicking outside the modal content
window.onclick = function(event) {
    if (event.target == qnaModel) {
        qnaModel.style.display = "none"; // Hide the modal
    }
};
