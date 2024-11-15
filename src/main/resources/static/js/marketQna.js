document.addEventListener("DOMContentLoaded", () => {
    const btnGoQuestion = document.querySelector('.btnGoQuestion');

    // 로그인 체크 후 리다이렉트 함수
    function loginredirect() {
        const uidElement = document.getElementById("uid");
        const uid = uidElement ? uidElement.value : null;

        // 로그인 상태 체크
        if (!uid) {
            alert('로그인 후 이용해 주세요');
            // 로그인 페이지로 리다이렉트, 현재 페이지 URL을 redirect 파라미터로 전달
            window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
        } else {
            // 로그인 상태라면 문의하기 페이지로 이동
            window.location.href = "/mypage/qnadetails";
        }
    }

    // "문의하기" 버튼 클릭 시 loginredirect 함수 실행
    if (btnGoQuestion) {
        btnGoQuestion.addEventListener('click', loginredirect);
    }
});
