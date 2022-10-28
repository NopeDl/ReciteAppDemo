let ws;
let answerArr = [];
let UserSelectArr = [];
let pk = getData('pk');
if (pk.length == 0) {
    pk[0] = 0;
    pk[1] = 0;
}
$('.pk_page .integral .cur').innerHTML = `${pk[0]}`;
for (let i = 0; i < pk[1]; i++) {
    $('.pk_page .start i')[i].classList.add('active')
}


function ConnectionClicked() {
    try {
        ws = new WebSocket(`ws://8.134.104.234:8080/ReciteMemory/PK/${curr.userId}/202/easy`) //连接服务器
        ws.onopen = function (event) {
            console.log("已经与服务器建立了连接当前连接状态：" + this.readyState);
        };

        let msgCount = 0; //接收信息的次数

        ws.onmessage = function (event) {
            console.log("接收到服务器发送的数据：" + event.data + "haha" + msgCount);
            msgCount++;
            if (event.data) {
                let res = JSON.parse(event.data).socketMsg.datas;
                console.log(res);
                //当连接成功后发送START开始游戏
                if (res.CONNECTION && msgCount == 1) {
                    this.send("START");
                    console.log("已经向服务器发送START信号！");
                }
                //当匹配成功后获取双方信息渲染，并且发送Ready开始答题
                if (res.MATCH_SUCCESS) {
                    Refresh_id(res.enemyInf.nickName);
                    Refresh_img(res.enemyInf);
                    Refresh_Template(res.context);
                    ws.send("Ready");
                    console.log("已经向服务器发送Ready信号！");
                }
                //判断匹配是否成功，成功后渲染挖空内容和答题的答案,当isReady为true即可进入pk界面
                if (res.isReady) {
                    animate_success();
                    console.log("我已经准备好啦！");
                }
                if (res.hpInf) {
                    Refresh_blook(res.hpInf);
                }

                if (res.MATCH_END) {
                    $(".show_answer .answer").innerHTML = ``;
                    for (let x of answerArr) {
                        $(".show_answer .answer").innerHTML += `
                            <div class="an">${x}</div>
                            `;
                    }
                    $(".show_answer .mine_an").innerHTML = ``;
                    for (let i = 0; i < UserSelectArr.length; i++) {
                        if (UserSelectArr[i] == "right") {
                            $(".show_answer .mine_an").innerHTML += `
                            <div class="dui">√</div>
                            `;
                        } else {
                            $(".show_answer .mine_an").innerHTML += `
                                <div class="cuo">×</div>`;
                        }
                        if (i == UserSelectArr.length - 1 && i < 10) {
                            for (let j = 1; j < 10 - i; j++) {
                                $(".show_answer .mine_an").innerHTML += `
                                <div class="cuo">×</div>`;
                            }
                        }
                    }
                }

                if (res.records) {
                    console.log("2222");
                    RefreshXing(res.winnerId);
                    RefreshWin_Lose(res.winnerId);
                    console.log(res.winnerId)
                    if (res.records[0].userId != curr.userId) {
                        let other_answer = res.records[0].answersRecord;
                        console.log(other_answer);
                        $(".show_answer .other_an").innerHTML = ``;
                        for (let i = 0; i < other_answer.length; i++) {
                            if (other_answer[i].right) {
                                $(".show_answer .other_an").innerHTML += `
                                <div class="dui">√</div>
                                `;
                            } else {
                                $(".show_answer .other_an").innerHTML += `
                                    <div class="cuo">×</div>`;
                            }
                            if (i == other_answer.length - 1 && i < 10) {
                                for (let j = 1; j < 10 - i; j++) {
                                    $(".show_answer .other_an").innerHTML += `
                                    <div class="cuo">×</div>`;
                                }
                            }
                        }
                    }
                    if (res.records[1].userId != curr.userId) {
                        other_answer = res.records[1].answersRecord;
                        console.log(other_answer);
                        $(".show_answer .other_an").innerHTML = ``;
                        for (let i = 0; i < other_answer.length; i++) {
                            if (other_answer[i].right) {
                                $(".show_answer .other_an").innerHTML += `
                                <div class="dui">√</div>
                                `;
                            } else {
                                $(".show_answer .other_an").innerHTML += `
                                    <div class="cuo">×</div>`;
                            }
                            if (i == other_answer.length - 1 && i < 10) {
                                for (let j = 1; j < 10 - i; j++) {
                                    $(".show_answer .other_an").innerHTML += `
                                    <div class="cuo">×</div>`;
                                }
                            }
                        }
                    }
                }
            }
        };

        //正在匹配关闭页面
        $('.newwaitPK .back_bock').onclick = () => {
            $('.img_box img').style.opacity = '0';
            $('.pk_end').style.display = 'none';
            $('.enterPk').classList.add('appear');
            $('.img_box').classList.remove("xz");
            $('.img_box').classList.add("disappear_xz");
            $('.img_box').classList.add("animated");
            $('.newwaitPK .mine').classList.add('disappearup');
            $('.newwaitPK .other').classList.add('disappearbottom');
            setTimeout(() => { 
                $('.newwaitPK').style.display = 'none'; 
                $('.newwaitPK .mine').classList.remove('disappearup')
                $('.newwaitPK .other').classList.remove('disappearbottom')
                $('.img_box').classList.remove("disappear_xz");
                
            }, 2000);
            ws.onclose = wsonclose(false,ws);
        }

        ws.onclose = wsonclose(true,ws);


        function wsonclose(judge,ws) {
            if (judge) {
                animate_pkend();
            };
            console.log("已经与服务器断开连接当前连接状态：" + this.readyState);
        };

        ws.onerror = function (event) {
            console.log("WebSocket异常！");
        };




    } catch (ex) {
        alert(ex.message);
    }
};

function resetPK() {
    $('.enterPk .other .other_name').innerHTML = '??????';
    $('.pk_interface .other .other_name').innerHTML = '??????';
    $('.other .box .other_name').innerHTML = '??????';

    $('.newwaitPK .other .head_portrait').innerHTML = `<div class="Unmatched">?</div>`
    $('.pk_end .ohter_portrait').innerHTML = `<img src="./images/头像/头像-女学生2.png">`
    $('.enterPk .other .head_portrait').innerHTML = `<img src="./images/头像/头像-女学生2.png">`

    $('.enterPk .text_box').innerHTML = '';
    $('.enterPk .text_box').scrollTop = '0';

    $('.enterPk .mine .mine_blood_change').style.width = '38.13vw';
    $('.enterPk .name_blood .mine_blook').innerHTML = '100%';
    $('.enterPk .other .other_blood_change').style.width = '38.13vw';
    $('.enterPk .name_blood .other_blook').innerHTML = '100%';
}


//刷新id
function Refresh_id(data) {
    $('.enterPk .other .other_name').innerHTML = data;
    $('.pk_interface .other .other_name').innerHTML = data;
    $('.other .box .other_name').innerHTML = data;
}

//刷新头像
function Refresh_img(data) {
    $('.newwaitPK .other .head_portrait').innerHTML = `<img src="${data.base64}">`
    $('.pk_end .ohter_portrait').innerHTML = `<img src="${data.base64}">`
    $('.enterPk .other .head_portrait').innerHTML = `<img src="${data.base64}">`
}


//刷新模板
function Refresh_Template(data) {
    $('.enterPk .text_box').innerHTML = data;
    // console.log(all('.enterPk .text_box div'))
    for (let x of all('.enterPk .text_box div')) {
        x.classList.add("highlight");
        x.classList.add("recite");
    }
    Refresh_answer();
}

//刷新答案
function Refresh_answer() {
    //数组用来存放答案
    answerArr = [];
    let selectArr = [];
    UserSelectArr = [];
    let slArr = [];
    let answerIndex = 0;

    let timerx = null;
    let timero = null;
    /*窗口滚动缓动动画*/
    function animate(obj, target, stop) {
        obj.scrollTop = stop;
        clearInterval(obj.timerx);
        obj.timerx = setInterval(function () {
            var step = (target - obj.scrollTop) / 15; //缓动动画位移
            if (obj.scrollTop.toFixed() == target.toFixed()) {
                clearInterval(obj.timerx); //如果运动到目标值时清除定时器
            };

            obj.scroll(0, obj.scrollTop + step);

        }, 10);
    }
    //当鼠标滚动时清除定时器
    $('.enterPk .text_box').addEventListener('touchstart', () => {
        clearInterval($('.enterPk .text_box').timerx);
        clearTimeout(timero);
    });

    // 随机排列数组
    function sortArr(arr) {
        arr.sort(function () {
            return 0.5 - Math.random();
        })
        return arr.slice(0, 4);
    }

    Array.from(all('.enterPk .highlight')).forEach((x, i) => {
        x.classList.add('recite');
        answerArr.push(x.innerHTML);
        selectArr.push(x.innerHTML);
        console.log(x.offsetHeight);
    });


    for (let x of $('.enterPk .option .com')) {
        x.onclick = () => {
            if (answerIndex < all('.enterPk .highlight').length) {
                if (x.querySelector('.answer').innerHTML == answerArr[answerIndex]) {
                    all('.enterPk .highlight')[answerIndex].classList.add('right');
                    UserSelectArr.push("right");
                    console.log(UserSelectArr)
                    ws.send(JSON.stringify({
                        answerName: x.querySelector('.answer').innerHTML,
                        answerValue: true,
                    }))

                } else {
                    all('.enterPk .highlight')[answerIndex].classList.add('wrong');
                    UserSelectArr.push("wrong");
                    console.log(UserSelectArr)
                    ws.send(JSON.stringify({
                        answerName: x.querySelector('.answer').innerHTML,
                        answerValue: false,
                    }))
                }
                // UserSelectArr.push(x.querySelector('.answer').innerHTML);
                if (answerIndex < all('.enterPk .highlight').length - 1)
                    answerSelect();
            } else {
                // for(let x of $('.enterPk .option .answer')){
                //     x.innerHTML = '';
                // }
            }
        }

    }

    //选择答案后
    function answerSelect() {
        answerIndex++;
        slArr = [];
        for (let x of selectArr) {
            if (x == answerArr[answerIndex])
                continue;
            slArr.push(x);
            if (slArr.length == 3)
                break;
        }
        slArr.push(answerArr[answerIndex]);
        slArr = sortArr(slArr);
        console.log(selectArr, slArr);
        let n = 0;


        for (let x of $('.enterPk .option .answer')) {
            x.innerHTML = slArr[n++];
        }
        if (answerIndex < all('.enterPk .highlight').length) {
            let picsTop = all('.enterPk .highlight')[answerIndex].offsetTop - $('.enterPk .text_box').offsetTop;

            if (picsTop >= $('.enterPk .text_box').offsetTop / 2) {
                setTimeout(function () {
                    animate($('.enterPk .text_box'), picsTop - $('.enterPk .text_box').clientHeight / 2, $('.enterPk .text_box').scrollTop)
                }, 1000);

            }
        }
    }
    answerSelect();
    gameInit();

    function gameInit() {
        answerIndex = 0;
    }
    //pkend的界面渲染
}


//匹配成功之后的动画
function animate_success() {
    setTimeout(function () {
        $('.img_box img').style.opacity = '0';
        $('.enterPk').style.display = 'block';
        $('.enterPk').classList.add('appear');
        $('.img_box').classList.remove("xz");
        $('.img_box').classList.add("disappear_xz");
        $('.img_box').classList.add("animated");
        $('.newwaitPK .mine').classList.add('disappearup')
        $('.newwaitPK .other').classList.add('disappearbottom')
        setTimeout(() => $('.newwaitPK').style.display = 'none', 2000);
    }, 3000);
}



//匹配结束之后的动画
function animate_pkend() {
    setTimeout(function () {

        $('.enterPk .head_nav_pk').classList.add('disappear');
        $('.enterPk .head_nav_pk').classList.add('animated');
        $('.enterPk .pk_blood .mine').classList.add('disLeft');
        $('.enterPk .pk_blood .mine').classList.add('animated');
        $('.enterPk .pk_blood .enter_vs').classList.add('disappear');
        $('.enterPk .pk_blood .enter_vs').classList.add('animated');
        // $('.enterPk .pk_blood .time').classList.add('disappear');
        // $('.enterPk .pk_blood .time').classList.add('animated');
        $('.enterPk .pk_blood .other').classList.add('disRight');
        $('.enterPk .pk_blood .other').classList.add('animated');
        $('.enterPk .text_box').classList.add('animated');
        $('.enterPk .text_box').classList.add('disappear');
        $('.enterPk .option').classList.add('animated');
        $('.enterPk .option').classList.add('disappear');
    }, 3000);

    setTimeout(function () {
        $('.pk_end').style.display = 'block';
        setTimeout(() => {
            $('.win_lose .win').style.display = "block";
            $('.win_lose .win').classList.add('zoom2');

        }, 1800)
        setTimeout(() => {
            $('.win_lose .bgc').style.display = "block";
            $('.win_lose .bgc').classList.add('zoom');
        }, 2400)
    }, 1000);
}


//刷线血条和血量
function Refresh_blook(data) {
    if (data[0].userId == curr.userId) {
        $('.enterPk .mine .mine_blood_change').style.width = (Math.round(data[0].hp) / 100) * 38.13 + 'vw';
        $('.enterPk .name_blood .mine_blook').innerHTML = Math.round(data[0].hp) + '%';
    } else {
        $('.enterPk .other .other_blood_change').style.width = (Math.round(data[0].hp) / 100) * 38.13 + 'vw';
        $('.enterPk .name_blood .other_blook').innerHTML = Math.round(data[0].hp) + '%';
    }

    if (data[1].userId == curr.userId) {
        $('.enterPk .mine .mine_blood_change').style.width = (Math.round(data[1].hp) / 100) * 38.13 + 'vw';
        $('.enterPk .name_blood .mine_blook').innerHTML = Math.round(data[1].hp) + '%';
    } else {
        $('.enterPk .other .other_blood_change').style.width = (Math.round(data[1].hp) / 100) * 38.13 + 'vw';
        $('.enterPk .name_blood .other_blook').innerHTML = Math.round(data[1].hp) + '%';
    }

}

//结束之后刷新星星和积分
function RefreshXing(winnerId) {
    if (winnerId == -1) {
        pk[0] += 5;
        $('.pk_end .mine_add_reduce').innerHTML = `+0`;
        $('.pk_page .integral .cur').innerHTML = `${pk[0]}`;
        $('.pk_end .other_add_reduce').innerHTML = `+0`;
        saveData('pk', pk);
        for (let i = 0; i < pk[1]; i++) {
            $('.pk_page .start i')[i].classList.add('active')
        }
    } else {
        if (curr.userId == winnerId) {
            pk[0] += 10;
            if (pk[1] != 4)
                pk[1] += 1;
            $('.pk_end .mine_add_reduce').innerHTML = `+1`;
            $('.pk_page .integral .cur').innerHTML = `${pk[0]}`;
            $('.pk_end .other_add_reduce').innerHTML = `-1`;
            saveData('pk', pk);
            for (let i = 0; i < pk[1]; i++) {
                $('.pk_page .start i')[i].classList.add('active')
            }
        } else {
            if (pk[0] != 0)
                pk[0] -= 5;
            if (pk[1] != 0)
                pk[1] -= 1;
            $('.pk_end .mine_add_reduce').innerHTML = `-1`;
            $('.pk_page .integral .cur').innerHTML = `${pk[0]}`;
            $('.pk_end .other_add_reduce').innerHTML = `+1`;
            saveData('pk', pk);
            for (let i = 0; i < pk[1]; i++) {
                $('.pk_page .start i')[i].classList.add('active')
            }
        }
    }

}

//刷新输赢动画
function RefreshWin_Lose(winnerId) {
    if (winnerId == -1) {
        $('.pk_end .win_lose .bgc').src = "./images/pk/win_bgc.png";
        $('.pk_end .win_lose .win').src = "./images/pk/win.png";
    } else {
        if (curr.userId == winnerId) {
            $('.pk_end .win_lose .bgc').src = "./images/pk/win_bgc.png";
            $('.pk_end .win_lose .win').src = "./images/pk/win.png";
        } else {
            $('.pk_end .win_lose .bgc').src = "./images/pk/defeat_bgc .png";
            $('.pk_end .win_lose .win').src = "./images/pk/defeat.png";
            $('.pk_end .win_lose .win').style.width = "29.33vw";
            $('.pk_end .win_lose .win').style.left = "2vh";
        }
    }
}

$(".click_back").addEventListener('click', () => {

    $('.enterPk').classList.remove('appear');
    img_box.classList.remove("disappear_xz");
    // img_box.classList.remove("animated");
    $('.newwaitPK .mine').classList.remove('disappearup')
    $('.newwaitPK .other').classList.remove('disappearbottom')

    $('.enterPk .head_nav_pk').classList.remove('disappear');
    // $('.enterPk .head_nav_pk').classList.remove('animated');
    $('.enterPk .pk_blood .mine').classList.remove('disLeft');
    // $('.enterPk .pk_blood .mine').classList.remove('animated');
    $('.enterPk .pk_blood .enter_vs').classList.remove('disappear');
    // $('.enterPk .pk_blood .enter_vs').classList.remove('animated');
    $('.enterPk .pk_blood .other').classList.remove('disRight');
    // $('.enterPk .pk_blood .other').classList.remove('animated');
    $('.enterPk .text_box').classList.remove('disappear');
    // $('.enterPk .text_box').classList.remove('animated');
    $('.enterPk .option').classList.remove('disappear');
    // $('.enterPk .option').classList.remove('animated');

    $('.enterPk').style.display = 'none';
    $('.pk_end').style.display = 'none';
});