let barChart, pieChart;

window.onload = function() {
    console.log("window.onload triggered");

    fetch('/api/getNewData')
        .then(response => {
            console.log("Response received:", response);
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            if (data.labels && data.labels.length && data.orderData && data.orderData.length) {
                // Ensure the data format for pie chart matches expected format
                updatePieChartData(pieChart, { labels: data.labels, datasets: [{ data: data.orderData }] });
            } else {
                console.error("Data format is incorrect for Pie chart update.");
            }

            // Set default empty arrays if data is missing or undefined
            const orderData = data.orderData && data.orderData.length ? data.orderData : [];
            const paymentData = data.paymentData && data.paymentData.length ? data.paymentData : [];
            const cancelData = data.cancelData && data.cancelData.length ? data.cancelData : [];
            const labels = data.labels && data.labels.length ? data.labels : ["No Data Available"];

            // Log to confirm values
            console.log("Order Data:", orderData);
            console.log("Payment Data:", paymentData);
            console.log("Cancel Data:", cancelData);
            console.log("Labels:", labels);

            // Call your update function with safe data for bar chart
            updateBarChartData(orderData, paymentData, cancelData, labels);
        })
        .catch(error => console.error('Error fetching data:', error));

    // Initialize bar chart
    const ctxBar = document.getElementById('barChart').getContext('2d');
    barChart = new Chart(ctxBar, {
        type: 'bar',
        data: {
            labels: ['10-10', '10-11', '10-12', '10-13'],
            datasets: [
                {
                    label: '주문',
                    data: [12, 19, 3, 5],
                    backgroundColor: 'rgba(75, 192, 192, 0.8)',
                    barPercentage: 0.3
                },
                {
                    label: '결제',
                    data: [8, 12, 4, 7],
                    backgroundColor: 'rgba(255, 99, 132, 0.8)',
                    barPercentage: 0.3
                },
                {
                    label: '취소',
                    data: [10, 15, 6, 9],
                    backgroundColor: 'rgba(255, 206, 86, 0.8)',
                    barPercentage: 0.3
                }
            ]
        },
        options: {
            scales: {
                x: { stacked: false },
                y: { beginAtZero: true }
            },
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true, position: 'top' }
            }
        }
    });

    // Initialize pie chart
    const ctxPie = document.getElementById('pieChart').getContext('2d');
    pieChart = new Chart(ctxPie, {
        type: 'pie',
        data: {
            labels: ['가전', '식품', '패션', '홈'],
            datasets: [{
                label: '색상 비율',
                data: [130, 30, 50, 180],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: { maintainAspectRatio: false }
    });
};

// Function to update bar chart data
function updateBarChartData(newOrderData, newPaymentData, newCancelData, newLabels) {
    barChart.data.labels = newLabels;
    barChart.data.datasets[0].data = newOrderData;
    barChart.data.datasets[1].data = newPaymentData;
    barChart.data.datasets[2].data = newCancelData;
    barChart.update();
}

// Function to update pie chart data
function updatePieChartData(chart, newData) {
    chart.data.labels = newData.labels || [];  // Default to empty array
    chart.data.datasets[0].data = newData.datasets[0].data || [];  // Default to empty data array
    chart.update();
}
