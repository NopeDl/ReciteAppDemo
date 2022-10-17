//点击用户按钮出现登录和注册
let usericon = document.getElementById("usericon");
let collapse = document.getElementById("collapse");
let boxIcon = document.getElementById("box_icon");
let iconfont265 = document.querySelector(".icon-biaoqianA01_wode-265");
usericon.addEventListener('click', function(event) {
    console.log(collapse.style.height);
    if (collapse.style.height == "3.5rem") {
        collapse.style.height = "0rem";
        boxIcon.style.marginTop = "-3.5rem";
        iconfont265.classList.remove("white");
        event.stopPropagation();
        return;
    }
    collapse.style.height = "3.5rem";
    boxIcon.style.marginTop = "0rem";
    iconfont265.classList.add("white");

})

document.addEventListener('click', function() {
    collapse.style.height = "0rem";
    boxIcon.style.marginTop = "-3.5rem";
    iconfont265.classList.remove("white");
});

collapse.addEventListener('click', function(event) {
    iconfont265.classList.add("white");
    event.stopPropagation(); //阻止冒泡
})

usericon.addEventListener('click', function(event) {
    event.stopPropagation(); //阻止冒泡
})