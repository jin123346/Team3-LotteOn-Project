fetch('../../include/footer.html')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                document.getElementById('footer').innerHTML = data;
            })
            .catch(error => console.error('Error loading footer:', error));


        fetch('../../include/header.html') // 헤더 파일 경로
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                document.getElementById('header').innerHTML = data;
            })
            .catch(error => console.error('Error loading header:', error));

        fetch('../../include/aside.html') // 헤더 파일 경로
            .then(response => {
                return response.text();
            })
            .then(data => {
                document.getElementById('aside').innerHTML = data;
            })
            .catch(error => console.error('Error loading header:', error));
