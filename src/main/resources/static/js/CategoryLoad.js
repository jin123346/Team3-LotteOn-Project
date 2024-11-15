document.addEventListener('DOMContentLoaded', function() {
    // Fetch 1st level categories when the page loads
    fetchCategoriesByLevel(1, document.querySelector('select[name="firstLevelCategory"]'));

    // Event listener for 1st level category change
    document.querySelector('select[name="firstLevelCategory"]').addEventListener('change', function() {
        const parentId = this.value;
        if (parentId) {
            fetchCategoriesByParent(parentId, document.querySelector('select[name="secondLevelCategory"]'));
        }
    });

    // Event listener for 2nd level category change
    document.querySelector('select[name="secondLevelCategory"]').addEventListener('change', function() {
        const parentId = this.value;
        if (parentId) {
            fetchCategoriesByParent(parentId, document.querySelector('select[name="thirdLevelCategory"]'));
        }
    });


    // Array to store options temporarily
    let options = [];





    //파일 크기 조정
    var productImg1 = document.querySelector('input[name="files[0]"]');
    var productImg2 = document.querySelector('input[name="files[1]"]');
    var productImg3 = document.querySelector('input[name="files[2]"]');
    var productDesc = document.querySelector('input[name="files[3]"]');

    // // 파일 선택 시 검증 함수 호출
    // productImg1.addEventListener('change', function(event) {
    //     var file = this.files[0];
    //     if (file) {
    //         validateFile(file, 1024 * 1024, 190, 190, this); // 가로 190px, 세로 제한 없음, 1MB
    //     }
    // });
    //
    // productImg2.addEventListener('change', function(event) {
    //     var file = this.files[0];
    //     if (file) {
    //         validateFile(file, 1024 * 1024, 230, 230, this); // 가로 230px, 세로 제한 없음, 1MB
    //     }
    // });
    //
    // productImg3.addEventListener('change', function(event) {
    //     var file = this.files[0];
    //     if (file) {
    //         validateFile(file, 1024 * 1024, 456, 456, this); // 가로 456px, 세로 456px, 1MB
    //     }
    // });
    //
    // productDesc.addEventListener('change', function(event) {
    //     var file = this.files[0];
    //     if (file) {
    //         validateFile(file, 1024 * 1024, 940, null, this); // 가로 940px, 세로 제한 없음, 1MB
    //     }
    // });




});


let optionGroups = {};

function addOptionGroup(input) {
    const groupName = input.value.trim();
    if (groupName && !optionGroups[groupName]) {
        optionGroups[groupName] = [];
    }
}

function addOptionItem(input) {
    const optionValue = input.value.trim();
    if (optionValue) {
        const optionGroupDiv = input.closest(".option-group");
        const groupNameInput = optionGroupDiv.querySelector("input[name='optionGroupName[]']");
        const groupName = groupNameInput.value.trim();

        if (groupName && optionGroups[groupName]) {
            optionGroups[groupName].push(optionValue);
        }
    }
}
function addOptionItemField(button) {
    // `addOption` 클래스를 가진 div를 찾아 옵션 항목 추가
    const addOptionDiv = button.parentElement.querySelector(".addOption");

    // 새로운 옵션 항목 div 생성
    const newItem = document.createElement("div");
    newItem.className = "option-item";
    newItem.innerHTML = `
        <input type="text" name="optionItems" placeholder="옵션 항목을 입력하세요" onblur="addOptionItem(this)">
        <button type="button" onclick="removeOptionItem(this)">삭제</button>
    `;

    // `addOption` div 내부에 옵션 항목 추가
    addOptionDiv.appendChild(newItem);
}

function removeOptionItem(button) {
    const optionItemDiv = button.parentElement;
    const optionGroupDiv = optionItemDiv.closest(".option-group");
    const groupName = optionGroupDiv.querySelector("input[name='optionGroupName[]']").value.trim();

    // 옵션 그룹에서 해당 항목 제거
    const optionValue = optionItemDiv.querySelector("input[type='text']").value.trim();
    if (optionGroups[groupName]) {
        const index = optionGroups[groupName].indexOf(optionValue);
        if (index > -1) {
            optionGroups[groupName].splice(index, 1);
        }
    }

    // DOM에서 항목 삭제
    optionItemDiv.remove();
}


// function addOptionItemField(button) {
//     const optionItemsDiv = button.parentElement;
//     const newItem = document.createElement("div");
//     newItem.className = "option-item";
//     newItem.innerHTML = `
//         <input type="text" placeholder="옵션 항목을 입력하세요" onblur="addOptionItem(this)">
//     `;
//     optionItemsDiv.insertBefore(newItem, button);
// }

function addNewOptionGroup() {
    const optionContainer = document.getElementById("option-container");
    const newGroup = document.createElement("div");
    newGroup.className = "option-group";
    newGroup.innerHTML = `
        <div>
            <label>옵션 그룹명</label>
            <input type="text" name="optionGroupName[]" placeholder="옵션 그룹명을 입력하세요" onblur="addOptionGroup(this)">
            </div>
            <div class="option-items">
                <label>옵션 항목</label>
                <div class="addOption">
                    <input type="text" placeholder="옵션 항목을 입력하세요" onblur="addOptionItem(this)">
    
                </div>
                <button type="button" onclick="addOptionItemField(this)">옵션 항목 추가</button>
            </div>
    `;
    // 옵션 그룹 추가 버튼 요소 찾기
    const addButton = optionContainer.querySelector("button[onclick='addNewOptionGroup()']");

    // 새로운 옵션 그룹을 버튼 위쪽에 삽입
    optionContainer.insertBefore(newGroup, addButton);
}

function generateCombinations() {
    const stockInputArea = document.getElementById("stock-input-area");
    const codeArea = document.getElementById("codeArea");
    stockInputArea.innerHTML = ""; // Clear previous content

    // Validate option groups
    if (Object.keys(optionGroups).length === 0) {
        alert("Please add option groups and items first.");
        return;
    }

    // Generate all combinations of options
    let combinations = [[]];
    Object.keys(optionGroups).forEach(groupName => {
        const values = optionGroups[groupName].filter(value => value); // Filter out empty values
        if (values.length === 0) {
            return;
        }
        combinations = combinations.flatMap(combo =>
            values.map(value => [...combo, `${groupName}: ${value}`])
        );
    });

    if (combinations.length === 0) {
        alert("No valid option combinations found.");
        return;
    }

    // Create table
    const table = document.createElement("table");
    table.className = "option-stock-table";

    // Create table header
    const thead = document.createElement("thead");
    thead.innerHTML = `
        <tr>
            <th>Option Combination</th>
            <th>Product Code</th>
            <th>Additional Price</th>
            <th>Stock Quantity</th>
        </tr>
    `;
    table.appendChild(thead);

    // Create table body
    const tbody = document.createElement("tbody");

    // Loop through combinations and create rows
    combinations.forEach(combo => {
        const row = document.createElement("tr");
        const comboText = combo.join(" / ");
        row.innerHTML = `
            <td>${comboText}</td>
            <td><input type="text" name="optionCode" placeholder="Product Code"></td>
            <td><input type="number" name="additionalPrice" placeholder="Additional Price" min="0" value="0"></td>
            <td><input type="number" name="stockMap[${comboText}]" id="optionStock" placeholder="Stock Quantity" min="0"></td>
        `;
        tbody.appendChild(row);

        // // Event listener to apply the same `additionalPrice` to rows with matching parts
        // row.querySelector("input[name='additionalPrice']").addEventListener("input", function () {
        //     const newPrice = this.value;
        //     const partialComboText = combo[0]; // Use the first part (e.g., "234: 23")
        //
        //     // Apply the price to all rows that contain the same first part of the combination
        //     Array.from(tbody.querySelectorAll("tr")).forEach(otherRow => {
        //         const otherComboText = otherRow.querySelector("td").textContent;
        //         if (otherComboText.startsWith(partialComboText)) {
        //             otherRow.querySelector("input[name='additionalPrice']").value = newPrice;
        //         }
        //     });
        // });
    });

    table.appendChild(tbody);
    stockInputArea.appendChild(table);
    codeArea.classList.remove('hidden');
}
//기존꺼
// function generateCombinations() {
//     const stockInputArea = document.getElementById("stock-input-area");
//     const codeArea = document.getElementById("codeArea");
//     stockInputArea.innerHTML = ""; // 기존 내용을 초기화
//
//     // 옵션 그룹 유효성 검사
//     if (Object.keys(optionGroups).length === 0) {
//         alert("옵션 그룹과 항목을 먼저 추가해 주세요.");
//         return;
//     }
//
//     // 옵션 그룹의 모든 조합 생성
//     let combinations = [[]];
//     Object.keys(optionGroups).forEach(groupName => {
//         const values = optionGroups[groupName].filter(value => value); // 빈 값 제외
//         if (values.length === 0) {
//             return;
//         }
//         combinations = combinations.flatMap(combo =>
//             values.map(value => [...combo, `${groupName}: ${value}`])
//         );
//     });
//
//     // 중복되지 않은 조합이 예상대로 생성됐는지 확인
//     if (combinations.length === 0) {
//         alert("유효한 옵션 조합이 없습니다.");
//         return;
//     }
//
//     // 테이블 생성
//     const table = document.createElement("table");
//     table.className = "option-stock-table";
//
//     // 테이블 헤더 생성
//     const thead = document.createElement("thead");
//     thead.innerHTML = `
//         <tr>
//             <th>옵션 조합</th>
//             <th>개별상품코드</th>
//             <th>추가금액</th>
//             <th>재고 수량</th>
//         </tr>
//     `;
//     table.appendChild(thead);
//
//     // 테이블 본문 생성
//     const tbody = document.createElement("tbody");
//
//     // 옵션 조합별로 행 추가
//     combinations.forEach(combo => {
//         const row = document.createElement("tr");
//         row.innerHTML = `
//             <td>${combo.join(" / ")}</td>
//             <td><input type="text" name="optionCode" placeholder="개별상품코드" ></td>
//             <td><input type="number" name="additionalPrice" placeholder="추가금액" min="0" value="0"></td>
//             <td><input type="number" name="stockMap[${combo.join("-")}]" id="optionStock" placeholder="재고 수량" min="0"></td>
//         `;
//         tbody.appendChild(row);
//     });
//
//     table.appendChild(tbody);
//     stockInputArea.appendChild(table);
//     codeArea.classList.remove('hidden');
// }



//////////////////////////
// function generateCombinations() {
//     const stockInputArea = document.getElementById("stock-input-area");
//     const codeArea = document.getElementById("codeArea");
//     stockInputArea.innerHTML = ""; // 기존 내용을 초기화
//
//     // 옵션 그룹 유효성 검사
//     if (Object.keys(optionGroups).length === 0) {
//         alert("옵션 그룹과 항목을 먼저 추가해 주세요.");
//         return;
//     }
//
//     // 옵션 그룹의 모든 조합 생성
//     let combinations = [[]];
//     Object.keys(optionGroups).forEach(groupName => {
//         const values = optionGroups[groupName].filter(value => value); // 빈 값 제외
//         if (values.length === 0) {
//             return;
//         }
//         combinations = combinations.flatMap(combo =>
//             values.map(value => [...combo, `${groupName}: ${value}`])
//         );
//     });
//
//     // 중복되지 않은 조합이 예상대로 생성됐는지 확인
//     if (combinations.length === 0) {
//         alert("유효한 옵션 조합이 없습니다.");
//         return;
//     }
//
//     // 테이블 생성
//     const table = document.createElement("table");
//     table.className = "option-stock-table";
//
//     // 테이블 헤더 생성
//     const thead = document.createElement("thead");
//     thead.innerHTML = `
//         <tr>
//             <th>옵션 조합</th>
//             <th>개별상품코드</th>
//             <th>추가금액</th>
//             <th>재고 수량</th>
//         </tr>
//     `;
//     table.appendChild(thead);
//
//     // 테이블 본문 생성
//     const tbody = document.createElement("tbody");
//
//     // 옵션 조합별로 행 추가
//     combinations.forEach(combo => {
//         const row = document.createElement("tr");
//         row.innerHTML = `
//             <td>${combo.join(" / ")}</td>
//             <td><input type="text" name="optionCode" placeholder="개별상품코드" ></td>
//             <td><input type="number" name="additionalPrice" placeholder="추가금액" min="0" value="0"></td>
//             <td><input type="number" name="stock[${combo.join("-")}]" id="optionStock" placeholder="재고 수량" min="0"></td>
//         `;
//         tbody.appendChild(row);
//     });
//
//     table.appendChild(tbody);
//     stockInputArea.appendChild(table);
//     codeArea.classList.remove('hidden');
// }


//
// function collectOptionData() {
//     const optionGroups = document.querySelectorAll("#option-container .option-group");
//     const optionsData = {
//         productName: document.querySelector("input[name='productName']").value,
//         options: [],
//         combinations: []
//     };
//
//     // 옵션 그룹과 항목 수집
//     optionGroups.forEach(group => {
//         const groupName = group.querySelector("input[name='optionGroupName[]']").value.trim();
//         const items = Array.from(group.querySelectorAll(".addOption input"))
//             .map(input => input.value.trim())
//             .filter(value => value);
//
//         if (groupName && items.length > 0) {
//             optionsData.options.push({
//                 groupName: groupName,
//                 items: items
//             });
//         }
//     });
//
//     // 옵션 조합과 개별 상품 코드 및 재고 수량 수집
//     const combinationRows = document.querySelectorAll("#stock-input-area tbody tr");
//     combinationRows.forEach(row => {
//         const combination = row.querySelector("td").textContent.trim();
//         const optionCode = row.querySelector("input[name='optionCode']").value.trim();
//         const stock = row.querySelector("input[type='number']").value.trim();
//
//         if (combination && optionCode && stock) {
//             optionsData.combinations.push({
//                 combination: combination,
//                 optionCode: optionCode,
//                 stock: parseInt(stock, 10)
//             });
//         }
//     });
//
//     // JSON 형식으로 변환하여 임시 저장 및 폼 전송 준비
//     const optionsJson = JSON.stringify(optionsData);
//     localStorage.setItem("optionsData", optionsJson);
//     console.log("Options JSON:", optionsJson);
//
//     // JSON을 히든 필드에 저장하여 폼에 추가
//     const form = document.getElementById("productForm");
//     let hiddenField = document.getElementById("optionsJson");
//     if (!hiddenField) {
//         hiddenField = document.createElement("input");
//         hiddenField.type = "hidden";
//         hiddenField.name = "optionsJson";
//         hiddenField.id = "optionsJson";
//         form.appendChild(hiddenField);
//     }
//     hiddenField.value = optionsJson;
// }


// function collectProductData() {
//     const productData = {
//         thirdLevelCategory: document.querySelector("select[name='thirdLevelCategory']").value,
//         firstLevelCategory: document.querySelector("select[name='firstLevelCategory']").value,
//         secondLevelCategory: document.querySelector("select[name='secondLevelCategory']").value,
//         productName: document.querySelector("input[name='productName']").value.trim(),
//         productDesc: document.querySelector("input[name='productDesc']").value.trim(),
//         madeIn: document.querySelector("input[name='madeIn']").value.trim(),
//         sellerId: document.querySelector("input[name='sellerId']").value.trim(),
//         price: parseFloat(document.querySelector("input[name='price']").value.trim()),
//         discount: parseInt(document.querySelector("input[name='discount']").value.trim(), 10),
//         stock: parseInt(document.querySelector("input[name='stock']").value.trim(), 10),
//         shippingFee: parseInt(document.querySelector("input[name='shippingFee']").value.trim(), 10),
//         shippingTerms: parseInt(document.querySelector("input[name='shippingTerms']").value.trim(), 10),
//         condition: document.querySelector("input[name='condition']").value.trim(),
//         tax: document.querySelector("input[name='tax']").value.trim(),
//         receiptIssuance: document.querySelector("input[name='receiptIssuance']").value.trim(),
//         busniesstype: document.querySelector("input[name='busniesstype']").value.trim(),
//         manufactureCountry: document.querySelector("input[name='manufactureCountry']").value.trim(),
//     };
//
//     // JSON 형식으로 변환하여 로컬에 임시 저장
//     const productJson = JSON.stringify(productData);
//     localStorage.setItem("productData", productJson);
//     console.log("Product JSON:", productJson);
//
//     // JSON을 히든 필드에 저장하여 폼에 추가
//     const form = document.getElementById("productForm");
//     let hiddenField = document.getElementById("productJson");
//     if (!hiddenField) {
//         hiddenField = document.createElement("input");
//         hiddenField.type = "hidden";
//         hiddenField.name = "productJson";
//         hiddenField.id = "productJson";
//         form.appendChild(hiddenField);
//     }
//     hiddenField.value = productJson;
// }





// 연속된 코드 생성
function generateSequentialCodes() {
    const baseCode = document.getElementById("baseCode").value.trim();
    if (!baseCode) {
        alert("기본 코드를 입력하세요.");
        return;
    }

    const optionCodes = document.querySelectorAll("input[name='optionCode']");
    optionCodes.forEach((input, index) => {
        input.value = `${baseCode}${index + 1}`;  // 기본 코드 + 순차 번호
    });
}

// 일괄 재고 수량 적용
function applyBulkStock() {
    const bulkStock = parseInt(document.getElementById("bulkStock").value, 10);
    if (isNaN(bulkStock)) {
        alert("유효한 재고 수량을 입력하세요.");
        return;
    }

    const stockInputs = document.querySelectorAll("#optionStock");
    stockInputs.forEach(input => {
        input.value = bulkStock;
    });

    // 재고 수량 합계 업데이트 (옵션이 없는 경우를 위한 필드)
    calculateTotalStock();
}

// 옵션 재고의 합계를 계산하여 재고수량 필드에 자동 업데이트
function calculateTotalStock() {
    const stockInputs = document.querySelectorAll("#stock-input-area input[type='number']");
    let totalStock = 0;

    stockInputs.forEach(input => {
        const stockValue = parseInt(input.value) || 0;
        totalStock += stockValue;
    });

    // 재고수량 필드에 합계를 표시
    const totalStockField = document.querySelector("input[name='stock']");
    totalStockField.value = totalStock;
}

function resetCombinations() {
    // 조합 테이블 초기화 (DOM에서만 제거)
    const stockInputArea = document.getElementById("stock-input-area");
    stockInputArea.innerHTML = ""; // 테이블 내용을 지워서 초기화

    // 옵션 그룹 데이터와 DOM 초기화
    optionGroups = {}; // 데이터 초기화
    const optionContainer = document.getElementById("option-container");
    optionContainer.innerHTML = `
        <button type="button" onclick="addNewOptionGroup()">옵션 그룹 추가</button>
    `; // 옵션 그룹 DOM 초기화

    // 코드 생성 영역 숨기기
    const codeArea = document.getElementById("codeArea");
    codeArea.classList.add('hidden');

    console.log("옵션 조합과 옵션 그룹이 초기화되었습니다.");
}



// // 옵션 재고 필드가 변경될 때마다 합계를 업데이트
// document.querySelector("#stock-input-area").addEventListener("input", calculateTotalStock);
// 폼 제출 시 collectOptionData를 호출하여 JSON 데이터를 히든 필드에 추가
// document.getElementById("productForm").addEventListener("submit", function(event) {
//     collectOptionData();
// });

// 옵션 재고 필드가 변경될 때마다 합계를 다시 계산
document.querySelectorAll("#stock-input-area").forEach(inputArea => {
    inputArea.addEventListener("input", calculateTotalStock);
});

document.getElementById("productForm").addEventListener("submit", function (event) {
    event.preventDefault();

    // // Collect product and option data
    // const productData = {
    //     firstLevelCategory: document.querySelector("select[name='firstLevelCategory']").value,
    //     secondLevelCategory: document.querySelector("select[name='secondLevelCategory']").value,
    //     thirdLevelCategory: document.querySelector("select[name='thirdLevelCategory']").value,
    //     productName: document.querySelector("input[name='productName']").value.trim(),
    //     productDesc: document.querySelector("input[name='productDesc']").value.trim(),
    //     madeIn: document.querySelector("input[name='madeIn']").value.trim(),
    //     sellerId: document.querySelector("input[name='sellerId']").value.trim(),
    //     price: parseFloat(document.querySelector("input[name='price']").value.trim()),
    //     discount: parseInt(document.querySelector("input[name='discount']").value.trim(), 10),
    //     stock: parseInt(document.querySelector("input[name='stock']").value.trim(), 10),
    //     shippingFee: parseInt(document.querySelector("input[name='shippingFee']").value.trim(), 10),
    //     shippingTerms: parseInt(document.querySelector("input[name='shippingTerms']").value.trim(), 10),
    //     condition: document.querySelector("input[name='condition']").value.trim(),
    //     tax: document.querySelector("input[name='tax']").value.trim(),
    //     receiptIssuance: document.querySelector("input[name='receiptIssuance']").value.trim(),
    //     busniesstype: document.querySelector("input[name='busniesstype']").value.trim(),
    //     manufactureCountry: document.querySelector("input[name='manufactureCountry']").value.trim(),
    // }
    // const optionsData = collectOptionData();
    // const options = optionsData.options;
    // const combinations = optionsData.combinations;
    //
    //
    //
    // // Create FormData to hold both JSON data and files
    // const formData = new FormData();
    // formData.append("productData", JSON.stringify(productData)); // Append JSON data as "productData"
    // formData.append("options", JSON.stringify(options));
    // formData.append("combinations", JSON.stringify(combinations));

    const productData = collectProductData();
    const formData = new FormData();

    formData.append("productData", JSON.stringify(productData)); // Main product data with options and combinations


    // Append files to FormData
    const fileInputs = document.querySelectorAll("input[type='file']");
    fileInputs.forEach((fileInput) => {
        if (fileInput.files.length > 0) {
            formData.append("files", fileInput.files[0]); // Append each file
        }
    });

    // Send FormData to the server
    fetch("/seller/product/register", {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if(data.result>0){
                alert("Product registration successful");
                window.location.href = "/seller/product/list";
            }else{
                alert("등록 실패!")
                window.location.href = "/seller/product/register";
            }

        })
        .catch(error => {
            console.error("Error:", error);
            alert("There was an error submitting the form.");
        });
});

// Function to collect product data
function collectProductData() {
    const productData = {
        firstLevelCategory: document.querySelector("select[name='firstLevelCategory']").value,
        secondLevelCategory: document.querySelector("select[name='secondLevelCategory']").value,
        thirdLevelCategory: document.querySelector("select[name='thirdLevelCategory']").value,
        productName: document.querySelector("input[name='productName']").value.trim(),
        productDesc: document.querySelector("input[name='productDesc']").value.trim(),
        madeIn: document.querySelector("input[name='madeIn']").value.trim(),
        sellerId: document.querySelector("input[name='sellerId']").value.trim(),
        price: parseFloat(document.querySelector("input[name='price']").value.trim()),
        discount: parseInt(document.querySelector("input[name='discount']").value.trim(), 10),
        stock: parseInt(document.querySelector("input[name='stock']").value.trim(), 10),
        shippingFee: parseInt(document.querySelector("input[name='shippingFee']").value.trim(), 10),
        shippingTerms: parseInt(document.querySelector("input[name='shippingTerms']").value.trim(), 10),
        condition: document.querySelector("input[name='condition']").value.trim(),
        tax: document.querySelector("input[name='tax']").value.trim(),
        receiptIssuance: document.querySelector("input[name='receiptIssuance']").value.trim(),
        busniesstype: document.querySelector("input[name='busniesstype']").value.trim(),
        manufactureCountry: document.querySelector("input[name='manufactureCountry']").value.trim(),
    };

    const optionData = collectOptionData();
    productData.options = optionData.options;
    productData.combinations = optionData.combinations;

    return productData;
}

// Function to collect option data
function collectOptionData() {
    const options = [];
    const combinations = [];

    document.querySelectorAll("#option-container .option-group").forEach(group => {
        const groupName = group.querySelector("input[name='optionGroupName[]']").value.trim();
        const items = Array.from(group.querySelectorAll(".addOption input"))
            .map(input => input.value.trim())
            .filter(value => value);
        if (groupName && items.length > 0) {
            options.push({ groupName, items });
        }
    });

    document.querySelectorAll("#stock-input-area tbody tr").forEach(row => {
        const combination = row.querySelector("td").textContent.trim();
        const optionCode = row.querySelector("input[name='optionCode']").value.trim();

        // Default additionalPrice and stock to 0 if empty
        const additionalPrice = parseInt(row.querySelector("input[name='additionalPrice']").value.trim(), 10) || 0;
        const stockElement = row.querySelector("input[name^='stockMap']");
        const stock = stockElement ? parseInt(stockElement.value.trim(), 10) || 0 : 0;

        if (combination && optionCode) {
            combinations.push({ combination, optionCode, additionalPrice, stock });
        }
    });

    return { options, combinations };



}








