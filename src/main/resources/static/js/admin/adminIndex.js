document.querySelectorAll('.admin_menu > li').forEach(menu => {
    const submenu = menu.querySelector('.admin_sub_menu');

    menu.addEventListener('click', function (e) {
        e.stopPropagation();


        if (submenu) {
            submenu.classList.toggle('open');

        }
    });

    // 서브 메뉴 항목 클릭 이벤트
    if (submenu) {
        submenu.querySelectorAll('li').forEach(subLi => {
            subLi.addEventListener('click', function (e) {
                e.stopPropagation();
            })
        })
    }


})


document.getElementById('toggle-aside').addEventListener('click', function (e) {
    e.preventDefault(); // 기본 링크 동작 방지

    const aside = document.getElementById('admin_aside');
    const main = document.getElementById('admin_service');
    const gate = document.getElementById('admin_gate'); // admin_gate 추가

    // aside가 숨겨져 있는지 여부에 따라 클래스 조정
    if (aside.classList.contains('hidden')) {
        aside.classList.remove('hidden'); // aside 보이기
        main.classList.remove('expanded'); // main 원래 너비로
        gate.classList.remove('expanded'); // gate도 원래 너비로
    } else {
        aside.classList.add('hidden'); // aside 숨기기
        main.classList.add('expanded'); // main 너비 늘리기
        gate.classList.add('expanded'); // gate도 너비 늘리기
    }
});

// 막대 그래프
const ctxBar = document.getElementById('barChart').getContext('2d');
const barChart = new Chart(ctxBar, {
    type: 'bar',
    data: {
        labels: ['10-10', '10-11', '10-12', '10-13'], // x축 레이블
        datasets: [
            {
                label: '주문',
                data: [12, 19, 3, 5],
                backgroundColor: 'rgba(75, 192, 192, 0.8)', // 색깔 설정
                barPercentage: 0.3 // 막대 너비 조정
            },
            {
                label: '결제',
                data: [8, 12, 4, 7],
                backgroundColor: 'rgba(255, 99, 132, 0.8)', // 색깔 설정
                barPercentage: 0.3 // 막대 너비 조정
            },
            {
                label: '취소',
                data: [10, 15, 6, 9],
                backgroundColor: 'rgba(255, 206, 86, 0.8)', // 색깔 설정
                barPercentage: 0.3 // 막대 너비 조정
            }
        ]
    },
    options: {
        scales: {
            x: {
                stacked: false // 막대를 쌓지 않음
            },
            y: {
                beginAtZero: true
            }
        },
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: true,
                position: 'top',
            },
        }
    }
});

// 원형 그래프
const ctxPie = document.getElementById('pieChart').getContext('2d');
const pieChart = new Chart(ctxPie, {
    type: 'pie',
    data: {
        labels: ['가전', '식품', '의류', '기타'], // 레이블 추가
        datasets: [{
            label: '색상 비율',
            data: [130, 30, 50, 180], // 데이터 값 추가
            backgroundColor: [
                'rgba(255, 99, 132, 0.6)',
                'rgba(54, 162, 235, 0.6)',
                'rgba(255, 206, 86, 0.6)',
                'rgba(75, 192, 192, 0.6)' // 새로운 색상 추가
            ],
            borderColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)' // 새로운 색상 추가
            ],
            borderWidth: 1
        }]
    },
    options: {maintainAspectRatio: false}
});