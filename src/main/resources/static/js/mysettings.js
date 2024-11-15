document.addEventListener("DOMContentLoaded", function () {
    document.getElementById('modify').addEventListener('click', function () {
        // 사용자 정보 가져오기
        var memberData = {
            uid: document.getElementById('uid').textContent.trim(),
            name: document.getElementById('name').value.trim(),
            email: document.getElementById('email').value.trim(),
            hp: document.getElementById('phone').value.trim(),
            postcode: document.getElementById('zipcode').value.trim(),
            addr: document.getElementById('address1').value.trim(),
            addr2: document.getElementById('address2').value.trim()
        };

        // 유효성 검사 (예시)
        if (!memberData.name || !memberData.email || !memberData.hp || !memberData.addr) {
            alert("모든 필드를 입력해주세요.");
            return;
        }

        // 서버로 데이터 보내기 (Ajax)
        fetch('/mysettings/update', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(memberData),
        })
            .then(response => response.json())
            .then(data => {
                if (data.message) {
                    alert(data.message); // 성공 메시지
                    window.location.reload(); // 페이지 새로고침
                } else {
                    alert("회원 정보 업데이트에 실패했습니다.");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert("오류가 발생했습니다.");
            });
    });
});