document.addEventListener('DOMContentLoaded', function() {
    const reviewImages = document.querySelectorAll('.reviewImg img'); // 모든 리뷰 이미지 선택
    const pReviewModal = document.getElementById('pReviewModal'); // 리뷰 작성 모달 선택
    const closeModalButtons = document.querySelectorAll('.close.pReview'); // 모달 닫기 버튼 선택

    reviewImages.forEach((img) => {
        img.addEventListener('click', function() {
            const reviewImgDiv = this.closest('.reviewImg');
            const ReviewWriter = reviewImgDiv.getAttribute("data-writer-content");
            const ReviewProdName = reviewImgDiv.getAttribute("data-prodName-content");
            const ReviewRating = reviewImgDiv.getAttribute("data-rating-content");
            const ReviewContent = reviewImgDiv.getAttribute("data-review-content");
            const ReviewImages = reviewImgDiv.getAttribute("data-reviewImages-content");

            console.log(ReviewImages); // 이 부분에서 ReviewImages 출력 확인

            document.getElementById("modalProductName").textContent = ReviewProdName;
            document.getElementById("modalReviewWriter").textContent = ReviewWriter;
            document.getElementById("modalReviewWriter2").value = ReviewWriter;
            document.getElementById("rating-display").textContent = ReviewRating;
            document.getElementById("verContent").textContent = ReviewContent;

            // 쉼표로 구분된 이미지 파일 이름 추출
            const imgNames = ReviewImages.split(',').map(img => img.match(/sname=([^,]+)/)).filter(Boolean).map(match => match[1]);

            console.log("여기가", imgNames);

            const reviewImgsContainer = document.querySelector('.reviewImgs'); // 이미지가 추가될 컨테이너

            // 컨테이너 초기화
            reviewImgsContainer.innerHTML = '';

            // 서버 주소 설정 (로컬/배포 환경에 따라 동적으로 설정)
            const serverUrl = window.location.hostname === 'localhost'
                ? 'http://localhost:8085'
                : 'http://43.202.32.28:8085';

            // 각 이미지에 대해 <img> 태그 생성 및 추가
            imgNames.forEach(imgName => {
                const imgElement = document.createElement('img');
                imgElement.src = `${serverUrl}/uploads/ReviewImg/${imgName.trim()}`;
                imgElement.alt = 'Review Image';
                reviewImgsContainer.appendChild(imgElement);
            });





            document.querySelectorAll('.rating-display2').forEach(display => {
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

            // 여기에 추가로 다른 정보를 설정할 수 있습니다.

            // 모달 표시
            pReviewModal.style.display = 'block';
        });
    });

    // 모달 닫기 버튼 클릭 시 모달 닫기
    closeModalButtons.forEach((button) => {
        button.addEventListener('click', function() {
            pReviewModal.style.display = 'none'; // 모달 숨기기
        });
    });

    // 모달 외부 클릭 시 모달 닫기 (선택 사항)
    window.addEventListener('click', function(event) {
        if (event.target === pReviewModal) {
            pReviewModal.style.display = 'none'; // 모달 숨기기
        }
    });

});


