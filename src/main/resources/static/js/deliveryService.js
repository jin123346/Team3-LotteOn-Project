// 배송지 리스트를 동적으로 생성

var deliveryDTOList = /*[[${deliveryDTOList}]]*/[];
renderAddressList(deliveryDTOList);


function renderAddressList(deliveryDTOList) {
    const addressListDiv = document.querySelector('.address-list');
    addressListDiv.innerHTML = '';  // 기존 목록 초기화

    // 각 배송지 정보에 대해 동적으로 주소 항목 생성
    deliveryDTOList.forEach(function (delivery) {
        const addressItemDiv = document.createElement('div');
        addressItemDiv.classList.add('address-item');

        // 주소 정보
        const addressInfoDiv = document.createElement('div');
        addressInfoDiv.classList.add('address-info');
        addressItemDiv.appendChild(addressInfoDiv);

        // 라디오 버튼
        const radioInput = document.createElement('input');
        radioInput.type = 'radio';
        radioInput.name = 'address';
        radioInput.id = `address${delivery.id}`; // 배송지 ID로 고유값 설정
        addressInfoDiv.appendChild(radioInput);

        // 배송지 이름
        const label = document.createElement('label');
        label.setAttribute('for', radioInput.id);
        label.textContent = delivery.name;
        addressInfoDiv.appendChild(label);

        // 주소
        const addressText = document.createElement('p');
        addressText.innerHTML = `[${delivery.postcode}] ${delivery.addr}`;
        addressInfoDiv.appendChild(addressText);

        // 전화번호
        const phoneText = document.createElement('p');
        phoneText.textContent = delivery.hp;
        addressInfoDiv.appendChild(phoneText);

        // 수정, 삭제 버튼
        const addressBtnDiv = document.createElement('div');
        addressBtnDiv.classList.add('address-btn');
        addressItemDiv.appendChild(addressBtnDiv);

        const editButton = document.createElement('button');
        editButton.classList.add('edit-btn');
        editButton.textContent = '수정';
        addressBtnDiv.appendChild(editButton);

        const deleteButton = document.createElement('button');
        deleteButton.classList.add('delete-btn');
        deleteButton.textContent = '삭제';
        addressBtnDiv.appendChild(deleteButton);

        const closeModalButton = document.querySelector("#closeUpdateModalButton");
        if (closeModalButton) {
            closeModalButton.addEventListener("click", () => {
                addressUpdateModal.style.display = "none";  // 수정 모달 닫기
            });
        }

        // 주소 항목을 리스트에 추가
        addressListDiv.appendChild(addressItemDiv);
    });
}

document.addEventListener("DOMContentLoaded", function () {
    const memberId = "${memberId}";
    console.log("memberId: " + memberId);  // 확인용
    const addressSelectModal = document.getElementById("addressSelectModal");
    const addressUpdateModal = document.getElementById("addressUpdateModal");  // 배송지 수정 모달
    const addressListDiv = document.querySelector(".address-list");

    function editDelivery(memberId, deliveryId) {
        fetch(`/api/member/${memberId}/delivery/${deliveryId}`)
            .then(response => response.json())
            .then(data => {

                console.log("deliveryId 포함", data);  // 데이터 확인용

                console.log("data.id: ", data.id)

                // 배송 정보 데이터를 가져온 후 각 필드에 값을 넣음
                document.getElementById("deliveryIdInput").value = data.id;
                document.getElementById("name2").value = data.name;
                document.getElementById("phoneNumber2").value = data.hp;
                document.getElementById("postalCode2").value = data.postcode;
                document.getElementById("addressLine12").value = data.addr;
                document.getElementById("addressLine22").value = data.addr2;
                document.getElementById("deliveryMessage2").value = data.deliveryMessage;


                console.log("deliveryIdInput에 설정된 값:", document.getElementById("deliveryIdInput").value);

                // 배송지 수정 모달을 보임
                const modal = document.getElementById("addressUpdateModal");
                if (modal) {
                    modal.style.display = "block";  // 수정 모달 표시
                }

                // 배송지 목록 모달을 숨김
                const deliveryListModal = document.getElementById("addressListModal");
                if (deliveryListModal) {
                    deliveryListModal.style.display = "none";  // 목록 모달 숨김
                }
            })
            .catch(error => console.error("배송지 정보를 가져오는 중 오류 발생:", error));
    }


    // 배송지 목록 불러오기
    function loadDeliveries() {
        fetch(`/api/member/${memberId}/deliveries`)
            .then(response => response.json())
            .then(data => {
                const member = data.member;  // 기본 주소
                const deliveries = data.deliveries;  // 나머지 배송지 목록

                console.log(JSON.stringify(deliveries, null, 2));

                addressListDiv.innerHTML = '';  // 배송지 목록을 지우고 새로 추가

                // 기본 주소 추가
                if (member) {
                    const defaultAddress = document.createElement("div");
                    defaultAddress.classList.add("address-item", "default-address");

                    defaultAddress.innerHTML = `
                    <div class="address-info">
                        <input type="radio" id="defaultAddress" name="address" checked
                               data-postcode="${member.postcode}" 
                               data-addr="${member.addr}" 
                               data-addr2="${member.addr2}" 
                               data-hp="${member.hp}" 
                               data-name="${member.name}">
                        <label for="defaultAddress">${member.name}</label>
                        <p>[${member.postcode}] ${member.addr} ${member.addr2}</p>
                        <p>${member.hp}</p>
                    </div>
                `;
                    addressListDiv.appendChild(defaultAddress);
                }


                // 나머지 배송지 목록 추가
                deliveries.forEach((delivery, index) => {
                    const addressItem = document.createElement("div");
                    addressItem.classList.add("address-item");

                    addressItem.innerHTML = `
                    <div class="address-info">
                        <input type="radio" id="address${index}" name="address"
                               data-delivey-id="${delivery.id}"
                               data-postcode="${delivery.postcode}" 
                               data-addr="${delivery.addr}" 
                               data-addr2="${delivery.addr2}" 
                               data-hp="${delivery.hp}" 
                               data-name="${delivery.name}">
                        <label for="address${index}">${delivery.name}</label>
                        <p>[${delivery.postcode}] ${delivery.addr} ${delivery.addr2}</p>
                        <p>${delivery.hp}</p>
                    </div>
                    <div class="address-btn">
                        <button class="edit-btn" data-delivery-id="${delivery.id}" data-member-id="${member.id}">수정</button>
                        <button class="delete-btn" data-delivery-id="${delivery.id}" data-member-id="${member.id}">삭제</button>
                    </div>
                `;
                    addressListDiv.appendChild(addressItem);

                    // 이제 버튼이 DOM에 추가된 후, deliveryId를 조회할 수 있습니다.
                    const deleteButton = addressItem.querySelector(".delete-btn");
                    const editButton = addressItem.querySelector(".edit-btn");

                    // 삭제 버튼에 클릭 이벤트 리스너 추가
                    deleteButton.addEventListener("click", (event) => {
                        const deliveryId = event.target.getAttribute("data-delivery-id");
                        const memberId = event.target.getAttribute("data-member-id");

                        console.log("삭제 버튼 클릭:", deliveryId, memberId);  // deliveryId가 정상적으로 가져와지는지 확인
                        if (!deliveryId || deliveryId === '0') {
                            console.error('deliveryId가 잘못 전달되었습니다.');
                        }
                        deleteDelivery(deliveryId, memberId);  // deleteDelivery 함수에 deliveryId와 memberId를 전달
                    });

                    // 수정 버튼에 클릭 이벤트 리스너 추가
                    editButton.addEventListener("click", (event) => {
                        const deliveryId = event.target.getAttribute("data-delivery-id");
                        const memberId = event.target.getAttribute("data-member-id");

                        console.log("수정 버튼 클릭:", deliveryId, memberId);  // deliveryId와 memberId를 확인

                        editDelivery(memberId, deliveryId);  // editDelivery 함수에 memberId와 deliveryId를 전달

                    });


                });
            })
            .catch(error => console.error("배송지 목록 불러오기 실패:", error));
    }


    // 새 배송지 추가

    document.getElementById("addressForm").addEventListener("submit", function (event) {
        event.preventDefault();

        const memberId = document.getElementById('memberId').value;

        if (!memberId) {
            console.error("memberId가 없습니다. 회원 ID를 확인하세요.");
            return;
        }

        const deliveryData = {
            name: document.getElementById("name").value,
            hp: document.getElementById("phoneNumber").value,
            postcode: document.getElementById("postalCode").value,
            addr: document.getElementById("addressLine1").value,
            addr2: document.getElementById("addressLine2").value,
            deliveryMessage: document.getElementById("deliveryMessage").value,
        };

        console.log("Member ID:", memberId);
        console.log("보내는 JSON 데이터:", JSON.stringify(deliveryData));


        fetch(`/api/member/${memberId}/delivery`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(deliveryData)
        })
            .then(response => {
                // JSON 형식 응답이 아니면 에러 처리
                if (!response.ok) {
                    throw new Error(`Server error: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("배송지 추가 성공:", data);
                alert('배송지가 등록되었습니다.');
                loadDeliveries();
            })
            .catch(error => {
                console.error("배송지 추가 실패:", error);
                alert("배송지 추가 중 문제가 발생했습니다. 다시 시도해 주세요.");
            });
    });

    loadDeliveries();

    // 배송지 수정
    document.addEventListener("DOMContentLoaded", function () {
        document.getElementById("addressForm2").addEventListener("submit", function (event) {
            event.preventDefault();
            const memberId = document.getElementById('memberId').value;
            const deliveryId = document.getElementById("deliveryIdInput").value;  // 여기서 확인
            console.log("Hidden Field deliveryIdInput:", deliveryId);

            const deliveryData = {
                id: deliveryId,
                name: document.getElementById("name2").value,
                hp: document.getElementById("phoneNumber2").value,
                postcode: document.getElementById("postalCode2").value,
                addr: document.getElementById("addressLine12").value,
                addr2: document.getElementById("addressLine22").value,
                deliveryMessage: document.getElementById("deliveryMessage2").value,
            };

            console.log("보내는 JSON 데이터:", JSON.stringify(deliveryData));

            console.log("Member ID:", memberId);
            console.log("Delivery ID:", deliveryId);
            console.log("보내는 JSON 데이터:", JSON.stringify(deliveryData));

            fetch(`/api/member/${memberId}/delivery/${deliveryId}/update`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(deliveryData)
            })
                .then(response => response.json())
                .then(data => {
                    if (data) {
                        console.log("배송지 수정 성공:", data);
                        alert('배송지가 수정되었습니다.');
                    } else {
                        console.log("응답이 없거나 잘못된 응답입니다.");
                        alert('배송지 수정 실패');
                    }

                    const modal = document.getElementById("addressUpdateModal");
                    if (modal) {
                        modal.style.display = "none";
                    }
                })
                .catch(error => {
                    console.error("배송지 수정 실패:", error);
                    alert('배송지 수정 중 오류가 발생했습니다.');
                });

            loadDeliveries();
        });
    });
});


function confirmAddressSelection() {
    const selectedAddress = document.querySelector('input[name="address"]:checked');

    if (selectedAddress) {
        // 선택된 주소에서 정보 추출
        const postcode = selectedAddress.dataset.postcode;
        const addr = selectedAddress.dataset.addr;
        const addr2 = selectedAddress.dataset.addr2;
        const hp = selectedAddress.dataset.hp;
        const name = selectedAddress.dataset.name;

        // 배송지 정보 업데이트
        document.getElementById("M_postcode").innerText = postcode;  // 우편번호 업데이트
        document.getElementById("M_postcode").setAttribute("data-postcode", postcode);  // 우편번호 data 속성 업데이트
        document.querySelector(".totalAddress").innerText = `${addr} ${addr2}`;  // 주소 업데이트
        document.querySelector(".totalAddress").setAttribute("data-addr", addr);  // 주소 data 속성 업데이트
        document.querySelector(".totalAddress").setAttribute("data-addr2", addr2);  // 추가 주소 data 속성 업데이트

        // 주소 변경 알림
        alert(`배송지 정보가 변경되었습니다: ${name}, ${addr} ${addr2}`);

        // 모달 창 닫기
        closeModal();
    } else {
        alert("배송지를 선택하세요.");
    }
}


function deleteDelivery(deliveryId, memberId) {
    const confirmation = confirm("정말로 이 배송지를 삭제하시겠습니까?");
    console.log("de: " + deliveryId)
    if (confirmation) {
        fetch(`/api/member/${memberId}/delivery/${deliveryId}`, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    alert("배송지 삭제 성공");
                    // loadDeliveries(); // 배송지 목록 새로 불러오기

                    // 모달 창 닫기
                    closeModal();

                    // 모달을 새로 띄워서 변경된 배송지 목록을 표시
                    openModal();
                } else {
                    alert("배송지 삭제 실패");
                }
            })
            .catch(error => console.error("배송지 삭제 실패:", error));
    }
}


function closeModal() {
    const modal = document.getElementById("addressSelectModal");
    if (modal) {
        modal.style.display = "none"; // 모달을 숨김
    }
}

document.querySelector(".close").addEventListener("click", function () {
    document.getElementById("addressUpdateModal").style.display = "none";
});

function openModal(modalElement) {
    modalElement.style.display = "block"; // 모달을 보임
}

// DOMContentLoaded를 사용하여 DOM이 완전히 로드된 후에 함수 설정
document.addEventListener("DOMContentLoaded", function () {
    document.getElementById('btnPostCode').onclick = function () {
        postcode(); // postcode 함수 호출
    };
});
