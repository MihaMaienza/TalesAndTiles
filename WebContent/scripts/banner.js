document.addEventListener('DOMContentLoaded', function () {
    let currentIndex = 0;
    const slidesContainer = document.querySelector('.slides');
    const slides = document.querySelectorAll('.slide');
    const indicators = document.querySelectorAll('.indicator');
    const prevButton = document.querySelector('.prev');
    const nextButton = document.querySelector('.next');
    let autoSlideInterval;

    function updateSlideWidth() {
        const slideWidth = slidesContainer.clientWidth;
        slides.forEach(slide => {
            slide.style.width = `${slideWidth}px`;
        });
    }

    function showSlide(index) {
        const offset = -index * slidesContainer.clientWidth;
        slidesContainer.style.transform = `translateX(${offset}px)`;
        indicators.forEach((indicator, i) => {
            indicator.classList.toggle('active', i === index);
        });
    }

    function nextSlide() {
        currentIndex = (currentIndex + 1) % slides.length;
        showSlide(currentIndex);
        resetAutoSlide();
    }

    function prevSlide() {
        currentIndex = (currentIndex - 1 + slides.length) % slides.length;
        showSlide(currentIndex);
        resetAutoSlide();
    }

    function resetAutoSlide() {
        clearInterval(autoSlideInterval);
        autoSlideInterval = setInterval(nextSlide, 5000);
    }

    nextButton.addEventListener('click', nextSlide);
    prevButton.addEventListener('click', prevSlide);

    indicators.forEach((indicator, index) => {
        indicator.addEventListener('click', () => {
            currentIndex = index;
            showSlide(currentIndex);
            resetAutoSlide();
        });
    });

    window.addEventListener('resize', () => {
        updateSlideWidth();
        showSlide(currentIndex);
    });

    updateSlideWidth();
    autoSlideInterval = setInterval(nextSlide, 5000); // Cambia slide ogni 5 secondi
});
