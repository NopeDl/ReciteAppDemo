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
        // createWebSocket ();
        resetPK();
        ConnectionClicked();
    }, 2200);
}

ajax(`http://8.134.104.234:8080/ReciteMemory/inf.get/rankingList`, 'get', ``, (str) => {
    let newstr = JSON.parse(str).msg;
    console.log(newstr);
    let ranking = newstr.data.ranking;
    0
    Array.from(ranking).forEach((x, i) => {
        if (i < 10) {
            $('.others_nav')[i].querySelectorAll('.left span')[1].innerHTML = x.nickName;
            $('.others_nav')[i].querySelector('.right').innerHTML = `${x.stars}分`;
        }
        if (x.nickName == curr.userInfo.nickName) {
            $('.ranking_list .mine .rank').innerHTML = `第${i+1}名`
            $('.ranking_list .mine .score').innerHTML = `${x.stars}颗星`
        }
    })
}, true);