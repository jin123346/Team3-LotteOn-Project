document.addEventListener('DOMContentLoaded', function () {
    fetchBestProducts();
    const eventSource = new EventSource('/sse/best-products');
    eventSource.onopen = function(event) {
        console.log("SSE 연결이 열렸습니다.");
    };



    eventSource.onerror = function (error) {
        if (eventSource.readyState === EventSource.CLOSED) {
            console.error("연결이 닫혔습니다.");
        }
            console.error('Error receiving SSE:', error);
            eventSource.close();

        setTimeout(() => {
            // Reconnect after 5 seconds
            new EventSource('http://127.0.0.1:8085/sse/best-products');
        }, 5000);

    };

    eventSource.onmessage = function (event) {
        const data = JSON.parse(event.data);
        updateBestProductList(data);  // Define this function to update the DOM
    };








});


function fetchBestProducts() {
    fetch('/api/best-products')
        .then(response => response.json())
        .then(data => updateBestProductList(data))
        .catch(error => console.error('Error fetching best products:', error));
}

function updateBestProductList(products) {
    const bestProductContainer = document.querySelector('.bestProduct');
    bestProductContainer.innerHTML = `
        <span><img src="/images/common/aside_best_product_tagR.png" alt="태그">베스트 상품</span>
    `;

    // Loop over products and append to the bestProductContainer
    products.forEach((product, index) => {
        console.log(product.file230);
        const imgSrc = product.savedPath
            ? `/uploads/${product.savedPath}/${product.file230}`
            : `/uploads/productImg/${product.file230}`;
        console.log("imgSrc",imgSrc);

        const productHTML = `
            <div class="products ${index === 0 ? 'first' : ''}">
                <div class="productimg ${index === 0 ? 'first' : ''}">
                    <img src="${imgSrc}" alt="">
                    <p>${index + 1}</p>
                </div>
                <div class="productInfo">
                    <span class="product-name">${product.productName}</span>
                    <span class="original-price">${product.originalPrice.toLocaleString()}원</span>
                    <span class="discounted-price">
                        <p class="discount-rate">${product.discount}%</p> <p>↓</p>
                    </span>
                    <span class="final-price">${product.finalPrice.toLocaleString()}원</span>
                </div>
            </div>
        `;
        bestProductContainer.insertAdjacentHTML('beforeend', productHTML);
    });
}