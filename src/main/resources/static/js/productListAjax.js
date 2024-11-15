window.onload = function() {

    // Select/Deselect all checkboxes
    document.getElementById("selectAll").addEventListener("click", function() {
        const checkboxes = document.querySelectorAll(".product-checkbox");
        checkboxes.forEach(checkbox => checkbox.checked = this.checked);
    });

    // Delete Selected button event listener
    document.getElementById("deleteSelectedButton").addEventListener("click", function() {
        const selectedProducts = Array.from(document.querySelectorAll('.product-checkbox:checked'))
            .map(checkbox => {
                console.log(checkbox.dataset.productId); // Log each data-product-id to verify
                return Number(checkbox.dataset.productId); // Convert ID to number
            });

        console.log("selectedProducts", selectedProducts);

        if (selectedProducts.length === 0) {
            alert("선택된 상품이 없습니다.");
            return;
        }

        if (confirm("선택된 상품을 삭제하시겠습니까?")) {
            fetch('/seller/product/deleteSelected', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(selectedProducts) // Send selected product IDs as JSON array
            })
                .then(response => {
                    if (response.headers.get("content-type")?.includes("application/json")) {
                        return response.json();
                    } else {
                        throw new Error("Non-JSON response received");
                    }
                })
                .then(data => {
                    console.log("Response received:", data);
                    if (data.success) {
                        alert("선택된 상품이 삭제되었습니다.");
                        location.reload();
                    } else {
                        alert("삭제 중 오류가 발생했습니다.");
                    }
                })
                .catch(error => {
                    console.error("Error deleting products:", error);
                    alert("삭제 중 오류가 발생했습니다.");
                });
        }
    });

    document.getElementById("searchbtn").addEventListener("click", function () {
        const searchType = document.getElementById("shopsearch").value;
        const keyword = document.getElementById("search").value;

        fetchProductList(1, searchType, keyword);

    });

    function fetchProductList(pg, type, keyword) {
        // Construct the URL with query parameters
        const url = `/product/list/ajax?pg=${pg}&type=${type}&keyword=${keyword}`;

        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                try {
                    updateProductList(data);
                } catch (e) {
                    console.error("Error in updateProductList", e);
                }
            })
            .catch(error => {
                console.error("Error fetching product list", error);
            });
    }

    function updateProductList(data) {
        const productListContainer = document.getElementById("productListContainer");
        const paginationContainer = document.querySelector(".paging");

        // Clear previous content
        productListContainer.innerHTML = '';
        paginationContainer.innerHTML = '';

        // Check if productDTOs exist and have items
        if (!data.productDTOs || data.productDTOs.length === 0) {
            const noProductsRow = document.createElement("tr");
            noProductsRow.innerHTML = `<th colspan="11">등록하신 상품이 없습니다.</th>`;
            productListContainer.appendChild(noProductsRow);
            return;
        }

        // Loop through products and create rows
        data.productDTOs.forEach(product => {
            const productRow = document.createElement("tr");

            productRow.innerHTML = `
            <td><input type="checkbox" class="product-checkbox" data-product-id="${product.productId || ''}"></td>
            <td>${product.savedPath == null ? `<img src="/${product.file190 || ''}">` : `<img src="/uploads/${product.savedPath + product.file190}">`}</td>
            <td><a href="/market/view/${product.categoryId || ''}/${product.productId || ''}">${product.productCode || ''}</a></td>
            <td>${product.productName || ''}</td>
            <td>${product.price || ''}</td>
            <td><span class="discount">${product.discount || ''}</span></td>
            <td>${product.point || ''}</td>
            <td>${product.stock || ''}</td>
            <td>${product.sellerId || ''}</td>
            <td>${product.hit || ''}</td>
            <td>
                <a href="/seller/product/delete?id=${product.productId || ''}" class="delete">[삭제]</a><br>
                <a href="/seller/product/modify?id=${product.productId || ''}" class="edit">[수정]</a>
            </td>
        `;

            productListContainer.appendChild(productRow);
        });

        // Create pagination links
        if (data.prev) {
            const prevLink = document.createElement("a");
            prevLink.className = "prev";
            prevLink.href = data.keyword ? `/seller/product/list?pg=${data.start - 1}&type=${data.type}&keyword=${data.keyword}` : `/seller/product/list?pg=${data.start - 1}`;
            prevLink.innerText = "이전";
            paginationContainer.appendChild(prevLink);
        }

        for (let num = data.start; num <= data.end; num++) {
            const pageLink = document.createElement("a");
            pageLink.className = num === data.pg ? "current" : "num";
            pageLink.href = data.keyword ? `/seller/product/list?pg=${num}&type=${data.type}&keyword=${data.keyword}` : `/seller/product/list?pg=${num}`;
            pageLink.innerText = num;
            paginationContainer.appendChild(pageLink);
        }

        if (data.next) {
            const nextLink = document.createElement("a");
            nextLink.className = "next";
            nextLink.href = data.keyword ? `/seller/product/list?pg=${data.end + 1}&type=${data.type}&keyword=${data.keyword}` : `/seller/product/list?pg=${data.end + 1}`;
            nextLink.innerText = "다음";
            paginationContainer.appendChild(nextLink);
        }
    }
}