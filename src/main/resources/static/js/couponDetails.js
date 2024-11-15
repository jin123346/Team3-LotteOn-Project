// 쿠폰 리스트를 불러오는 함수
function fetchCouponList() {
    $.ajax({
        url: '/api/mypage/coupons',  // 쿠폰 리스트를 반환하는 API
        type: 'GET',
        dataType: 'json',
        success: function(coupons) {
            // 받은 쿠폰 리스트를 처리하여 화면에 출력
            let couponListHtml = '';  // 쿠폰 리스트 HTML
            if (coupons && coupons.length > 0) {
                coupons.forEach(function(coupon) {
                    couponListHtml += `
                        <div class="coupon-item">
                            <p>쿠폰명: ${coupon.couponName}</p>
                            <p>발급일: ${coupon.issueDate}</p>
                            <p>만료일: ${coupon.expiryDate}</p>
                            <p>상태: ${coupon.status}</p>
                        </div>
                    `;
                });
            } else {
                couponListHtml = '<p>발급된 쿠폰이 없습니다.</p>';
            }

            // #coupon-list에 쿠폰 리스트 출력
            $('#coupon-list').html(couponListHtml);
        },
        error: function(xhr, status, error) {
            console.error('쿠폰 리스트를 불러오는 데 실패했습니다.');
            $('#coupon-list').html('<p>쿠폰을 불러오는 데 실패했습니다.</p>');
        }
    });
}

// 페이지 로드 시 쿠폰 리스트를 불러옴
$(document).ready(function() {
    fetchCouponList();  // 쿠폰 리스트 불러오기
});
