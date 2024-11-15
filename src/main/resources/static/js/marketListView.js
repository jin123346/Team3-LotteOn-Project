 document.addEventListener("DOMContentLoaded", function() {
            document.querySelectorAll('.shipping-fee').forEach(function(feeElement) {

                // shippingFee 값을 가져와 숫자로 변환
                const shippingFee = parseFloat(feeElement.innerText);
                console.log("shippingFee");

                // shippingFee가 0이면 무료배송으로 텍스트 변경 및 클래스 추가
                if (shippingFee === 0) {
                    feeElement.innerText = "무료배송";
                    feeElement.classList.add("shipping-label");
                }
            });


            // 모든 seller-rating 요소를 가져옵니다.
            document.querySelectorAll('.seller-rating').forEach(function(ratingElement) {
                // data-rating 속성에서 rating 값을 가져옵니다.
                const rating = parseFloat(ratingElement.getAttribute('data-rating'));
                const fullStars = Math.floor(rating);   // 꽉 찬 별 수
                const halfStar = rating % 1 !== 0;     // 반쪽 별 여부 확인

                // 꽉 찬 별 추가
                for (let i = 0; i < fullStars; i++) {
                    const star = document.createElement('span');
                    star.classList.add('star', 'full-star');
                    star.innerHTML = '⭐';
                    ratingElement.appendChild(star);
                }

                // 반쪽 별 추가
                if (halfStar) {
                    const halfStar = document.createElement('span');
                    halfStar.classList.add('star', 'half-star');
                    halfStar.innerHTML = '⭐'; // 반쪽 별을 나타낼 경우, CSS를 추가해 스타일링 가능
                    ratingElement.appendChild(halfStar);
                }

                // 5개 미만인 경우 빈 별 추가
                const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);
                for (let i = 0; i < emptyStars; i++) {
                    const star = document.createElement('span');
                    star.classList.add('star', 'empty-star');
                    star.innerHTML = '⭐'; // 빈 별도 CSS를 통해 스타일링 가능
                    ratingElement.appendChild(star);
                }
            });
        });