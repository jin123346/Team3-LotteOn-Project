const priceElements = document.querySelectorAll('.price');
priceElements.forEach(priceElement => {
    let priceValue = priceElement.textContent.trim().replace(/[^0-9]/g, '');
    priceValue = parseInt(priceValue, 10);
    if (!isNaN(priceValue)) {
        priceElement.textContent = priceValue.toLocaleString();
    }
});