document.addEventListener('DOMContentLoaded', function() {
    const links = document.querySelectorAll('#aside .aside_list ul li a');
    const titleElement = document.querySelector('#service .title h2');  // <h2> 요소
    const descriptionElement = document.querySelector('#service .title p');  // <p> 요소
    const urlParams = new URLSearchParams(window.location.search);
    const cate = urlParams.get('cate') || '전체';  // URL에서 cate 파라미터를 가져오고, 없으면 '전체'로 기본 설정

    // 페이지 번호 변수 정의 및 초기화
    let pageNumber = 1; // 필요한 페이지 번호를 설정하세요 (기본값 1)

    // 제목 및 설명 업데이트
    titleElement.textContent = cate;
    descriptionElement.textContent = `${cate} 관련 공지사항입니다.`;

    // 첫 번째 링크에 기본적으로 active 클래스 추가
    links.forEach(link => {
        if (link.textContent.trim() === cate) {
            link.classList.add('active');
        }

        // 카테고리 클릭 시 해당 데이터 가져오기
        link.addEventListener('click', function() {
            const selectedCategory = this.textContent;

            // URL의 cate 파라미터 업데이트
            const newUrl = new URL(window.location.href);
            newUrl.searchParams.set('cate', selectedCategory);
            window.history.pushState({ path: newUrl.href }, '', newUrl.href);
        });
    });
});
