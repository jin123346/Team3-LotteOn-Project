

let currentQuery = ""; // 현재 검색어를 저장할 변수
document.addEventListener("DOMContentLoaded", function() {

    document.querySelectorAll('.sort-options li').forEach(item => {
        item.addEventListener('click', event => {
            event.preventDefault();

            // Get selected sort option
            const sortType = event.currentTarget.getAttribute('data-sort');

            // Update active class
            document.querySelectorAll('.sort-options li').forEach(el => el.classList.remove('active'));
            event.currentTarget.classList.add('active');

            // Perform AJAX request with the selected sort type
            const query = document.getElementById('headerSearchId').value || document.getElementById('searchKeyword').textContent;
            fetchSortedProducts(query, sortType);
        });
    });
});






    function fetchSortedProducts(query, sortType) {
    fetch(`/api/search?query=${encodeURIComponent(query)}&sort=${sortType}`)
        .then(response => response.json())
        .then(data => {
            updateProductList(data.productSummaryDTOs, query, data.total);
        })
        .catch(error => console.error('Error:', error));
}

    function updateProductList(productDTOs, query, total) {
        const productList = document.getElementById('productList');
        productList.innerHTML = ''; // Clear existing content

        // Update the search keyword and total count
        document.getElementById('searchKeyword').textContent = query;
        document.getElementById('totalResults').textContent = total;

        // Loop through products and add them to the product list
        productDTOs.forEach(product => {
            const productItem = document.createElement('a');


            // Check if there is a discount to conditionally render the discount area
            const discountArea = product.discount > 0 ? `
            <section class="discountArea">
                <span class="original-price">${product.price.toLocaleString()}원</span>
                <span class="discount">${product.discount}%↓</span>
            </section>
        ` : '';
            const imagePath = product.savedPath
                ? `/uploads/${product.savedPath}/${product.file190}`
                : `/uploads/productImg/${product.file190}`;

            // Conditionally render the shipping fee if it's available
            const shippingFee = product.shippingFee ? `${product.shippingFee}원` : '무료배송';

            // Set up the product item structure
            productItem.innerHTML = `
             <div class="product-search-item">
                <div class="product-image">
                    <img src="${imagePath}" alt="상품 이미지">
                </div>
                <div class="product-details">
                    <h4 class="product-name">${product.productName}</h4>
                    <p class="product-description">${product.productDesc}</p>
                </div>
                <div class="price-seller-info">
                    <div class="product-price">
                        <span class="final-price">${product.finalPrice.toLocaleString()}원</span>
                        ${discountArea}
                    </div>
                    <div class="shipping-info">
                        <span class="shipping-fee">${shippingFee}</span>
                    </div>
                </div>
                <div class="product-seller">
                    <span class="seller-name">${product.company}</span>
                    <div class="customer">
                        <div class="seller-rating" data-rating="${parseFloat(product.rating).toFixed(1)}"></div>
                        <p>${product.rating.toFixed(1)} (${product.reviewCount || 0})</p>
                    </div>
                </div>
             </div>
        `;

            productList.appendChild(productItem);
        });
        renderStarRatings();
        renderShippingLabels();
    }


function renderStarRatings() {
    document.querySelectorAll('.seller-rating').forEach(function (ratingElement) {
        const rating = parseFloat(ratingElement.getAttribute('data-rating'));
        console.log("Rating:", rating);  // rating 값 확인용 로그

        if (isNaN(rating)) {
            console.warn("Rating attribute is missing or not a number.");
            return;  // rating 값이 유효하지 않으면 생성을 중단
        }

        const fullStars = Math.floor(rating);
        const halfStar = rating % 1 !== 0;

        // 꽉 찬 별 추가
        for (let i = 0; i < fullStars; i++) {
            const star = document.createElement('span');
            star.classList.add('star', 'full-star');
            star.innerHTML = '★';
            ratingElement.appendChild(star);
        }

        // 반쪽 별 추가
        if (halfStar) {
            const star = document.createElement('span');
            star.classList.add('star', 'half-star');
            star.innerHTML = '★';
            ratingElement.appendChild(star);
        }

        // 빈 별 추가
        const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);
        for (let i = 0; i < emptyStars; i++) {
            const star = document.createElement('span');
            star.classList.add('star', 'empty-star');
            star.innerHTML = '☆'; // 빈 별
            ratingElement.appendChild(star);
        }
    });
}

function renderShippingLabels() {
    document.querySelectorAll('.shipping-fee').forEach(function(feeElement) {
        // shippingFee 값을 가져와 숫자로 변환
        const shippingFee = parseFloat(feeElement.innerText);
        console.log("shippingFee:", shippingFee);

        // shippingFee가 0이면 무료배송으로 텍스트 변경 및 클래스 추가
        if (shippingFee === 0) {
            feeElement.innerText = "무료배송";
            feeElement.classList.add("shipping-label");
        }
    });
}



function performInitialSearch() {
    // 초기 검색 시 검색어를 저장하고 필터링 없이 검색 수행
    currentQuery = document.getElementById('searchKeyword').value;
    applyFilters();
    fetchResults(currentQuery);
}
function applyFilters() {
    // 검색어 값을 가져옵니다.
    currentQuery = document.getElementById('searchKeyword').innerText;

    const searchMode = document.querySelector('input[name="searchMode"]:checked').value;
    const searchInput = document.getElementById('searchSecKeyword'); // 검색어 입력 필드

    // 검색어가 비어 있으면 함수 종료
    if (!currentQuery) {
        console.warn("검색어가 설정되지 않았습니다.");
        return;
    }

    const filterByName = document.getElementById('filterByName').checked;
    const filterByDescription = document.getElementById('filterByDescription').checked;
    const filterByPrice = document.getElementById('filterByPrice').checked;
    const minPrice = document.getElementById('minPrice').value;
    const maxPrice = document.getElementById('maxPrice').value;

    const filters = {
        filterByName,
        filterByDescription,
        filterByPrice,
        minPrice,
        maxPrice,
        searchMode
    };

    // fetchResults 함수에 필터 및 검색어를 전달
    fetchResults(currentQuery, filters);
}

function fetchResults(query, filters = {}) {
    if (!query) {
        console.warn("검색어가 유효하지 않습니다.");
        return;
    }

    const params = new URLSearchParams();
    params.append("query", query);

    // 필터 조건을 URL 파라미터로 추가
    if (filters.filterByName) params.append("filterByName", "true");
    if (filters.filterByDescription) params.append("filterByDescription", "true");
    if (filters.filterByPrice) params.append("filterByPrice", "true");
    if (filters.minPrice) params.append("minPrice", filters.minPrice);
    if (filters.maxPrice) params.append("maxPrice", filters.maxPrice);
    if (filters.searchMode) params.append("searchMode", filters.searchMode);

    fetch(`/api/advanced-search?${params.toString()}`)
        .then(response => response.json())
        .then(data => updateProductList(data.productSummaryDTOs, query, data.total))
        .catch(error => console.error('Error:', error));
}

// function applyFilters() {
//
//     // 선택된 검색 모드 가져오기
//     const searchMode = document.querySelector('input[name="searchMode"]:checked').value;
//     currentQuery = document.getElementById('searchKeyword').value;
//
//     // 필터 선택 시 기존 검색어와 함께 필터 조건을 전송
//     const filterByName = document.getElementById('filterByName').checked;
//     const filterByDescription = document.getElementById('filterByDescription').checked;
//     const filterByPrice = document.getElementById('filterByPrice').checked;
//     const minPrice = document.getElementById('minPrice').value;
//     const maxPrice = document.getElementById('maxPrice').value;
//
//     const params = new URLSearchParams();
//     params.append("query", currentQuery); // 현재 검색어 유지
//     if (filterByName) params.append("filterByName", "true");
//     if (filterByDescription) params.append("filterByDescription", "true");
//     if (filterByPrice) params.append("filterByPrice", "true");
//     if (minPrice) params.append("minPrice", minPrice);
//     if (maxPrice) params.append("maxPrice", maxPrice);
//     console.log(params);
//
//     fetch(`/api/advanced-search?${params.toString()}&searchMode=${searchMode}`)
//         .then(response => response.json())
//         .then(data => updateProductList(data.productSummaryDTOs))
//         .catch(error => console.error('Error:', error));
// }

// function fetchResults(query) {
//     fetch(`/api/advanced-search?query=${encodeURIComponent(query)}`)
//         .then(response => response.json())
//         .then(data => updateProductList(data.productSummaryDTOs))
//         .catch(error => console.error('Error:', error));
// }

