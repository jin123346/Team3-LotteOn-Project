document.addEventListener('DOMContentLoaded', function () {
    // Script for toggling the category menu
    // const viewAllButton = document.getElementById('viewAllButton');
    // const categoryMenu = document.querySelector('.categoryMenu');
    // const header = document.querySelector('.category.on');


    // 재귀적으로 카테고리 계층 구조를 DOM에 추가하는 함수
    function buildCategoryTree(container, categories) {
        categories.forEach(category => {
            const li = document.createElement('li');
            li.textContent = category.name;

            // 하위 카테고리가 있을 경우, 하위 목록을 생성하고 추가
            if (category.children && category.children.length > 0) {
                const subUl = document.createElement('ul');
                buildCategoryTree(subUl, category.children);
                li.appendChild(subUl);
            }

            container.appendChild(li);
        });
    }


    document.addEventListener('click', function (event) {
        if (event.target.tagName === 'LI' && event.target.querySelector('ul')) {
            event.target.classList.toggle('expanded');
        }
    });

    const searchInput = document.getElementById('headerSearchId');
    searchInput.addEventListener('keyup', function (event) {
        if (event.key === 'Enter') { // Enter 키 확인
            performSearch();
        }
    });



});

function performSearch() {
    const query = document.getElementById('headerSearchId').value;
    if (query) {
        window.location.href =`/market/search?query=${encodeURIComponent(query)}`
            // Fetch product list via AJAX
        // fetch(`/market/search?query=${encodeURIComponent(query)}`)
        //     .then(response => response.json())
        //     .then(data => {
        //         updateProductList(data.products); // assuming `products` is the list in `ProductListPageResponseDTO`
        //     })
        //     .catch(error => console.error('Error:', error));
    } else {
        alert('검색어를 입력하세요');
    }
}


// function updateProductList(products, query, totalCount) {
//     const productList = document.getElementById('productList');
//     productList.innerHTML = ''; // Clear existing content
//
//     // Update keyword and total count
//     document.getElementById('searchKeyword').textContent = query;
//     document.getElementById('totalResults').textContent = totalCount;
//
//     products.forEach(product => {
//         const productItem = document.createElement('div');
//         productItem.className = 'product-item';
//
//         productItem.innerHTML = `
//             <div class="product-image">
//                 <img src="/path/to/image.jpg" alt="상품 이미지">
//             </div>
//             <div class="product-details">
//                 <h4 class="product-name">${product.productName}</h4>
//                 <p class="product-description">${product.description}</p>
//             </div>
//             <div class="price-seller-info">
//                 <div class="product-price">
//                     <span class="final-price">${product.price}원</span>
//                 </div>
//             </div>
//         `;
//
//         productList.appendChild(productItem);
//     });
// }


// function updateProductList(productDTOs, query, total) {
//     const productList = document.getElementById('productList');
//     productList.innerHTML = ''; // Clear existing content
//
//     // Update the keyword and total count
//     document.getElementById('searchKeyword').textContent = query;
//     document.getElementById('totalResults').textContent = total;
//
//     // Iterate over the productDTOs and create product items
//     productDTOs.forEach(product => {
//         const productItem = document.createElement('div');
//         productItem.className = 'product-item';
//
//         productItem.innerHTML = `
//             <div class="product-image">
//                 <img src="/uploads/${product.file190}" alt="상품 이미지">
//             </div>
//             <div class="product-details">
//                 <h4 class="product-name">${product.productName}</h4>
//                 <p class="product-description">${product.productDesc}</p>
//             </div>
//             <div class="price-seller-info">
//                 <div class="product-price">
//                     <span class="final-price">${product.price.toLocaleString()}원</span>
//                     <span class="original-price">${(product.price / (1 - product.discount / 100)).toLocaleString()}원</span>
//                     <span class="discount">${product.discount}% ↓</span>
//                 </div>
//                 <div class="shipping-info">
//                     <span class="shipping-label">${product.shippingFee === 0 ? '무료배송' : `${product.shippingFee.toLocaleString()}원`}</span>
//                 </div>
//             </div>
//             <div class="product-seller">
//                 <span class="seller-name">${product.sellerId}</span>
//                 <span class="seller-rating">고객만족우수</span>
//                 <span class="seller-rank">⭐⭐⭐⭐⭐</span>
//             </div>
//         `;
//
//         productList.appendChild(productItem);
//     });
// }
// 하위 메뉴 보이기 함수
function showSubmenu(element) {
    const submenu = element.querySelector('ul');
    console.log('submenu : ' + submenu);
    if (submenu) {
        submenu.style.display = 'block';
        adjustHeightAndPosition();
    }
}

// 하위 메뉴 숨기기 함수
function hideSubmenu(element) {
    const submenu = element.querySelector('ul');
    if (submenu) submenu.style.display = 'none';
}

// 1depth의 총 높이에 맞춰 2depth, 3depth 높이 및 위치 설정 함수
function adjustHeightAndPosition() {
    const menu1depth = document.getElementById('menu-1depth');
    const menu2depths = document.querySelectorAll('.menu-2depth');
    const menu3depths = document.querySelectorAll('.menu-3depth');

    // 1depth의 전체 높이를 가져와서 하위 메뉴에 적용
    const height = menu1depth.offsetHeight;

    // 2depth와 3depth의 높이를 1depth의 높이에 맞춤
    menu2depths.forEach(menu2 => {
        menu2.style.height = `${height}px`;
    });

    menu3depths.forEach(menu3 => {
        const parentMenu2 = menu3.closest('.menu-2depth');
        const firstMenu2Item = parentMenu2.querySelector('li');

        // 2depth의 첫 번째 항목과 동일한 위치에 3depth 배치
        if (firstMenu2Item) {
            const offsetTop = firstMenu2Item.offsetTop;
            menu3.style.top = `${offsetTop}px`;
        }
        menu3.style.height = `${height}px`;
    });
}