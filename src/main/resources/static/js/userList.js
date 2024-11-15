document.getElementById('deleteSelectedBtn').addEventListener('click', function () {
    const selectedIds = Array.from(document.querySelectorAll('.memberCheckbox:checked'))
        .map(checkbox => {
            console.log(checkbox.value); // 값 확인
            return Number(checkbox.value); // ID를 숫자로 변환
        });
    if (selectedIds.length === 0) {
        alert('삭제할 항목을 선택하세요.');
        return;
    }

    // 삭제 API 호출
    fetch('/admin/store/deletmembers', { // 적절한 URL로 변경 필요
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedIds)
    })
        .then(response => {
            if (response.ok) {
                alert('선택한 항목이 삭제되었습니다.');
                selectedIds.forEach(id => {
                    // 체크된 행을 DOM에서 삭제
                    document.querySelector(`input[value="${id}"]`).closest('tr').remove();
                });
            } else {
                alert('삭제에 실패했습니다.');
            }
        })
        .catch(error => console.error('Error:', error));
    console.log(selectedIds); // 디버깅: 선택된 ID 배열 확인
});
