let currentSlide = 0;
const slides = document.querySelectorAll('.slide');
const progressBar = document.getElementById('progressBar');
const slideIndicator = document.getElementById('slide-indicator');

function showSlide(index) {
    if (index < 0) index = 0;
    if (index >= slides.length) index = slides.length - 1;

    slides.forEach((slide, i) => {
        slide.classList.remove('active');
        if (i === index) {
            slide.classList.add('active');
        }
    });

    currentSlide = index;
    updateProgress();
}

function nextSlide() {
    showSlide(currentSlide + 1);
}

function prevSlide() {
    showSlide(currentSlide - 1);
}

function updateProgress() {
    const progress = ((currentSlide + 1) / slides.length) * 100;
    if (progressBar) progressBar.style.width = progress + '%';
    if (slideIndicator) slideIndicator.textContent = `${currentSlide + 1} / ${slides.length}`;
}

// Keyboard Navigation
document.addEventListener('keydown', (e) => {
    if (e.key === 'ArrowRight' || e.key === ' ') {
        nextSlide();
    } else if (e.key === 'ArrowLeft') {
        prevSlide();
    }
});

// Initialize
updateProgress();
