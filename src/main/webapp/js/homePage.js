var startY, endY;
document.addEventListener('touchstart', function(e) {
    startY = e.touches[0].pageY;
}, false);
document.addEventListener('touchmove', function(e) {
    endY = e.changedTouches[0].pageY;
    moveLoad();
}, false);

function moveLoad() {
    var movY = endY - startY;
    var nav = document.getElementById("nav");
    if (movY > 0) {
        nav.style.height = "5rem";
    } else {
        if (document.documentElement.scrollTop > 82) {
            nav.style.height = "0rem";
        }
    }
}
let judge = true;
window.onscroll = function() {
    let pics = document.querySelectorAll(".pic_com");
    let item_text = document.querySelectorAll(".item_text");
    let container = document.getElementById("container");
    let threeGrid = document.getElementById("three_grid");
    let footerTitle = document.getElementById("footer_title");
    for (let i = 0; i < pics.length; i++) {
        // 用元素距离最顶部的高度减去页面可视区域的高度就是元素到可视区域的高度
        let picsTop = pics[i].offsetTop - document.documentElement.clientHeight;
        if (judge && window.scrollY < picsTop) {
            if (i % 2 == 0) {
                pics[i].classList.remove('appearRight');
            } else {
                pics[i].classList.remove('appearLeft');
            }
            judge = false;
        }
        if (window.scrollY > picsTop) {
            if (i % 2 == 0) {
                pics[i].classList.add('appearRight');
                item_text[i].classList.add('slideleft');
            } else {
                pics[i].classList.add('appearLeft');
                item_text[i].classList.add('slideRight');
            }
        }
        //  else {
        //     if (i % 2 == 0) {
        //         pics[i].classList.remove('appearRight');
        //     } else {
        //         pics[i].classList.remove('appearLeft');
        //     }
        // }
    }
    let footerTitleTop = footerTitle.offsetTop - document.documentElement.clientHeight;
    let threeGridTop = container.offsetTop - document.documentElement.clientHeight;
    if (window.scrollY > threeGridTop) {
        threeGrid.classList.add("fadeIn");
    }
    if (window.scrollY > footerTitleTop) {
        footerTitle.classList.add("appearP2")
    }
    // else {
    //threeGrid.classList.remove("fadeIn");
    //}
};