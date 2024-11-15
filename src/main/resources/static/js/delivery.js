document.addEventListener('DOMContentLoaded', function () {
    const modal = document.querySelector('.modal-section-shipping');
    const orderModal = document.querySelector('.order-modal-shipping');
    const modalClose = document.querySelector('.shipping-info-close');

    function openModal() {
        modal.style.display = 'flex'; // 모달을 flex로 표시
        orderModal.style.display = 'block'; // 내부 모달도 표시
    }

    function closeModal() {
        modal.style.display = 'none'; // 모달을 숨김
        orderModal.style.display = 'none'; // 내부 모달도 숨김
    }

    document.querySelectorAll('.shipping-info-btn').forEach(button => {
        button.addEventListener('click', openModal);
    });

    modalClose.addEventListener('click', closeModal);

    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });
});