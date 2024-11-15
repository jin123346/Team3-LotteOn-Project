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
    const footerHeight = 440; // 푸터 높이
    const asideHeight = aside.offsetHeight; // aside 높이
    console.log('asdieHeight:'+asideHeight)
    function handleAsideScroll() {
        const scrollPosition = window.scrollY; // 현재 스크롤 위치
        console.log(scrollPosition)
        const footerTop = footer.getBoundingClientRect().top + window.scrollY; // Footer의 상단 위치
        console.log('footerTop:'+footerTop);
        // 스크롤 위치가 헤더 아래에 있고, 푸터에 도달하기 전이면 aside에 scroll 클래스를 추가
        if (scrollPosition <= headerHeight && (scrollPosition + asideHeight + 50) < footerTop ) {
            aside.classList.remove('scroll');
            // aside.style.position = 'fixed';
            // aside.style.top = `${headerHeight}px`; // 헤더 아래에 고정
            aside.style.bottom = ''; // 푸터 닿지 않음
        } else if ((scrollPosition + asideHeight + 50) >= footerTop) {
            // 푸터에 도달하면 aside를 푸터 상단에서 멈추게 하기
            aside.classList.add('scroll');
            aside.style.position = 'absolute';
            aside.style.top = `${footerTop - asideHeight - 50}px`;  // 푸터 상단에 고정
            // console.log('asdide top : '+`${footerTop - asideHeight - 50}px`)
        } else {
            aside.classList.add('scroll');
            aside.style.position = ''; // 초기 위치로 돌아감
            aside.style.top = ''; // 원래 위치로 초기화
        }
    }

    // 스크롤할 때마다 실행
    window.addEventListener('scroll', handleAsideScroll);
    // 화면 크기가 변경될 때도 실행
    window.addEventListener('resize', handleAsideScroll);

});