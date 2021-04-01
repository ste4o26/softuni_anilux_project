window.addEventListener("load", initSwiperJs);

function initSwiperJs() {
    return new Swiper('.swiper-container', {
        direction: "horizontal",
        slidesPerView: 5,
        pagination: {
            el: ".swiper-pagination",
            clickable: true
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
        slidesPerGroup: 5
    });
}