let interactive = document.querySelectorAll(".inter_box");

var dainzan = document.querySelectorAll(".dainzan");
let jifen = document.querySelectorAll(".jifen");

var menu = document.querySelectorAll(".menu");
Array.from(menu).forEach((x, i) => {
    x.addEventListener('click', function(event) {
        for (let k of interactive) {
            k.style.width = "0";
        }
        interactive[i].style.width = "160px";
        event.stopPropagation(); //阻止冒泡
    })
})

Array.from(dainzan).forEach((x, i) => {
    x.addEventListener('click', function(event) {
        if (dainzan[i].classList.contains("yellow")) {
            dainzan[i].classList.remove("yellow");
        } else {
            dainzan[i].classList.add("yellow");
        }
        setTimeout(() => { interactive[i].style.width = "0"; }, 600);
        event.stopPropagation(); //阻止冒泡
    })
})

Array.from(jifen).forEach((x, i) => {
    x.addEventListener('click', function(event) {
        if (jifen[i].classList.contains("yellow")) {
            jifen[i].classList.remove("yellow");
        } else {
            jifen[i].classList.add("yellow");
        }
        setTimeout(() => { interactive[i].style.width = "0"; }, 600);
        event.stopPropagation(); //阻止冒泡
    })
})

document.addEventListener('click', function(event) {
    for (let k of interactive) {
        k.style.width = "0";
    }
    event.stopPropagation(); //阻止冒泡

});