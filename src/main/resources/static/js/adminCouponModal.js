document.addEventListener("DOMContentLoaded", function () {
    var modal = document.getElementById("couponModal");
    var issuedModal = document.getElementById("couponInfoModal");
    var closeBtn = document.querySelector(".close"); // X 버튼
    var closeFooterBtn = document.querySelector(".modal-close-btn"); // 하단 '닫기' 버튼
    var orderLinks = document.querySelectorAll(".order-link");
    var issuedOrderLink = document.querySelectorAll(".issued-order-link");

    // 링크 클릭 시 모달 열기 및 데이터 로드
    orderLinks.forEach(function (link) {
        link.addEventListener("click", function (event) {
            event.preventDefault();

            var couponNumber = this.getAttribute("data-coupon-id");
            var issuer = this.getAttribute("data-issuer");
            var couponType = this.getAttribute("data-type");
            var couponName = this.getAttribute("data-name");
            var benefit = this.getAttribute("data-benefit");
            var period = this.getAttribute("data-period");
            var notes = this.getAttribute("data-notes");
            console.log(period)
            console.log()
            // 모달에 데이터 채우기
            document.getElementById("modalCouponNumber").innerText = couponNumber;
            document.getElementById("modalIssuer").innerText = issuer;
            document.getElementById("modalCouponType").innerText = couponType;
            document.getElementById("modalCouponName").innerText = couponName;
            document.getElementById("modalBenefit").innerText = benefit;
            document.getElementById("modalPeriod").innerText = period;
            document.getElementById("modalNotes").innerText = notes;





            // 모달 보이기
            modal.style.display = "block";
        });
    });

    issuedOrderLink.forEach(function (link) {
        link.addEventListener("click", function (event) {
            event.preventDefault();

            // 데이터 속성 가져오기
            const issuedCouponId = this.getAttribute('data-issued-couponId');
            const issuedIssuanceNumber = this.getAttribute('data-issued-id');
            const issuedCouponType = this.getAttribute('data-issued-couponType');
            const issuedUsageStatus = this.getAttribute('data-issued-usageStatus');
            const issuedMemberName = this.getAttribute('data-issued-memberName');
            const issuedCouponName = this.getAttribute('data-issued-couponName');
            const issuedRestrictions = this.getAttribute('data-issued-restrictions');
            const issuedBenefit = this.getAttribute('data-issued-benefit');
            const issuedPeriod = this.getAttribute('data-period');
            const issuedIssuer = this.getAttribute('data-issued-issuer');

            // 모달에 데이터 채우기
            document.getElementById('modalCouponNumber').innerText = issuedCouponId;
            document.getElementById('modalIssueNumber').innerText = issuedIssuanceNumber;
            document.getElementById('modalCouponType').innerText = issuedCouponType;
            document.getElementById('modalUsageStatus').innerText = issuedUsageStatus;
            document.getElementById('modalTarget').innerText = issuedMemberName;
            document.getElementById('modalCouponName').innerText = issuedCouponName;
            document.getElementById('modalBenefit').innerText = issuedBenefit;
            document.getElementById('modalPeriod').innerText = issuedPeriod;
            document.getElementById('modalNotes').innerText = issuedRestrictions;
            document.getElementById('modalIssuer').innerText = issuedIssuer;


            // 모달 보이기
            issuedModal.style.display = "block";
        });
    });


    // 모달 X 버튼 클릭 시 닫기
    closeBtn.onclick = function () {
        modal.style.display = "none";
    };

    // 모달 하단 '닫기' 버튼 클릭 시 닫기
    closeFooterBtn.onclick = function () {
        modal.style.display = "none";
    };

    // 모달 외부 클릭 시 닫기
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    };
});


document.addEventListener('DOMContentLoaded', () => {
    // 쿠폰 등록 모달 관련 요소
    const addCouponBtn = document.querySelector('.add-coupon-btn');
    const addCouponModal = document.getElementById('addCouponModal');
    const closeBtn = addCouponModal.querySelector('.close');
    const cancelBtn = document.getElementById('cancelBtn');
    const submitBtn = document.getElementById('submitBtn');
    const productSelect = document.getElementById("productSelect"); // 상품 선택 요소
    const couponTypeSelect = document.getElementById("couponType"); // 쿠폰 타입 선택 박스

    // 쿠폰 등록 모달 열기
    addCouponBtn.addEventListener('click', () => {
        addCouponModal.style.display = 'block';
        loadProducts(); // 상품 불러오기 호출

    });

    // 모달 닫기 함수
    function closeModal() {
        addCouponModal.style.display = 'none';
    }

    // 모달 닫기 (x 아이콘 클릭 또는 취소 버튼 클릭 시)
    closeBtn.addEventListener('click', closeModal);
    cancelBtn.addEventListener('click', closeModal);
    // 등록 버튼 클릭 시 폼 제출 처리
    submitBtn.addEventListener('click', (event) => {
        event.preventDefault();

        const benefit = document.querySelector('input[name="benefit"]:checked');
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;

        const inputcouponName = couponForm.couponName.value;
        const inputcouponType = couponForm.couponType.value;
        const inputbenefit = couponForm.benefit.value;
        const inputstartDate = couponForm.startDate.value;
        const inputendDate = couponForm.endDate.value;
        const inputnotes = couponForm.notes.value;
        const inputrestrictions = couponForm.restrictions.value;

        // 상품 아이디 들고오기
        const selectedProductId = productSelect.value; // 여기가 중요합니다.
        console.log(selectedProductId,"상품아이디")

        // 모든 필드가 입력되었는지 확인
        if (!inputcouponName || !inputcouponType || !inputbenefit || !inputstartDate || !inputendDate || !inputnotes || !inputrestrictions) {
            alert("모든 필드를 입력해 주세요.");
            return; // 폼 제출 방지
        }
        // 쿠폰 타입이 '개별상품할인'일 때 상품 선택 유효성 검사
        if (inputcouponType === "discount" && !selectedProductId) {
            alert("상품을 선택해 주세요.");
            return; // 폼 제출 방지
        }
        if (startDate > endDate) {
            alert("사용 기간이 잘못 설정 되었습니다.");
            return; // 폼 제출 방지
        }

        const sellerCompany = document.getElementById("addCouponModal").getAttribute("data-seller-company");
        console.log("Seller Company:", sellerCompany);

        const couponData = {
            couponId: 0,
            couponName: document.getElementById("couponName").value,
            couponType: document.getElementById("couponType").value,
            restrictions: document.getElementById("restrictions").value,
            benefit: benefit ? benefit.value : '',
            startDate: startDate,
            endDate: endDate,
            notes: document.getElementById("notes").value,
            rdate: new Date().toISOString().split('T')[0], // 현재 날짜
            productId: selectedProductId, // 선택된 상품 ID 추가
            sellerDTO: {
                // 필요한 SellerDTO 필드 추가
                company   : sellerCompany
            }
        };
        console.log("Selected Product ID:", selectedProductId);

        console.log("쿠폰 데이터 전송:", JSON.stringify(couponData)); // JSON 데이터 확인

        fetch('/seller/coupon/register', {
            method: 'POST',
            headers:{
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(couponData)

        })
            .then(resp => {
                if (resp.ok){
                    alert("쿠폰이 등록되었습니다.")
                    closeModal();
                    document.getElementById("couponForm").reset();
                    window.location.href = '/seller/coupon/list'; // 리다이렉트

                }else {
                    return resp.json().then(errorData => {
                        alert(`등록에 실패했습니다: ${errorData.message || "서버 오류"}.`);
                    });
                }
            })
            .catch(error => console.error("Error------------" + error))
    });
    // 쿠폰 종류 변경 시 상품 선택 박스 상태 변경
    couponTypeSelect.addEventListener('change', () => {
        loadProducts(); // 쿠폰 타입에 따라 상품 목록 다시 로딩
    });

    function loadProducts(){
        const sellerCompany = addCouponModal.getAttribute("data-seller-company");
        const selectedCouponType = couponTypeSelect.value; // 현재 선택된 쿠폰 타입


        console.log("Seller Company:", sellerCompany);


        if (selectedCouponType === 'discount') {
            productSelect.style.visibility = 'visible'; // 상품 선택 박스를 보이게 함
            productSelect.style.position = 'relative'; // 레이아웃 위치를 고정시킴
        fetch(`/seller/coupon/products`)
            .then(resp => {
                if (!resp.ok) {
                    throw new Error("상품을 불러오는 데 실패했습니다."); // 400 오류가 발생했을 때 처리
                }
                return resp.json();
            })
            .then(products => {
                console.log("불러온 상품 데이터:", products); // 전체 제품 데이터 로그

                productSelect.innerHTML = '<option value="" disabled selected>쿠폰 적용상품</option>'; // 기본 옵션 추가

                if (products.length === 0) {
                    alert("등록된 상품이 없습니다."); // 상품이 없을 때 알림
                    return; // 더 이상 진행하지 않음
                }

                products.forEach(product => {
                    console.log("상품 ID:", product.productId, "상품명:", product.productName); // 로그 추가
                    const option = document.createElement('option');
                    option.value = product.productId; // 상품 ID로 설정
                    option.textContent = product.productName; // 상품명
                    productSelect.appendChild(option);
                });
            })
            .catch(err => {
                console.error(err);
                alert("오류가 발생했습니다: " + err.message);

        });
        } else {
            productSelect.style.visibility = 'hidden'; // 'discount' 이외의 쿠폰 타입일 경우 상품 선택 박스를 숨김
        }
    }
    // 모달 외부 클릭 시 모달 닫기
    window.addEventListener('click', (event) => {
        if (event.target === addCouponModal) {
            closeModal();
        }
    });

});



// 모달 엘리먼트와 닫기 버튼
document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById('couponInfoModal');
    const closeModal = modal.querySelector('.close'); // X 아이콘
    const modalCloseBtn = modal.querySelector('.modal-close-btn'); // 닫기 버튼

    // 쿠폰 정보 업데이트 함수
    function updateModal(couponData) {
        document.getElementById('modalCouponNumber').textContent = couponData.couponNumber;
        document.getElementById('modalIssueNumber').textContent = couponData.issueNumber;
        document.getElementById('modalCouponType').textContent = couponData.couponType;
        document.getElementById('modalIssuer').textContent = couponData.issuer;
        document.getElementById('modalUsageStatus').textContent = couponData.usageStatus;
        document.getElementById('modalTarget').textContent = couponData.target;
        document.getElementById('modalCouponName').textContent = couponData.couponName;
        document.getElementById('modalBenefit').textContent = couponData.benefit;
        document.getElementById('modalPeriod').textContent = couponData.period;
        document.getElementById('modalNotes').textContent = couponData.notes;
    }

    // 발급번호 클릭 이벤트
    document.querySelectorAll('.order-link').forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            const couponData = {
                couponNumber: this.dataset.couponNumber,
                issueNumber: this.textContent, // 발급번호
                couponType: this.dataset.couponType,
                issuer: this.dataset.issuer,
                usageStatus: this.dataset.usageStatus,
                target: this.dataset.target,
                couponName: this.dataset.couponName,
                benefit: this.dataset.benefit,
                period: this.dataset.period,
                notes: this.dataset.notes,
            };

            updateModal(couponData);
            modal.style.display = 'block';
        });
    });

    // 모달 닫기 이벤트 (X 아이콘 클릭)
    closeModal.onclick = function () {
        modal.style.display = 'none';
    };

    // 모달 닫기 이벤트 (취소 버튼 클릭)
    modalCloseBtn.onclick = function () {
        modal.style.display = 'none';
    };

    // 모달 외부 클릭 시 닫기
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    };
});

// 쿠폰 종료 버튼 처리
document.querySelectorAll('.end-button').forEach(button => {
    button.addEventListener('click', function () {

        if (confirm("정말로 이 쿠폰을 종료하시겠습니까?")) {

            const couponId = this.closest('tr').querySelector('.order-link').getAttribute('data-coupon-id');
            const couponRow = this.closest('tr');
            const statusElement = couponRow.querySelector('.coupon-status');
            const currentStatus = statusElement.innerText.trim(); // 상태 가져오기
            console.log("쿠폰 ID:", couponId); // 확인용 로그

            fetch(`/seller/coupon/${couponId}/end`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' }
            })
                .then(resp => {
                    if (!resp.ok) throw new Error("Response not OK");
                    return resp.json();
                })
                .then(updatedCoupon => {
                    this.disabled = true; // 버튼 비활성화
                    statusElement.innerText = updatedCoupon.status; // 상태 업데이트
                    localStorage.setItem(`coupon-${couponId}`, JSON.stringify(updatedCoupon.status));
                    window.location.href = '/seller/coupon/list'; // 리다이렉트
                })
                .catch(error => console.error('Error:', error));
        }
    });
    document.querySelectorAll('.coupon-status').forEach(statusElement => {
        const couponRow = statusElement.closest('tr');
        const button = couponRow.querySelector('.end-button');
        const currentStatus = statusElement.innerText;

        if (currentStatus !== '발급 중') {
            button.style.backgroundColor = '#888'; // 회색 배경
            button.style.color = 'white'; // 흰색 글자
            button.disabled = true; // 상태에 따라 버튼 비활성화
        }
    });
});



// .issued-end-button 처리
document.querySelectorAll('.issued-end-button').forEach(button => {
    button.addEventListener('click', function () {
        if (confirm("정말로 이 쿠폰을 종료하시겠습니까?")) {

            // 쿠폰 ID를 찾기
            const issuanceNumber = this.getAttribute('data-id'); // 'data-id'에 저장된 issuanceNumber 사용
            const couponRow = this.closest('tr');
            const statusElement = couponRow.querySelector('.coupon-status');

            console.log("발급된 쿠폰 ID(issuanceNumber):", issuanceNumber); // 확인용 로그

            // 요청할 URL 구성 (쿠폰 ID에 맞는 URL로 요청)
            const requestUrl = `/seller/coupon/issued/${issuanceNumber}/end`; // API 엔드포인트를 변경

            // PUT 요청을 보내서 상태 변경
            fetch(requestUrl, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(resp => {
                    if (!resp.ok) throw new Error("Response not OK");
                    return resp.json(); // 응답을 JSON으로 파싱
                })
                .then(updatedCoupon => {
                    // 상태를 변경한 후 UI 업데이트
                    this.disabled = true; // 버튼 비활성화
                    statusElement.innerText = updatedCoupon.status; // 변경된 상태로 업데이트
                    console.log("업데이트된 쿠폰 상태:", updatedCoupon.status);

                    // 로컬 스토리지에 상태 업데이트 (선택사항)
                    localStorage.setItem(`issuanceNumber-${issuanceNumber}`, JSON.stringify(updatedCoupon.status));

                    // 페이지 새로 고침 또는 리다이렉트
                    window.location.reload(); // 페이지를 새로 고침하여 상태를 반영
                })
                .catch(error => console.error('Error:', error));
        }
    });
    // 버튼 클릭 외에도 상태에 따른 버튼 활성화/비활성화 및 스타일 변경
    const couponRow = button.closest('tr');  // 버튼이 포함된 행을 찾음
    const statusElement = couponRow.querySelector('.coupon-status');  // 상태 텍스트를 가진 요소 찾기
    const currentStatus = statusElement ? statusElement.innerText.trim() : '';  // 상태 가져오기

    if (currentStatus !== '사용가능') {
        button.style.backgroundColor = '#888'; // 비활성화된 버튼의 배경을 회색으로 변경
        button.style.color = 'white';  // 버튼 글자색을 흰색으로 변경
        button.disabled = true;  // 상태가 '발급 중'이 아닐 경우 버튼 비활성화
    } else {
        button.style.backgroundColor = ''; // 기본 배경으로 되돌리기
        button.style.color = '';  // 기본 글자색으로 되돌리기
        button.disabled = false;  // 버튼 활성화
    }

});






document.addEventListener('DOMContentLoaded', function () {
    // 쿠폰 목록 검색 기능
    const searchBtnCoupon = document.getElementById('searchBtnCoupon');
    const searchQuery = document.getElementById('searchQuery');

    if (searchBtnCoupon && searchQuery) {
        searchBtnCoupon.addEventListener('click', searchCoupon);
        searchQuery.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                searchCoupon();  // 엔터 키가 눌리면 검색 실행
            }
        });
    }

    // 쿠폰발급현황 검색 기능
    const searchBtnIssued = document.getElementById('searchBtnIssued');
    const searchIssuedQuery = document.getElementById('searchIssuedQuery');

    if (searchBtnIssued && searchIssuedQuery) {
        searchBtnIssued.addEventListener('click', searchIssuedCoupon);
        searchIssuedQuery.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                searchIssuedCoupon();  // 엔터 키가 눌리면 검색 실행
            }
        });
    }
});

// 쿠폰 목록 검색 기능
function searchCoupon() {
    const category = document.getElementById('searchCategory').value;
    const query = document.getElementById('searchQuery').value;
    console.log('Request URL:', `/seller/coupon/search?category=${category}&query=${encodeURIComponent(query)}`);

    const url = `/seller/coupon/search?category=${category}&query=${encodeURIComponent(query)}`;

    fetch(url)
        .then(response => {
            console.log('Response Status:', response.status); // 응답 상태 코드 확인
            return response.json();
        })
        .then(data => {
            console.log('Response received:', data);
            displayCoupons(data);
        })
        .catch(error => {
            console.error('검색 중 오류 발생:', error);
        });
}

// 검색 결과를 화면에 표시
function displayCoupons(data) {
    const couponTable = document.getElementById('couponTable');
    const couponContainer = couponTable.querySelector('#couponTbody');

    couponContainer.innerHTML = ''; // 이전 검색 결과를 지움

    if (data && data.length > 0) {
        data.forEach(coupon => {
            const couponElement = document.createElement('tr');
            couponElement.classList.add('coupon-item');
            couponElement.innerHTML = `
               <td><a href="#" class="order-link"
                       data-coupon-id="${coupon.couponId}"
                       data-issuer="${coupon.sellerCompany}"
                       data-type="${coupon.couponType}"
                       data-name="${coupon.couponName}"
                       data-benefit="${coupon.benefit}"
                       data-period="${coupon.startDate} ~ ${coupon.endDate}"
                       data-notes="${coupon.notes}"
                       >${coupon.couponId}</a></td>
                <td>${coupon.couponName}</td>
                <td>${coupon.couponType}</td>
                <td>${coupon.benefit}</td>
                <td>
                    <span>${coupon.startDate}</span> ~
                    <span>${coupon.endDate}</span>
                </td>
                <td>${coupon.sellerCompany}</td>
                <td>${coupon.issuedCount}</td>
                <td>${coupon.usedCount}</td>
                <td class="coupon-status">${coupon.status}</td>
                <td>${coupon.rdate}</td>
                <td><button class="end-button">종료</button></td>
            `;
            couponContainer.appendChild(couponElement);
        });
    } else {
        couponContainer.innerHTML = '검색 결과가 없습니다.';
    }
}

// 쿠폰발급현황 검색 기능
function searchIssuedCoupon() {
    const category = document.getElementById('searchIssuedCategory').value;
    const query = document.getElementById('searchIssuedQuery').value;
    console.log('Request URL:', `/seller/coupon/searchIssued?category=${category}&query=${encodeURIComponent(query)}`);

    const url = `/seller/coupon/searchIssued?category=${category}&query=${encodeURIComponent(query)}`;

    fetch(url)
        .then(response => {
            console.log('Response Status:', response.status); // 응답 상태 코드 확인
            return response.json();
        })
        .then(data => {
            console.log('Response received:', data);
            displayIssuedCoupons(data);
        })
        .catch(error => {
            console.error('검색 중 오류 발생:', error);
        });
}

// 발급된 쿠폰 결과를 화면에 표시
function displayIssuedCoupons(data) {
    const issuedTable = document.getElementById('issuedTable');
    const issuedContainer = issuedTable.querySelector('#issuedTbody');

    issuedContainer.innerHTML = ''; // 이전 검색 결과를 지움

    if (data && data.length > 0) {
        data.forEach(issued => {
            const couponElement = document.createElement('tr');
            couponElement.classList.add('coupon-item');
            couponElement.innerHTML = `
          <td>
                    <a href="#" class="issued-order-link"
                       data-issued-couponId="${issued.couponId}"
                       data-issued-id="${issued.sellerCompany}"
                       data-issued-couponType="${issued.couponType}"
                       data-issued-usageStatus="${issued.usageStatus}"
                       data-issued-memberName="${issued.memberName}"
                       data-issued-couponName="${issued.couponName}"
                       data-issued-restrictions="${issued.restrictions}"
                       data-issued-benefit="${issued.benefit}"
                       data-period="${issued.startDate} ~ ${issued.endDate}"
                       data-issued-issuer="${issued.sellerCompany}">
                       ${issued.issuanceNumber}
                    </a>
                </td>
                <td>${issued.couponId}</td>
                <td>${issued.couponType}</td>
                <td>${issued.couponName}</td>
                <td>${issued.memberName}</td>
                <td class="coupon-status">${issued.status}</td>
                <td>${issued.usageDate ? issued.usageDate : '미사용'}</td>
                <td>
                    <button class="issued-end-button" data-id="${issued.issuanceNumber}">종료</button>
                </td>
            `;
            issuedContainer.appendChild(couponElement);
        });
    } else {
        issuedContainer.innerHTML = '<tr><td colspan="8" style="text-align: center;">발급된 쿠폰이 없습니다.</td></tr>';
    }
}
