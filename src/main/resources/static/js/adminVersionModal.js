
// 모달 열고 닫는 기능 구현
var modal = document.getElementById("orderModal");
var btn = document.getElementById("openModalBtn");
var span = document.getElementsByClassName("close")[0];
// 버튼을 클릭하면 모달을 엽니다.
btn.onclick = function() {
    modal.style.display = "block";
}
// 닫기 버튼을 클릭하면 모달을 닫습니다.
span.onclick = function() {
    modal.style.display = "none";
}
// 모달 외부를 클릭하면 모달을 닫습니다.
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// 모달과 버튼을 선택
var modal2 = document.querySelector(".modal.orderModal"); // .modal.orderModal 클래스를 가진 첫 번째 모달 선택
var btn2 = document.querySelectorAll(".openModalBtn2"); // 모든 모달 열기 버튼 선택
var span2 = document.querySelector(".close2"); // 닫기 버튼
var closeButton2 = document.querySelector(".closeButton"); // '닫기' 버튼 선택

// 두 번째 모달 열기 버튼 클릭 이벤트 추가
var btn2 = document.querySelectorAll(".openModalBtn2"); // 모든 두 번째 모달 열기 버튼 선택
btn2.forEach(function(button) {
    button.onclick = function() {
        // 데이터 속성에서 버전명과 변경내역 가져오기
        const verName = this.getAttribute("data-ver_name");
        const verContent = this.getAttribute("data-ver_content").replace(/\n/g, "<br>"); // Enter로 줄바꿈

        // 모달 내 요소에 데이터 설정
        document.querySelector(".modal.orderModal .verName").textContent = verName;
        document.querySelector(".modal.orderModal .verContent").innerHTML = verContent; // HTML로 설정하여 줄바꿈 적용

        // 두 번째 모달 열기
        modal2.style.display = "block"; // 두 번째 모달 표시
    };
});

// 두 번째 모달 닫기 버튼 클릭 이벤트
span2.onclick = function() {
    modal2.style.display = "none"; // 두 번째 모달 숨기기
};

// '닫기' 버튼 클릭 이벤트
closeButton2.onclick = function() {
    modal2.style.display = "none"; // 두 번째 모달 숨기기
};

// 모달 외부 클릭 시 모달 닫기
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none"; // 첫 번째 모달 숨기기
    } else if (event.target == modal2) {
        modal2.style.display = "none"; // 두 번째 모달 숨기기
    }
};