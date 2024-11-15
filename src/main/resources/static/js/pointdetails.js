// 날짜 포맷을 YYYY-MM-DD 형식으로 맞추는 함수
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



