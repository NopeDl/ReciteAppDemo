// setInterval(() => $('.loading_icon').src = './images/gif小人/红色小人.gif', 600);

//拉取排行榜
let laqu = document.getElementById("laiqu");
let rankingList = document.getElementById("ranking_list");
let judge = true;
laqu.onclick = function() {
    if (judge) {
        $('.pk_footer').style.overflow = "scroll";
        $('.pk_footer').style.top = "0";
        judge = false;
    } else {
        $('.pk_footer').style.overflow = "hidden";
        $('.pk_footer').style.top = "70%";
        judge = true;
    }
}

let img_box = document.querySelector(".img_box");
$('.start_game').onclick = () => {
    $('.img_box img').style.opacity = '1';
    $('.newwaitPK').style.display = 'block';
    setTimeout(function() {
        if (img_box.classList.contains("appear_xz")) {
            img_box.classList.remove("appear_xz");
            img_box.classList.remove("animated");
        }
        img_box.classList.add("xz");
    }, 2200);

}