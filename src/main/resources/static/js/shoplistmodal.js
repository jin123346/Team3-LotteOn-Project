// 모달 열고 닫는 기능 구현
var modal = document.getElementById("productModal"); // ID로 모달 요소 선택
var btn = document.querySelectorAll(".openModalBtn"); // 클래스 선택자 수정
var span = modal.querySelector(".close"); // 모달 내부의 닫기 버튼 선택
var order_modal; // 주문 모달을 나중에 정의하기 위해 초기화
var order_btn; // 주문 버튼을 나중에 정의하기 위해 초기화
var shipping_modal; // 배송 모달을 나중에 정의하기 위해 초기화
var shipping_btn; // 배송 버튼을 나중에 정의하기 위해 초기화

// 버튼을 클릭하면 기본 모달을 엽니다.
btn.forEach(function(button) {
    button.onclick = function() {
        modal.style.display = "block"; // 모달 열기
    };
});

// 닫기 버튼을 클릭하면 기본 모달을 닫습니다.
span.onclick = function() {
    modal.style.display = "none";
}

// 모달 외부를 클릭하면 모달을 닫습니다.
window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    } else if (order_modal && event.target === order_modal) {
        order_modal.style.display = "none";
    } else if (shipping_modal && event.target === shipping_modal) {
        shipping_modal.style.display = "none";
    }
}

// 주문 상세 모달 로드
fetch('../../include/admin-order-modal/order-modal.html')
    .then(resp => resp.text())
    .then(data => {
        document.getElementById('order-modal-div').innerHTML = data;

        // HTML이 로드된 후 모달 버튼 클릭 이벤트 설정
        order_btn = document.querySelectorAll('.order-modal-btn'); // 모든 주문 버튼 선택
        order_modal = document.querySelector('.modal-section'); // .modal-section 선택
        var close_btn = document.getElementsByClassName('order-close')[0]; // "X" 버튼 선택

        // 주문 상세 모달 열기
        order_btn.forEach(function(btn) {
            btn.onclick = function () {
                if (order_modal) {
                    order_modal.style.display = "flex"; // 중앙 정렬을 위해 flex 사용
                }
            };
        });

        // "X" 버튼 클릭 시 모달 닫기
        close_btn.onclick = function () {
            if (order_modal) {
                order_modal.style.display = "none"; // 모달 닫기
            }
        };

        // 첫 번째 추가 HTML 파일 불러오기
        return fetch('../../include/another-modal.html'); // 첫 번째 추가 HTML 파일
    })
    .then(resp => {
        if (!resp.ok) throw new Error("Error fetching another-modal.html");
        return resp.text();
    })
    .then(data => {
        // 첫 번째 추가 HTML을 모달에 삽입
        const additionalContent1 = document.createElement('div');
        additionalContent1.innerHTML = data;
        order_modal.appendChild(additionalContent1); // 모달에 추가

        // 두 번째 추가 HTML 파일 불러오기
        return fetch('../../include/yet-another-modal.html'); // 두 번째 추가 HTML 파일
    })
    .then(resp => {
        if (!resp.ok) throw new Error("Error fetching yet-another-modal.html");
        return resp.text();
    })
    .then(data => {
        // 두 번째 추가 HTML을 모달에 삽입
        const additionalContent2 = document.createElement('div');
        additionalContent2.innerHTML = data;
        order_modal.appendChild(additionalContent2); // 모달에 추가
    })
    .catch(err => {
        console.error("Error loading modal:", err);
    });

// 배송 상세 모달 로드
fetch('../../include/admin-shipping-modal/shipping-modal.html')
    .then(resp => resp.text())
    .then(data => {
        document.getElementById('shipping-modal-div').innerHTML = data;

        // HTML이 로드된 후 배송 버튼 클릭 이벤트 설정
        shipping_btn = document.getElementById('shipping-modal-btn');
        shipping_modal = document.querySelector('.shipping-section'); // .shipping-section 선택
        var shipping_close_btn = document.getElementsByClassName('shipping-close')[0]; // "X" 버튼 선택

        // 배송 상세 모달 열기
        shipping_btn.onclick = function () {
            if (shipping_modal) {
                shipping_modal.style.display = "flex"; // 중앙 정렬을 위해 flex 사용
            }
        };

        // "X" 버튼 클릭 시 모달 닫기
        shipping_close_btn.onclick = function () {
            if (shipping_modal) {
                shipping_modal.style.display = "none"; // 모달 닫기
            }
        };
    })
    .catch(err => {
        console.error("Error loading shipping modal:", err);
    });
