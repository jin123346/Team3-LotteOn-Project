// 전화번호 뒷부분을 잘라서 표시하는 함수
function hidePhoneNumber(phoneNumber) {
    return phoneNumber.slice(0, 3) + '...';
}

// Function to fetch categories by level (for 1st level)
function fetchCategoriesByLevel(level, dropdown) {
    fetch(`/api/categories/level/${level}`)
        .then(response => response.json())
        .then(data => populateDropdown(data, dropdown))
        .catch(error => console.error('Error fetching categories by level:', error));
}

// Function to fetch categories by parent ID (for 2nd and 3rd levels)
function fetchCategoriesByParent(parentId, dropdown) {
    fetch(`/api/categories/parent/${parentId}`)
        .then(response => response.json())
        .then(data => populateDropdown(data, dropdown))
        .catch(error => console.error('Error fetching categories by parent:', error));
}

// Function to populate dropdown with fetched categories
function populateDropdown(categories, dropdown) {
    dropdown.innerHTML = '<option value="">분류 선택</option>';  // Reset dropdown
    categories.forEach(category => {
        const option = document.createElement('option');
        option.value = category.id;
        option.text = category.name;
        dropdown.appendChild(option);
    });
}



// Example of collecting data (before form submission)
function collectOptions() {
    const optionItems = document.querySelectorAll('.optionItem');
    options = [];

    // Loop through each optionItem and collect values
    optionItems.forEach(item => {
        const optionName = item.querySelector('input[name="option"]').value;
        const optionStock = item.querySelector('input[name="optionStock"]').value;

        // Store each option as an object
        options.push({
            name: optionName,
            stock: optionStock
        });
    });

    console.log(options); // This will show the collected options in the console
    // You can now send 'options' via form submission or AJAX
}



// 파일 검증 함수: 크기, 확장자, 이미지 세로 가로 크기 검증
function validateFile(file, maxSize, maxWidth,maxHeight,inputElement) {
    var allowedExtensions = /(\.jpg|\.jpeg|\.png)$/i; // 허용 파일 확장자

    // 파일 크기 확인
    if (file.size > maxSize) {
        alert('파일 ' + file.name + '의 크기는 최대 ' + (maxSize / (1024 * 1024)) + 'MB까지 허용됩니다.');
        inputElement.value = ''; // 선택된 파일 초기화
        return false;
    }

    // 파일 확장자 확인
    if (!allowedExtensions.exec(file.name)) {
        alert(file.name + ' 파일은 jpg, jpeg, png 형식만 허용됩니다.');
        inputElement.value = ''; // 선택된 파일 초기화
        return false;
    }

    // 이미지의 가로 크기 확인 (비동기 처리)
    var img = new Image();
    img.src = URL.createObjectURL(file);
    img.onload = function() {
        if (img.width > maxWidth || maxHeight==null) {
            alert(file.name + '의 이미지 가로 크기는 ' + maxWidth + 'px를 초과할 수 없습니다.');
            inputElement.value = ''; // 선택된 파일 초기화
            return false;
        }else if(img.width> maxWidth || img.height > maxHeight) {
            alert(file.name + '의 이미지  크기는 ' + maxWidth + 'X'+maxHeight+'px를 초과할 수 없습니다.');
            inputElement.value = ''; // 선택된 파일 초기화
            return false;
        }

    };
    return true; // 파일 크기와 확장자가 적합할 경우 true 반환
}

//유저 등급 변경
function confirmGradeChange(selectElement) {
    const selectedGrade = selectElement.value;
    const memberId = selectElement.dataset.id; // 데이터 속성에서 멤버 ID 가져오기

    const confirmChange = confirm(`해당 등급으로 변경하시겠습니까? (${selectedGrade})`);
    if (confirmChange) {
        // 등급 수정 요청을 서버로 보냄
        const updatedData = {
            id: memberId,
            grade: selectedGrade
        };

        fetch(`/admin/user/member/updateGrade`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedData), // 수정할 데이터 전송
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                alert(data.message); // 성공 메시지 표시
                // 여기에서 필요에 따라 페이지를 새로 고치거나 UI를 업데이트할 수 있습니다.
                // 예: location.reload(); // 페이지 새로 고침
            })
            .catch(error => console.error("Error updating grade:", error));
    } else {
        // 변경 취소 시
        // 선택된 옵션을 원래 등급으로 재설정하려면 추가 로직이 필요할 수 있음
        // 원래 등급 값을 저장하고 그 값을 사용하여 재설정할 수 있습니다.
    }
}


// 로그인 확인 후, 로그인 페이지 또는 주어진 URL로 리디렉션
function loginredirect(url){
    const uidElement = document.getElementById("uid");
    const uid = uidElement ? uidElement.value : null;
    if(!uid){
        alert('로그인 후 이용해 주세요');
        window.location.href = `/user/login?redirect=${encodeURIComponent(window.location.href)}`;
        return;
    }else{
        window.location.href= url;
        return;
    }
}




// Function to enable editing of options
function enableOptionEditing() {
    document.querySelectorAll('#option-container input[readonly]').forEach(input => input.removeAttribute('readonly'));
}

// Add a new option group row
function addOptionGroup() {
    const optionGroupTable = document.getElementById("optionGroupTable");

    // Create the new group row
    const newGroupRow = document.createElement("tr");
    newGroupRow.innerHTML = `
        <td>
            <label>옵션 그룹명</label>
            <input type="checkbox">
        </td>
        <td>
            <div>
                <input type="text" name="optionGroupName[]" placeholder="옵션 그룹명">
                <button type="button" onclick="removeOptionGroup(this)">삭제</button>
                <button type="button" onclick="addOptionItem(this)">옵션 항목 추가</button>
            </div>
        </td>
    `;
    optionGroupTable.appendChild(newGroupRow);

    // Create the item row for the new group
    const newItemRow = document.createElement("tr");
    newItemRow.classList.add("optionItem");
    newItemRow.innerHTML = `
        <td>옵션 항목</td>
        <td class="itemContainer">
            <div class="option-items">
                <div>
                    <input type="text" name="optionItems" placeholder="옵션 항목">
                    <button type="button" onclick="removeOptionItem(this)">삭제</button>
                </div>
            </div>
        </td>
    `;
    optionGroupTable.appendChild(newItemRow);

}

// Add a new option item within an option group
function addOptionItem(button) {
    // Find the closest .itemContainer element following the current row
    const itemContainer = button.closest("tr").nextElementSibling.querySelector(".itemContainer");

    // Find or create the .option-items div within itemContainer
    let optionItemsDiv = itemContainer.querySelector(".option-items");
    if (!optionItemsDiv) {
        optionItemsDiv = document.createElement("div");
        optionItemsDiv.className = "option-items";
        itemContainer.appendChild(optionItemsDiv); // Append it directly under itemContainer
    }

    // Create the new option item HTML structure
    const newOptionItem = document.createElement("div");
    newOptionItem.innerHTML = `
        <input type="text" name="optionItems" placeholder="옵션 항목">
        <button type="button" onclick="removeOptionItem(this)">삭제</button>
    `;

    // Append the new option item to the option-items div
    itemContainer.appendChild(newOptionItem);
}

// Remove an option group row
function removeOptionGroup(button) {
    const row = button.closest("tr");
    row.remove();
}

// Remove an option item within an option group
function removeOptionItem(button) {
    const item = button.closest("div");
    item.remove();
}

// function generateCombinations() {
//     const selectedOptions = collectSelectedOptions();
//     fetch('/generateCombinations', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(selectedOptions),
//     })
//         .then(response => response.json())
//         .then(data => displayCombinations(data))
//         .catch(error => console.error('Error:', error));
// }
function collectSelectedOptions() {
    const selectedOptions = [];
    const optionGroupTable = document.getElementById("optionGroupTable");

    // Iterate over each row in the option group table
    optionGroupTable.querySelectorAll("tr").forEach((row) => {
        const checkbox = row.querySelector(`input[type="checkbox"]`);
        const groupNameInput = row.querySelector(`input[type="text"][name*="optionGroupName[]"]`);

        // Process only if checkbox is checked and group name input exists
        if (checkbox && checkbox.checked && groupNameInput) {
            const groupName = groupNameInput.value.trim();

            // Find the next row (which contains option items for the group)
            const nextRow = row.nextElementSibling;

            if (nextRow && nextRow.classList.contains("itemContainer")) {
                // Collect all option items for the selected group
                const optionItems = Array.from(
                    nextRow.querySelectorAll(".option-items input[type='text']")
                ).map(input => input.value.trim())
                    .filter(value => value); // Ignore empty values

                // Only add groups that have at least one item
                if (groupName && optionItems.length > 0) {
                    selectedOptions.push({
                        groupName: groupName,
                        items: optionItems
                    });
                }
            }
        }
    });
    console.log(selectedOptions);
    return selectedOptions;
}
//
// function generateCombinations() {
//     const stockInputArea = document.getElementById("stock-input-area");
//     stockInputArea.innerHTML = ""; // Clear previous content
//     const codeArea = document.getElementById("codeArea");
//     codeArea.classList.remove("hidden");
//
//     // Initialize an object to hold the selected option groups and their items
//     const optionGroups = {};
//     const optionGroupTable = document.getElementById("optionGroupTable");
//
//     // Select all option group rows (with checkboxes)
//     const groupRows = optionGroupTable.querySelectorAll("tr");
//
//     // Iterate through the rows to collect checked groups and items
//     groupRows.forEach((row, index) => {
//         const checkbox = row.querySelector(`input[type="checkbox"]`);
//         const groupNameInput = row.querySelector(`input[type="text"][name*="groupName"]`);
//
//         // Debugging output
//         console.log(`Row ${index}: Checkbox ${checkbox ? "found" : "not found"}, GroupName ${groupNameInput ? groupNameInput.value : "not found"}`);
//
//         // Process only if checkbox is checked and group name input exists
//         if (checkbox && checkbox.checked && groupNameInput) {
//             const groupName = groupNameInput.value.trim();
//             const nextRow = row.nextElementSibling;
//
//             // Ensure the next row contains items for this group
//             if (groupName && nextRow && nextRow.classList.contains("itemContainer")) {
//                 const optionItems = Array.from(nextRow.querySelectorAll(".option-items input[type='text']"))
//                     .map(input => input.value.trim())
//                     .filter(value => value);
//
//                 // Debugging output for items found
//                 console.log(`Option Group: ${groupName}, Items: ${optionItems}`);
//
//                 // Only add groups that have valid items
//                 if (optionItems.length > 0) {
//                     optionGroups[groupName] = optionItems;
//                 }
//             }
//         }
//     });
//
//     // Debugging output to check the structure of optionGroups
//     console.log("Collected optionGroups:", optionGroups);
//
//     // Check if there are any groups and items selected
//     if (Object.keys(optionGroups).length === 0) {
//         alert("No valid option combinations found. Please select option groups and add items.");
//         return;
//     }
//
//     // Generate all possible combinations of selected option groups and items
//     let combinations = [[]];
//     Object.keys(optionGroups).forEach(groupName => {
//         const values = optionGroups[groupName];
//         combinations = combinations.flatMap(combo =>
//             values.map(value => [...combo, `${groupName}: ${value}`])
//         );
//     });
//
//     // Check if combinations were generated
//     if (combinations.length === 0) {
//         alert("Unable to generate option combinations. Please check selected groups and items.");
//         return;
//     }
//
//     // Create a table to display the combinations
//     const table = document.createElement("table");
//     table.className = "option-stock-table";
//     table.innerHTML = `
//         <thead>
//             <tr>
//                 <th>Option Combination</th>
//                 <th>Product Code</th>
//                 <th>Additional Price</th>
//                 <th>Stock Quantity</th>
//             </tr>
//         </thead>
//     `;
//
//     // Populate the table with each combination
//     const tbody = document.createElement("tbody");
//     combinations.forEach(combo => {
//         const row = document.createElement("tr");
//         const comboText = combo.join(" / ");
//         row.innerHTML = `
//             <td>${comboText}</td>
//             <td><input type="text" name="optionCodeMap[${comboText}]" placeholder="Product Code"></td>
//             <td><input type="number" name="additionalPriceMap[${comboText}]" placeholder="Additional Price" min="0" value="0"></td>
//             <td><input type="number" name="stockMap[${comboText}]" placeholder="Stock Quantity" min="0"></td>
//         `;
//         tbody.appendChild(row);
//     });
//
//     table.appendChild(tbody);
//     stockInputArea.appendChild(table);
// }




//
// function generateCombinations() {
//     const stockInputArea = document.getElementById("stock-input-area");
//     stockInputArea.innerHTML = ""; // Clear previous content
//     const codeArea = document.getElementById("codeArea");
//     codeArea.classList.remove("hidden");
//
//     // Collect selected option groups and their items
//     const optionGroups = {};
//     const optionGroupTable = document.getElementById("optionGroupTable");
//
//     // Select all option group rows (with checkboxes)
//     const groupRows = optionGroupTable.querySelectorAll("tr");
//
//     // Iterate through the rows to collect checked groups and items
//     groupRows.forEach((row, index) => {
//         const checkbox = row.querySelector(`input[type="checkbox"]`);
//         const groupNameInput = row.querySelector(`input[type="text"][name*="groupName"]`);
//
//         // Debugging output
//         console.log(`Row ${index}: Checkbox ${checkbox ? "found" : "not found"}, GroupName ${groupNameInput ? groupNameInput.value : "not found"}`);
//
//         // Process only if checkbox is checked and group name input exists
//         if (checkbox && checkbox.checked && groupNameInput) {
//             const groupName = groupNameInput.value.trim();
//             const nextRow = row.nextElementSibling;
//
//             // Ensure the next row contains items for this group
//             if (groupName && nextRow && nextRow.classList.contains("itemContainer")) {
//                 const optionItems = Array.from(nextRow.querySelectorAll(".option-items input[type='text']"))
//                     .map(input => input.value.trim())
//                     .filter(value => value);
//
//                 console.log(`Option Group: ${groupName}, Items: ${optionItems}`);
//
//                 // Only add groups that have valid items
//                 if (optionItems.length > 0) {
//                     optionGroups[groupName] = optionItems;
//                 }
//             }
//         }
//     });
//
//     // Check if there are any groups and items selected
//     if (Object.keys(optionGroups).length === 0) {
//         alert("No valid option combinations found. Please select option groups and add items.");
//         return;
//     }
//
//     // Generate all possible combinations of selected option groups and items
//     let combinations = [[]];
//     Object.keys(optionGroups).forEach(groupName => {
//         const values = optionGroups[groupName];
//         combinations = combinations.flatMap(combo =>
//             values.map(value => [...combo, `${groupName}: ${value}`])
//         );
//     });
//
//     // Check if combinations were generated
//     if (combinations.length === 0) {
//         alert("Unable to generate option combinations. Please check selected groups and items.");
//         return;
//     }
//
//     // Create a table to display the combinations
//     const table = document.createElement("table");
//     table.className = "option-stock-table";
//     table.innerHTML = `
//         <thead>
//             <tr>
//                 <th>Option Combination</th>
//                 <th>Product Code</th>
//                 <th>Additional Price</th>
//                 <th>Stock Quantity</th>
//             </tr>
//         </thead>
//     `;
//
//     // Populate the table with each combination
//     const tbody = document.createElement("tbody");
//     combinations.forEach(combo => {
//         const row = document.createElement("tr");
//         const comboText = combo.join(" / ");
//         row.innerHTML = `
//             <td>${comboText}</td>
//             <td><input type="text" name="optionCodeMap[${comboText}]" placeholder="Product Code"></td>
//             <td><input type="number" name="additionalPriceMap[${comboText}]" placeholder="Additional Price" min="0" value="0"></td>
//             <td><input type="number" name="stockMap[${comboText}]" placeholder="Stock Quantity" min="0"></td>
//         `;
//         tbody.appendChild(row);
//     });
//
//     table.appendChild(tbody);
//     stockInputArea.appendChild(table);
// }
function generateCombinations() {
    // 옵션 그룹과 항목 데이터를 저장할 배열
    const optionGroups = [];

    // optionGroupName이 있는 <tr> 요소들만 선택
    document.querySelectorAll('#optionGroupTable tr').forEach((groupElem) => {
        const groupNameElem = groupElem.querySelector('input[name="optionGroupName[]"]');

        // groupNameElem이 있는 경우에만 처리
        if (groupNameElem) {
            const groupName = groupNameElem.value;
            console.log("groupName:", groupName); // 디버깅용으로 콘솔 출력

            // 옵션 항목을 저장할 배열
            const optionItems = [];

            // 현재 groupElem 바로 다음에 optionItem <tr> 요소가 있는지 확인
            if (groupElem.nextElementSibling && groupElem.nextElementSibling.classList.contains('optionItem')) {
                const itemElements = groupElem.nextElementSibling.querySelectorAll('.option-items input[name="optionItems"]');

                itemElements.forEach((itemElem) => {
                    const itemName = itemElem.value;

                    if (itemName) { // 빈 값 제외
                        optionItems.push({ optionName: itemName });
                    }
                });
            }

            // 유효한 optionItems가 있는 경우에만 그룹 추가
            if (optionItems.length > 0) {
                optionGroups.push({ groupName: groupName, optionItems: optionItems });
            }
        } else {
            console.log("groupNameElem is null for this groupElem"); // 디버깅용 출력
        }
    });
    console.log("optionGroups",optionGroups);

    // 수집된 옵션 그룹과 항목 데이터를 JSON으로 변환 후 AJAX 요청
    const requestData = { optionGroups: optionGroups };

    fetch('/generate-combinations', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            if (!response.ok) { // 응답 상태가 성공(200-299)이 아닌 경우
                return response.text().then(text => { throw new Error(`Error: ${response.status} - ${text}`); });
            }
            return response.text(); // 먼저 텍스트로 응답 확인
        })
        .then(text => {
            console.log("Response Text:", text); // 응답 내용을 로그로 확인

            if (text.trim()) { // 응답이 비어 있지 않다면 JSON 파싱 시도
                try {
                    return JSON.parse(text);
                } catch (e) {
                    throw new Error("Invalid JSON response");
                }
            }
            throw new Error('Empty response from server'); // 응답이 빈 경우 예외 발생
        })
        .then(data => {
            displayCombinations(data); // 서버에서 받은 조합을 화면에 표시하는 함수 호출
        })
        .catch(error => console.error('Error:', error));
}

function displayCombinations(combinations) {
    const combinationTable = document.querySelector('#combination-container table');

    // 기존의 모든 행을 지우고 새로운 데이터를 추가
    combinationTable.querySelectorAll('tr.combination-row').forEach(row => row.remove());

    combinations.forEach((combination, index) => {
        const row = document.createElement('tr');
        row.classList.add('combination-row');
        row.innerHTML = `
            <td><input type="hidden" name="optionCombinations[${index}].combinationId" value="${combination.combinationId}">
                <input type="text" name="optionCombinations[${index}].combination" value="${combination.combination}">
            </td>
            <td><input type="text" name="optionCombinations[${index}].optionCode" value="${combination.optionCode}"></td>
            <td><input type="text" name="optionCombinations[${index}].additionalPrice" value="${combination.additionalPrice}"></td>
            <td><input type="text" name="optionCombinations[${index}].stock" value="${combination.stock}"></td>
        `;
        combinationTable.appendChild(row);
    });
}
