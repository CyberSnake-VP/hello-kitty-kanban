
// === ПРЕДЗАГРУЗКА КАРТИНОК ===
const preloadNinja = new Image();
const preloadFairy = new Image();
preloadNinja.src = '/img/ninja.png';
preloadFairy.src = '/img/fairy.png';

// === СОСТОЯНИЕ ===
// храним переменную ключ значение localStorage.setItem('ninjaMode', isNinjaMode(true, false));
let isNinjaMode = window.__initialMode !== undefined ? window.__initialMode :
    localStorage.getItem('ninjaMode') === 'true';


// функция вызывается сразу при работе скрипта === МГНОВЕННОЕ ПРИМЕНЕНИЕ (без рывков) ==
(function immediateApply() {
    const img = document.getElementById('magicIcon');

    if(!img) return

    // В зависимости от state текущего, мы убираем или добавляем класс для css
    if(isNinjaMode) {
        img.src = preloadNinja.src;
        document.body.classList.add('ninja-mode')
    } else {
        img.src = preloadFairy.src;
        document.body.classList.remove('ninja-mode')
    }
    document.documentElement.classList.remove('ninja-init');
})();


function applyMode(isNinja) {
    const img = document.getElementById('magicIcon');
    const headerText = document.querySelector('.header__text h2');
    const title = document.querySelector('.page-title');

    if (!img) return;

    if (isNinja) {
        // меняем картинку
        img.src = preloadNinja.src;
        // меняем текст заголовка
        if(headerText) headerText.textContent = "Hello Ninja's day";
        if(title) title.textContent = "Hello Ninja's day";

        // добавляем класс в html тег html class = ''
        document.body.classList.add('ninja-mode');

        // фон и контейнер делаем цвета для ninja мы используем переменные, они при смене класса не изменяются
        // можно было вообще их не использовать, но тогда при загрузке страницы будут микрофризы другого фона
        document.documentElement.style.setProperty('--bg-color', '#2c2c2c');
        document.documentElement.style.setProperty('--bg-color-container', '#3a3a3a');

        isNinjaMode = true;

    } else {
        img.src = preloadFairy.src;

        // Меняем текст заголовков
        if (headerText) headerText.textContent = "Hello Kitty's day";
        if (title) title.textContent = "Hello Kitty's day";

        // убираем класс из html тега
        document.body.classList.remove('ninja-mode');  // убираем

        // фон и контейнер делаем цвета для fairy
        document.documentElement.style.setProperty('--bg-color', '#bd36ff');
        document.documentElement.style.setProperty('--bg-color-container', '#fff');

        isNinjaMode = false;
    }
}

function toggleImage() {
    isNinjaMode = !isNinjaMode;
    applyMode(isNinjaMode);
    localStorage.setItem('ninjaMode', isNinjaMode);
}

const magicIcon = document.getElementById('magicIcon');
if(magicIcon) {
    magicIcon.addEventListener('click', ()=> {
        toggleImage(isNinjaMode)
    });
}

window.addEventListener('DOMContentLoaded', () => {
    applyMode(isNinjaMode);
});



