// setInterval(() => $('.loading_icon').src = './images/gif小人/红色小人.gif', 600);

//拉取排行榜
let laqu = document.getElementById("laiqu");
let rankingList = document.getElementById("ranking_list");

laqu.onclick = function () {
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
let pkstars = 0;
//获取旋转圈圈对象
let img_box = document.querySelector(".img_box");
//排行榜渲染
function rlRendering() {
    ajax(`http://8.134.104.234:8080/ReciteMemory/inf.get/rankingList`, 'get', ``, (str) => {
        let newstr = JSON.parse(str).msg;
        console.log(newstr);
        let ranking = newstr.data.ranking;
        Array.from(ranking).forEach((x, i) => {
            $('.others_nav')[i].querySelectorAll('.left span')[1].innerHTML = x.nickName;
            $('.others_nav')[i].querySelector('.right').innerHTML = `${x.stars}颗星`;
        })
    }, true);
    ajax(`http://8.134.104.234:8080/ReciteMemory/user.do/userRanking`, 'post', `userId=${curr.userId}`, (str) => {
        let newstr = JSON.parse(str).msg;
        console.log(newstr);
        let userdata = newstr.data.userData;
        $('.ranking_list .mine .rank').innerHTML = `第${userdata.userRanking}名`;
        $('.ranking_list .mine .score').innerHTML = `${userdata.user.stars}颗星`;
        $('.pk_page .integral .cur').innerHTML = userdata.user.points;
        pkstars = userdata.user.stars;
        switch (Math.floor(pkstars / 5)) {
            case 0:
                $('.competition_season .season_name').innerHTML = '初级学士';
                $('.competition_season .role img').src = './images/段位/1.png';
                break;
            case 1:
                $('.competition_season .season_name').innerHTML = '中级学士';
                $('.competition_season .role img').src = './images/段位/2.png';
                break;
            case 2:
                $('.competition_season .season_name').innerHTML = '高级学士';
                $('.competition_season .role img').src = './images/段位/4.png';
                break;
            case 3:
                $('.competition_season .season_name').innerHTML = '初级硕士';
                $('.competition_season .role img').src = './images/段位/3.png';
                break;
            case 4:
                $('.competition_season .season_name').innerHTML = '高级硕士';
                $('.competition_season .role img').src = './images/段位/5.png';
                break;
            case 5:
                $('.competition_season .season_name').innerHTML = '博士';
                $('.competition_season .role img').src = './images/段位/6.png';
                break;
            default:
                $('.competition_season .season_name').innerHTML = '博士';
                $('.competition_season .role img').src = './images/段位/6.png';
                break;
        }
        let star = pkstars >= 30 ? 5 : pkstars % 5;
        for (let i = 0; i < star; i++) {
            $('.pk_page .start i')[i].classList.add('active')
        }
    }, true);
}


let close_select = document.getElementById("close_select");
let select_dif_temp = document.querySelector(".select_dif_temp");
let select_bcg = document.getElementById("select_bcg");

//选择难度
let button_active = document.querySelectorAll(".select_dif_temp .select_card button");
let difficult = 'hard';
for (let x of button_active) {
    x.addEventListener("click", () => {
        for (let index = 0; index < button_active.length; index++) {
            if (button_active[index].classList.contains("active")) {
                button_active[index].classList.remove("active");
            }
        }
        x.classList.add("active");
        $(".select_dif .icon-cuowu").style.display = 'none'; //选择之后取消cha
        for (let index = 0; index < button_active.length; index++) {
            if (button_active[index].classList.contains("active")) {
                if (index == 0) {
                    difficult = 'easy';
                } else if (index == 1) {
                    difficult = 'normal';
                }
            }
        }
    })
}



let modle_id; //获取当前选择的模板id

//pk界面选择模板和选择难度
let start_game = document.getElementById("start_game");
start_game.addEventListener('click', () => {
    select_dif_temp.style.display = 'block';
    select_bcg.style.display = 'block';
    //渲染模板
    let ul_context = $(".select_dif_temp .select_temp ul");
    let tp_inner = all(".tp_inner");
    let context = "";
    for (let x of tp_inner) {
        let model_id = x.childNodes[1];
        let model_title = x.querySelector('.title');
        let model_label = x.querySelector('.label_title');
        context += `
        <li class="uploadLis_fadeIn">
            <div class="modleId ">${model_id.innerHTML}</div>
            <div class="content ">
                <div class="title_label ">
                    <h3 class="title ellipsis ">${model_title.innerHTML}</h3>
                    <div class="label ">
                        <span>#</span>
                        <span class="label_title ">${model_label.innerHTML}</span>
                    </div>
                </div>
            </div>
            <div class="select">
                <div class="circle">
                    <i class="iconfont icon-xuanze1 "></i>
                </div>
            </div>
        </li>
    `;
    }
    ul_context.innerHTML = context;



    let modleIdBox = document.querySelectorAll(".select_temp .modleId");

    //选择模板
    for (let x of all(".select_temp li")) {
        x.addEventListener("click", () => {
            for (let index = 0; index < all(".select_temp li").length; index++) {
                if (all(".select_temp li")[index].classList.contains("active")) {
                    all(".select_temp li")[index].classList.remove("active");
                }
                if (all(".select_temp .circle")[index].classList.contains("selected")) {
                    all(".select_temp .circle")[index].classList.remove("selected");
                }
            }
            x.classList.add("active");
            $(".select_temp .icon-cuowu").style.display = 'none'; //选择模板之后取消叉
            for (let index = 0; index < all(".select_temp li").length; index++) {
                if (all(".select_temp li")[index].classList.contains("active")) {
                    all(".select_temp .circle")[index].classList.add("selected");
                    modle_id = modleIdBox[index].innerHTML;
                }
            }
        })
    }
})

let li_active = document.querySelectorAll(".select_temp li");
let circle_active = document.querySelectorAll(".select_temp .circle");
close_select.addEventListener('click', () => {
    //取消选择困难的active
    for (let index = 0; index < button_active.length; index++) {
        if (button_active[index].classList.contains("active")) {
            button_active[index].classList.remove("active");
        }
    }

    //取消选择模板的active和selected
    for (let index = 0; index < li_active.length; index++) {
        if (li_active[index].classList.contains("active")) {
            li_active[index].classList.remove("active");
        }
        if (circle_active[index].classList.contains("selected")) {
            circle_active[index].classList.remove("selected");
        }
    }
    select_dif_temp.style.display = 'none';
    select_bcg.style.display = 'none';
    $(".select_dif .icon-cuowu").style.display = 'none'; //取消困难叉
    $(".select_temp .icon-cuowu").style.display = 'none'; //取消模板叉
})

//开始游戏
$('.determine .queding').onclick = () => {
    //记录已选择项数
    let count_selected_dif = 0; //选择困难
    let count_selected_model = 0; //选择模板

    //没有选择困难程度
    for (let index = 0; index < button_active.length; index++) {
        if (button_active[index].classList.contains("active")) {
            count_selected_dif++;
        }
    }
    if (count_selected_dif == 0) {
        $(".select_dif .icon-cuowu").style.display = 'block';
    }
    //没有选择模板的active和selected
    console.log(all(".select_temp li").length)
    for (let index = 0; index < all(".select_temp li").length; index++) {
        if (all(".select_temp li")[index].classList.contains("active")) {
            count_selected_model++;
        }
    }

    if (count_selected_model == 0) {
        $(".select_temp .icon-cuowu").style.display = 'block';
    }

    //如果都选择就可以开始
    console.log(count_selected_dif + count_selected_model)
    if (count_selected_dif + count_selected_model == 2) {
        select_dif_temp.style.display = 'none';
        select_bcg.style.display = 'none';
        $('.img_box img').style.opacity = '1';
        $('.img_box').classList.add("appear_img");
        img_box.classList.add("animated");
        $('.newwaitPK').style.display = 'block';
        $('.pk_end .mine').classList.add('appearLeft');
        $('.pk_end .other').classList.add('appearRight');
        $('.runle').style.display = 'none';
        $('.newwaitPK .back_bock').style.display = 'block';
        //取消选择困难的active
        for (let index = 0; index < button_active.length; index++) {
            if (button_active[index].classList.contains("active")) {
                button_active[index].classList.remove("active");
            }
        }

        //取消选择模板的active和selected
        for (let index = 0; index < li_active.length; index++) {
            if (li_active[index].classList.contains("active")) {
                li_active[index].classList.remove("active");
            }
            if (circle_active[index].classList.contains("selected")) {
                circle_active[index].classList.remove("selected");
            }
        }

        setTimeout(function () {
            if (img_box.classList.contains("appear_xz")) {
                img_box.classList.remove("appear_xz");
            }
            img_box.classList.add("xz");
            // createWebSocket ();
            resetPK();
            ConnectionClicked();
        }, 2200);
    }
}