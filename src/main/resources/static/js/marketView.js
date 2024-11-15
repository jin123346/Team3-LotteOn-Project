let selectedOptionValue = "";
let selectedOptionText = "";
document.getElementById("option").addEventListener("change", function () {
     selectedOptionValue = this.value; // 선택된 옵션의 값
     selectedOptionText = this.options[this.selectedIndex].text; // 서택된 옵션의 텍스트

    console.log("선택된 옵션 id : ================", selectedOptionValue)
    console.log("선택된 옵션 설명 : ================", selectedOptionText)
});
console.log("선택된 옵션 id : ================2", selectedOptionValue)
console.log("선택된 옵션 설명 : ================2", selectedOptionText)

const productId = document.getElementById("productId").value;
const point =document.getElementById("point").value;
const productName =document.getElementById("productName").value;
const originalPrice =document.getElementById("originalPrice").value;
const finalPrice =document.getElementById("finalPrice").value;
const file190 =document.getElementById("file190").value;
const discount =document.getElementById("discount").value;
const shippingFee =document.getElementById("shippingFee").value;
const quantity =document.getElementById("quantity").value;

console.log('productId=======',productId)
console.log('point=======',point)
console.log('productName=====',productName)
console.log('originalPrice====',originalPrice)
console.log('finalPrice====',finalPrice)
console.log('file190====',file190)
console.log('discount====',discount)
console.log('shippingFee====',shippingFee)
console.log('quantity====',quantity)

document.addEventListener("DOMContentLoaded", function () {
    // 모든 장바구니 버튼에 클릭 이벤트 추가
    const addToCartBtn = document.querySelectorAll('.add-to-cart');
    console.log('클릭됨')

    addToCartBtn.forEach(btn => {
        btn.addEventListener('click', function (){

        // 상품 정보 수집
            const productId = this.dataset.productId; // 버튼에 data-product 속성 추가
            const quantity = document.querySelector(`#quantity`).value; // 수정된 부분
            console.log("Product ID:", productId);
            if(quantity <= 0){
                alert('수량을 1 이상으로 설정해 주세요.');
                return
            }else if(!selectedOptionValue){
                alert("옵션을 선택해 주세요")
                return;
            }
            console.log("Quantity:", quantity);

            const isConfirmed = confirm("장바구니에 추가 하시겠습니까.")
            if(isConfirmed){
                // json 깩체 생성
                const productCart = {
                    productId: productId,         // 실제 값 추가
                    productName: productName,     // 실제 값 추가
                    originalPrice: originalPrice, // 실제 값 추가
                    finalPrice: finalPrice,       // 실제 값 추가
                    quantity: quantity,            // 실제 값 추가
                    file190: file190,
                    optionId :selectedOptionValue,
                    optionName : selectedOptionText,
                    point : point,
                    discount : discount

                }
            }
        fetch('/api/cart',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({productId, quantity}), // JSON 형식으로 전송
        })
            .then(resp => {
                if (resp.status === 401) {
                    // 사용자 인증이 안 된 경우 로그인 페이지로
                    alert('로그인 없이 이곳은 접근 금지! 빨리 로그인해 주세요');
                    window.location.href = '/user/login';
                    return; // 추가적인 처리 중단
                }
                if(!resp.ok){
                    throw new Error('궁시렁궁시렁 리스폰이 안됨');

                }
                return resp.json();
            })
            .then(data => {
                // 성공적으로 장바구니에 추가되었을때
                alert('장바구니에 추가 되었습니다!!!!!!!!!!!!!!');

            })
            .catch(error => {
                console.error('궁시렁궁시렁 추가하는데 실패함ㅋ');
                alert('장바구니에 추가하는 데 실패하심');

            });
        });
    });
});